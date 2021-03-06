package com.example.mynotebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Signup extends AppCompatActivity {

    Firebase fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fb = new Firebase();
    }

    public void signup(View v) {
        // Finding the view, casting them to editText (which they are) to get access to their getText property
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        fb.signUp(email, password);
        // if successful
        // Toast?
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}