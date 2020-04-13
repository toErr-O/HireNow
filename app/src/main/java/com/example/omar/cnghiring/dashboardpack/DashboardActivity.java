package com.example.omar.cnghiring.dashboardpack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.example.omar.cnghiring.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;

    private ArrayList<reqDetails> fairRequestList;

    private reqDetails fairRequest;
    private dashboard_item_view_adapter requestDetails_adapter;

    public DashboardActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        Firebase.setAndroidContext(DashboardActivity.this);
        recyclerView = findViewById(R.id.uc6_rv);
        fairRequestList = new ArrayList<reqDetails>();

        databaseReference = FirebaseDatabase.getInstance().getReference();


        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager( new LinearLayoutManager(DashboardActivity.this));
        recyclerView.addItemDecoration( new DividerItemDecoration(DashboardActivity.this, LinearLayout.VERTICAL));
        set_adapter("");
    }

    public void set_adapter(final String s)
    {

        databaseReference.child("fairRequest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                fairRequestList.clear();
                recyclerView.removeAllViews();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Double sourceLat= (Double) snapshot.child("sourceLat").getValue();
                    Double sourceLong= (Double) snapshot.child("sourceLong").getValue();
                    Double destinationLat= (Double) snapshot.child("destinationLat").getValue();
                    Double destinationLong= (Double) snapshot.child("destinationLong").getValue();
                    Double fair= (Double) snapshot.child("fair").getValue();
                    Long phoneNumber= (Long) snapshot.child("phoneNumber").getValue();
                    Double distance = (Double) snapshot.child("distance").getValue();
                    String duration = (String) snapshot.child("duration").getValue();
                    String insertTime = (String) snapshot.child("insertTime").getValue();

                    fairRequest= new reqDetails();

                    fairRequest.setSourceLat(sourceLat);
                    fairRequest.setSourceLong(sourceLong);
                    fairRequest.setDestinationLat(destinationLat);
                    fairRequest.setDestinationLong(destinationLong);
                    fairRequest.setFair(fair);
                    fairRequest.setPhoneNumber(phoneNumber);
                    fairRequest.setDistance(distance);
                    fairRequest.setDuration(duration);
                    fairRequest.setInsertTime(insertTime);

                    fairRequestList.add(fairRequest);

                }

                requestDetails_adapter = new dashboard_item_view_adapter(DashboardActivity.this, fairRequestList);
                recyclerView.setAdapter(requestDetails_adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
