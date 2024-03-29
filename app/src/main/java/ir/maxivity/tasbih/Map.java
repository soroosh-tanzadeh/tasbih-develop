package ir.maxivity.tasbih;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.neshan.core.Bounds;
import org.neshan.core.LngLat;
import org.neshan.core.Range;
import org.neshan.core.ViewportBounds;
import org.neshan.core.ViewportPosition;
import org.neshan.layers.VectorElementLayer;
import org.neshan.services.NeshanMapStyle;
import org.neshan.services.NeshanServices;
import org.neshan.styles.MarkerStyle;
import org.neshan.styles.MarkerStyleCreator;
import org.neshan.ui.MapView;
import org.neshan.utils.BitmapUtils;
import org.neshan.vectorelements.Marker;
import org.osmdroid.config.Configuration;

import java.text.DateFormat;
import java.util.Date;

import ir.maxivity.tasbih.dataAccess.map.DownloadTask;

public class Map extends Fragment implements DownloadTask.Callback{
    private static final String TAG = Map.class.getName();

    // used to track request permissions
    final int REQUEST_CODE = 123;
    // layer number in which map is added
    final int BASE_MAP_INDEX = 0;

    // location updates interval - 1 sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    // fastest updates interval - 1 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    // map UI element
    MapView map;
    FloatingActionButton actionButton;

    // You can add some elements to a VectorElementLayer
    VectorElementLayer userMarkerLayer;

    // User's current location
    private Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    private String lastUpdateTime;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Context ctx = getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setMapViewHardwareAccelerated(true);

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        map = view.findViewById(R.id.map);
        actionButton = view.findViewById(R.id.focusonuser);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusOnUserLocation();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // everything related to ui is initialized here
        initLayoutReferences();
        // Initializing user location
        initLocation();
        startReceivingLocationUpdates();
    }

    @Override
    public void onResume(){
        super.onResume();
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    // Initializing layout references (views, map and map events)
    private void initLayoutReferences() {
        // Initializing mapView element
        initMap();

        if (checkInternet()) {
            DownloadTask downloadTask = new DownloadTask(this);
            downloadTask.execute("https://api.neshan.org/points.geojson");
        }
    }


    // Initializing map
    private void initMap(){
        // Creating a VectorElementLayer(called userMarkerLayer) to add user marker to it and adding it to map's layers
        userMarkerLayer = NeshanServices.createVectorElementLayer();
        map.getLayers().add(userMarkerLayer);

        // add Standard_day map to layer BASE_MAP_INDEX
        map.getOptions().setZoomRange(new Range(4.5f, 18f));
        map.getLayers().insert(BASE_MAP_INDEX, NeshanServices.createBaseMap(NeshanMapStyle.STANDARD_DAY));

        // Setting map focal position to a fixed position and setting camera zoom
        map.setFocalPointPosition(new LngLat(51.330743, 35.767234),0 );
        map.setZoom(14,0);
    }


    private void initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        settingsClient = LocationServices.getSettingsClient(getActivity());

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                userLocation = locationResult.getLastLocation();
                lastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                onLocationChange();
            }
        };

        mRequestingLocationUpdates = false;

        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }


    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                        onLocationChange();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(Map.this.getActivity(), REQUEST_CODE);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(Map.this.getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }

                        onLocationChange();
                    }
                });
    }

    public void stopLocationUpdates() {
        // Removing location updates
        fusedLocationClient
                .removeLocationUpdates(locationCallback)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity().getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void startReceivingLocationUpdates() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void onLocationChange() {
        if(userLocation != null) {
            addUserMarker(new LngLat(userLocation.getLongitude(), userLocation.getLatitude()));
        }
    }


    // This method gets a LngLat as input and adds a marker on that position
    private void addUserMarker(LngLat loc){
        // Creating marker style. We should use an object of type MarkerStyleCreator, set all features on it
        // and then call buildStyle method on it. This method returns an object of type MarkerStyle
        MarkerStyleCreator markStCr = new MarkerStyleCreator();
        markStCr.setSize(30f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker_blue)));
        MarkerStyle markSt = markStCr.buildStyle();

        // Creating user marker
        Marker marker = new Marker(loc, markSt);

        // Clearing userMarkerLayer
        userMarkerLayer.clear();

        // Adding user marker to userMarkerLayer, or showing marker on map!
        userMarkerLayer.add(marker);
    }

    // This method gets a LngLat as input and adds a marker on that position
    private void addPinMarker(LngLat loc){
        // Creating marker style. We should use an object of type MarkerStyleCreator, set all features on it
        // and then call buildStyle method on it. This method returns an object of type MarkerStyle
        MarkerStyleCreator markStCr = new MarkerStyleCreator();
        markStCr.setSize(30f);
        markStCr.setBitmap(BitmapUtils.createBitmapFromAndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.t_pin_coffeeshop)));
        MarkerStyle markSt = markStCr.buildStyle();

        // Creating user marker
        Marker marker = new Marker(loc, markSt);

        // Adding user marker to userMarkerLayer, or showing marker on map!
        userMarkerLayer.add(marker);
    }

    public void focusOnUserLocation() {
        if(userLocation != null) {
            map.setFocalPointPosition(
                    new LngLat(userLocation.getLongitude(), userLocation.getLatitude()), 0.25f);
            map.setZoom(15, 0.25f);
        }
    }

    // Check for Internet connectivity.
    private Boolean checkInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }


    @Override
    public void onJsonDownloaded(JSONObject jsonObject) {
        try {
            JSONArray features = jsonObject.getJSONArray("features");
            // variable for creating bound
            // min = south-west
            // max = north-east
            double minLat = Double.MAX_VALUE;
            double minLng = Double.MAX_VALUE;
            double maxLat = Double.MIN_VALUE;
            double maxLng = Double.MIN_VALUE;
            for (int i = 0; i < features.length(); i++) {
                JSONObject geometry = features.getJSONObject(i).getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                LngLat lngLat = new LngLat(coordinates.getDouble(0), coordinates.getDouble(1));

                // validating min and max
                minLat = Math.min(lngLat.getY(), minLat);
                minLng = Math.min(lngLat.getX(), minLng);
                maxLat = Math.max(lngLat.getY(), maxLat);
                maxLng = Math.max(lngLat.getX(), maxLng);

                addPinMarker(lngLat);
            }
            map.moveToCameraBounds(
                    new Bounds(new LngLat(minLng, minLat), new LngLat(maxLng, maxLat)),
                    new ViewportBounds(
                            new ViewportPosition(0,0),
                            new ViewportPosition(map.getWidth(),map.getHeight())
                    ),
                    true, 0.25f);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
