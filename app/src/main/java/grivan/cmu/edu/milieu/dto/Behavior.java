package grivan.cmu.edu.milieu.dto;

import java.util.List;

import com.google.gson.Gson;

public class Behavior {
    private String behavior;
    private List<Point> verificationPoints;

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }
    
    public void setVerificationPoints(List<Point> list) {
    	this.verificationPoints = list;
    }
    
    public List<Point> getVerificationPoints() {
    	return verificationPoints;
    }

    public String getBehavior() {
        return behavior;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
