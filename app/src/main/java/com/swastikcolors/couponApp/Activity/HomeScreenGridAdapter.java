package com.swastikcolors.couponApp.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swastikcolors.couponApp.R;

public class HomeScreenGridAdapter extends RecyclerView.Adapter<HomeScreenGridAdapter.ViewHolder> {

    private Context mContext;
    private Integer[] mThumbIds;
    private String[] title;
    private String[] tags;

    // Constructor
    public HomeScreenGridAdapter(Context c,Integer[] mThumbIds,String[] title,String[] tags) {
        mContext = c;
        this.mThumbIds = mThumbIds;
        this.title = title;
        this.tags = tags;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeScreenGridAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_main_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(position);
    }

    private void itemClickListener(View dynamicView,int position){
        dynamicView.setOnClickListener(v -> {
            String tag = tags[position];
            try {
                Class c = Class.forName("com.swastikcolors.couponApp.Activity.Home."+tag);
                Intent intent = new Intent(mContext, c);
                mContext.startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mainMenuItemName;
        ImageView mainMenuItemImage;
        View layout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            mainMenuItemName = layout.findViewById(R.id.mainMenuItemName);
            mainMenuItemImage = layout.findViewById(R.id.mainMenuItemImage);
        }

        public void bindView(int pos) {
            try {
                mainMenuItemName.setText(title[pos]);
                mainMenuItemImage.setImageResource(mThumbIds[pos]);
                itemClickListener(layout,pos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
