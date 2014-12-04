package grivan.cmu.edu.milieu;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;

import grivan.cmu.edu.milieu.dto.Behavior;
import grivan.cmu.edu.milieu.dto.Evaluation;
import grivan.cmu.edu.milieu.dto.Point;
import grivan.cmu.edu.milieu.dto.StreetSegment;
import grivan.cmu.edu.milieu.rest.RestHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by grivan on 11/20/14.
 */
public class EvaluationService  {

    String TAG = "milieu.Evaluation";

    public final float TRIGGER_VP_DISTANCE = 10;

    ArrayList<Point> points;

    private boolean evaluating = false;

    private long startTime;

    private Evaluation evaluation = null;

    public EvaluationService(Behavior behavior) {
        points = (ArrayList<Point>) behavior.getVerificationPoints();
    }

    private Point nearestVerficationLocation(ArrayList<Point> vPoints, Point curLocation) {

        Point nearest = null;
        float min_dist = Float.MAX_VALUE;

        for (Point pt : vPoints) {
            float dist = distanceBw(pt, curLocation);
            if (dist < min_dist) {
                nearest = pt;
                min_dist = dist;
            }
        }

        return nearest;
    }

    private float distanceBw(Point pt1, Point pt2) {
        float[] results = new float[1];
        Location.distanceBetween(pt1.getLatitude(),pt1.getLongitude(),pt2.getLatitude(),
                pt2.getLongitude(),results);
        return results[0];
    }

    public boolean locationUpdate(Location curLocation, String streetName) {
        Point currentPt = new Point(curLocation.getLongitude() ,curLocation.getLatitude());
        Point nearestVp = nearestVerficationLocation(points,currentPt);

        float dist = Float.MAX_VALUE;
        if (nearestVp != null) {
            dist = distanceBw(nearestVp, currentPt);
        }
        else {
            //End the evaluation
            return true;
        }

        if (evaluating) {
            if (dist < TRIGGER_VP_DISTANCE) {
                return endEvaluation(nearestVp);
            }
        }
        else {
            if (dist < TRIGGER_VP_DISTANCE) {
                evaluation = new Evaluation();
                evaluation.setStreetName(streetName);
                StreetSegment segment = new StreetSegment();
                evaluation.setSegment(segment);
                startEvaluation(nearestVp);
            }
        }
        return false;
    }

    private void startEvaluation(Point pt) {
        points.remove(pt);
        evaluation.getSegment().setPointA(pt);
        evaluating = true;
        startTime = System.currentTimeMillis();
        RestHelper.postEvaluationStart(evaluation, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.i(TAG, "Success at Evaluation Start");
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.i(TAG, "Failure at Evaluation Start"+retrofitError.toString());
            }
        });
    }

    private boolean endEvaluation(Point pt) {
        long duration = System.currentTimeMillis() - startTime;
        evaluation.getSegment().setPointB(pt);
        evaluation.setMilliseconds(duration);
        evaluating = false;
        RestHelper.postEvaluationComplete(evaluation, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.i(TAG, "Success at Evaluation End");
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.i(TAG, "Failure at Evaluation Complete"+retrofitError.toString());
            }
        });
        if (points.isEmpty()) {
            return true;
        }
        //get ready for next evaluation-segment
        evaluation.getSegment().setPointA(pt);
        return false;
    }
}
