package grivan.cmu.edu.milieu.dto;

import com.google.gson.Gson;

public class Evaluation {
    private StreetSegment segment;
    private long milliseconds;

    public void setSegment(StreetSegment segment) {
        this.segment = segment;
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
