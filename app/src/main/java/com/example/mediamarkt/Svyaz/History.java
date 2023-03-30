package com.example.mediamarkt.Svyaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mediamarkt.CatFire.adapter.MyHistoryAdapter;
import com.example.mediamarkt.CatFire.eventbus.MyUpdateCartEvent;
import com.example.mediamarkt.CatFire.listener.LoadListenerHistory;
import com.example.mediamarkt.CatFire.utils.SpaceItemDecoration;
import com.example.mediamarkt.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class History extends AppCompatActivity implements LoadListenerHistory{

    @BindView(R.id.recycler_all)
    RecyclerView recycler_all;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
    @BindView(R.id.btnback)
    ImageView btnBack;

    LoadListenerHistory loadListenerHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_history);

        init();
        loadFromFirebase();
    }
    private void loadFromFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<com.example.mediamarkt.CatFire.Model.History> Models = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Пользователи").child(uid).child("buy")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot Snapshot : snapshot.getChildren()) {
                                com.example.mediamarkt.CatFire.Model.History model = Snapshot.getValue(com.example.mediamarkt.CatFire.Model.History.class);
                                model.setKey(Snapshot.getKey());
                                Models.add(model);
                            }
                            loadListenerHistory.onHistorySuccess(Models);
                            Snackbar.make(mainLayout, "есть", Snackbar.LENGTH_SHORT).show();
                        } else {
                            loadListenerHistory.onHistoryFailed("Товары не найдены!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadListenerHistory.onHistoryFailed(error.getMessage());
                    }
                });

    }

    private void init() {
        ButterKnife.bind(this);

        loadListenerHistory = this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recycler_all.setLayoutManager(gridLayoutManager);
        recycler_all.addItemDecoration(new SpaceItemDecoration());
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onHistorySuccess(List<com.example.mediamarkt.CatFire.Model.History> ModelList) {
        MyHistoryAdapter adapter = new MyHistoryAdapter(this, ModelList, loadListenerHistory);
        recycler_all.setAdapter(adapter);
    }

    @Override
    public void onHistoryFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT).show();
    }
}