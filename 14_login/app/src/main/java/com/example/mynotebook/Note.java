package com.example.mynotebook;

import androidx.annotation.NonNull;

public class Note {

    public String title;
    public String text;
    public String user; // As identified by their e-mail address

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public Note(String title, String text, String user) {
        this.title = title;
        this.text = text;
        this.user = user;
    }

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
