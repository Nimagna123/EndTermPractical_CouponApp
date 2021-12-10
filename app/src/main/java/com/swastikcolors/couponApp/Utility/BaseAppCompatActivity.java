package com.swastikcolors.couponApp.Utility;
import android.content.Context;
import android.os.Bundle;

import com.swastikcolors.couponApp.Common;

import androidx.appcompat.app.AppCompatActivity;

public class BaseAppCompatActivity extends AppCompatActivity {
    public BaseAppCompatActivity() {
        LocaleUtils.updateConfig(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            Common.sharedPreferences = this.getSharedPreferences("", Context.MODE_PRIVATE);
            Common.startDatabase();
        }
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
    }

    public void onResume() {
        super.onResume();
        ManageServices.showInternetConnectionAlertDialog(this);
    }
}
