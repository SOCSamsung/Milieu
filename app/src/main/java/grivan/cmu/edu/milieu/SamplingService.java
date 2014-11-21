package grivan.cmu.edu.milieu;

import android.location.Location;
import grivan.cmu.edu.milieu.dto.Behavior;
import grivan.cmu.edu.milieu.dto.Point;
import grivan.cmu.edu.milieu.dto.Recommendation;
import grivan.cmu.edu.milieu.dto.StreetSample;
import grivan.cmu.edu.milieu.rest.RestHelper;

/**
 * Created by grivan on 11/21/14.
 */
public class SamplingService {

    Behavior behavior;

    public SamplingService(Behavior behav) {
        behavior = behav;
    };

    public Recommendation locationUpdate(Location location, String street) {
        StreetSample sample = new StreetSample();
        sample.setStreetName(street);
        sample.setSample(new Point(location.getLongitude(),location.getLatitude()));
        RestHelper.postStreetSample(sample);
        return RestHelper.getRecommendation(sample.getSample());
    }
}
