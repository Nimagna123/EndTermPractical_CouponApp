package com.swastikcolors.couponApp.Activity.Home;

import androidx.appcompat.app.AppCompatActivity;
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
import com.google.gson.JsonObject;
import com.swastikcolors.couponApp.Activity.HomeScreenGridAdapter;
import com.swastikcolors.couponApp.Controller.ReadDatabaseAsyncClass;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.BaseAppCompatActivity;
import com.swastikcolors.couponApp.Utility.ManageJson;

import java.util.HashMap;
import java.util.Objects;

public class PointsStatement extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_statement);
        setView();
    }

    private void setView(){
        ((TextView)findViewById(R.id.header)).setText(getResources().getString(R.string.app_name));
        getPointsStatement();
        getCurrentPoint();
    }

    private void getPointsStatement(){
        HashMap<String,String> filter = new HashMap<>();
        @SuppressLint("StaticFieldLeak")
        ReadDatabaseAsyncClass readDatabaseAsyncClass = new ReadDatabaseAsyncClass(PointsStatement.this,
                "PointsStatement","getPointsStatementCustomer",filter,true){
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                if(error.length()==0) {
                    try {
                        JsonArray jsonArray = (JsonArray) ManageJson.jsonStringToObject(data, JsonArray.class);
                        setPointsStatement(jsonArray);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        };
        readDatabaseAsyncClass.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void getCurrentPoint(){
        HashMap<String,String> filter = new HashMap<>();
        @SuppressLint("StaticFieldLeak")
        ReadDatabaseAsyncClass readDatabaseAsyncClass = new ReadDatabaseAsyncClass(PointsStatement.this,
                "CustomerDetails","getCurrentPointCustomer",filter,true){
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                if(error.length()==0) {
                    try {
                        JsonArray jsonArray = (JsonArray) ManageJson.jsonStringToObject(data, JsonArray.class);
                        ((TextView)findViewById(R.id.currentPoint)).setText(jsonArray.get(0).getAsJsonObject().get("points").getAsString());
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        };
        readDatabaseAsyncClass.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setPointsStatement(JsonArray pointsStatement){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PointsStatement.this);
        RecyclerView mRecyclerView = findViewById(R.id.points_statement);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new PointsStatementAdapter(pointsStatement));
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }
}