package ir.maxivity.tasbih;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.neshan.layers.VectorElementLayer;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ir.maxivity.tasbih.fragments.mapFragments.AddEventDialogFragment;
import ir.maxivity.tasbih.fragments.mapFragments.AddLocationFragment;
import ir.maxivity.tasbih.fragments.mapFragments.AddLocationInfoFragment;
import ir.maxivity.tasbih.fragments.mapFragments.BaseFragment;
import ir.maxivity.tasbih.fragments.mapFragments.FilterFragment;
import ir.maxivity.tasbih.fragments.mapFragments.LocationInfoFragment;
import ir.maxivity.tasbih.interfaces.MapListener;
import ir.maxivity.tasbih.models.AddFavortiePlace;
import ir.maxivity.tasbih.models.AddNewEvent;
import ir.maxivity.tasbih.models.AddNewEventBody;
import ir.maxivity.tasbih.models.AddNewLocationBody;
import ir.maxivity.tasbih.models.AddNewPlaceResponse;
import ir.maxivity.tasbih.models.GetPlaceBody;
import ir.maxivity.tasbih.models.GetPlaces;
import ir.maxivity.tasbih.models.GetPlacesBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tools.Utilities;

public class Map extends BaseFragment implements MapListener, AddEventDialogFragment.OnSubmitButtonClick {
    private static final String TAG = "FUCK MAP";

    // used to track request permissions
    final int REQUEST_CODE = 123;
    // layer number in which map is added
    final int BASE_MAP_INDEX = 0;

    // location updates interval - 1 sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    // fastest updates interval - 1 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    // map UI element


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

    private Boolean onAddLocationProgress = false;

    private Boolean onFilterSelect = false;
    private String filterName = "";


    //TAGS////
    private final String ADD_LOCATION_TAG = "add location";
    private final String ADD_LOCATION_INFO_TAG = "add location info";
    private final String FILTER_FRAGMENT = "filter fragment";
    private final String LOCATION_INFO_FRAGMENT = "location info fragment";


    //OSM MAP//
    private MapView map;
    private IMapController controller;
    private MyLocationNewOverlay myLocationNewOverlay;
    private boolean goToMyLocationAtFirst = false;
    private Marker newLocationMarker;
    private ArrayList<Marker> mapMarkers;
    private static final double MAP_ZOOM = 17;

    //VIEWS///
    FloatingActionButton actionButton, addLocationButton;
    private ImageView addLocationMarker, filterImage;
    private RelativeLayout bottomSheet;
    private BottomSheetBehavior behavior;
    private RelativeLayout searchBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Context ctx = getContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setMapViewHardwareAccelerated(true);

        View view = inflater.inflate(R.layout.fragment_map_reworked, container, false);
        bindViews(view);

        mapMarkers = new ArrayList<>();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                focusOnUserLocation();
            }
        });
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MainActivity main = (MainActivity) getActivity();
                if (main.application.getLoginLater()) {
                    Toast.makeText(getContext(), "شما باید در برنامه ثبت نام کنید", Toast.LENGTH_SHORT).show();
                } else {
                    startAddingLocation();
                }
            }
        });

        goToMyLocationAtFirst = true;

        return view;
    }

    private void startAddingLocation() {
        addLocationButton.hide();
        addLocationMarker.setVisibility(View.VISIBLE);
        searchBar.setVisibility(View.GONE);
        if (getCurrentFragment() instanceof FilterFragment) {
            dismissChildFragment(FILTER_FRAGMENT);
        }
        loadChildFragment(new AddLocationFragment(), ADD_LOCATION_TAG, false);

        onAddLocationProgress = true;
    }
    public boolean onBackPressed() {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return true;
        } else {
            return false;
        }
    }

    private void bindViews(View view) {
        map = view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        actionButton = view.findViewById(R.id.focusonuser);
        addLocationButton = view.findViewById(R.id.add_location);
        addLocationMarker = view.findViewById(R.id.location_marker);
        bottomSheet = view.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        searchBar = view.findViewById(R.id.search_bar_wrapper);
        filterImage = view.findViewById(R.id.filter_icon);

        filterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBar.setVisibility(View.VISIBLE);
                dissmissAllPreviousFragment();
                if (getCurrentFragment() instanceof FilterFragment) {
                    dismissChildFragment(FILTER_FRAGMENT);
                } else
                    loadChildFragment(new FilterFragment(), FILTER_FRAGMENT, true);
            }
        });

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                if (state == BottomSheetBehavior.STATE_COLLAPSED) {
                    searchBar.setVisibility(View.VISIBLE);
                    addLocationButton.show();
                    if (getCurrentFragment() instanceof AddLocationFragment) {
                        dismissChildFragment(ADD_LOCATION_TAG);
                        addLocationMarker.setVisibility(View.GONE);
                        onAddLocationProgress = false;
                    }
                    if (getCurrentFragment() instanceof AddLocationInfoFragment) {
                        onAddLocationInfoCancel();
                        onAddLocationCancel();
                    }
                    if (getCurrentFragment() instanceof FilterFragment) {
                        dismissChildFragment(FILTER_FRAGMENT);
                    }

                    if (getCurrentFragment() instanceof LocationInfoFragment) {
                        dismissChildFragment(LOCATION_INFO_FRAGMENT);
                        addLocationButton.show();
                        actionButton.show();
                    }
                }

                if (state == BottomSheetBehavior.STATE_EXPANDED) {

                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);

    }

    private void loadChildFragment(Fragment fragment, String tag, boolean replace) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        // transaction.setCustomAnimations(R.anim.enter_from_bottom , null);
        if (!replace)
            transaction.add(R.id.child_fragment, fragment, tag).commit();
        else
            transaction.replace(R.id.child_fragment, fragment, tag).commit();

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }


    @Override
    public void onStart() {
        super.onStart();
        initLocation();
        startReceivingLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
        initMap();
        startLocationUpdates();
        focusOnUserLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
        stopLocationUpdates();
    }


    // Initializing map
    private void initMap() {

        controller = map.getController();
        controller.setZoom(MAP_ZOOM);

        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.circle_16);

        myLocationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getContext()), map);
        myLocationNewOverlay.enableMyLocation();
        myLocationNewOverlay.setDirectionArrow(icon, icon);
        map.getOverlays().add(myLocationNewOverlay);

        controller.setCenter(myLocationNewOverlay.getMyLocation());
        controller.animateTo(myLocationNewOverlay.getMyLocation());

        onMarkersClickAction();
        mapEventReceiver();
        mapListener();
    }


    private void mapListener() {
        map.addMapListener(new org.osmdroid.events.MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                if (getCurrentFragment() instanceof FilterFragment) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    dismissChildFragment(FILTER_FRAGMENT);
                }
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                if (getCurrentFragment() instanceof FilterFragment) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    dismissChildFragment(FILTER_FRAGMENT);
                }
                return false;
            }
        });
    }

    private void mapEventReceiver() {
        MapEventsReceiver receiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                Log.v(TAG, "on map click");
                if (getCurrentFragment() instanceof FilterFragment) {
                    Log.v(TAG, "current fragment is not this");
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    dismissChildFragment(FILTER_FRAGMENT);
                }
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                Log.v(TAG, "on map long click");
                if (getCurrentFragment() instanceof FilterFragment) {
                    Log.v(TAG, "current fragment is not this");
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    dismissChildFragment(FILTER_FRAGMENT);
                }
                return false;
            }
        };

        MapEventsOverlay overlay = new MapEventsOverlay(getContext(), receiver);
        map.getOverlays().add(overlay);
    }

    private void addOrRemoveNewLocationMarker(boolean remove, Marker marker) {
        if (remove)
            map.getOverlays().remove(marker);
        else
            map.getOverlays().add(marker);
        map.invalidate();

    }

    private Marker createMarker(GeoPoint position, int drawable, String id) throws NullPointerException {
        Marker marker = new Marker(map);
        marker.setPosition(position);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(drawable));
        marker.setId(id);
        return marker;
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
                if (goToMyLocationAtFirst) {
                    focusOnUserLocation();
                    goToMyLocationAtFirst = false;
                }
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

    private void addFavoritePlaceRequest(String id) throws NullPointerException {
        final MainActivity main = (MainActivity) getActivity();
        final NasimDialog dialog = main.showLoadingDialog();

        RequestBody pass = RequestBody.create(Utilities.TEXT, main.application.getToken());
        RequestBody user_id = RequestBody.create(Utilities.TEXT, main.application.getUserId());
        RequestBody place_id = RequestBody.create(Utilities.TEXT, id);
        dialog.show();
        main.application.api.addFavoritePlace(user_id, pass, place_id)
                .enqueue(new Callback<AddFavortiePlace>() {
                    @Override
                    public void onResponse(Call<AddFavortiePlace> call, Response<AddFavortiePlace> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body().result == 1) {
                                Toast.makeText(getContext(), getString(R.string.add_successfully), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), response.body().data, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddFavortiePlace> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onLocationChange() {
        if (userLocation != null) {

        }

        try {
            if (!onAddLocationProgress) {
                if (onFilterSelect) {
                    getPlacesByFilter(filterName);
                } else {
                    getPlaces();
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void getPlacesByFilter(String name) {
        final MainActivity main = (MainActivity) getActivity();
        GetPlaceBody body = new GetPlaceBody();
        body.place_name = name;

        main.application.api.getPlace(RequestBody.create(Utilities.JSON, Utilities.createBody(body)))
                .enqueue(new Callback<GetPlaces>() {
                    @Override
                    public void onResponse(Call<GetPlaces> call, Response<GetPlaces> response) {
                        if (response.isSuccessful()) {
                            if (response.isSuccessful()) {
                                if (response.body().result == 1) {
                                    map.getOverlays().clear();
                                    map.getOverlays().add(myLocationNewOverlay);
                                    map.invalidate();
                                    mapMarkers.clear();
                                    for (GetPlaces.response res : response.body().data) {
                                        Double lat = Double.parseDouble(res.lat);
                                        Double lon = Double.parseDouble(res.lon);
                                        try {
                                            Marker marker = createMarker(new GeoPoint(lat, lon),
                                                    Utilities.getMarkerOnType(Utilities.getLocationType(Integer.parseInt(res.type))),
                                                    res.id);
                                            mapMarkers.add(marker);
                                            addOrRemoveNewLocationMarker(false, marker);
                                            onMarkersClickAction();
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetPlaces> call, Throwable t) {
                        Log.v(TAG, "body : " + t.getMessage());
                    }
                });
    }

    private void getPlaces() throws NullPointerException {
        final MainActivity main = (MainActivity) getActivity();
        GetPlacesBody body = new GetPlacesBody("200",
                map.getMapCenter().getLatitude() + "",
                map.getMapCenter().getLongitude() + "");


        main.application.api.getPlaces(RequestBody.create(Utilities.JSON, Utilities.createBody(body)))
                .enqueue(new Callback<GetPlaces>() {
                    @Override
                    public void onResponse(Call<GetPlaces> call, Response<GetPlaces> response) {
                        if (response.isSuccessful()) {
                            if (response.body().result == 1) {
                                map.getOverlays().clear();
                                map.getOverlays().add(myLocationNewOverlay);
                                map.invalidate();
                                mapMarkers.clear();
                                for (GetPlaces.response res : response.body().data) {
                                    Double lat = Double.parseDouble(res.lat);
                                    Double lon = Double.parseDouble(res.lon);
                                    try {
                                        Marker marker = createMarker(new GeoPoint(lat, lon),
                                                Utilities.getMarkerOnType(Utilities.getLocationType(Integer.parseInt(res.type))),
                                                res.id);
                                        mapMarkers.add(marker);
                                        addOrRemoveNewLocationMarker(false, marker);
                                        onMarkersClickAction();
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetPlaces> call, Throwable t) {
                        Log.v(TAG, "body : " + t.getMessage());
                    }
                });
    }

    private void getPlaceById(final String id) throws NullPointerException {
        final MainActivity main = (MainActivity) getActivity();
        GetPlaceBody body = new GetPlaceBody();
        body.id = id;

        main.application.api.getPlace(RequestBody.create(Utilities.JSON, Utilities.createBody(body)))
                .enqueue(new Callback<GetPlaces>() {
                    @Override
                    public void onResponse(Call<GetPlaces> call, Response<GetPlaces> response) {
                        if (response.isSuccessful()) {
                            if (response.body().result == 1) {
                                loadChildFragment(LocationInfoFragment.newInstance(response.body().data.get(0)), LOCATION_INFO_FRAGMENT, false);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GetPlaces> call, Throwable t) {
                        Log.v(TAG, "body : " + t.getMessage());
                    }
                });
    }


    private void onMarkersClickAction() {
        for (Marker marker : mapMarkers) {
            marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    Log.v(TAG, "marker :" + marker.getId());
                    dissmissAllPreviousFragment();
                    try {
                        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                        if (getCurrentFragment() instanceof AddLocationFragment) {
                            dismissChildFragment(ADD_LOCATION_TAG);
                        }
                        getPlaceById(marker.getId());
                        addLocationButton.hide();
                        actionButton.hide();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        }
    }


    public void focusOnUserLocation() {
        if (userLocation != null) {
            controller.setCenter(new GeoPoint(userLocation.getLatitude(), userLocation.getLongitude()));
            controller.animateTo(myLocationNewOverlay.getMyLocation());
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
    public void onAddLocationSubmit() {
        loadChildFragment(new AddLocationInfoFragment(), ADD_LOCATION_INFO_TAG, true);
        try {
            newLocationMarker = createMarker(new GeoPoint(map.getMapCenter().getLatitude()
                            , map.getMapCenter().getLongitude())
                    , R.drawable.marker2, "-1");
            addLocationMarker.setVisibility(View.GONE);

            addOrRemoveNewLocationMarker(false, newLocationMarker);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void addNewLocationRequest(HashMap<String, String> fields) throws NullPointerException {
        MainActivity main = (MainActivity) getActivity();
        AddNewLocationBody body = new AddNewLocationBody();
        body.img_address = fields.get("img_address");
        body.img_documents = fields.get("img_documents");
        body.description = fields.get("description");
        body.place_name = fields.get("place_name");
        body.phone = fields.get("phone");
        body.web_address = fields.get("web_address");
        body.lat = newLocationMarker.getPosition().getLatitude() + "";
        body.lon = newLocationMarker.getPosition().getLongitude() + "";
        body.user_id = main.application.getUserId();
        body.type = fields.get("type");

        final NasimDialog dialog = main.showLoadingDialog();
        dialog.show();
        main.application.api.addPlace(RequestBody.create(Utilities.JSON
                , Utilities.createBody(body)))
                .enqueue(new Callback<AddNewPlaceResponse>() {
                    @Override
                    public void onResponse(Call<AddNewPlaceResponse> call
                            , Response<AddNewPlaceResponse> response) {
                        dialog.dismiss();
                        dismissChildFragment(ADD_LOCATION_INFO_TAG);
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        addLocationButton.show();
                        addOrRemoveNewLocationMarker(true, newLocationMarker);

                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "مکان با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "مکان شما ثبت نشد", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AddNewPlaceResponse> call, Throwable t) {
                        Log.v(TAG, "fail add : " + t.getMessage());
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        addLocationButton.show();
                        dialog.dismiss();
                    }
                });

    }

    @Override
    public void onAddLocationCancel() {
        addLocationButton.show();
        addLocationMarker.setVisibility(View.GONE);
        dismissChildFragment(ADD_LOCATION_TAG);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        addOrRemoveNewLocationMarker(true, newLocationMarker);

        onAddLocationProgress = false;
    }

    @Override
    public void onAddLocationInfoSubmit(HashMap<String, String> fields) {
        //todo send request to server
    /*    addLocationButton.show();
        dismissChildFragment(ADD_LOCATION_INFO_TAG);*/
        addNewLocationRequest(fields);
    }

    @Override
    public void onAddLocationInfoCancel() {
        //addLocationButton.show();
        dismissChildFragment(ADD_LOCATION_INFO_TAG);
        addLocationMarker.setVisibility(View.VISIBLE);
        addOrRemoveNewLocationMarker(true, newLocationMarker);
    }

    @Override
    public void onSelectFilter(String name) {
        onFilterSelect = true;
        filterName = name;
        dismissChildFragment(FILTER_FRAGMENT);
    }

    @Override
    public void onFavoritePlaceClick(String id) {
        addFavoritePlaceRequest(id);
    }

    @Override
    public void onAddEventClick(final String id) {
        DialogFragment dialogFragment = AddEventDialogFragment.newInstance(id);
        dialogFragment.show(getChildFragmentManager(), "EVENT DIALOG FRAGMENT");
    }

    private void addEventRequest(String id, HashMap<String, String> fields) throws NullPointerException {
        final MainActivity main = (MainActivity) getActivity();
        final NasimDialog dialog = main.showLoadingDialog();
        dialog.show();
        AddNewEventBody body = new AddNewEventBody();
        body.disable = fields.get(AddEventDialogFragment.MAP_STATUS_KEY);
        body.place_id = id;
        body.text = fields.get(AddEventDialogFragment.MAP_TEXT_KEY);
        body.thumbnail = fields.get(AddEventDialogFragment.MAP_IMAGE_KEY);

        main.application.api.addnewEvent(RequestBody.create(Utilities.JSON, Utilities.createBody(body)))
                .enqueue(new Callback<AddNewEvent>() {
                    @Override
                    public void onResponse(Call<AddNewEvent> call, Response<AddNewEvent> response) {
                        dialog.dismiss();
                        dismissEventDialogFragment();
                        if (response.isSuccessful()) {
                        }
                        Toast.makeText(getContext(), "رویداد با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<AddNewEvent> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onEditSubmit(HashMap<String, String> fields) {
        dismissChildFragment(LOCATION_INFO_FRAGMENT);
        Toast.makeText(getContext(), "ذخیره شد.", Toast.LENGTH_SHORT).show();
    }

    private void dismissChildFragment(String tag) {
        Fragment fragment = getChildFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            if (fragment.getFragmentManager() != null) {
                fragment.getFragmentManager().popBackStack();
            }
        }
    }

    private void dismissEventDialogFragment() {
        Fragment prev = getChildFragmentManager().findFragmentByTag("EVENT DIALOG FRAGMENT");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }


    private void dissmissAllPreviousFragment() {
        FragmentManager fm = getChildFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    private Fragment getCurrentFragment() {
        FragmentManager manager = getChildFragmentManager();
        List<Fragment> fragments = manager.getFragments();

        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;

    }

    @Override
    public void onSubmitEventClick(String id, HashMap<String, String> fields) {
        try {
            addEventRequest(id, fields);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
