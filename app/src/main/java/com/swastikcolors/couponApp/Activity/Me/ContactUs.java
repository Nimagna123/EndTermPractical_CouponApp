package com.swastikcolors.couponApp.Activity.Me;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.swastikcolors.couponApp.Controller.ReadDatabaseAsyncClass;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.ManageJson;

import java.util.HashMap;

public class ContactUs extends AppCompatActivity {

    LinearLayout dynamicHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setView();
    }

    public void setView(){
        ((TextView)findViewById(R.id.header)).setText(getResources().getString(R.string.contactUs));
        dynamicHolder = findViewById(R.id.dynamicLayout);
        setContactUs();
    }

    public void setContactUs(){
        HashMap<String,String> filter = new HashMap<>();
        @SuppressLint("StaticFieldLeak")
        ReadDatabaseAsyncClass readDatabaseAsyncClass = new ReadDatabaseAsyncClass(ContactUs.this,
                "CompanyDetails","getContactUs",filter,true){
            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                try {
                    JsonArray jsonArray = (JsonArray) ManageJson.jsonStringToObject(data, JsonArray.class);
                    setPhoneNumber(jsonArray.get(0).getAsJsonObject().get("phoneNumber").getAsString().split(";"));
                    setWhatsAppNumber(jsonArray.get(0).getAsJsonObject().get("whatsAppNumber").getAsString().split(";"));
                    setEmail(jsonArray.get(0).getAsJsonObject().get("email").getAsString().split(";"));
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        };
        readDatabaseAsyncClass.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void setWhatsAppNumber(String[] phoneNumber){
        for (int i=0;i<phoneNumber.length;i++) {
            View dynamicView = LayoutInflater.from(ContactUs.this).inflate(R.layout.me_linear_layout, null, false);
            TextView optionTextView = dynamicView.findViewById(R.id.optionText);
            optionTextView.setText(phoneNumber[i]);

            ImageView imageView = dynamicView.findViewById(R.id.optionImage);
            imageView.setImageResource(R.drawable.whatsapp);
            dynamicHolder.addView(dynamicView);

            View divider = LayoutInflater.from(ContactUs.this).inflate(R.layout.layout_divider, null, false);
            dynamicHolder.addView(divider);
            int finalI = i;
            dynamicView.setOnClickListener(v -> {
                String url = "https://api.whatsapp.com/send?phone=" + phoneNumber[finalI];
                Intent i1 = new Intent(Intent.ACTION_VIEW);
                i1.setData(Uri.parse(url));
                startActivity(i1);
            });
        }
    }

    public void setPhoneNumber(String[] phoneNumber){
        for(int i=0;i<phoneNumber.length;i++) {
            View dynamicView = LayoutInflater.from(ContactUs.this).inflate(R.layout.me_linear_layout, null, false);
            TextView optionTextView = dynamicView.findViewById(R.id.optionText);
            optionTextView.setText(phoneNumber[i]);

            ImageView imageView = dynamicView.findViewById(R.id.optionImage);
            imageView.setImageResource(R.drawable.phone);
            dynamicHolder.addView(dynamicView);

            View divider = LayoutInflater.from(ContactUs.this).inflate(R.layout.layout_divider, null, false);
            dynamicHolder.addView(divider);
            int finalI = i;
            dynamicView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri u = Uri.parse("tel:" + phoneNumber[finalI]);
                    Intent i = new Intent(Intent.ACTION_DIAL, u);
                    startActivity(i);
                }
            });
        }
    }

    public void setEmail(String[] email){
        for(int i=0;i<email.length;i++) {
            View dynamicView = LayoutInflater.from(ContactUs.this).inflate(R.layout.me_linear_layout, null, false);
            TextView optionTextView = dynamicView.findViewById(R.id.optionText);
            optionTextView.setText(email[i]);

            ImageView imageView = dynamicView.findViewById(R.id.optionImage);
            imageView.setImageResource(R.drawable.email);
            dynamicHolder.addView(dynamicView);

            View divider = LayoutInflater.from(ContactUs.this).inflate(R.layout.layout_divider, null, false);
            dynamicHolder.addView(divider);
            int finalI = i;
            dynamicView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:"+email[finalI]));
                    startActivity(intent);
                }
            });
        }
    }
}