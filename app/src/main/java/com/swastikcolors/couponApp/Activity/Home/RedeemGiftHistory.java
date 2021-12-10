package com.swastikcolors.couponApp.Activity.Home;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.swastikcolors.couponApp.Controller.ReadDatabaseAsyncClass;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.BaseAppCompatActivity;
import com.swastikcolors.couponApp.Utility.ManageJson;

import java.util.HashMap;

public class RedeemGiftHistory extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_gift_history);
        setView();
    }

    private void setView(){
        ((TextView)findViewById(R.id.header)).setText(getResources().getString(R.string.app_name));
        getGiftRedeemHistory();
    }

    private void getGiftRedeemHistory(){
        HashMap<String,String> filter = new HashMap<>();
        @SuppressLint("StaticFieldLeak")
        ReadDatabaseAsyncClass readDatabaseAsyncClass = new ReadDatabaseAsyncClass(RedeemGiftHistory.this,
                "RedeemGift","getRedeemGiftHistoryCustomer",filter,true){
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                if(error.length()==0) {
                    try {
                        JsonArray jsonArray = (JsonArray) ManageJson.jsonStringToObject(data, JsonArray.class);
                        setRedeemGiftHistory(jsonArray);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        };
        readDatabaseAsyncClass.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setRedeemGiftHistory(JsonArray pointsStatement){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RedeemGiftHistory.this);
        RecyclerView mRecyclerView = findViewById(R.id.gift_redeem_history);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new RedeemGiftHistoryAdapter(pointsStatement));
    }
}