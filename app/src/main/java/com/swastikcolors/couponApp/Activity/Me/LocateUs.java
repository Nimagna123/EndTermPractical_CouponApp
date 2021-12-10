package com.swastikcolors.couponApp.Activity.Me;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.swastikcolors.couponApp.Controller.ReadDatabaseAsyncClass;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.BaseAppCompatActivity;
import com.swastikcolors.couponApp.Utility.ManageJson;

import java.util.HashMap;
import java.util.Locale;

public class LocateUs extends BaseAppCompatActivity {

    LinearLayout dynamicHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_us);
        setView();
    }

    public void setView(){
        ((TextView)findViewById(R.id.header)).setText(getResources().getString(R.string.app_name));
        dynamicHolder = findViewById(R.id.dynamicLayout);
        setContactUs();
    }

    public void setContactUs(){
        HashMap<String,String> filter = new HashMap<>();
        @SuppressLint("StaticFieldLeak")
        ReadDatabaseAsyncClass readDatabaseAsyncClass = new ReadDatabaseAsyncClass(LocateUs.this,
                "CompanyDetails","getLocateUs",filter,true){
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                try {
                    JsonArray jsonArray = (JsonArray) ManageJson.jsonStringToObject(data, JsonArray.class);
                    setAddress(jsonArray.get(0).getAsJsonObject().get("address").getAsString().split(";"),
                            jsonArray.get(0).getAsJsonObject().get("latLng").getAsString().split(";"));
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        readDatabaseAsyncClass.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void setAddress(String[] address,String[] latLng){
        for (int i=0;i<address.length;i++) {
            View dynamicView = LayoutInflater.from(LocateUs.this).inflate(R.layout.me_linear_layout, null, false);
            TextView optionTextView = dynamicView.findViewById(R.id.optionText);
            optionTextView.setText(address[i].trim());

            ImageView imageView = dynamicView.findViewById(R.id.optionImage);
            imageView.setImageResource(R.drawable.google_map);
            dynamicHolder.addView(dynamicView);

            View divider = LayoutInflater.from(LocateUs.this).inflate(R.layout.layout_divider, null, false);
            dynamicHolder.addView(divider);
            int finalI = i;

            dynamicView.setOnClickListener(v -> {

                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Double.parseDouble(latLng[finalI].split(",")[0]), Double.parseDouble(latLng[finalI].split(",")[1]));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            });
        }
    }

}