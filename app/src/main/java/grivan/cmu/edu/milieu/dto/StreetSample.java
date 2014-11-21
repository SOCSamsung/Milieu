package grivan.cmu.edu.milieu.dto;

import com.google.gson.Gson;

public class StreetSample {
    private String streetName;
    private Point sample;

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetName() {
        return streetName;
    }

    public Point getSample() {
        return sample;
    }

    public void setSample(Point sample) {
        this.sample = sample;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
