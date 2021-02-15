package com.example.jeopardy.Models;

public class Random {
    private int id;
    private String answer;
    private String question;
    private int value;
    private int category_id;
    private CategoryObject category;

    public int getId() {
        return id;
    }

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

    public int getCategory_id() {
        return category_id;
    }

    public CategoryObject getCategory() {
        return category;
    }
}
