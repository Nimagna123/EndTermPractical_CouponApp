package com.swastikcolors.couponApp.Activity.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.swastikcolors.couponApp.Activity.Login.Login;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.BaseAppCompatActivity;

public class StartLoginButton extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_login_button);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void login(View v){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

}
