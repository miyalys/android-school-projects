package com.example.mynotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String saved;
    public static List<Note> notes = new ArrayList<>();
    public ArrayAdapter<Note> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView mylw = findViewById(R.id.myListView);

        // Create list of MyNotes for the ListView, and populate it with some example entries
        //Notes notes = new Notes();
        //notes.testInit();

        // To combine our list to the ListView an ArrayAdapter is needed.
        // It needs 1: a context, 2: a layout and 3: the data itself
        Notes notes = Notes.init();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes.getNoteList());

        // Pair the ArrayAdapter with the ListView:
        mylw.setAdapter(arrayAdapter);

        // Using this hacky workaround because we pass this object to the new Intent, and by that time 'this' refers to OnItemClickListener. And apparently it insists on it being final, so...
        final MainActivity bind = this;

        // Add a click listener to handle what happens when an item on the list is clicked:
        mylw.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    // The AdapterView is the entire containing element, the View the specific element clicked, int its position in the Array and long its id? What ID?
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(bind, SeeNote.class);
                        i.putExtra("position", position);
                        startActivity(i);
                    }
                });

    }

    public void addButtonClicked(View v){
      Log.i("myadd", "Add button clicked and registered");
      arrayAdapter.add(new Note("New", "New"));
      arrayAdapter.notifyDataSetChanged();
    }
}
