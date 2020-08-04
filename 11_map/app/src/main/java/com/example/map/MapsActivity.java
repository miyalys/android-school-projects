package com.example.map;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    public List<LatLng> latLngs;
    private EditText in;
    private Marker curMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Store a reference to the add description input field (EditText)
        in = findViewById(R.id.in);

        // Store all the markers. Not that its currently needed
        latLngs = new ArrayList<>();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Start in Copenhagen and add a starter marker there
        LatLng copenhagen = new LatLng(55.676111, 12.568333);
        mMap.addMarker(new MarkerOptions().position(copenhagen).title("Marker in Copenhagen"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(copenhagen));
        //mMap.animateCamera(CameraUpdateFactory.newLatLng(copenhagen));
        // Higher values: More zoomed in:
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(copenhagen, 13));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng coordinate) {
                latLngs.add(coordinate);
                //mMap.clear();
                mMap.addMarker(new MarkerOptions().position(coordinate));
            }

        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            public boolean onMarkerClick(Marker m) {

                curMarker = m;
                in.setText(m.getTitle());

                // Afterwards do default behaviour of moving to the marker.
                return false;
            }

        });
    }

    public void btnSaveDescriptionClicked(View v) {
        // Captures any button currently, which so far is fine since there's only one
        //startActivity( new Intent(this, AddMarkerActivity.class ) );

        // Get the text, save it to the current marker, then clear it from the input field
        if (curMarker != null) { // Somewhat sketchy right now, since it means you basically update the descripttion of the most recently clicked Marker, even if none is currently clicked
            curMarker.setTitle(in.getText().toString());
            in.setText("");
        }
    }
}
