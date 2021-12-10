package com.swastikcolors.couponApp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.swastikcolors.couponApp.Activity.HomeScreen;
import com.swastikcolors.couponApp.Activity.Login.StartLoginButton;
import com.swastikcolors.couponApp.Utility.BaseAppCompatActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class MainActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    requestPermission();
                }
            }
        };
        welcomeThread.start();
    }

    public void setView(){
        Common.sharedPreferences = this.getSharedPreferences("", Context.MODE_PRIVATE);
        Common.startDatabase();
        if (Common.loggedIn.equals("1")) {
            Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), StartLoginButton.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissionsList, int[] grantResults) {
        int i=0;
        if (grantResults.length > 0) {
            for (String ignored : permissionsList) {
                if (grantResults[i++] == PackageManager.PERMISSION_DENIED) {
                    break;
                }
            }
        }
        requestPermission();
    }

    public void requestPermission(){
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {

        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else{
            setView();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
