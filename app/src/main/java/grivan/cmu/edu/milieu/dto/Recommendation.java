package grivan.cmu.edu.milieu.dto;

import com.google.gson.Gson;

public class Recommendation {
    private String recommendedURI;

    public void setRecommendedURI(String uri) {
        this.recommendedURI = uri;
    }

    public String getRecommendedURI() {
        return recommendedURI;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
