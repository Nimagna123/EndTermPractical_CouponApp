package com.swastikcolors.couponApp.Activity.Login;

import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.swastikcolors.couponApp.Activity.HomeScreen;
import com.swastikcolors.couponApp.Common;
import com.swastikcolors.couponApp.Constants;
import com.swastikcolors.couponApp.Controller.CallUrlAsyncClass;
import com.swastikcolors.couponApp.Controller.WriteDatabaseAsyncClass;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.BaseAppCompatActivity;
import com.swastikcolors.couponApp.Utility.ManageJson;
import com.swastikcolors.couponApp.Utility.UploadAndDownload;

import java.util.HashMap;

public class SignUp extends BaseAppCompatActivity {

    EditText nameView,phoneNumberView;
    String cuser;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setViews();
        if(savedInstanceState!=null){
            cuser = savedInstanceState.getString("cuser");
        }
        else{
            cuser = getIntent().getStringExtra("cuser");
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("cuser",cuser);
    }

    public void setViews() {
        nameView = findViewById(R.id.name);
    }

    @SuppressLint("StaticFieldLeak")
    public void signUp(View view) {
        String customerName = nameView.getText().toString();
        if(customerName.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(),"Please fill your name",Toast.LENGTH_LONG).show();
            return;
        }
        HashMap<String,String> data = new HashMap<>();
        data.put("customerName",customerName);

        HashMap<String,String> filter = new HashMap<>();
        @SuppressLint("StaticFieldLeak") WriteDatabaseAsyncClass writeDatabaseAsyncClass = new WriteDatabaseAsyncClass(
                SignUp.this,"CustomerDetails","customerSignUp",
                data,filter,true
        ){
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                if(error.length()>0){
                    Toast.makeText(getApplicationContext(),error, Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        Common.name = customerName;
                        Common.sharedPreferences.edit().putString("name", Common.name).apply();
                        Common.loggedIn = "1";
                        Common.sharedPreferences.edit().putString("loggedIn", Common.loggedIn).apply();
                        Intent intent = new Intent(SignUp.this, HomeScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.error),Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        writeDatabaseAsyncClass.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}