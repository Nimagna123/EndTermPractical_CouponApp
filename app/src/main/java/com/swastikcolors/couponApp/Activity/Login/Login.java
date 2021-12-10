package com.swastikcolors.couponApp.Activity.Login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;
import com.swastikcolors.couponApp.Activity.HomeScreen;
import com.swastikcolors.couponApp.Common;
import com.swastikcolors.couponApp.Constants;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.BaseAppCompatActivity;
import com.swastikcolors.couponApp.Utility.ManageJson;
import com.swastikcolors.couponApp.Utility.UploadAndDownload;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class Login extends BaseAppCompatActivity {

    EditText phoneNumberView;
    String phoneNumber;
    Button loginBtnView;
    AlertDialog alertDialog;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    FirebaseAuth auth;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setViews();
    }

    public void setViews()
    {
        alertDialog = new SpotsDialog.Builder().setContext(Login.this).build();
        alertDialog.setMessage("Sending OTP");
        phoneNumberView = findViewById(R.id.phoneNumberInput);
        loginBtnView = findViewById(R.id.loginBtn);
    }

    public void login(View v){
        phoneNumber = "+91"+phoneNumberView.getText().toString();
        if(phoneNumber.length()!=13){
            Toast.makeText(Login.this, "Error!!! Phone Number must be of 10 digits.", Toast.LENGTH_SHORT).show();
            return;
        }
        StartFirebaseLogin();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,    // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                Login.this,        // Activity (for callback binding)
                mCallback);
    }

    public void StartFirebaseLogin() {
        alertDialog.show();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Intent intent = new Intent(getApplicationContext(), OTPLogin.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("verified", "1");
                intent.putExtra("verificationCode", "");
                auth= FirebaseAuth.getInstance();
                auth.signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(task -> {
                            alertDialog.cancel();
                            startActivity(intent);
                        });

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                alertDialog.cancel();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(Login.this, "Error!!! Wrong Phone Number", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(Login.this, "Error!!! Please Try After Sometime...Too Many Request", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "Error!!! Please Try After Sometime..."+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                e.printStackTrace();
                e.getMessage();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                alertDialog.cancel();
                Intent intent = new Intent(getApplicationContext(), OTPLogin.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("verified", "0");
                intent.putExtra("verificationCode", s);
                startActivity(intent);
            }
        };
    }
}
