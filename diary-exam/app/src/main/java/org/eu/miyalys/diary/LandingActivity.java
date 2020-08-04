package org.eu.miyalys.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.eu.miyalys.diary.login.FirebaseLogin;
import org.eu.miyalys.diary.login.SignupActivity;

/* The first Activity started, requiring login */
public class LandingActivity extends AppCompatActivity {

    FirebaseLogin fbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        fbl = new FirebaseLogin(this);
        // If already signed in while on this Activity, sign the user out
        //if (fbl.auth.getCurrentUser() != null) fbl.signOut();
        Toast.makeText(this, "User: m@m.me, pw: 111111", Toast.LENGTH_SHORT).show();
    }

    public void buttonHandler(View v) {

        switch (v.getId()) {

            case R.id.button_signup:
                Intent i = new Intent(this, SignupActivity.class);
                startActivity(i);
                break;
            case R.id.button_signin:
                signin(v);
                break;
        }
    }


    public void signin(View v) {
        // Finding the view, casting them to editText (which they are) to get access to their getText property
        String email = ((EditText) findViewById(R.id.email)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        fbl.signIn(email, password);
        // if successful: TODO: I need a different check, this doesn't always work
        if (fbl.auth.getCurrentUser() != null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        else Toast.makeText(this, "Login unsuccessful - try again!", Toast.LENGTH_SHORT).show();
    }
}