package com.example.omar.cnghiring;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.omar.cnghiring.Registration.registerActivty;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText phone, password;
    String phonel,passwordl;
    public static String phoneNumberGlobal;


    Button login_btn, register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone = findViewById(R.id.phoneHome);
        password = findViewById(R.id.passHome);
        login_btn = findViewById(R.id.buttonHomeLogin);
        register_btn = findViewById(R.id.buttonHomeRegister);




        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phonel=phone.getText().toString().trim();
                passwordl=password.getText().toString().trim();

                if(phonel.length()!=11){
                    phone.setError("Phone length is not correct");
                    phone.requestFocus();
                    return;
                }
                if(passwordl.isEmpty()){
                    password.setError("Password can't be empty");
                    password.requestFocus();
                    return;
                }

                phonel="+88"+phonel;

                DatabaseReference rootref=FirebaseDatabase.getInstance().getReference();
                DatabaseReference userPhoneRef=rootref.child("Login").child(phonel);

                ValueEventListener eventListener=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            String pass = dataSnapshot.child("pass1login").getValue(String.class);
                            if (pass.equals(passwordl))
                            {
                                Toast.makeText(MainActivity.this,"Successfully logged in!",Toast.LENGTH_LONG).show();

                                phoneNumberGlobal=phonel;
                                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                                startActivity(intent);

                            }
                            else
                            {
                                password.setError("Wrong password!");
                                password.requestFocus();
                                return;
                            }
                        }
                        else {
                            phone.setError("This phone number is not registered!");
                            phone.requestFocus();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("TAG",databaseError.getMessage());
                    }
                };
                userPhoneRef.addListenerForSingleValueEvent(eventListener);
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, registerActivty.class);
                startActivity(intent);

            }
        });
    }
    /*protected void onStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }*/
}
