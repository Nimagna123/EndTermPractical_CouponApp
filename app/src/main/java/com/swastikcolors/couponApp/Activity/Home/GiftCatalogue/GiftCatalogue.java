package com.swastikcolors.couponApp.Activity.Home.GiftCatalogue;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class GiftCatalogue extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_catalogue);
        setView();
    }

    private void setView(){
        ((TextView)findViewById(R.id.header)).setText(getResources().getString(R.string.app_name));
        getGiftList();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    private void getGiftList(){
        HashMap<String,String> filter = new HashMap<>();
        @SuppressLint("StaticFieldLeak")
        ReadDatabaseAsyncClass readDatabaseAsyncClass = new ReadDatabaseAsyncClass(GiftCatalogue.this,
                "GiftCatalogue","getGiftCatalogueCustomer",filter,true){
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                try {
                    JsonArray jsonArray = (JsonArray) ManageJson.jsonStringToObject(data, JsonArray.class);
                    if(jsonArray.size()>0){
                        setGiftList(jsonArray);
                    }
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        readDatabaseAsyncClass.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setGiftList(JsonArray productsList){
        try {
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
            RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            mRecyclerView.setAdapter(new GiftAdapter(productsList, GiftCatalogue.this));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}