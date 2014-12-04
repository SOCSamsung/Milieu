package grivan.cmu.edu.milieu.dto;

import com.google.gson.Gson;

public class StreetRegistration {
    private String streetName;

    public void setStreetName(String name) {
        this.streetName = name;
    }

    public String getStreetName() {
        return streetName;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}