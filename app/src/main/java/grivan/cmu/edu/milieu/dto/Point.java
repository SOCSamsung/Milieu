package grivan.cmu.edu.milieu.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class Point implements Parcelable {
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

    public static Creator<Point> CREATOR =
            new Creator<Point>() {

                @Override
                public Point createFromParcel(Parcel source) {
                    double[] array = new double[2];
                    source.readDoubleArray(array);
                    return new Point(array[0],array[1]);
                }

                @Override
                public Point[] newArray(int size) {
                    return new Point[size];
                }
            };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        double[] array = {longitude,latitude};
        dest.writeDoubleArray(array);
    }
}
