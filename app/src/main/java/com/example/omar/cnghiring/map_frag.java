package com.example.omar.cnghiring;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omar.cnghiring.dashboardpack.DashboardActivity;
import com.example.omar.cnghiring.map1Helper.CustomPlaceInfoWindowAdapter;
import com.example.omar.cnghiring.map1Helper.LocationCallBack;
import com.example.omar.cnghiring.map1Helper.PlaceAutoCompleteAdapter;
import com.example.omar.cnghiring.map1Helper.PlaceInfo;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


public class map_frag extends Fragment implements GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String FINE_LOCATOIN = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATOIN = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int PLACE_PICKER_REQUEST = 45698;
    private static GoogleApiClient mGoogleApiClient;
    LocationManager locationManager;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -100), new LatLng(71, 136));
    static Location source, destination;

    //user permission
    private Boolean mLocationPermissionGranted = true;
    private ImageView placepicker, mGps, placeinfo,dashboard;
    private AutoCompleteTextView searchbar;
    private PlaceAutoCompleteAdapter placeACAdapter;
    private static PlaceInfo userPlaceInfo;
    private Marker mMarker;
    private SupportMapFragment mapFragment;
    private View view;
    private static boolean gpsEnabled = false;
    private static map_frag mapFrag;
    private FragmentActivity child_activity;

    static double dist;




    public map_frag() {

    }


    public static map_frag newInstance() {
        if (mapFrag == null)
            mapFrag = new map_frag();

        return mapFrag;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Toast.makeText(getActivity(), "Map is ready to show", Toast.LENGTH_LONG).show();
        // Add a marker in Sydney and move the camera

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.


                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init_searchbar();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map_frag, container, false);


        searchbar = (AutoCompleteTextView) view.findViewById(R.id.input_search_ma);
        placepicker = view.findViewById(R.id.place_picker);
        mGps = view.findViewById(R.id.gps);
        placeinfo = view.findViewById(R.id.place_info);
        dashboard=view.findViewById(R.id.dashboard);
        //direction = view.findViewById(R.id.direction);

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        isGPSEnable();

        if (gpsEnabled) {
            getLocationPermission();
        } else {
            Toast.makeText(getActivity(), "turn on gps", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, LOCATION_PERMISSION_REQUEST_CODE);
                        gpsEnabled = true;
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        child_activity = childFragment.getActivity();

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient
                    .Builder(child_activity)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(child_activity, this)
                    .build();
        }

        if (mMap == null) {
            initializeMap();
        }
    }

    private void init_searchbar() {

        placeACAdapter = new PlaceAutoCompleteAdapter(getActivity(), mGoogleApiClient, LAT_LNG_BOUNDS, null);
        searchbar.setAdapter(placeACAdapter);

        searchbar.setOnItemClickListener(AutoCompleteListener);

        searchbar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocate();
                }

                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });

        placeinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!mMarker.isInfoWindowShown()){
                        Double lat=mMarker.getPosition().latitude;
                        Double lng=mMarker.getPosition().longitude;

                        Intent intent=new Intent(getActivity(),mapsActivity2.class);

                        Bundle b=new Bundle();
                        b.putDouble("lat",lat);
                        b.putDouble("lng",lng);

                        intent.putExtras(b);

                        startActivity(intent);

                    }
                } catch (NullPointerException e) {
                    Log.e("init_searchbar: null", e.getMessage());
                }

            }
        });

        placepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),DashboardActivity.class);
                startActivity(intent);
            }
        });

        hideSoftKeyBoard();
    }

    private void geoLocate() {
        String searchString = searchbar.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);

        } catch (IOException e) {
            Log.e("Error in geocoder", "IOExepction " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            Log.e("Flocation", "geolocate found a location: " + address.toString());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }

    }

    private void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(child_activity);

        try {
            if (mLocationPermissionGranted) {
                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");

                        } else {
                            Toast.makeText(getActivity(), "Unable to locate current Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        } catch (SecurityException e) {

        } catch (NullPointerException e) {

        }
    }


    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        mMap.clear();

        mMap.setInfoWindowAdapter(new CustomPlaceInfoWindowAdapter(getActivity()));


        if (placeInfo != null) {
            try {
                String snippet = "Name: " + placeInfo.getName() + "\n" +
                        "Address: " + placeInfo.getAddress() + "\n" +
                        "LatLng: " + placeInfo.getLatLng() + "\n";

                MarkerOptions options = new MarkerOptions().position(latLng).title(placeInfo.getName()).snippet(snippet);
                mMarker = mMap.addMarker(options);

            } catch (NullPointerException e) {
                Log.e("Move camera got null", e.getMessage());
            }
        } else {
            mMap.addMarker(new MarkerOptions().position(latLng));
        }

        hideSoftKeyBoard();
    }

    public void moveCamera(LatLng latLng, float zoom, String title) {


        if (mMap == null) {
            initializeMap();
        }

        mMap.clear();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));


        MarkerOptions options = new MarkerOptions().position(latLng).title(title);
        mMap.addMarker(options);

        hideSoftKeyBoard();


    }

    private void initializeMap () {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
    }


    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                String toastMsg = String.format("Place: %s", place.getLatLng().latitude);
                //Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                Log.e("Entrd in onactivrslt", toastMsg);
                //searchbar.setText(place.getAddress());
                //hideSoftKeyBoard();

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, place.getId());
                placeResult.setResultCallback(mUpdatePlaceDetailsCallBack);
            }
        } else if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (!mLocationPermissionGranted) {
                getLocationPermission();
            }

        }
    }

    private void getLocationPermission ()
    {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getActivity(), FINE_LOCATOIN) == PackageManager.PERMISSION_GRANTED) {


            if (ContextCompat.checkSelfPermission(this.getActivity(), COARSE_LOCATOIN) == PackageManager.PERMISSION_GRANTED) {
                isGPSEnable();
                mLocationPermissionGranted = true;
                initializeMap();

            }
            else {
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }


    }

    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
    @NonNull int[] grantResults){
        mLocationPermissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;

                        }
                    }
                    mLocationPermissionGranted = true;
                    initializeMap();
                }
            }
        }

    }


    @Override
    public void onConnectionFailed (@NonNull ConnectionResult connectionResult){

    }


    public void hideSoftKeyBoard () {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void isGPSEnable () {
        LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            buildAlertMessageNoGps();
        }


    }

    private AdapterView.OnItemClickListener AutoCompleteListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



        final AutocompletePrediction item = placeACAdapter.getItem(position);
        final String placeid = item.getPlaceId();


        PendingResult<PlaceBuffer> pendingResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeid);

        pendingResult.setResultCallback(mUpdatePlaceDetailsCallBack);

        hideSoftKeyBoard();

        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallBack = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
        if (!places.getStatus().isSuccess()) {
            Log.e("ResultCallback", places.getStatus().toString());
            places.release();
            return;
        }

            final Place place = places.get(0);

            try {

                userPlaceInfo = new PlaceInfo();
                userPlaceInfo.setName(place.getName().toString() + "");
                userPlaceInfo.setAddress(place.getAddress().toString() + "");
                userPlaceInfo.setPhoneNumber(place.getPhoneNumber().toString() + "");
                /* userPlace.setAttributions(place.getAttributions().toString()+"");*/
                userPlaceInfo.setWebsiteUri(place.getWebsiteUri());
                userPlaceInfo.setLatLng(place.getLatLng());

                Log.d("ResultCallback Result", "" + userPlaceInfo.toString());


            } catch (NullPointerException e) {
                Log.e("Error to get place", e.toString());

            }

            moveCamera(place.getLatLng(), DEFAULT_ZOOM, userPlaceInfo);
            places.release();
        }
    };


    public static void get_distance (String from,final String to,
    final LocationCallBack locationCallBack)
    {
        source = new Location("to");
        destination = new Location("form");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        final GeoFire geoFire = new GeoFire(databaseReference);

        geoFire.getLocation(from, new com.firebase.geofire.LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                source.setLatitude(location.latitude);
                source.setLongitude(location.longitude);

                geoFire.getLocation(to, new com.firebase.geofire.LocationCallback() {
                    @Override
                    public void onLocationResult(String key, GeoLocation location) {
                        destination.setLatitude(location.latitude);
                        destination.setLongitude(location.longitude);
                        Log.e("source latlong ", source.getLatitude() + "  ..  " + source.getLongitude()); //value hase turned into 0.0
                        Log.e("destination latlong", destination.getLatitude() + "  ..  " + destination.getLongitude());
                        dist = source.distanceTo(destination);
                        Log.e("dist in mapfrag", "" + dist);
                        locationCallBack.onCallBack(dist);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}