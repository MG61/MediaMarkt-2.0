package com.example.mediamarkt.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediamarkt.CatFire.CartActivity.CartActivity;
import com.example.mediamarkt.CatFire.Model.CartModel;
import com.example.mediamarkt.CatFire.Model.Model;
import com.example.mediamarkt.CatFire.adapter.MyToyAdapter;
import com.example.mediamarkt.CatFire.listener.LoadListener;
import com.example.mediamarkt.CatFire.listener.LoadListenerCart;
import com.example.mediamarkt.CatFire.utils.SpaceItemDecoration;
import com.example.mediamarkt.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllSearch extends AppCompatActivity implements LoadListener, LoadListenerCart {


    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    SearchView search_view;
    TextView errortext;
    ImageView errorimage;
    LoadListener loadListener;
    LoadListenerCart loadListenerCart;

    DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_search);

        mref = FirebaseDatabase.getInstance().getReference("AllElec");

        recyclerView = findViewById(R.id.recycleView);
        search_view = findViewById(R.id.search_view);
        errorimage = findViewById(R.id.errorimage);
        errortext = findViewById(R.id.errortext);

        init();
        loadFromFirebase("");
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        search_view.requestFocus();
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.toString()!=null) {
                    loadFromFirebase(newText);
                }
                else{
                    loadFromFirebase("");
                }
                return true;
            }
        });
    }


    private void loadFromFirebase(String name) {
            List<Model> Model = new ArrayList<>();
            FirebaseDatabase.getInstance()
                    .getReference("AllElec").orderByChild("name")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                                    Model model = Snapshot.getValue(Model.class);
                                    model.setKey(Snapshot.getKey());
                                    Model.add(model);
                                }
                                loadListener.onLoadSuccess(Model);
                            } else {
                                loadListener.onLoadFailed("Товары не найдены!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            loadListener.onLoadFailed(error.getMessage());
                        }
                    });
    }

    private void init() {
        ButterKnife.bind(this);

        loadListener = this;
        loadListenerCart = this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration());
    }

    @Override
    public void onLoadSuccess(List<Model> ModelList) {
        MyToyAdapter adapter = new MyToyAdapter(this, ModelList, loadListenerCart);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoadFailed(String message) {
    }

    @Override
    public void onCartSuccess(List<CartModel> cartModelList) {
    }

    @Override
    public void onCartFailed(String message) {
    }
}