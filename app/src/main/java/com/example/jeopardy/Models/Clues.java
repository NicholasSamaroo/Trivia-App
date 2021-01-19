package com.example.jeopardy.Models;

public class Clues {
    private String answer;
    private String question;
    private int value;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getQuestion() {
        return question;
    }

    public int getValue() {
        return value;
    }
}
