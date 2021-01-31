package com.example.jeopardy.View;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jeopardy.R;

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
    private EditText userAnswer;
    private TextView countdown;
    boolean check;

    private final View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkAnswer();
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

        TextView question = view.findViewById(R.id.question);
        question.setText(requireArguments().getString("question"));

        value = requireArguments().getInt("value");

        answer = requireArguments().getString("answer");
        Log.d("fragmentAnswer", answer);
        userAnswer = view.findViewById(R.id.answer);

        countdown = view.findViewById(R.id.countdown);

        Button submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(submitListener);
        startTimer();
    }

    private void checkAnswer() {
        check = answer.toLowerCase().equals(userAnswer.getText().toString().toLowerCase());
    }

    private void startTimer() {
        CountDownTimer countDownTimer = new CountDownTimer(BlitzActivity.COUNT_DOWN_IN_MILLIS, 1000) {
            String countDownHolder;

            @Override
            public void onTick(long millisUntilFinished) {
                countDownHolder = "" + millisUntilFinished / 1000;
                countdown.setText(countDownHolder);
                if(check) {
                    cancel();
                    mainShowInterface.returnMainShowScore(value);
                    getActivity().getSupportFragmentManager().beginTransaction().remove(QuestionFragment.this).commit();
                }
            }

            @Override
            public void onFinish() {
                cancel();
                mainShowInterface.returnMainShowScore(value * -1);
                getActivity().getSupportFragmentManager().beginTransaction().remove(QuestionFragment.this).commit();
            }
        }.start();
    }
}