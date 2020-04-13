package com.example.omar.cnghiring.dashboardpack;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.omar.cnghiring.MapsActivity;
import com.example.omar.cnghiring.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestDetailsClick_activity extends AppCompatActivity {

    TextView rqSourceLat,rqSourceLong, rqDestinationLat,rqDestinationLong, rqFair, rqPhonenumber, rqDistance, rqDuration, rqInsertTime;
    private reqDetails rqdetail;
    DatabaseReference databaseReference;

    Button btn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details_click);

        Firebase.setAndroidContext(RequestDetailsClick_activity.this);
        rqdetail = (reqDetails) getIntent().getSerializableExtra("request");

        rqSourceLat =findViewById(R.id.slat2);
        rqSourceLong=findViewById(R.id.slong2);

        rqDestinationLat=findViewById(R.id.dlat2);
        rqDestinationLong=findViewById(R.id.dlong2);

        rqFair =findViewById(R.id.fair2);
        rqPhonenumber =findViewById(R.id.phn2);
        rqDistance =findViewById(R.id.distance2);
        rqDuration =findViewById(R.id.dur2);
        rqInsertTime=findViewById(R.id.time2);

        btn=findViewById(R.id.buttonacreq);


        rqSourceLat.setText(rqdetail.getSourceLat()+"");
        rqSourceLong.setText(rqdetail.getSourceLong()+"");
        rqDestinationLat.setText(rqdetail.getDestinationLat()+"");
        rqDestinationLong.setText(rqdetail.getDestinationLong()+"");
        rqFair.setText(rqdetail.getFair()+"");
        rqPhonenumber.setText("+"+rqdetail.getPhoneNumber());
        rqDistance.setText(rqdetail.getDistance()+"");
        rqDuration.setText(rqdetail.getDuration());
        rqInsertTime.setText(rqdetail.getInsertTime());




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long phone=rqdetail.getPhoneNumber();
                databaseReference=FirebaseDatabase.getInstance().getReference("fairRequest").child("+"+phone);
                databaseReference.removeValue();

                final int REQUEST_PHONE_CALL=1;
                Intent callIntent=new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+"+phone));


                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(RequestDetailsClick_activity.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(RequestDetailsClick_activity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                    }
                    else {
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(callIntent);
                    }
                }


            }
        });
    }

}
