package com.example.mynotebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // This cannot be done in onCreate because it may be called multiple times, each time emptying the list
    public static List<Note> notes = new ArrayList<>();
    public static ArrayAdapter<Note> arrayAdapter;
    // Determines the name of the data on Firestore?:
    public static String notesPath;

    // Do I need this?
    //@Override
    //protected void onResume() {
    //    super.onResume();
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        notesPath = "notes";

        setContentView(R.layout.activity_main);
        ListView mylw = findViewById(R.id.myListView);

        // To combine our list to the ListView an ArrayAdapter is needed.
        // It needs 1: a context, 2: a layout and 3: the data itself

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);

        // Pair the ArrayAdapter with the ListView:
        mylw.setAdapter(arrayAdapter);
        // Give FirebaseRepo access to the arrayAdapter as well
        //FirebaseRepo.adapter = arrayAdapter;

        // Using this hacky workaround because we pass this object to the new Intent, and by that time 'this' refers to OnItemClickListener.
        // And apparently it insists on it being final, so...
        final MainActivity bind = this;

        // Add a click listener to handle what happens when an item on the list is clicked:
        mylw.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    // The AdapterView is the entire containing element, the View the specific element clicked, int its position in the Array and long its id? What ID?
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(bind, SeeNote.class);
                        // Tell SeeNote which specific Note to show/edit
                        i.putExtra("position", position);
                        startActivity(i);
                    }
                });
    }

    public void addButtonClicked(View v){
      Log.i("myadd", "Add button clicked and registered");

      // Access a Cloud Firestore instance from my Activity
      DocumentReference docRef = FirebaseRepo.db.collection(notesPath).document();
      Map<String, String> map = new HashMap<>();
      map.put("title", "New");
      map.put("text", "New");
      docRef.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
              Log.i("all", "Added successfully");
          }
      });
    }
}
