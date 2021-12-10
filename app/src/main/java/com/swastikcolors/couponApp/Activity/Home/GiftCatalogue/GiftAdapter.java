package com.swastikcolors.couponApp.Activity.Home.GiftCatalogue;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.swastikcolors.couponApp.Common;
import com.swastikcolors.couponApp.Constants;
import com.swastikcolors.couponApp.R;
import com.swastikcolors.couponApp.Utility.ManageJson;
import com.swastikcolors.couponApp.Utility.UploadAndDownload;

import java.util.HashMap;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder> {

    private JsonArray productList;
    private Context context;

    GiftAdapter(JsonArray productList, Context context) {
        super();
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_gift_catalogue, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(position);
    }

    private void itemClickListener(View v){
        int pos = ((RecyclerView)v.getParent()).getChildLayoutPosition(v);
        String giftId = productList.get(pos).getAsJsonObject().get("giftId").getAsString();
        Intent intent = new Intent(context, GiftDescription.class);
        intent.putExtra("giftId",giftId);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        TextView giftValue;
        ImageView productImage;
        View layout;

        private ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            productName = layout.findViewById(R.id.productName);
            giftValue = layout.findViewById(R.id.giftValue);
            productImage = layout.findViewById(R.id.productImage);
        }

        @SuppressLint("SetTextI18n")
        void bindView(int pos) {
            try {
                JsonObject product = productList.get(pos).getAsJsonObject();
                layout.setTag(this);
                productName.setText(product.get("giftName").getAsString());
                giftValue.setText(product.get("giftValue").getAsString()+" Points");
                productImage.setImageDrawable(null);
                if(product.has("image")) {
                    final HashMap<String, String> params = new HashMap<>();
                    params.put("cuser", Common.cuser);
                    params.put("department", Constants.department);
                    params.put("fileType", "ProductImage");
                    final HashMap<String, String> data = new HashMap<>();
                    data.put("image", product.get("image").getAsString());
                    params.put("data", ManageJson.objectToJsonString(data));
                    params.put("filter", ManageJson.objectToJsonString(new HashMap<String, String>()));
                    String imageUrl = Constants.downloadFile + "?" + UploadAndDownload.setURLVariables(params);
                    Glide.with(context)
                            .load(Uri.parse(imageUrl))
                            .into(productImage);
                }
                layout.setOnClickListener(GiftAdapter.this::itemClickListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}