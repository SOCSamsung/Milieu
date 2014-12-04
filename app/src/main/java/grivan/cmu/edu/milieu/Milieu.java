package grivan.cmu.edu.milieu;

import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import grivan.cmu.edu.milieu.dto.Behavior;
import grivan.cmu.edu.milieu.dto.StreetRegistration;
import grivan.cmu.edu.milieu.rest.RestHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        //allow network on main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
        (new GetAddressTask(this,this)).execute(location);
        if (behavior != null && behavior.getBehavior().equals("evaluate")) {
            if (evalService != null) {
                if (evalService.locationUpdate(location, currentStreet)) {
                    evalService = null;
                    behavior = null;
                }
            }
            else {
                evalService = new EvaluationService(behavior);
            }
        }
        if (behavior != null && behavior.getBehavior().equals("sample")) {
            if (samplService != null) {
                samplService.locationUpdate(location, currentStreet);
            }
            else {
                samplService = new SamplingService(behavior);
            }
        }
    }

    @Override
    public void receiveAddress(String street) {
        if (street == null || street.equals("")) {
            Toast.makeText(this,"Address Not found!",2);
        }

        if (behavior == null) {
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText(street);
            currentStreet = street;
            StreetRegistration sr = new StreetRegistration();
            sr.setStreetName(street);
            RestHelper.register(sr, new Callback<Behavior>() {
                @Override
                public void success(Behavior behav, Response response) {
                    TextView tv = (TextView) findViewById(R.id.textView2);
                    tv.setText(behav.getBehavior());
                    behavior = behav;
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    Log.i(TAG,"Failed to retrieve behaviour");
                }
            });
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