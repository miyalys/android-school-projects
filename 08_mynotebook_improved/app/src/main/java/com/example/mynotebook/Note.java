package com.example.mynotebook;

import androidx.annotation.NonNull;

public class Note {

    public String title;
    public String text;

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
