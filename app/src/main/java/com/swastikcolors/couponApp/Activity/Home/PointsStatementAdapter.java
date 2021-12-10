package com.swastikcolors.couponApp.Activity.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.swastikcolors.couponApp.R;

public class PointsStatementAdapter extends RecyclerView.Adapter<PointsStatementAdapter.ViewHolder> {

    private JsonArray pointsStatement;

    // Constructor
    public PointsStatementAdapter(JsonArray pointsStatement) {
        this.pointsStatement = pointsStatement;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PointsStatementAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_points_statement, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindView(position);
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#F2F2F2"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return pointsStatement.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView statement;
        TextView time;
        TextView updatedPoint;
        View layout;

        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            statement = layout.findViewById(R.id.statement);
            time = layout.findViewById(R.id.time);
            updatedPoint = layout.findViewById(R.id.updatedPoint);
        }

        public void bindView(int pos) {
            try {
                JsonObject jsonObject = pointsStatement.get(pos).getAsJsonObject();
                statement.setText(jsonObject.get("statement").getAsString());
                time.setText(jsonObject.get("time").getAsString());
                updatedPoint.setText(jsonObject.get("updatedPoint").getAsString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
