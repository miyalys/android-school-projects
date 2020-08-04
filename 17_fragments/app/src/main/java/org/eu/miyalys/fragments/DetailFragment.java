package org.eu.miyalys.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    private String clicked_text;

    public DetailFragment() {}

    public DetailFragment(String clicked_text) {
       this.clicked_text = clicked_text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        // Setting the TextView to show the text that was clicked, as an example of passing data from one fragment to another (indirectly)
        ((TextView) v.findViewById(R.id.midetails)).setText(clicked_text);
        return v;
    }
}