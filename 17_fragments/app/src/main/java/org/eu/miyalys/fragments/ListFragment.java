package org.eu.miyalys.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    private static MainActivity mainActivity;
    private ListView myListView;

    public static void setMainActivity(MainActivity m) {
        mainActivity = m;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create a list, add some data to it, create an ArrayAdapter for it
        List<String> miList = new ArrayList<>();
        miList.add("First");
        miList.add("Second");
        miList.add("Third");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, miList);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        // Obtain a reference to the ListView from the XML, and attach the adapter to the it
        myListView = view.findViewById(R.id.myListViewDesign);
        myListView.setAdapter(arrayAdapter);

        // When receiving a click propagate it to MainActivity, the FrameLayout container
        myListView.setOnItemClickListener((adapterView, view2, adapter_pos, row_id) -> {
          mainActivity.clickHandler(miList.get((int) row_id));
        });
        return view;
    }
}