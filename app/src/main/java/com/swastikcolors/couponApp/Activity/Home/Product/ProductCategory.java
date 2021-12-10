package com.swastikcolors.couponApp.Activity.Home.Product;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.swastikcolors.couponApp.Activity.Home.GiftCatalogue.GiftDescription;
import com.swastikcolors.couponApp.Controller.ReadDatabaseAsyncClass;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.BaseAppCompatActivity;
import com.swastikcolors.couponApp.Utility.ManageJson;

import java.util.HashMap;

public class ProductCategory extends BaseAppCompatActivity {

    String parentCategoryId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        if(savedInstanceState!=null){
            parentCategoryId = savedInstanceState.getString("parentCategoryId","");
        }
        else{
            parentCategoryId = getIntent().getStringExtra("parentCategoryId");
        }
        setView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("parentCategoryId",parentCategoryId);
    }

    public void setView(){
        ((TextView)findViewById(R.id.header)).setText(getResources().getString(R.string.app_name));
        getProductList();
    }

    private void getProductList(){
        HashMap<String,String> filter = new HashMap<>();
        if(parentCategoryId == null || parentCategoryId.equalsIgnoreCase("")) {
            filter.put("prc.parentCategoryId is null", "");
        }
        else{
            filter.put("prc.parentCategoryId=", parentCategoryId);
        }
        @SuppressLint("StaticFieldLeak")
        ReadDatabaseAsyncClass readDatabaseAsyncClass = new ReadDatabaseAsyncClass(ProductCategory.this,
                "ProductCategory","getProductCategoryListCustomer",filter,true){
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                try {
                    JsonArray jsonArray = (JsonArray) ManageJson.jsonStringToObject(data, JsonArray.class);
                    if(jsonArray.size()>0){
                        setProductCategoryList(jsonArray);
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

    private void setProductCategoryList(JsonArray productCategoryList){
        try {
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
            RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            mRecyclerView.setAdapter(new ProductCategoryAdapter(productCategoryList, ProductCategory.this));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error!!!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}