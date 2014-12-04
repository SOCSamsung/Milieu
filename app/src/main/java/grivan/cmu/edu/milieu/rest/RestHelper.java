package grivan.cmu.edu.milieu.rest;

import grivan.cmu.edu.milieu.dto.Behavior;
import grivan.cmu.edu.milieu.dto.Evaluation;
import grivan.cmu.edu.milieu.dto.Recommendation;
import grivan.cmu.edu.milieu.dto.StreetRegistration;
import grivan.cmu.edu.milieu.dto.StreetSample;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by grivan on 11/21/14.
 */
public class RestHelper {

    private static final String REST_URL = "http://192.168.1.231:8080";

    interface MilieuInterface {
        @POST("/register")
        void register(@Body StreetRegistration registration,
                      Callback<Behavior> behaviorCallback);

        @POST("/recommendation")
        void recommendation(@Body Evaluation evaluation,
                            Callback<Recommendation> recommendationCallback);

        @POST("/evaluationstart")
        void evaluationStart(@Body Evaluation evaluation,
                                 Callback<Response> responseCallback);

        @POST("/evaluate")
        void evaluate(@Body Evaluation evaluation,
                      Callback<Response> responseCallback);

        @POST("/streetsample")
        void streetSample(@Body StreetSample sample,
                          Callback<Response> responseCallback);
    }

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setEndpoint(REST_URL)
            .build();

    private static MilieuInterface service = restAdapter.create(MilieuInterface.class);

    private static final String TAG = "RestHelper";

    public static void register(StreetRegistration registration, Callback<Behavior> callback) {
        service.register(registration,callback);
    }

    public static void getRecommendation(Evaluation evaluation,
                                                        Callback<Recommendation> callback) {
        service.recommendation(evaluation, callback);
    }

    public static void postEvaluationStart(Evaluation evaluation,
                                           Callback<Response> responseCallback) {
        service.evaluationStart(evaluation,responseCallback);
    }

    public static void postEvaluationComplete(Evaluation evaluation,
                                              Callback<Response> responseCallback) {
        service.evaluate(evaluation,responseCallback);
    }

    public static void postStreetSample(StreetSample sample,
                                        Callback<Response> responseCallback) {
        service.streetSample(sample,responseCallback);
    }
}