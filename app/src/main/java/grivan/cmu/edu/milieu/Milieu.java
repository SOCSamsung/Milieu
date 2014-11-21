package grivan.cmu.edu.milieu;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

import grivan.cmu.edu.milieu.dto.Behavior;
import grivan.cmu.edu.milieu.dto.Recommendation;
import grivan.cmu.edu.milieu.dto.StreetRegistration;
import grivan.cmu.edu.milieu.rest.RestHelper;

public class Milieu extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GetAddressCallback {

    String TAG = "milieu";

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private Behavior behavior = null;

    private EvaluationService evalService = null;
    private SamplingService samplService = null;

    private String currentStreet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milieu);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000*60); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        (new GetAddressTask(this)).execute(location);
        if (behavior != null && behavior.getBehavior().equals("evaluation")) {
            if (evalService != null) {
                if (evalService.locationUpdate(location)) {
                    evalService = null;
                }
            }
            else {
                evalService = new EvaluationService(behavior);
            }
        }
        if (behavior != null && behavior.getBehavior().equals("sample")) {
            if (samplService != null) {
                Recommendation reco = samplService.locationUpdate(location, currentStreet);
                Log.i(TAG,reco.toString());
            }
            else {
                samplService = new SamplingService(behavior);
            }
        }
    }

    @Override
    public void receiveAddress(String street) {
        if (behavior == null) {
            currentStreet = street;
            StreetRegistration sr = new StreetRegistration();
            sr.setStreetName(street);
            behavior = RestHelper.register(sr);
        }
        else {
            if (!currentStreet.equals(street)) {
                behavior = null;
                samplService = null;
                evalService = null;
            }
        }
    }
}