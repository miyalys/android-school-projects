package com.example.mynotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SeeNote extends AppCompatActivity {

    private EditText title;
    private EditText text;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_note);

        // Get access to the editText fields from the layout
        title = findViewById(R.id.title);
        text = findViewById(R.id.text);

        // Fetch the specific Note clicked and populate the EditTexts
        position = getIntent().getIntExtra("position", 0);
        // The note properties are currently public, so accessing them directly
        title.setText(FirebaseRepo.getNote(position).title);
        text.setText(FirebaseRepo.getNote(position).text);
    }

    public void deletePressed(View v) {
        FirebaseRepo.deleteNote(position);

        // Switch back to MainActivity after deleting
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void savePressed(View v) {
        // Fetch values typed in
        String savedTitle = title.getText().toString();
        String savedText = text.getText().toString();

        // Overwrite existing values with those typed in
        //MainActivity.notes.set(position, new Note(savedTitle, savedText));
        // Update: This should be enough, and then MainActivity should fetch the ones from Firebase?
        FirebaseRepo.editNote(position, savedTitle, savedText);

        // Log info just for testing
        // Better than System.out.println, because this can be turned on/off easily.
        // The tag is useful because in LogCat you can specify in the right top side to only view log output for a specific tag.
        Log.i("all","title: " + savedTitle);
        Log.i("all","text: " + savedText);

        // Switch back to MainActivity after saving
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
