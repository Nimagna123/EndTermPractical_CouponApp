package com.swastikcolors.couponApp.Activity.Home.Product;

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

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder> {

    private JsonArray productCategoryList;
    private Context context;

    ProductCategoryAdapter(JsonArray productCategoryList, Context context) {
        super();
        this.productCategoryList = productCategoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(position);
    }

    private void itemClickListener(View v){
        int pos = ((RecyclerView)v.getParent()).getChildLayoutPosition(v);
        String categoryId = productCategoryList.get(pos).getAsJsonObject().get("categoryId").getAsString();
        int subCategoryCount = Integer.parseInt(productCategoryList.get(pos).getAsJsonObject().get("subCategoryCount").toString());
        if(subCategoryCount>0){
            Intent intent = new Intent(context, ProductCategory.class);
            intent.putExtra("parentCategoryId",categoryId);
            context.startActivity(intent);
        }
        else{
            Intent intent = new Intent(context, Products.class);
            intent.putExtra("categoryId",categoryId);
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return productCategoryList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        ImageView productImage;
        View layout;

        private ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            productName = layout.findViewById(R.id.productName);
            productImage = layout.findViewById(R.id.productImage);
        }

        @SuppressLint("SetTextI18n")
        void bindView(int pos) {
            try {
                JsonObject product = productCategoryList.get(pos).getAsJsonObject();
                layout.setTag(this);
                productName.setText(product.get("categoryName").getAsString());
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
                layout.setOnClickListener(ProductCategoryAdapter.this::itemClickListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}