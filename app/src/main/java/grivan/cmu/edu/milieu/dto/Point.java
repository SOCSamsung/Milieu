package grivan.cmu.edu.milieu.dto;

import com.google.gson.Gson;

public class Point {
    private double longitude;
    private double latitude;

    public Point(double longitude, double latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;

    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

