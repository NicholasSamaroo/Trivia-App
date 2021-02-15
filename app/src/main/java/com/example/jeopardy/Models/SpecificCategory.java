package com.example.jeopardy.Models;

import java.util.List;

public class SpecificCategory {
    String title;
    int clues_count;
    private List<Clues> clues;

    public String getTitle() {
        return title;
    }

    public int getClues_count() {
        return clues_count;
    }

    public List<Clues> getClues() {
        return clues;
    }
}
