package com.swastikcolors.couponApp.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.swastikcolors.couponApp.R;
import java.util.Objects;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, null);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();
    }

    private void setView(){
        ((TextView) Objects.requireNonNull(getActivity()).findViewById(R.id.header)).setText(getResources().getString(R.string.app_name));
        createGridView();
    }

    private void createGridView(){
        Integer[] mThumbIds = new Integer[]{R.drawable.profile,R.drawable.product,R.drawable.coupon,R.drawable.document,
        R.drawable.gift,R.drawable.order_history};
        String[] title = new String[]{"Personal Profile","Products","Redeem Coupon","Points Statement","Gift Catalogue","Gift Redeem History"};
        String[] tags = new String[]{"PersonalProfile","Product.ProductCategory","RedeemCoupon","PointsStatement","GiftCatalogue.GiftCatalogue","RedeemGiftHistory"};
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        RecyclerView mRecyclerView = getActivity().findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(new HomeScreenGridAdapter(getActivity(), mThumbIds,title,tags));
    }
}
