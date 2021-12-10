package com.swastikcolors.couponApp.Utility;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.swastikcolors.couponApp.Common;

import java.util.Locale;

public class MyApplication extends Application {
    public void onCreate(){
        super.onCreate();
        Common.sharedPreferences = this.getSharedPreferences("", Context.MODE_PRIVATE);
        String language = Common.sharedPreferences.getString("language","");
        if(language.equals("")) {
            language = "hi";
        }

        LocaleUtils.setLocale(new Locale(language));
        LocaleUtils.updateConfig(this, getBaseContext().getResources().getConfiguration());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleUtils.updateConfig(this, newConfig);
    }
}