package com.swastikcolors.couponApp.Activity.Home.Product;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.swastikcolors.couponApp.Activity.Home.GiftCatalogue.GiftDescription;
import com.swastikcolors.couponApp.Common;
import com.swastikcolors.couponApp.Constants;
import com.swastikcolors.couponApp.Controller.ReadDatabaseAsyncClass;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.BaseAppCompatActivity;
import com.swastikcolors.couponApp.Utility.ManageJson;
import com.swastikcolors.couponApp.Utility.UploadAndDownload;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductDescription extends BaseAppCompatActivity {

    String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
        if(savedInstanceState!=null){
            productId = savedInstanceState.getString("productId");
        }
        else{
            productId = getIntent().getStringExtra("productId");
        }
    }

    public void onStart() {
        super.onStart();
        setView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("productId",productId);
    }

    public void setView(){
        ((TextView)findViewById(R.id.header)).setText("Product");
        getProductDescription();
    }

    private void getProductDescription(){
        HashMap<String,String> filter = new HashMap<>();
        filter.put("productId=",productId);
        @SuppressLint("StaticFieldLeak")
        ReadDatabaseAsyncClass readDatabaseAsyncClass = new ReadDatabaseAsyncClass(ProductDescription.this,
                "Product","getProductDescriptionCustomer",filter,true){
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                try {
                    JsonArray jsonArray = (JsonArray) ManageJson.jsonStringToObject(data, JsonArray.class);
                    if(jsonArray.size()>0){
                        setProductDescription(jsonArray.get(0).getAsJsonObject());
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

    @SuppressLint("SetTextI18n")
    public void setProductDescription(JsonObject json) {
        try {
            ArrayList<String> imageUrlList = new ArrayList<>();
            for(int i=1;i<=4;i++) {
                if(json.has("image"+i) && !json.get("image"+i).getAsString().equals("")) {
                    final HashMap<String, String> params = new HashMap<>();
                    params.put("cuser", Common.cuser);
                    params.put("department", Constants.department);
                    params.put("fileType", "productImage");
                    final HashMap<String, String> data = new HashMap<>();
                    data.put("image", json.get("image"+i).getAsString());
                    params.put("data", ManageJson.objectToJsonString(data));
                    params.put("filter", ManageJson.objectToJsonString(new HashMap<String, String>()));
                    String imageUrl = Constants.downloadFile + "?" + UploadAndDownload.setURLVariables(params);
                    imageUrlList.add(imageUrl);
                }
            }

            SliderView sliderView = findViewById(R.id.imageSlider);

            ImageSliderAdapter adapter = new ImageSliderAdapter(this,imageUrlList);

            sliderView.setSliderAdapter(adapter);

            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            sliderView.setIndicatorSelectedColor(Color.WHITE);
            sliderView.setIndicatorUnselectedColor(Color.GRAY);
            sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
            sliderView.startAutoCycle();


            if(json.has("productName") && !json.get("productName").getAsString().equals("")){
                ((TextView)(findViewById(R.id.productName))).setText(json.get("productName").getAsString());
            }

            if(json.has("productDescription") && !json.get("productDescription").getAsString().equals("")){
                ((WebView)(findViewById(R.id.description))).loadDataWithBaseURL(null, json.get("productDescription").getAsString(), "text/html", "utf-8", null);
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
