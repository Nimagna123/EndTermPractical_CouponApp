package com.swastikcolors.couponApp.Activity.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.swastikcolors.couponApp.Controller.WriteDatabaseAsyncClass;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.BaseAppCompatActivity;
import com.swastikcolors.couponApp.Utility.ManageJson;

import java.util.HashMap;

public class RedeemCoupon extends BaseAppCompatActivity {

    TextInputLayout couponCodeLayout;
    EditText couponCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_coupon);
        setView();
    }

    public void setView() {
        ((TextView) findViewById(R.id.header)).setText(getResources().getString(R.string.app_name));
        couponCodeLayout = findViewById(R.id.couponCodeLayout);
        couponCodeView = findViewById(R.id.couponCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                couponCodeView.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void scanCoupon(View v) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan coupon");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    public void redeemCoupon(View v) {
        String couponCode = couponCodeView.getText().toString();
        if (couponCode.equals("")) {
            couponCodeLayout.setError("Fill this!!!");
            return;
        } else if (couponCode.length() < 15) {
            couponCodeLayout.setError("Coupon code must be of 15 characters");
            return;
        }

        HashMap<String, String> data = new HashMap<>();
        data.put("couponCode", couponCode);
        HashMap<String, String> filter = new HashMap<>();
        @SuppressLint("StaticFieldLeak") WriteDatabaseAsyncClass writeDatabaseAsyncClass = new WriteDatabaseAsyncClass(
                RedeemCoupon.this, "Coupon", "redeemCoupon",
                data, filter, true
        ) {
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                if (error.length() > 0) {
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Coupon Successfully Redeemed.", Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    TextView toastMessage = toastView.findViewById(android.R.id.message);
                    toastMessage.setTextColor(getResources().getColor(R.color.white));
                    toastMessage.setBackgroundColor(getResources().getColor(R.color.black));
                    toast.show();
                    finish();
                    startActivity(getIntent());
                }
            }
        };
        writeDatabaseAsyncClass.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
