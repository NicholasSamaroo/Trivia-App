package com.example.jeopardy.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.jeopardy.Models.SpecificCategory;
import com.example.jeopardy.R;
import com.example.jeopardy.ViewModel.TriviaViewModel;

import java.util.List;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener, QuestionFragment.mainShowInterface {
    private final int COUNT = 4;
    private final int ROWS = 5;
    private final int COLUMNS = 4;

    private int tagRow;
    private int tagColumn;
    private int gameScore;
    private int questionCountDown = 16;
    private int categoryOffset;

    private List<SpecificCategory> listOfClues = null;
    private TriviaViewModel triviaViewModel;

    private ProgressBar progressBar;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        gameScore = 0;
        sharedPreferences = BoardActivity.this.getPreferences(Context.MODE_PRIVATE);
        triviaViewModel = new ViewModelProvider(this).get(TriviaViewModel.class);
        setObservers();
        fetchCategoriesAndClues();
        progressBar = findViewById(R.id.pbLoading);
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    private void setObservers() {
        triviaViewModel.getCategoryData().observe(this, new Observer<List<SpecificCategory>>() {
            @Override
            public void onChanged(List<SpecificCategory> specificCategories) {
                listOfClues = specificCategories;
                updateCategoryOffset();
                createBoard();
                progressBar.setVisibility(ProgressBar.GONE);
            }
        });
    }

    private void updateCategoryOffset() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        categoryOffset += 4;
        editor.putInt(getString(R.string.CATEGORY_OFFSET), categoryOffset);
        editor.apply();
    }

    private void fetchCategoriesAndClues() {
        categoryOffset = sharedPreferences.getInt(getString(R.string.CATEGORY_OFFSET), 0);
        triviaViewModel.initializeBoard(COUNT, categoryOffset);
    }

    public void createBoard() {
        FrameLayout boardContainer = findViewById(R.id.boardContainer);
        GridLayout board = new GridLayout(this);
        board.setOrientation(GridLayout.HORIZONTAL);
        board.setRowCount(ROWS);
        board.setColumnCount(COLUMNS);

        /*GridLayout.LayoutParams boardLayoutParams = new GridLayout.LayoutParams();
        boardLayoutParams.height = GridLayout.LayoutParams.MATCH_PARENT;
        boardLayoutParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
        boardLayoutParams.setMargins(0,0,0,0);*/
        boardContainer.addView(board);

        GridLayout.Spec categoryRow = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
        GridLayout.Spec categoryColumn = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
        for (int i = 0; i < COLUMNS; i++) {
            TextView categoryName = new TextView(this);
            categoryName.setText(listOfClues.get(i).getTitle());
            categoryName.setGravity(Gravity.CENTER);
            categoryName.setTextColor(getResources().getColor(R.color.yellow));
            // If you don't instantiate a new layout params object the last category and last clue value will take up the entire screen
            GridLayout.LayoutParams categoryParams = new GridLayout.LayoutParams(categoryRow, categoryColumn);
            categoryParams.setMargins(20, 20, 20, 20);
            categoryName.setLayoutParams(categoryParams);
            categoryName.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.main_show_cell_background, null));
            board.addView(categoryName);
        }

        GridLayout.Spec clueRow = GridLayout.spec(GridLayout.UNDEFINED, 1, 2f);
        GridLayout.Spec clueColumn = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
        for (int i = 1, r = 0; i < ROWS; i++, r++) {
            for (int j = 0, c = 0; j < COLUMNS; j++, c++) {
                TextView clue = new TextView(this);
                // Set the text
                clue.setText(String.valueOf(listOfClues.get(c).getClues().get(r).getValue()));
                // Set the text color
                clue.setTextColor(getResources().getColor(R.color.yellow));
                // Set the size
                clue.setTextSize(25);
                clue.setGravity(Gravity.CENTER);
                GridLayout.LayoutParams clueParams = new GridLayout.LayoutParams(clueRow, clueColumn);
                clueParams.setMargins(20, 20, 20, 20);
                clue.setLayoutParams(clueParams);
                // Set the background
                clue.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.main_show_cell_background, null));
                // Set the tag
                String tag = c + "" + r;
                clue.setTag(tag);
                clue.setOnClickListener(this);
                board.addView(clue);
            }
        }
    }

    @Override
    public void onClick(View v) {
        tagColumn = Character.getNumericValue(v.getTag().toString().charAt(0));
        tagRow = Character.getNumericValue(v.getTag().toString().charAt(1));

        Bundle bundle = new Bundle();
        bundle.putInt("value", listOfClues.get(tagColumn).getClues().get(tagRow).getValue());
        bundle.putString("question", listOfClues.get(tagColumn).getClues().get(tagRow).getQuestion());
        bundle.putString("answer", listOfClues.get(tagColumn).getClues().get(tagRow).getAnswer());
        bundle.putString("category", listOfClues.get(tagColumn).getTitle());
        bundle.putInt("gameScore", gameScore);

        v.setClickable(false);
        ((TextView) v).setText(null);
        v.setBackgroundColor(getColor(R.color.indigo_dark));

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragmentContainer, QuestionFragment.class, bundle)
                .commit();
    }

    @Override
    public void returnMainShowScore(int score) {
        gameScore += score;
        --questionCountDown;
        Log.d("qcd", String.valueOf(questionCountDown));
        if (questionCountDown == 0) {
            finishGame();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to quit the current game and forfeit your score?")
                .setTitle("Quit game")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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

    private void finishGame() {
        Intent intent = new Intent();
        intent.putExtra("newMainHighScore", gameScore);
        setResult(RESULT_OK, intent);
        finish();
    }
}
