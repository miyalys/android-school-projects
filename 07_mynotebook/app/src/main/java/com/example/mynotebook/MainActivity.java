package com.example.mynotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private String saved;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.myEditText);
    }

    public void savePressed(View v) {
        saved = editText.getText().toString();
        // Log info just for testing
        // Better than System.out.println, because this can be turned on/off easily.
        // The tag is useful because in LogCat you can specify in the right top side to only view log output for a specific tag.
        Log.i("all","saved: " + saved);
    }

    public void page2(View v) {
        Intent i = new Intent( this, Note.class);
        i.putExtra("mynote", saved);
        startActivity(i);
    }

}
