package com.example.mynotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String saved;
    public static ArrayAdapter<Note> arrayAdapter;
    public static Notes notes;
    public static boolean runOnce = false;
    private Firebase fb;

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("all", "onResume running");

        if (!runOnce) {

            //notes = (Notes) getIntent().getSerializableExtra("notes");
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes.getNotes());
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Debugging
        Log.i("all", "onCreate Running");
        if  (savedInstanceState == null) Log.i("all", "savedInstanceState is null");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView mylw = findViewById(R.id.myListView);

        // Takes a context (how is this both a Context and an Activity? One is a subclass of another maybe?)
        FirebaseApp.initializeApp(this); // Android-studio apparently insisted on calling this before constructing a Firebase object
        fb = new Firebase(this);

        // Only run this once to avoid reinitializing and deleting saved notes
        if (!runOnce) {
        //if (savedInstanceState == null) {
            // To combine our list to the ListView an ArrayAdapter is needed.
            // It needs 1: a context, 2: a layout and 3: the data itself
            notes = new Notes();
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes.getNotes());

            runOnce = true;
        }

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
                        // Context, class to create an instance of
                        Intent i = new Intent(bind, SeeNote.class);
                        i.putExtra("position", position);
                        startActivity(i);
                    }
                }
        );
    }

    public void buttonHandler(View v) {

        Log.d("all", "buttonHandler running");

        switch(v.getId()) {

            case R.id.addnote:
                Log.d("myadd", "Add button clicked");
                // Add a new Note to the list. Adding it via the arrayAdapter instead of the list automatically updates the display to show that it was added
                // ...in which case notifyDataSetChanged is unnecessary in this case.
                arrayAdapter.add(new Note("New", "New"));

                //notes.add(new Note("New", "New"));
                // Update the display to show the newly added note - except it doesn't work!
                arrayAdapter.notifyDataSetChanged();

                // This was a useless attempt at making the list update properly:
                //runOnUiThread(new Runnable() {
                //    public void run() {
                //        arrayAdapter.notifyDataSetChanged();
                //    }
                //});
                break;
            case R.id.signup:
                // Using this hacky workaround because we pass this object to the new Intent, and by that time 'this' refers to OnItemClickListener. And apparently it insists on it being final, so...
                Intent i = new Intent(this, Signup.class);
                startActivity(i);
                break;
            case R.id.signin:
                // It's complaining that i is already defined in this scope which is false, or at least in a switch both of these branches won't happen simultaneously when there's a break statement there
                Intent i2 = new Intent(this, Signin.class);
                startActivity(i2);
                break;
        }
    }
}
