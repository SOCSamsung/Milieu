package grivan.cmu.edu.milieu;

import android.location.Location;
import java.util.ArrayList;
import grivan.cmu.edu.milieu.dto.Behavior;
import grivan.cmu.edu.milieu.dto.Evaluation;
import grivan.cmu.edu.milieu.dto.StreetSegment;
import grivan.cmu.edu.milieu.dto.Point;
import grivan.cmu.edu.milieu.rest.RestHelper;

/**
 * Created by grivan on 11/20/14.
 */
public class EvaluationService  {

    String TAG = "milieu.Evaluation";

    public final float TRIGGER_VP_DISTANCE = 10;

    ArrayList<Point> points;

    private StreetSegment segment;

    private boolean evaluating = false;

    private long startTime;


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

    public boolean locationUpdate(Location curLocation) {
        Point currentPt = new Point(curLocation.getLongitude() ,curLocation.getLatitude());
        Point nearestVp = nearestVerficationLocation(points,currentPt);
        float dist = distanceBw(nearestVp, currentPt);

        if (evaluating) {
            if (dist < TRIGGER_VP_DISTANCE) {
                return endEvaluation(nearestVp);
            }
        }
        else {
            if (dist < TRIGGER_VP_DISTANCE) {
                startEvaluation(nearestVp);
            }
        }
        return false;
    }

    private void startEvaluation(Point pt) {
        segment = new StreetSegment();
        points.remove(pt);
        segment.setPointA(pt);
        evaluating = true;
        startTime = System.currentTimeMillis();
        RestHelper.postEvaluationStart(pt);
    }

    private boolean endEvaluation(Point pt) {
        points.remove(pt);
        long duration = System.currentTimeMillis() - startTime;
        segment.setPointB(pt);
        Evaluation evaluation = new Evaluation();
        evaluation.setSegment(segment);
        evaluation.setMilliseconds(duration);
        evaluating = false;
        RestHelper.postEvaluationComplete(evaluation);
        if (points.isEmpty()) {
            return true;
        }
        return false;
    }
}
