package com.example.quarantineapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quarantineapplication.R;
import com.example.quarantineapplication.model.Place;

import java.util.List;

public class ForbiddenPlacesAdapter extends ArrayAdapter<Place> {

    Context mCtx;
    int resource;
    List<Place> placeList;

    //Adapter constructor
    public ForbiddenPlacesAdapter(Context mCtx,int resource,List<Place> placeList){
        super(mCtx,resource,placeList);
        this.mCtx = mCtx;
        this.resource = resource;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx); //Inflater from context

        View view = inflater.inflate(resource,null);
        TextView placeTitle = view.findViewById(R.id.placeTitle);
        TextView placeDescr = view.findViewById(R.id.placeDescr);
        ImageView imageView_place = view.findViewById(R.id.imageView_place);

        //Data to be displayed
        Place place = placeList.get(position); //Get the pos
        placeTitle.setText(place.getName());
        placeDescr.setText(place.getDescription());
        imageView_place.setImageDrawable(mCtx.getResources().getDrawable(place.getImage()));

        return view;
    }
}
