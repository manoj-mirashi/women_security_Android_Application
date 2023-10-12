package com.example.women_security_app;

public class petrolpump {

    private String name,open_now;
    private Double rating;

    public petrolpump(String name, String open_now, Double rating) {

        this.name = name;
        this.open_now = open_now;
        this.rating = rating;
    }



    public String getName() {
        return name;
    }

    public String getOpen_now() {
        return open_now;
    }

    public Double getRating() {
        return rating;
    }
}
