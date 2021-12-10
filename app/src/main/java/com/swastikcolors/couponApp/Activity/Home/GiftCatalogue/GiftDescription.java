package com.swastikcolors.couponApp.Activity.Home.GiftCatalogue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.swastikcolors.couponApp.Common;
import com.swastikcolors.couponApp.Constants;
import com.swastikcolors.couponApp.Controller.ReadDatabaseAsyncClass;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.BaseAppCompatActivity;
import com.swastikcolors.couponApp.Utility.ManageJson;
import com.swastikcolors.couponApp.Utility.UploadAndDownload;

import java.util.HashMap;

public class GiftDescription extends BaseAppCompatActivity {

    String giftId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_description);
        if(savedInstanceState!=null){
            giftId = savedInstanceState.getString("giftId");
        }
        else{
            giftId = getIntent().getStringExtra("giftId");
        }
        setView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("giftId",giftId);
    }

    public void setView(){
        ((TextView)findViewById(R.id.header)).setText(getResources().getString(R.string.app_name));
        getGiftDescription();
    }

    @SuppressLint("StaticFieldLeak")
    private void getGiftDescription(){
        HashMap<String,String> filter = new HashMap<>();
        filter.put("gc.giftId=", giftId);
        ReadDatabaseAsyncClass readDatabaseAsyncClass = new ReadDatabaseAsyncClass(GiftDescription.this,
                "GiftCatalogue","getGiftCatalogueWithDescriptionCustomer",filter,true){
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                try {
                    JsonArray jsonArray = (JsonArray) ManageJson.jsonStringToObject(data, JsonArray.class);
                    if(jsonArray.size()>0){
                        setGiftDescription(jsonArray.get(0).getAsJsonObject());
                    }
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        readDatabaseAsyncClass.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressLint("SetTextI18n")
    public void setGiftDescription(JsonObject json) {
        try {
            final HashMap<String, String> params = new HashMap<>();
            params.put("cuser", Common.cuser);
            params.put("department", Constants.department);
            params.put("fileType", "productImage");
            final HashMap<String, String> data = new HashMap<>();
            data.put("image",json.get("image").getAsString());
            params.put("data", ManageJson.objectToJsonString(data));
            params.put("filter", ManageJson.objectToJsonString(new HashMap<String,String>()));
            String imageUrl = Constants.downloadFile +"?"+ UploadAndDownload.setURLVariables(params);
            Glide.with(GiftDescription.this)
                    .load(Uri.parse(imageUrl))
                    .into((ImageView)findViewById(R.id.giftImage));

            if(json.has("giftName") && !json.get("giftName").getAsString().equals("")){
                ((TextView)(findViewById(R.id.giftName))).setText(json.get("giftName").getAsString());
            }

            if(json.has("giftValue") && !json.get("giftValue").getAsString().equals("")){
                ((TextView)(findViewById(R.id.giftValue))).setText(json.get("giftValue").getAsString()+" Points");
            }

            if(json.has("description") && !json.get("description").getAsString().equals("")){
                ((WebView)(findViewById(R.id.description))).loadDataWithBaseURL(null, json.get("description").getAsString(), "text/html", "utf-8", null);
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void redeemGift(View view){
        Intent intent = new Intent(GiftDescription.this, RedeemGift.class);
        intent.putExtra("giftId",giftId);
        startActivity(intent);
    }
}