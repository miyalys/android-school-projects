package org.eu.miyalys.diary.login;

import org.eu.miyalys.diary.LandingActivity;
import org.eu.miyalys.diary.R; // It was complaining about R missing
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    FirebaseLogin fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fb = new FirebaseLogin();
    }

    public void signup(View v) {
        // Finding the view, casting them to editText (which they are) to get access to their getText property
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        fb.signUp(email, password);
        // if successful
        // Toast?
        Intent i = new Intent(this, LandingActivity.class);
        startActivity(i);
    }
}