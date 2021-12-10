package com.swastikcolors.couponApp.Activity.Home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.swastikcolors.couponApp.R;

public class RedeemGiftHistoryAdapter extends RecyclerView.Adapter<RedeemGiftHistoryAdapter.ViewHolder> {

    private JsonArray giftRedeemHistory;

    // Constructor
    public RedeemGiftHistoryAdapter(JsonArray giftRedeemHistory) {
        this.giftRedeemHistory = giftRedeemHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RedeemGiftHistoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_redeem_gift_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(position);
        /*if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#F2F2F2"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }*/
    }

    @Override
    public int getItemCount() {
        return giftRedeemHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView giftName,orderTime,giftValue,receiverName,receiverPhoneNumber,receiverPincode,receiverAddress,orderStatus;
        View layout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            giftName = layout.findViewById(R.id.giftName);
            giftValue = layout.findViewById(R.id.giftValue);
            orderTime = layout.findViewById(R.id.orderTime);
            receiverName = layout.findViewById(R.id.receiverName);
            receiverPhoneNumber = layout.findViewById(R.id.receiverPhoneNumber);
            receiverPincode = layout.findViewById(R.id.receiverPincode);
            receiverAddress = layout.findViewById(R.id.receiverAddress);
            orderStatus = layout.findViewById(R.id.orderStatus);
        }

        public void bindView(int pos) {
            try {
                JsonObject jsonObject = giftRedeemHistory.get(pos).getAsJsonObject();
                System.out.println(jsonObject);
                giftName.setText(jsonObject.get("giftName").getAsString());
                giftValue.setText(jsonObject.get("giftValue").getAsString()+" Points");
                orderTime.setText(jsonObject.get("orderTime").getAsString());
                receiverName.setText(jsonObject.get("receiverName").getAsString());
                receiverPhoneNumber.setText(jsonObject.get("receiverPhoneNumber").getAsString());
                receiverPincode.setText(jsonObject.get("receiverPincode").getAsString());
                receiverAddress.setText(jsonObject.get("receiverAddress").getAsString());
                orderStatus.setText(jsonObject.get("orderStatus").getAsString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
