package com.example.omar.cnghiring;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omar.cnghiring.dashboardpack.reqDetails;
import com.example.omar.cnghiring.directionHelper.FetchURL;
import com.example.omar.cnghiring.directionHelper.TaskLoadedCallback;
import com.example.omar.cnghiring.distanceHelper.GeoTask;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.omar.cnghiring.MainActivity.phoneNumberGlobal;

public class mapsActivity2 extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback,GeoTask.Geo {

    private double sourceLat,sourceLong;
    private double destinationLat,destinationLong;
    private double totalFair;

    Long phoneNumber;
    private double distance;
    private String duration;
    private String insertTime;

    private GoogleMap mMap;
    private MarkerOptions mksource,mkdest;
    private Polyline currentPolyline;
    private FusedLocationProviderClient client;

    DatabaseReference databaseReference;


    TextView distValue;
    TextView fairValue;
    TextView durValue;

    Button btnAC;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseReference=FirebaseDatabase.getInstance().getReference("fairRequest");


        //getting device current location
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(mapsActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(mapsActivity2.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                    sourceLat=location.getLatitude();
                    sourceLong=location.getLongitude();
                    LatLng source=new LatLng(sourceLat,sourceLong);

                    mksource = new MarkerOptions().position(source).title("Source");

                    Bundle b=getIntent().getExtras();
                    destinationLat=b.getDouble("lat");
                    destinationLong=b.getDouble("lng");
                    LatLng destination=new LatLng(destinationLat,destinationLong);

                    mkdest= new MarkerOptions().position(destination).title("Destination");

                    //for direction
                    new FetchURL(mapsActivity2.this).execute(getUrl(mksource.getPosition(), mkdest.getPosition(), "driving"), "driving");


                    //for distance
                    String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imerial&origins="+mksource.getPosition()+"&destinations="+mkdest.getPosition()+"&mode=driving&key=" + getString(R.string.google_maps_key)+"\n";
                    new GeoTask(mapsActivity2.this).execute(url);
                }
            }
        });



        setContentView(R.layout.activity_maps2);

        distValue=findViewById(R.id.distVal);
        fairValue=findViewById(R.id.distVal);
        durValue=findViewById(R.id.durVal);

        btnAC=findViewById(R.id.buttonAccept);

        btnAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //source got
                //destination got
                //totalFair got
                //distance got
                phoneNumber=Long.parseLong(phoneNumberGlobal);//getting the phone number
                insertTime=getCurrentTimeUsingDate();

                reqDetails reqDetailsObject=new reqDetails(sourceLat,sourceLong,destinationLat,destinationLong,totalFair,phoneNumber,distance,duration,insertTime);
                databaseReference.child(phoneNumberGlobal).setValue(reqDetailsObject);

                Intent intent=new Intent(mapsActivity2.this,MapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                Toast.makeText(mapsActivity2.this,"Successfully Requested.",Toast.LENGTH_SHORT).show();
            }
        });

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
    }
    public void setDouble(String result) {
        String res[]=result.split(",");

        Double dist=Double.parseDouble(res[1])/1000.0;
        distValue.setText(dist+"");
        distance=dist;//for database insertion


        Double minute=Double.parseDouble(res[0])/60.0;
        durValue.setText((int) (minute / 60) + " hr(s) " + (int) (minute % 60) + " min(s)");
        duration=(int) (minute / 60) + " hr(s) " + (int) (minute % 60) + " min(s)";//for database insertion

        //calculating fair
        //1st kilometer 40 taka and 10 taka per kilometer after that.
        double fair=40.0;
        if(dist>1.0)fair+=(dist-1.0)*10.0;
        fairValue.setText(fair+"");

        totalFair=fair;//for database insertion
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        mMap.addMarker(mksource);
        mMap.addMarker(mkdest);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sourceLat,sourceLong),15f));
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key)+"\n";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null) currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
    public String getCurrentTimeUsingDate() {

        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        return formattedDate;
    }
}
