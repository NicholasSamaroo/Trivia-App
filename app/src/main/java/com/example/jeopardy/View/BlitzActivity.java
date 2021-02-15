package com.example.jeopardy.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.jeopardy.Models.Random;
import com.example.jeopardy.R;
import com.example.jeopardy.ViewModel.TriviaViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;


public class BlitzActivity extends AppCompatActivity {
    public final static long COUNT_DOWN_IN_MILLIS = 30000;
    private final int COUNT = 5;
    private int currentQuestion;
    private int blitzScore;

    private TextView blitzScoreText;
    private TextView blitzValue;
    private TextView blitzCategory;
    private TextView blitzQuestion;
    private TextInputEditText blitzAnswer;
    private TextView countdown;

    private CountDownTimer countDownTimer;
    private List<Random> questions;
    private TriviaViewModel viewModel;

    private final TextView.OnEditorActionListener editTextListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (checkAnswer()) {
                    countDownTimer.cancel();
                    blitzScore += questions.get(currentQuestion).getValue();
                    blitzAnswer.getText().clear();
                    ++currentQuestion;
                    showQuestion();
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blitz);

        blitzScore = 0;
        countDownTimer = null;
        currentQuestion = 0;

        blitzScoreText = findViewById(R.id.blitzScore);
        blitzValue = findViewById(R.id.blitzValue);
        blitzCategory = findViewById(R.id.blitzCategory);
        blitzQuestion = findViewById(R.id.blitzQuestion);
        blitzAnswer = findViewById(R.id.blitzAnswer);
        blitzAnswer.setOnEditorActionListener(editTextListener);
        countdown = findViewById(R.id.blitzCountdown);

        viewModel = new ViewModelProvider(this).get(TriviaViewModel.class);
        initializeObserver();
        getQuestions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void initializeObserver() {
        viewModel.getBlitzData().observe(this, new Observer<List<Random>>() {
            @Override
            public void onChanged(List<Random> randoms) {
                questions = randoms;
                prepData();
                showQuestion();
            }
        });
    }

    private void prepData() {
        for(Random i: questions) {
            if(i.getValue() == 0) {
                i.setValue(200);
            }
        }
    }

    private void getQuestions() {
        viewModel.startBlitz(COUNT);
    }

    public void showQuestion() {
        if (currentQuestion == COUNT) {
            finishBlitz();
        } else {
            blitzScoreText.setText(String.valueOf(blitzScore));
            blitzValue.setText(String.valueOf(questions.get(currentQuestion).getValue()));
            blitzCategory.setText(questions.get(currentQuestion).getCategory().getTitle());
            blitzQuestion.setText(questions.get(currentQuestion).getQuestion());
            Log.d("answer", questions.get(currentQuestion).getAnswer());
            startTimer();
        }
    }

    private boolean checkAnswer() {
        return !(TextUtils.isEmpty(blitzAnswer.getText().toString())) &&
                blitzAnswer.getText().toString().toLowerCase().equals(questions.get(currentQuestion).getAnswer().toLowerCase());
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(COUNT_DOWN_IN_MILLIS, 1000) {
            String countDownHolder;

            @Override
            public void onTick(long millisUntilFinished) {
                countDownHolder = "" + millisUntilFinished / 1000;
                countdown.setText(countDownHolder);
            }

            @Override
            public void onFinish() {
                cancel();
                blitzScore += questions.get(currentQuestion).getValue() * -1;
                ++currentQuestion;
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to quit the current game and forfeit your score?")
                .setTitle("Quit game")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
}
