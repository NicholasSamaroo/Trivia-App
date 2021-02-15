package com.example.jeopardy.View;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jeopardy.R;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int value;
    private String answer;
    private CountDownTimer countDownTimer;
    private TextInputEditText userAnswer;
    private TextView countdown;
    private boolean check;

    private final TextView.OnEditorActionListener editTextListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(checkAnswer()) {
                    countDownTimer.cancel();
                    Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();

                    mainShowInterface.returnMainShowScore(value);

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(0, R.anim.fade_out)
                            .remove(QuestionFragment.this).commit();
                } else {
                    Toast.makeText(getActivity(), "Incorrect!", Toast.LENGTH_LONG).show();
                }
            }
            return false;
        }
    };

    private mainShowInterface mainShowInterface;

    public interface mainShowInterface {
        void returnMainShowScore(int score);
    }

    public QuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(String param1, String param2) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainShowInterface = (mainShowInterface) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        check = false;

        value = requireArguments().getInt("value");
        TextView valueText = view.findViewById(R.id.value);
        valueText.setText(String.valueOf(value));

        TextView category = view.findViewById(R.id.category);
        category.setText(requireArguments().getString("category"));

        TextView question = view.findViewById(R.id.question);
        question.setText(requireArguments().getString("question"));

        answer = requireArguments().getString("answer").toLowerCase();
        Log.d("fragmentAnswer", answer);

        userAnswer = view.findViewById(R.id.answer);
        userAnswer.setOnEditorActionListener(editTextListener);

        countdown = view.findViewById(R.id.countdown);

        TextView score = view.findViewById(R.id.mainScore);
        score.setText(String.valueOf(requireArguments().getInt("gameScore")));

        startTimer();
    }

    private boolean checkAnswer() {
        return !(TextUtils.isEmpty(userAnswer.getText().toString())) &&
                userAnswer.getText().toString().toLowerCase().equals(answer);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(BlitzActivity.COUNT_DOWN_IN_MILLIS, 1000) {
            String countDownHolder;

            @Override
            public void onTick(long millisUntilFinished) {
                countDownHolder = "" + millisUntilFinished / 1000;
                countdown.setText(countDownHolder);
            }

            @Override
            public void onFinish() {
                cancel();
                mainShowInterface.returnMainShowScore(value * -1);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(0, R.anim.fade_out)
                        .remove(QuestionFragment.this).commit();
            }
        }.start();
        // remember to deallocate the timer or whatever was mentioned in the docs and stack overflow posts
        // handle this when functionality and styling is complete
        // Whenever the fragment is destroyed, etc.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}