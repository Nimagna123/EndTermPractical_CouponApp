package com.swastikcolors.couponApp.Activity.Me;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.swastikcolors.couponApp.Activity.Home.RedeemGiftHistory;
import com.swastikcolors.couponApp.Controller.ReadDatabaseAsyncClass;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.BaseAppCompatActivity;
import com.swastikcolors.couponApp.Utility.ManageJson;

import java.util.HashMap;

public class AboutUs extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        setView();
    }

    private void setView(){
        ((TextView)findViewById(R.id.header)).setText(getResources().getString(R.string.app_name));
        getGiftRedeemHistory();
    }

    private void getGiftRedeemHistory(){
        HashMap<String,String> filter = new HashMap<>();
        @SuppressLint("StaticFieldLeak")
        ReadDatabaseAsyncClass readDatabaseAsyncClass = new ReadDatabaseAsyncClass(AboutUs.this,
                "CompanyDetails","getAboutUs",filter,true){
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                if(error.length()==0) {
                    try {
                        JsonArray jsonArray = (JsonArray) ManageJson.jsonStringToObject(data, JsonArray.class);
                        String aboutUs = jsonArray.get(0).getAsJsonObject().get("aboutUs").getAsString();
                        ((WebView)(findViewById(R.id.aboutUs))).loadDataWithBaseURL(null, aboutUs, "text/html", "utf-8", null);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        };
        readDatabaseAsyncClass.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}