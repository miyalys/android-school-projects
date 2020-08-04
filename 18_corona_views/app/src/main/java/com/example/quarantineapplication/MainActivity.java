package com.example.quarantineapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void place(View v) {
        Intent i = new Intent( this, PlaceActivity.class);
        // Pass into place what button was clicked, so it can load different lists on this basis
        i.putExtra ("btn_id_clicked", v.getId()); // v.getId returns an int
        startActivity(i);
    }

    public void covid19status(View v) {
        startActivity( new Intent(this, Covid19Activity.class ) );
    }

    public void forbidden(View v) {
        startActivity( new Intent(this, ForbiddenPlacesActivity.class ) );
    }

}
