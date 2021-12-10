package com.swastikcolors.couponApp.Activity.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.swastikcolors.couponApp.Activity.HomeScreen;
import com.swastikcolors.couponApp.Activity.Login.SignUp;
import com.swastikcolors.couponApp.Common;
import com.swastikcolors.couponApp.Controller.ReadDatabaseAsyncClass;
import com.swastikcolors.couponApp.Controller.WriteDatabaseAsyncClass;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.BaseAppCompatActivity;
import com.swastikcolors.couponApp.Utility.ManageJson;

import java.util.HashMap;
import java.util.Objects;

public class PersonalProfile extends BaseAppCompatActivity {

    EditText phoneNumberView,nameView,pointsView,accountStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);
        setView();
    }

    public void setView(){
        ((TextView)findViewById(R.id.header)).setText(getResources().getString(R.string.app_name));
        nameView = findViewById(R.id.name);
        phoneNumberView = findViewById(R.id.phoneNumber);
        pointsView = findViewById(R.id.myPoints);
        accountStatusView = findViewById(R.id.accountsStatus);
        setCustomerDetails();
    }

    private void setCustomerDetails(){
        HashMap<String,String> filter = new HashMap<>();
        @SuppressLint("StaticFieldLeak")
        ReadDatabaseAsyncClass readDatabaseAsyncClass = new ReadDatabaseAsyncClass(PersonalProfile.this,
                "CustomerDetails","getCustomerDetailsCustomer",filter,true){
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                try {
                    JsonArray jsonArray = (JsonArray) ManageJson.jsonStringToObject(data, JsonArray.class);
                    JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                    nameView.setText(jsonObject.get("customerName").getAsString());
                    phoneNumberView.setText(jsonObject.get("phoneNumber").getAsString());
                    pointsView.setText(jsonObject.get("points").getAsString());
                    accountStatusView.setText(jsonObject.get("accountStatus").getAsString());
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        readDatabaseAsyncClass.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void updateProfile(View view){
        String customerName = nameView.getText().toString();
        if(customerName.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(),"Please fill your name",Toast.LENGTH_LONG).show();
            return;
        }
        HashMap<String,String> data = new HashMap<>();
        data.put("customerName",customerName);

        HashMap<String,String> filter = new HashMap<>();
        @SuppressLint("StaticFieldLeak") WriteDatabaseAsyncClass writeDatabaseAsyncClass = new WriteDatabaseAsyncClass(
                PersonalProfile.this,"CustomerDetails","updateCustomerDetailsCustomer",
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
                        Toast.makeText(getApplicationContext(),"Successfully Updated",Toast.LENGTH_LONG).show();
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
