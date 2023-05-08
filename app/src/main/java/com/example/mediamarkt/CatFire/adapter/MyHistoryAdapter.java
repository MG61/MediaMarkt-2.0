package com.example.mediamarkt.CatFire.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mediamarkt.CatFire.Model.History;
import com.example.mediamarkt.R;

import java.util.ArrayList;

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.MyViewHolder> {

    Context context;

    ArrayList<History> list;

    public MyHistoryAdapter(Context context, ArrayList<History> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemcart, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        History history = list.get(position);
        holder.txtDate.setText(history.getDate());
        holder.txtWherebuy.setText(history.getWherebuy());
        holder.txtPrice.setText(history.getPrice());
        holder.txtQuantity.setText(history.getQuantity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtDate, txtWherebuy, txtPrice, txtQuantity;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtDate);
            txtWherebuy = itemView.findViewById(R.id.txtWherebuy);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);

        }
    }
}