package com.example.omar.cnghiring.Registration;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.omar.cnghiring.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class registerActivty extends AppCompatActivity {

    private EditText name , nid ,email , phone , pass1 , pass2;
    String namel, nidl, emaill, phonel, pass1l, pass2l;

    Button register;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String verificationCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.nameRegister);
        nid = findViewById(R.id.nidRegister);
        email = findViewById(R.id.emailRegister);
        phone = findViewById(R.id.phoneRegister);
        pass1 = findViewById(R.id.passRegister1);
        pass2 = findViewById(R.id.passRegister2);

        register = findViewById(R.id.buttonRegister);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                namel=name.getText().toString().trim();
                nidl=nid.getText().toString().trim();
                emaill=email.getText().toString().trim();
                phonel=phone.getText().toString().trim();
                pass1l=pass1.getText().toString().trim();
                pass2l=pass2.getText().toString().trim();


                if(namel.isEmpty()){
                    name.setError("Name can't be Empty!");
                    name.requestFocus();
                    return;
                }
                if(nidl.length()!=13 && nidl.length()!=19){
                    nid.setError("NID length is not correct!");
                    nid.requestFocus();
                    return;
                }
                if(emaill.isEmpty()){
                    email.setError("Email can't be Empty!");
                    email.requestFocus();
                    return;
                }
                if(phonel.length()!=11){
                    phone.setError("Phone number length is not correct!");
                    phone.requestFocus();
                    return;
                }
                if(pass1l.isEmpty()){
                    pass1.setError("Password can't be Empty!");
                    pass1.requestFocus();
                    return;
                }
                if(!pass2l.equals(pass1l)){
                    pass2.setError("Password did't matched!");
                    pass2.requestFocus();
                    return;
                }

                phonel="+88"+phonel;


                //database query
                DatabaseReference rootref=FirebaseDatabase.getInstance().getReference();
                DatabaseReference userPhoneRef=rootref.child("Login").child(phonel);


                ValueEventListener eventListener=new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            Intent intent = new Intent(registerActivty.this, confirmVerificationActivity.class);
                            intent.putExtra("name", namel);
                            intent.putExtra("nid", nidl);
                            intent.putExtra("email", emaill);
                            intent.putExtra("phonenumber", phonel);
                            intent.putExtra("password", pass1l);

                            startActivity(intent);
                        }
                        else {
                            phone.setError("This phone number is already registerd!");
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
    }

    /**
     * Created by Vishal on 10/20/2018.
     */
}
