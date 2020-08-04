package com.example.quarantineapplication;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quarantineapplication.adapter.ForbiddenPlacesAdapter;
import com.example.quarantineapplication.model.Place;
import com.example.quarantineapplication.model.PlaceCategory;

import java.util.ArrayList;
import java.util.List;

public class ForbiddenPlacesActivity extends AppCompatActivity {

    List<Place> placeList;
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forbidden_places);

        placeList = new ArrayList<>();

        //Add places
        placeList.add(new Place(R.drawable.islandsbrygge,"Islands Brygge","Islands Brygge, Omkring Havnebadet" +
                "og Kulturhuset og hele Havneparken", PlaceCategory.FORBIDDEN));

        placeList.add(new Place(R.drawable.goteborgplads,"Göteborg Plads","Omkring havneområdet", PlaceCategory.FORBIDDEN));
        placeList.add(new Place(R.drawable.dronninglouisesbro,"Dronning Louises Bro","Bænke i solen på broen og ved søerne", PlaceCategory.FORBIDDEN));


        listView = findViewById(R.id.listview_forbidden);
        //Adapter created, passing this as context, the list view as layout, and the placelist
        ForbiddenPlacesAdapter adapter = new
                ForbiddenPlacesAdapter(this,R.layout.place_list_layout,placeList);
        //Attach the adapter to the listview
        listView.setAdapter(adapter);


    }


}
