package com.example.mynotebook;

import androidx.annotation.NonNull;

public class Note {

    private static int counter = 0;
    public String id;
    public String title;
    public String text;

    public Note(String title, String text) {
        // Look into whether Firebase requires a String id or whether it can be int
        this.id = Integer.toString(counter);
        this.title = title;
        this.text = text;

        this.counter++;
    }

    public Note(String id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public String getId() { return this.id; }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
