package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the RecyclerView from the XML file
        RecyclerView rv = findViewById(R.id.myRecyclerView);
        // Create and set its LayoutManager
        rv.setLayoutManager(new LinearLayoutManager(this));
        // Create and set its Adapter
        rv.setAdapter( new MyAdapter() );
    }
}
