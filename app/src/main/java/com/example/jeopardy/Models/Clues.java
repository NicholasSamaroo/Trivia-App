package com.example.jeopardy.Models;

public class Clues {
    private String answer;
    private String question;
    private int value;

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
