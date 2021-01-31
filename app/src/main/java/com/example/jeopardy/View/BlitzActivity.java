package com.example.jeopardy.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jeopardy.JServiceAPI;
import com.example.jeopardy.Models.TriviaWrapper;
import com.example.jeopardy.R;
import com.example.jeopardy.RetroFitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BlitzActivity extends AppCompatActivity {
    public final static long COUNT_DOWN_IN_MILLIS = 30000;
    private final int COUNT = 10;
    private int currentQuestion;
    private TextView blitzQuestion;
    private TextView countdown;
    private EditText blitzAnswer;
    private Button submitButton;
    private int blitzScore = 0;
    private CountDownTimer countDownTimer;
    private List<TriviaWrapper> questions;

    private final View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean correctOrIncorrect = checkAnswer();
            if(correctOrIncorrect) {
                blitzScore += questions.get(currentQuestion).getValue();
                ++currentQuestion;
                blitzAnswer.getText().clear();
                countDownTimer.cancel();
                showQuestion();
            } else {
                Toast.makeText(getApplicationContext(), "WRONG", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blitz);
        blitzQuestion = findViewById(R.id.blitzQuestion);
        countdown = findViewById(R.id.countdown);
        blitzAnswer = findViewById(R.id.blitzAnswer);
        submitButton = findViewById(R.id.submit);
        countDownTimer = null;
        currentQuestion = 0;
        retrieveQuestions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void retrieveQuestions() {
        JServiceAPI jServiceAPI = RetroFitInstance.getRetrofitInstance().create(JServiceAPI.class);
        Call<List<TriviaWrapper>> call = jServiceAPI.getQuestions(COUNT);
        call.enqueue(new Callback<List<TriviaWrapper>>() {
            @Override
            public void onResponse(Call<List<TriviaWrapper>> call, Response<List<TriviaWrapper>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questions = response.body();
                    showQuestion();
                }
            }

            @Override
            public void onFailure(Call<List<TriviaWrapper>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showQuestion() {
        if(currentQuestion == COUNT) {
            finishBlitz();
        } else {
            blitzQuestion.setText(questions.get(currentQuestion).getQuestion());
            Log.d("answer", questions.get(currentQuestion).getAnswer());
            startTimer();
        }
    }

    private boolean checkAnswer() {
        return (blitzAnswer.getText().toString().toLowerCase()).equals(questions.get(currentQuestion).getAnswer().toLowerCase());
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(COUNT_DOWN_IN_MILLIS, 1000) {
            String countDownHolder;
            @Override
            public void onTick(long millisUntilFinished) {
                submitButton.setOnClickListener(submitListener);
                countDownHolder = "" + millisUntilFinished / 1000;
                countdown.setText(countDownHolder);
            }
            @Override
            public void onFinish() {
                ++currentQuestion;
                cancel();
                showQuestion();
            }
        }.start();
    }

    private void finishBlitz() {
        Intent intent = new Intent();
        intent.putExtra("newBlitzHighScore", blitzScore);
        setResult(RESULT_OK, intent);
        finish();
    }
}
