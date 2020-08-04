package org.eu.miyalys.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private DetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Give the listFragment a reference to mainActivity
        ListFragment.setMainActivity(this);
        ListFragment lf = new ListFragment();
        presentFragment(R.id.miFrame, lf);
    }

    // Handles clicks on the list, redirect the user to the detailed view for the item
    public void clickHandler(String clicked_text) {
        presentFragment(R.id.miFrame, new DetailFragment(clicked_text));
    }

    // Replaces the fragment that has the passed in resourceId with the passed in fragment
    private void presentFragment(int resourceId, Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(resourceId, fragment)
                .commit();
    }
}