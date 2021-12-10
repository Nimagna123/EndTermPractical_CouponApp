package com.swastikcolors.couponApp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swastikcolors.couponApp.Activity.Login.StartLoginButton;
import com.swastikcolors.couponApp.Common;
import com.swastikcolors.couponApp.R;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.swastikcolors.couponApp.Common.loggedIn;
import static com.swastikcolors.couponApp.Common.password;
import static com.swastikcolors.couponApp.Common.phoneNumber;


public class MeFragment extends Fragment {

    List<String> optionsList;
    List<Integer> imageList;
    List<String> tagList;
    LinearLayout dynamicHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        
        return inflater.inflate(R.layout.fragment_me, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();
    }

    private void setView(){
        dynamicHolder = getActivity().findViewById(R.id.dynamicLayout);
        ((TextView)getActivity().findViewById(R.id.name)).setText(Common.name);
        ((TextView)getActivity().findViewById(R.id.phoneNumber)).setText(phoneNumber);
        (getActivity().findViewById(R.id.logout)).setOnClickListener(v -> logout());
        setOptionsList();
    }

    private void setOptionsList(){
        optionsList = Arrays.asList(
                "About Us","Contact Us","Locate Us");
        imageList = Arrays.asList(
        R.drawable.about_us,R.drawable.contact_us,R.drawable.location);
        tagList = Arrays.asList("AboutUs","ContactUs","LocateUs");
        for(int i = 0; i<optionsList.size(); i++){
            View dynamicView = LayoutInflater.from(getContext()).inflate(R.layout.me_linear_layout, null, false);
            dynamicView.setTag(tagList.get(i));
            TextView optionTextView = dynamicView.findViewById(R.id.optionText);
            optionTextView.setText(optionsList.get(i));

            ImageView imageView = dynamicView.findViewById(R.id.optionImage);
            imageView.setImageResource(imageList.get(i));
            dynamicHolder.addView(dynamicView);

            View divider = LayoutInflater.from(getContext()).inflate(R.layout.layout_divider, null, false);
            dynamicHolder.addView(divider);
            setClickListener(dynamicView);
        }
    }

    private void setClickListener(View dynamicView){
        dynamicView.setOnClickListener(v -> {
            String tag = dynamicView.getTag().toString();
            try {
                Class c = Class.forName("com.swastikcolors.couponApp.Activity.Me."+tag);
                Intent intent = new Intent(getContext(), c);
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    private void logout(){
        Common.phoneNumber = "";
        Common.name = "";
        Common.password = "";
        Common.loggedIn="";
        Common.cuser = "";
        Common.sharedPreferences.edit().putString("name", Common.name).apply();
        Common.sharedPreferences.edit().putString("phoneNumber", Common.phoneNumber).apply();
        Common.sharedPreferences.edit().putString("password", Common.password).apply();
        Common.sharedPreferences.edit().putString("loggedIn", Common.loggedIn).apply();
        Intent intent = new Intent(getContext(), StartLoginButton.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }
}
