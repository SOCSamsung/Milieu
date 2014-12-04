package grivan.cmu.edu.milieu;

import android.location.Location;
import android.util.Log;

import grivan.cmu.edu.milieu.dto.Behavior;
import grivan.cmu.edu.milieu.dto.Point;
import grivan.cmu.edu.milieu.dto.StreetSample;
import grivan.cmu.edu.milieu.rest.RestHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by grivan on 11/21/14.
 */
public class SamplingService {

    private static final String TAG = "SamplingService";
    Behavior behavior;

    public SamplingService(Behavior behav) {
        behavior = behav;
    };

    public void locationUpdate(Location location, String street) {
        StreetSample sample = new StreetSample();
        sample.setStreetName(street);
        sample.setSample(new Point(location.getLongitude(),location.getLatitude()));
        RestHelper.postStreetSample(sample, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.i(TAG, "Success at Street Sample");
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.i(TAG, "Failure at Street Sample"+retrofitError.toString());
            }
        });
    }
}
