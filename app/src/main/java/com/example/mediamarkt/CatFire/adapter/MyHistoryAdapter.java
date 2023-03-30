package com.example.mediamarkt.CatFire.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mediamarkt.CatFire.Model.CartModel;
import com.example.mediamarkt.CatFire.Model.History;
import com.example.mediamarkt.CatFire.Model.Model;
import com.example.mediamarkt.CatFire.eventbus.MyUpdateCartEvent;
import com.example.mediamarkt.CatFire.listener.IRecyclerViewClickListener;
import com.example.mediamarkt.CatFire.listener.LoadListenerCart;
import com.example.mediamarkt.CatFire.listener.LoadListenerHistory;
import com.example.mediamarkt.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.MyHistoryViewHolder> {

    private Context context;
    private List<History> ModelList;
    private LoadListenerHistory iHistoriLoadListener;

    public MyHistoryAdapter(Context context, List<History> modelList, LoadListenerHistory iHistoriLoadListener) {
        this.context = context;
        ModelList = modelList;
        this.iHistoriLoadListener = iHistoriLoadListener;
    }

    public MyHistoryAdapter(com.example.mediamarkt.Svyaz.History context, List<History> modelList, LoadListenerHistory loadListenerHistory) {
    }

    @NonNull
    @Override
    public MyHistoryAdapter.MyHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHistoryAdapter.MyHistoryViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.itemcart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHistoryAdapter.MyHistoryViewHolder holder, int position) {
        holder.txtDate.setText(new StringBuilder().append(ModelList.get(position).getDate()));
        holder.txtWherebuy.setText(new StringBuilder().append(ModelList.get(position).getWherebuy()));
        holder.txtPrice.setText(new StringBuilder("₽ ").append(ModelList.get(position).getPrice()));
        holder.txtQuantity.setText(new StringBuilder().append(ModelList.get(position).getQuantity()));
    }

    private void updateFirebase(CartModel cartModel) {
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID")
                .child(cartModel.getKey())
                .setValue(cartModel)
                .addOnSuccessListener(aVoid -> EventBus.getDefault().postSticky(new MyUpdateCartEvent()));
    }

    @Override
    public int getItemCount() {
        return ModelList.size();
    }

    public class MyHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txtDate)
        TextView txtDate;
        @BindView(R.id.txtWherebuy)
        TextView txtWherebuy;
        @BindView(R.id.txtPrice)
        TextView txtPrice;
        @BindView(R.id.txtQuantity)
        TextView txtQuantity;
        IRecyclerViewClickListener listener;

        public void setListener(IRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        private Unbinder unbinder;

        public MyHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onRecyclerClick(v, getAdapterPosition());
        }
    }
}