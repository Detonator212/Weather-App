
package com.example.weatherapp;

public class ListCity {
    private String name;
    private String country;

    public ListCity(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }
}