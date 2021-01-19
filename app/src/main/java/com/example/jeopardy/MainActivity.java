package com.example.jeopardy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final int BLITZ_REQUEST_CODE = 0;
    private final int MAIN_REQUEST_CODE = 1;
    private final String MAIN_SHOW_SCORE_KEY = "mainBoardScore";
    private final String BLITZ_HIGH_SCORE_KEY = "blitzHighScore";

    private TextView mainBoardScoreTextView;
    private TextView blitzTextView;

    private int currentBlitzHighScore;
    private int currentMainHighScore;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        currentMainHighScore = sharedPreferences.getInt(MAIN_SHOW_SCORE_KEY, 0);
        currentBlitzHighScore = sharedPreferences.getInt(BLITZ_HIGH_SCORE_KEY, 0);

        String mainBoardHighScoreText = "High Score for the main show: " + currentMainHighScore;
        String blitzHighScoreText = "High Score for blitz: " + currentBlitzHighScore;

        mainBoardScoreTextView = findViewById(R.id.mainBoardHighScore);
        blitzTextView = findViewById(R.id.blitzHighScore);
        mainBoardScoreTextView.setText(mainBoardHighScoreText);
        blitzTextView.setText(blitzHighScoreText);

        Button mainBoardButton = findViewById(R.id.mainBoardButton);
        Button blitzButton = findViewById(R.id.blitzButton);
        mainBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainBoardNavigation = new Intent(MainActivity.this,MainBoardActivity.class);
                startActivity(mainBoardNavigation);
            }
        });

        blitzButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blitzNavigation = new Intent(MainActivity.this,BlitzActivity.class);
                startActivityForResult(blitzNavigation, BLITZ_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == BLITZ_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                int blitzScore = data.getIntExtra("newBlitzHighScore", 0);
                if(blitzScore > currentBlitzHighScore) {
                    updateBlitzHighScore(blitzScore);
                }
            }
        } else if(requestCode == MAIN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                int mainScore = data.getIntExtra("newMainHighScore", 0);
                if(mainScore > currentMainHighScore) {
                    updateMainHighScore(mainScore);
                }
            }
        }
    }

    private void updateBlitzHighScore(int newScore) {
        String newBlitzHighScore = "High Score for Blitz: " + newScore;
        blitzTextView.setText(newBlitzHighScore);
        sharedPreferences = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BLITZ_HIGH_SCORE_KEY, newScore);
        editor.apply();
    }

    private void updateMainHighScore(int newScore) {
        String newMainHighScore = "High Score for Main: " + newScore;
        mainBoardScoreTextView.setText(newMainHighScore);
        sharedPreferences = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(MAIN_SHOW_SCORE_KEY, newScore);
        editor.apply();
    }
}