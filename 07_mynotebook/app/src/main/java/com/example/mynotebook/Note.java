package com.example.mynotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Note extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        TextView tv = findViewById(R.id.note_textView);
        String fromPage1 = getIntent().getStringExtra("mynote");
        tv.setText(fromPage1);
    }

}
