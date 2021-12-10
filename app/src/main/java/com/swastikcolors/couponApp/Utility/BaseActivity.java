package com.swastikcolors.couponApp.Utility;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.swastikcolors.couponApp.Common;


public class BaseActivity extends Activity {
    public BaseActivity() {
        LocaleUtils.updateConfig(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            Common.sharedPreferences = this.getSharedPreferences("", Context.MODE_PRIVATE);
            Common.startDatabase();
        }
    }

    public void onResume() {
        super.onResume();
        ManageServices.showInternetConnectionAlertDialog(this);
    }
}
