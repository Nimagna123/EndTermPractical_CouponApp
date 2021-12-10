package com.swastikcolors.couponApp.Activity.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;
import com.swastikcolors.couponApp.Activity.HomeScreen;
import com.swastikcolors.couponApp.Common;
import com.swastikcolors.couponApp.Constants;
import com.swastikcolors.couponApp.Controller.CallUrlAsyncClass;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.BaseAppCompatActivity;
import com.swastikcolors.couponApp.Utility.ManageJson;
import com.swastikcolors.couponApp.Utility.UploadAndDownload;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;

public class OTPLogin extends BaseAppCompatActivity {

    EditText OTPView;
    FirebaseAuth auth;
    String phoneNumber;
    String OTP,verified,verificationCode;
    TextView numberMessageView;
    AlertDialog alertDialogMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_login);
        if(savedInstanceState!=null){
            verified = savedInstanceState.getString("verified");
            phoneNumber = savedInstanceState.getString("phoneNumber");
            verificationCode = savedInstanceState.getString("verificationCode");
        }
        else{
            verified = getIntent().getStringExtra("verified");
            phoneNumber = getIntent().getStringExtra("phoneNumber");
            verificationCode = getIntent().getStringExtra("verificationCode");
        }
        setViews();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("verified",verified);
        savedInstanceState.putString("phoneNumber",phoneNumber);
        savedInstanceState.putString("verificationCode",verificationCode);
    }

    public void setViews(){
        OTPView = (EditText) findViewById(R.id.OTP);
        numberMessageView = (TextView) findViewById(R.id.numberMessage);
        numberMessageView.setText("Please enter the OTP sent to "+phoneNumber);
        alertDialogMain = new SpotsDialog.Builder().setContext(OTPLogin.this).build();
        alertDialogMain.setMessage("Verifying");
        auth= FirebaseAuth.getInstance();
        if(verified.equals("1")) {
            alertDialogMain.show();
            OTPView.setText(verificationCode);
            serverLogin();
        }
    }

    public void verifyOTP(View view) {
        alertDialogMain.show();
        OTP=OTPView.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, OTP);
        SigninWithPhone(credential);
    }

    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            serverLogin();
                        } else {
                            OTPView.setText("");
                            alertDialogMain.cancel();
                            Toast.makeText(getApplicationContext(),"Incorrect OTP",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @SuppressLint("StaticFieldLeak")
    public void serverLogin(){
        HashMap<String,String> data = new HashMap<>();
        data.put("phoneNumber",phoneNumber);
        data.put("firebaseId",auth.getUid());
        String url = Constants.login+"?"+UploadAndDownload.setURLVariables(data);

        CallUrlAsyncClass callUrlAsyncClass= new CallUrlAsyncClass(OTPLogin.this,url,false){
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                alertDialogMain.cancel();
                if(error.length()>0){
                    Toast.makeText(getApplicationContext(),error, Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent;
                    JsonObject jsonObject = (JsonObject) ManageJson.jsonStringToObject(data, JsonObject.class);
                    Common.customerId = jsonObject.get("userId").getAsString();
                    Common.phoneNumber = jsonObject.get("phoneNumber").getAsString();
                    Common.password = jsonObject.get("password").getAsString();
                    Common.name = jsonObject.get("customerName").getAsString();
                    Common.cuser = Common.customerId +";"+ Common.password+";customer";
                    Common.sharedPreferences.edit().putString("name", Common.name).apply();
                    Common.sharedPreferences.edit().putString("customerId", Common.customerId).apply();
                    Common.sharedPreferences.edit().putString("phoneNumber", Common.phoneNumber).apply();
                    Common.sharedPreferences.edit().putString("password", Common.password).apply();
                    if(Common.name.equalsIgnoreCase("New Customer")){
                        intent = new Intent(OTPLogin.this, SignUp.class);
                        intent.putExtra("cuser",Common.cuser);
                    }
                    else {
                        Common.loggedIn = "1";
                        Common.sharedPreferences.edit().putString("loggedIn", Common.loggedIn).apply();
                        intent = new Intent(OTPLogin.this, HomeScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    startActivity(intent);
                    finish();
                }
            }
        };
        callUrlAsyncClass.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}