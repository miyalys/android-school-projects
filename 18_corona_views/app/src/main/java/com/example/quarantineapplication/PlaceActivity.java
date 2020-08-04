package com.example.quarantineapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.quarantineapplication.model.Place;
import com.example.quarantineapplication.model.PlaceCategory;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    private List<Place> placeList;
    private float markerColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize our own temporary db of places
        tempPlaceDB();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));

                mMap.setMinZoomPreference(9); //Sets the zoom minimum

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("testlocation", "onProviderDisabled running");
                //LatLng copenhagen = new LatLng(55.676111, 12.568333);
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(copenhagen));
            }

        };

        int buttonClicked = getIntent().getIntExtra("btn_id_clicked", R.id.btnBeaches);
        PlaceCategory pc = getPlaceCategory(buttonClicked);

        // Fetch all the places for the current category clicked
        List<Place> placesForCategory = getPlacesForCategory(pc);

        for (Place p : placesForCategory) {
            mMap.addMarker(
                    new MarkerOptions().position(p.getLatLng())
                            .title(p.getName())
                            .snippet(p.getDescription())
                            .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
            );
        }
        //Checks what android version the device is running
        if (Build.VERSION.SDK_INT < 23) {
            // TODO: Fix? Needs additional error handling?
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                // mMap.clear(); //Not sure if needed?
                LatLng currentLocation;

                // Default location if this fails
                if (lastKnownLocation == null) {
                    currentLocation = new LatLng(55.676111, 12.568333); // Copenhagen
                }
                else {
                    currentLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                }

                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Last known location"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLocation));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
            }
        }


    }

    // The DB of places we have so far, gathered together so they're easily refactored to use Firestore
    private void tempPlaceDB() {

        placeList = new ArrayList<>();

        /* PARKS */
        placeList.add(new Place(R.drawable.islandsbrygge,"H.C. Ørstedsparken","Parken er et hyggeligt sted for hele familien", PlaceCategory.PARK, new LatLng( 55.681416, 12.566048 )));
        placeList.add(new Place(R.drawable.islandsbrygge,"Frederiksberg Have","Haven er et hyggeligt sted for hele familien", PlaceCategory.PARK, new LatLng( 55.675829, 12.526480 )));
        placeList.add(new Place(R.drawable.islandsbrygge,"Assistens Kirkegård","Kirkegården er et (knap så) hyggeligt sted for hele familien. Husk at respektere de andre, potentielt sørgende, gæster", PlaceCategory.PARK, new LatLng(55.690663, 12.549740)));
        placeList.add(new Place(R.drawable.islandsbrygge,"Fælledparken","Parken er et hyggeligt sted for hele familien", PlaceCategory.PARK, new LatLng(55.701018, 12.567979)));
        placeList.add(new Place(R.drawable.islandsbrygge,"Østre Anlæg","Anlægget er et hyggeligt sted for hele familien", PlaceCategory.PARK, new LatLng(55.690566, 12.581025)));

        /* FORESTS */
        placeList.add(new Place(R.drawable.islandsbrygge,"Den Røde Port - Amager Fælled","Beskrivelse", PlaceCategory.FOREST, new LatLng(55.6180998, 12.5783916)));
        placeList.add(new Place(R.drawable.islandsbrygge,"Kongelunden","Beskrivelse", PlaceCategory.FOREST, new LatLng(55.577186, 12.589090)));
        placeList.add(new Place(R.drawable.islandsbrygge,"Hareskoven","Beskrivelse", PlaceCategory.FOREST, new LatLng(55.765030, 12.428580)));
        placeList.add(new Place(R.drawable.islandsbrygge,"Rude Skov","Beskrivelse", PlaceCategory.FOREST, new LatLng(55.844834, 12.475625)));
        placeList.add(new Place(R.drawable.islandsbrygge,"Dyrehaven","Beskrivelse", PlaceCategory.FOREST, new LatLng(55.801884, 12.562464)));
        placeList.add(new Place(R.drawable.islandsbrygge, "Geelskov", "Beskrivelse", PlaceCategory.FOREST, new LatLng(55.805394, 12.474641)));

        /* LAKES */
        placeList.add(new Place(R.drawable.islandsbrygge,"Søerne","Et oplagt sted til en svømmetur", PlaceCategory.LAKE, new LatLng(55.687038, 12.564192)));
        placeList.add(new Place(R.drawable.islandsbrygge,"Utterslev Mose","\"Den Livlige Mose\", som de lokale kalder den", PlaceCategory.LAKE, new LatLng(55.717432, 12.507372)));
        placeList.add(new Place(R.drawable.islandsbrygge,"Andedammen nær Frederiksberg Rådhus","Meget kort men godt", PlaceCategory.LAKE, new LatLng(55.678674, 12.527843)));
        placeList.add(new Place(R.drawable.islandsbrygge,"Furesø","Stor og dyb", PlaceCategory.LAKE, new LatLng(55.798793, 12.416859)));
        placeList.add(new Place(R.drawable.islandsbrygge,"Bagsværd Sø","Hvis du lige er i nærheden", PlaceCategory.LAKE, new LatLng(55.771522, 12.463379)));

        /* BEACHES */
        placeList.add(new Place(R.drawable.islandsbrygge,"Amager Strand","Beskrivelse", PlaceCategory.BEACH, new LatLng(55.654715, 12.649344)));
        placeList.add(new Place(R.drawable.islandsbrygge,"Bellavue Strand","Beskrivelse", PlaceCategory.BEACH, new LatLng(55.778649, 12.592366)));
        placeList.add(new Place(R.drawable.islandsbrygge,"Dragør Strand","Beskrivelse", PlaceCategory.BEACH, new LatLng(55.587838, 12.674814)));
        placeList.add(new Place(R.drawable.islandsbrygge,"Svanemølle Strand","Beskrivelse", PlaceCategory.BEACH, new LatLng(55.720347, 12.583552)));
        placeList.add(new Place(R.drawable.islandsbrygge,"Hellerup Strand","Beskrivelse", PlaceCategory.BEACH, new LatLng(55.733099, 12.582455)));
    }

    private PlaceCategory getPlaceCategory(int buttonClicked) {
        switch(buttonClicked) {
            case R.id.btnParks:
                markerColor = BitmapDescriptorFactory.HUE_ROSE;
                return PlaceCategory.PARK;
            case R.id.btnForests:
                markerColor = BitmapDescriptorFactory.HUE_GREEN;
                return PlaceCategory.FOREST;
            case R.id.btnLakes:
                markerColor = BitmapDescriptorFactory.HUE_AZURE;
                return PlaceCategory.LAKE;
            default: // btnBeaches
                markerColor = BitmapDescriptorFactory.HUE_YELLOW;
                return PlaceCategory.BEACH;
        }
    }

    private List<Place> getPlacesForCategory(PlaceCategory pc) {
        return placeList.stream().filter(p -> p.getCategory().equals(pc)).collect(Collectors.toList());
    }

}