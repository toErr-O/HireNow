package com.example.omar.cnghiring.Registration;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.omar.cnghiring.MainActivity;
import com.example.omar.cnghiring.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class confirmVerificationActivity extends AppCompatActivity {

    private String verificationID;
    FirebaseAuth mAuth;
    private  EditText editText;
    private ProgressBar progressBar;

    private  String name,nid,email,phoneNumber,pass1;
    //database
    DatabaseReference databaseLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_verification);

        mAuth=FirebaseAuth.getInstance();
        editText=findViewById(R.id.verification_code);
        progressBar=findViewById(R.id.progressBar1);
        //database code
        databaseLogin= FirebaseDatabase.getInstance().getReference("Login");


        name=getIntent().getStringExtra("name");
        nid=getIntent().getStringExtra("nid");
        email=getIntent().getStringExtra("email");
        phoneNumber=getIntent().getStringExtra("phonenumber");
        pass1=getIntent().getStringExtra("password");

        senderVerificationCode(phoneNumber);

        findViewById(R.id.verification_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=editText.getText().toString().trim();
                if(code.isEmpty() || code.length()!=6){
                    editText.setError("Enter the correct code");
                    editText.requestFocus();
                    return;
                }

                verifyCode(code);
            }
        });
    }
    private void verifyCode(String code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationID,code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //database code
                    LoginInformation loginInformation=new LoginInformation(name,nid,email,pass1);
                    databaseLogin.child(phoneNumber).setValue(loginInformation);
                    //database code ends here

                    Toast.makeText(confirmVerificationActivity.this,"Phone number successfully registered!",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(confirmVerificationActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    editText.setError("Wrong code!");
                    editText.requestFocus();

                    Toast.makeText(confirmVerificationActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void senderVerificationCode(String number){
        progressBar.setVisibility(View.VISIBLE);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID=s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if(code!=null){
                //progressBar.setVisibility(View.VISIBLE);
                editText.setText(code);
                verifyCode(code);
            }
            else {
                //database code
                LoginInformation loginInformation=new LoginInformation(name,nid,email,pass1);
                databaseLogin.child(phoneNumber).setValue(loginInformation);
                //database code ends here

                Toast.makeText(confirmVerificationActivity.this,"Phone number successfully registered!",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(confirmVerificationActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(confirmVerificationActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };

}
