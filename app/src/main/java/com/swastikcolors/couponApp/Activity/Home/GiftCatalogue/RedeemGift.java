package com.swastikcolors.couponApp.Activity.Home.GiftCatalogue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.swastikcolors.couponApp.Common;
import com.swastikcolors.couponApp.Controller.WriteDatabaseAsyncClass;
import com.swastikcolors.couponApp.R;

import java.util.HashMap;

public class RedeemGift extends AppCompatActivity {


    EditText nameEditText,phoneNumberEditText,pincodeEditText,addressEditText;
    TextInputLayout nameLayout,phoneNumberLayout,pincodeLayout,addressLayout;
    String giftId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_gift);
        if(savedInstanceState!=null){
            giftId = savedInstanceState.getString("giftId");
        }
        else{
            giftId = getIntent().getStringExtra("giftId");
        }
        ((TextView)findViewById(R.id.header)).setText(getResources().getString(R.string.app_name));
        setView();
    }

    public void setView(){
        nameEditText = findViewById(R.id.receiverName);
        phoneNumberEditText = findViewById(R.id.receiverPhoneNumber);
        pincodeEditText = findViewById(R.id.receiverPincode);
        addressEditText = findViewById(R.id.receiverAddress);

        nameLayout = findViewById(R.id.nameLayout);
        phoneNumberLayout = findViewById(R.id.phoneNumberLayout);
        pincodeLayout = findViewById(R.id.pincodeLayout);
        addressLayout = findViewById(R.id.addressLayout);
        nameEditText.setText(Common.name);
        phoneNumberEditText.setText(Common.phoneNumber.substring(3));

        nameErrorListener();
        phoneNumberErrorListener();
        pincodeErrorListener();
        addressErrorListener();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("giftId",giftId);
    }

    public void addressErrorListener(){
        addressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    addressLayout.setError(null);
                }
            }
        });
    }

    public void nameErrorListener(){
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    nameLayout.setError(null);
                }
            }
        });
    }

    public void phoneNumberErrorListener(){
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==10){
                    phoneNumberLayout.setError(null);
                }
            }
        });
    }

    public void pincodeErrorListener(){
        pincodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==6){
                    pincodeLayout.setError(null);
                }
            }
        });
    }

    public void redeemGift(View view){

        String receiverName = nameEditText.getText().toString();
        String receiverPhoneNumber = phoneNumberEditText.getText().toString();
        String receiverPincode = pincodeEditText.getText().toString();
        String receiverAddress = addressEditText.getText().toString();
        String fillThisField = "Fill This";
        String phoneNumberDigit = "Phone Number must be of 10 digits";
        String pincodeDigit = "Pincode must of 6 digits";
        int e=0;

        if(receiverName.equals("")){
            nameLayout.setError(fillThisField);
            e++;
        }

        if(receiverPincode.equals("")){
            pincodeLayout.setError(fillThisField);
            e++;
        }
        else if(receiverPincode.length()<6){
            pincodeLayout.setError(pincodeDigit);
            e++;
        }

        if(receiverPhoneNumber.equals("")){
            phoneNumberLayout.setError(fillThisField);
            e++;
        }
        else if(receiverPhoneNumber.length()<10){
            phoneNumberLayout.setError(phoneNumberDigit);
            e++;
        }

        if(receiverAddress.equals("")){
            addressLayout.setError(fillThisField);
            e++;
        }

        if(e==0){
            HashMap<String,String> data = new HashMap<>();
            data.put("receiverName",receiverName);
            data.put("receiverPhoneNumber",receiverPhoneNumber);
            data.put("receiverPincode",receiverPincode);
            data.put("receiverAddress",receiverAddress);
            data.put("giftId",giftId);

            HashMap<String,String> filter = new HashMap<>();
            @SuppressLint("StaticFieldLeak") WriteDatabaseAsyncClass writeDatabaseAsyncClass = new WriteDatabaseAsyncClass(
                    RedeemGift.this,"RedeemGift","redeemGift",
                    data,filter,true
            ){
                @Override
                protected void onPostExecute(String data) {
                    super.onPostExecute(data);
                    if(error.length()>0){
                        Toast.makeText(getApplicationContext(),error, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(),"Gift Redeemed Successfully. Product will be delivered soon.", Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = toastView.findViewById(android.R.id.message);
                        toastMessage.setTextColor(getResources().getColor(R.color.white));
                        toastMessage.setBackgroundColor(getResources().getColor(R.color.black));
                        toast.show();
                        //toastView.set(Color.parseColor("#000000"));
                        finish();
                    }
                }
            };
            writeDatabaseAsyncClass.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}