package grivan.cmu.edu.milieu.dto;

import com.google.gson.Gson;

public class Evaluation {
	private String streetName;
    private StreetSegment segment;
    private long milliseconds;

    public void setSegment(StreetSegment segment) {
        this.segment = segment;
    }
    
    public void setStreetName(String streetName) {
    	this.streetName = streetName;
    }
    
    public String getStreetName() {
    	return streetName;
    }

    public StreetSegment getSegment() {
        return segment;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
