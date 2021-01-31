package com.example.jeopardy.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.jeopardy.JServiceAPI;
import com.example.jeopardy.Models.SpecificCategory;
import com.example.jeopardy.R;
import com.example.jeopardy.ViewModel.TriviaViewModel;

import java.util.List;

public class MainBoardActivity extends AppCompatActivity implements View.OnClickListener, QuestionFragment.mainShowInterface {
    private final int COUNT = 4;
    private List<Integer> categoryID = null;
    private List<SpecificCategory> listOfClues = null;
    private int gameScore = 0;

    private JServiceAPI jServiceAPI;

    private TextView categoryOne;
    private TextView categoryTwo;
    private TextView categoryThree;
    private TextView categoryFour;
    private TextView row1column0;
    private TextView row2column0;
    private TextView row3column0;
    private TextView row4column0;
    private TextView row1column1;
    private TextView row2column1;
    private TextView row3column1;
    private TextView row4column1;
    private TextView row1column2;
    private TextView row2column2;
    private TextView row3column2;
    private TextView row4column2;
    private TextView row1column3;
    private TextView row2column3;
    private TextView row3column3;
    private TextView row4column3;

    private TriviaViewModel triviaViewModel;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        triviaViewModel = new ViewModelProvider(this).get(TriviaViewModel.class);
        setObservers();
        initializeViews();
        progressBar = findViewById(R.id.pbLoading);
        progressBar.setVisibility(ProgressBar.VISIBLE);

    }

    private void setObservers() {
        fetchCategoriesAndClues();
        triviaViewModel.getComplete().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    initializeBoard();
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                }
            }
        });
        triviaViewModel.getClueData().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                categoryID = integers;
            }
        });
        triviaViewModel.getCategoryData().observe(this, new Observer<List<SpecificCategory>>() {
            @Override
            public void onChanged(List<SpecificCategory> specificCategories) {
                listOfClues = specificCategories;
            }
        });
    }

    private void initializeViews() {
        categoryOne = findViewById(R.id.categoryOne);
        categoryTwo = findViewById(R.id.categoryTwo);
        categoryThree = findViewById(R.id.categoryThree);
        categoryFour = findViewById(R.id.categoryFour);

        row1column0 = findViewById(R.id.row1column0);
        row1column0.setOnClickListener(this);
        row2column0 = findViewById(R.id.row2column0);
        row2column0.setOnClickListener(this);
        row3column0 = findViewById(R.id.row3column0);
        row3column0.setOnClickListener(this);
        row4column0 = findViewById(R.id.row4column0);
        row4column0.setOnClickListener(this);

        row1column1 = findViewById(R.id.row1column1);
        row1column1.setOnClickListener(this);
        row2column1 = findViewById(R.id.row2column1);
        row2column1.setOnClickListener(this);
        row3column1 = findViewById(R.id.row3column1);
        row3column1.setOnClickListener(this);
        row4column1 = findViewById(R.id.row4column1);
        row4column1.setOnClickListener(this);

        row1column2 = findViewById(R.id.row1column2);
        row1column2.setOnClickListener(this);
        row2column2 = findViewById(R.id.row2column2);
        row2column2.setOnClickListener(this);
        row3column2 = findViewById(R.id.row3column2);
        row3column2.setOnClickListener(this);
        row4column2 = findViewById(R.id.row4column2);
        row4column2.setOnClickListener(this);

        row1column3 = findViewById(R.id.row1column3);
        row1column3.setOnClickListener(this);
        row2column3 = findViewById(R.id.row2column3);
        row2column3.setOnClickListener(this);
        row3column3 = findViewById(R.id.row3column3);
        row3column3.setOnClickListener(this);
        row4column3 = findViewById(R.id.row4column3);
        row4column3.setOnClickListener(this);
    }

    private void fetchCategoriesAndClues() {
        triviaViewModel.initializeBoard(COUNT);
        /*jServiceAPI = RetroFitInstance.getRetrofitInstance().create(JServiceAPI.class);
        jServiceAPI.getCategories(COUNT)
                .flatMapIterable(categoryResponseList -> categoryResponseList) // converts your list of movieResponse into an observable which emits one movieResponse object at a time.
                .flatMap(new Function<CategoryObject, ObservableSource<SpecificCategory>>() {
                    @Override
                    public ObservableSource<SpecificCategory> apply(CategoryObject categoryObject) throws Throwable {
                        categoryID.add(categoryObject.getId());
                        return jServiceAPI.getClues(categoryObject.getId());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SpecificCategory>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull SpecificCategory specificCategory) {
                        listOfClues.add(specificCategory);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        initializeBoard();
                    }
                });*/
    }


    public void initializeBoard() {
        if (listOfClues.get(0).getClues_count() >= COUNT) {
            categoryOne.setText(listOfClues.get(0).getTitle());
            row1column0.setText(String.valueOf(listOfClues.get(0).getClues().get(0).getValue()));
            row2column0.setText(String.valueOf(listOfClues.get(0).getClues().get(1).getValue()));
            row3column0.setText(String.valueOf(listOfClues.get(0).getClues().get(2).getValue()));
            row4column0.setText(String.valueOf(listOfClues.get(0).getClues().get(3).getValue()));
        }

        if (listOfClues.get(1).getClues_count() >= COUNT) {
            categoryTwo.setText(listOfClues.get(1).getTitle());
            row1column1.setText(String.valueOf(listOfClues.get(1).getClues().get(0).getValue()));
            row2column1.setText(String.valueOf(listOfClues.get(1).getClues().get(1).getValue()));
            row3column1.setText(String.valueOf(listOfClues.get(1).getClues().get(2).getValue()));
            row4column1.setText(String.valueOf(listOfClues.get(1).getClues().get(3).getValue()));
        }

        if (listOfClues.get(2).getClues_count() >= COUNT) {
            categoryThree.setText(listOfClues.get(2).getTitle());
            row1column2.setText(String.valueOf(listOfClues.get(2).getClues().get(0).getValue()));
            row2column2.setText(String.valueOf(listOfClues.get(2).getClues().get(1).getValue()));
            row3column2.setText(String.valueOf(listOfClues.get(2).getClues().get(2).getValue()));
            row4column2.setText(String.valueOf(listOfClues.get(2).getClues().get(3).getValue()));
        }

        if (listOfClues.get(3).getClues_count() >= COUNT) {
            categoryFour.setText(listOfClues.get(3).getTitle());
            row1column3.setText(String.valueOf(listOfClues.get(3).getClues().get(0).getValue()));
            row2column3.setText(String.valueOf(listOfClues.get(3).getClues().get(1).getValue()));
            row3column3.setText(String.valueOf(listOfClues.get(3).getClues().get(2).getValue()));
            row4column3.setText(String.valueOf(listOfClues.get(3).getClues().get(3).getValue()));
        }
    }


    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();

        if (v.getId() == R.id.row1column0 || v.getId() == R.id.row2column0 || v.getId() == R.id.row3column0 || v.getId() == R.id.row4column0) {
            if (v.getId() == R.id.row1column0) {
                bundle.putString("question", listOfClues.get(0).getClues().get(0).getQuestion());
                bundle.putString("answer", listOfClues.get(0).getClues().get(0).getAnswer());
                bundle.putInt("value", listOfClues.get(0).getClues().get(0).getValue());
            } else if (v.getId() == R.id.row2column0) {
                bundle.putString("question", listOfClues.get(0).getClues().get(1).getQuestion());
                bundle.putString("answer", listOfClues.get(0).getClues().get(1).getAnswer());
                bundle.putInt("value", listOfClues.get(0).getClues().get(1).getValue());
            } else if (v.getId() == R.id.row3column0) {
                bundle.putString("question", listOfClues.get(0).getClues().get(2).getQuestion());
                bundle.putString("answer", listOfClues.get(0).getClues().get(2).getAnswer());
                bundle.putInt("value", listOfClues.get(0).getClues().get(2).getValue());
            } else if (v.getId() == R.id.row4column0) {
                bundle.putString("question", listOfClues.get(0).getClues().get(3).getQuestion());
                bundle.putString("answer", listOfClues.get(0).getClues().get(3).getAnswer());
                bundle.putInt("value", listOfClues.get(0).getClues().get(3).getValue());
            }
        } else if (v.getId() == R.id.row1column1 || v.getId() == R.id.row2column1 || v.getId() == R.id.row3column1 || v.getId() == R.id.row4column1) {
            if (v.getId() == R.id.row1column1) {
                bundle.putString("question", listOfClues.get(1).getClues().get(0).getQuestion());
                bundle.putString("answer", listOfClues.get(1).getClues().get(0).getAnswer());
                bundle.putInt("value", listOfClues.get(1).getClues().get(0).getValue());
            } else if (v.getId() == R.id.row2column1) {
                bundle.putString("question", listOfClues.get(1).getClues().get(1).getQuestion());
                bundle.putString("answer", listOfClues.get(1).getClues().get(1).getAnswer());
                bundle.putInt("value", listOfClues.get(1).getClues().get(1).getValue());
            } else if (v.getId() == R.id.row3column1) {
                bundle.putString("question", listOfClues.get(1).getClues().get(2).getQuestion());
                bundle.putString("answer", listOfClues.get(1).getClues().get(2).getAnswer());
                bundle.putInt("value", listOfClues.get(1).getClues().get(2).getValue());
            } else if (v.getId() == R.id.row4column1) {
                bundle.putString("question", listOfClues.get(1).getClues().get(3).getQuestion());
                bundle.putString("answer", listOfClues.get(1).getClues().get(3).getAnswer());
                bundle.putInt("value", listOfClues.get(1).getClues().get(3).getValue());
            }
        } else if (v.getId() == R.id.row1column2 || v.getId() == R.id.row2column2 || v.getId() == R.id.row3column2 || v.getId() == R.id.row4column2) {
            if (v.getId() == R.id.row1column2) {
                bundle.putString("question", listOfClues.get(2).getClues().get(0).getQuestion());
                bundle.putString("answer", listOfClues.get(2).getClues().get(0).getAnswer());
                bundle.putInt("value", listOfClues.get(2).getClues().get(0).getValue());
            } else if (v.getId() == R.id.row2column2) {
                bundle.putString("question", listOfClues.get(2).getClues().get(1).getQuestion());
                bundle.putString("answer", listOfClues.get(2).getClues().get(1).getAnswer());
                bundle.putInt("value", listOfClues.get(2).getClues().get(1).getValue());
            } else if (v.getId() == R.id.row3column2) {
                bundle.putString("question", listOfClues.get(2).getClues().get(2).getQuestion());
                bundle.putString("answer", listOfClues.get(2).getClues().get(2).getAnswer());
                bundle.putInt("value", listOfClues.get(2).getClues().get(2).getValue());
            } else if (v.getId() == R.id.row4column2) {
                bundle.putString("question", listOfClues.get(2).getClues().get(3).getQuestion());
                bundle.putString("answer", listOfClues.get(2).getClues().get(3).getAnswer());
                bundle.putInt("value", listOfClues.get(2).getClues().get(3).getValue());
            }
        } else if (v.getId() == R.id.row1column3 || v.getId() == R.id.row2column3 || v.getId() == R.id.row3column3 || v.getId() == R.id.row4column3) {
            if (v.getId() == R.id.row1column3) {
                bundle.putString("question", listOfClues.get(3).getClues().get(0).getQuestion());
                bundle.putString("answer", listOfClues.get(3).getClues().get(0).getAnswer());
                bundle.putInt("value", listOfClues.get(3).getClues().get(0).getValue());
            } else if (v.getId() == R.id.row2column3) {
                bundle.putString("question", listOfClues.get(3).getClues().get(1).getQuestion());
                bundle.putString("answer", listOfClues.get(3).getClues().get(1).getAnswer());
                bundle.putInt("value", listOfClues.get(3).getClues().get(1).getValue());
            } else if (v.getId() == R.id.row3column3) {
                bundle.putString("question", listOfClues.get(3).getClues().get(2).getQuestion());
                bundle.putString("answer", listOfClues.get(3).getClues().get(2).getAnswer());
                bundle.putInt("value", listOfClues.get(3).getClues().get(2).getValue());
            } else if (v.getId() == R.id.row4column3) {
                bundle.putString("question", listOfClues.get(3).getClues().get(3).getQuestion());
                bundle.putString("answer", listOfClues.get(3).getClues().get(3).getAnswer());
                bundle.putInt("value", listOfClues.get(3).getClues().get(3).getValue());
            }
        }
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, QuestionFragment.class, bundle)
                .commit();
    }

    @Override
    public void returnMainShowScore(int score) {
        gameScore += score;
        Log.d("main", String.valueOf(gameScore));
    }
}
