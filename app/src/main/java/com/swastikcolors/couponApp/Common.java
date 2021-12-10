package com.swastikcolors.couponApp;

import android.content.SharedPreferences;

import java.text.NumberFormat;
import java.util.Locale;

public class Common {
    public static String loggedIn,customerId,name,phoneNumber,password,cuser;
    public static SharedPreferences sharedPreferences;


    public static void updateDatabase(){
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startDatabase(){
        try {
            loggedIn = Common.sharedPreferences.getString("loggedIn","0");
            name = Common.sharedPreferences.getString("name","");
            customerId = Common.sharedPreferences.getString("customerId","");
            phoneNumber = Common.sharedPreferences.getString("phoneNumber","");
            password = Common.sharedPreferences.getString("password","");
            cuser = customerId +";"+ password+";customer";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String noToCurrency(double number){
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        String currency = format.format(number);
        return currency;
    }
}
