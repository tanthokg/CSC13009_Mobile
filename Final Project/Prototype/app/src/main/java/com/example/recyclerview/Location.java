package com.example.recyclerview;

public class Location {
    private String name;
    private String country;
    private int visitors;
    private String imageUrl;

    public Location(String name, String country, int visitors, String imageUrl) {
        this.name = name;
        this.country = country;
        this.visitors = visitors;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVisitors() {
        return String.valueOf(visitors);
    }

    public void setVisitors(int visitors) {
        this.visitors = visitors;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
