package com.example.mynotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class SeeNote extends AppCompatActivity {

    private EditText title;
    private EditText text;
    private Notes notes;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_note);

        // Get access to the editText fields from the layout
        title = findViewById(R.id.title);
        text = findViewById(R.id.text);

        // Fetch the specific Note clicked and populate the EditTexts
        Intent r = getIntent();
        position = r.getIntExtra("position", 0);
        Note note = MainActivity.arrayAdapter.getItem(position);
        //Note note = Notes.getNotes().get(position);
        title.setText(note.title);
        text.setText(note.text);
    }

    public void savePressed(View v) {
        // Fetch values typed in
        String savedTitle = title.getText().toString();
        String savedText = text.getText().toString();

        // Overwrite existing values with those typed in
        //Notes.getNotes().set(position, new Note(savedTitle, savedText));
        MainActivity.arrayAdapter.remove(MainActivity.arrayAdapter.getItem(position));
        MainActivity.arrayAdapter.insert(new Note(savedTitle, savedText), position);

        // Log info just for testing
        // Better than System.out.println, because this can be turned on/off easily.
        // The tag is useful because in LogCat you can specify in the right top side to only view log output for a specific tag.
        Log.d("all","title: " + savedTitle);
        Log.d("all","text: " + savedText);

        // Switch back to MainActivity after saving
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


}
