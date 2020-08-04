package com.example.quarantineapplication.model;

import com.google.android.gms.maps.model.LatLng;

public class Place {

    private int image; //Image id´s
    private String name, description;
    private LatLng latLng;
    private PlaceCategory category;

    //Constructor
    public Place(int image, String name, String description, PlaceCategory category) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public Place(int image, String name, String description, PlaceCategory category, LatLng latLng){
        this(image,name,description, category);
        this.latLng = latLng;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public PlaceCategory getCategory() { return category; }

    public void setCategory(PlaceCategory category) { this.category = category; }

}
