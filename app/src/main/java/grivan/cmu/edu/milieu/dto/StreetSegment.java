package grivan.cmu.edu.milieu.dto;

import com.google.gson.Gson;

public class StreetSegment {
    private Point a;
    private Point b;


    public void setPointA(Point a) {
        this.a = a;
    }

    public void setPointB(Point b) {
        this.b = b;
    }

    public Point getPointA() {
        return a;
    }

    public Point getPointB() {
        return b;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
