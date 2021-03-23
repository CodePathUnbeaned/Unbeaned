package com.unbeaned.app.models;

public class Place {

    private String name;
    private double rating;
    private String address;
    private String price;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public String getAddress() {
        return address;
    }

    public String getPrice() {
        return price;
    }
}
