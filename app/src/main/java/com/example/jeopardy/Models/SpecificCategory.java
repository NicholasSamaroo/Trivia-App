package com.example.jeopardy.Models;

import java.util.List;

public class SpecificCategory {
    String title;
    int clues_count;
    private List<Clues> clues;

    public void setClues(List<Clues> clues) {
        this.clues = clues;
    }

    public List<Clues> getClues() {
        return clues;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getClues_count() {
        return clues_count;
    }

    public void setClues_count(int clues_count) {
        this.clues_count = clues_count;
    }
}
