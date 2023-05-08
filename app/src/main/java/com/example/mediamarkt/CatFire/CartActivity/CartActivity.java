package com.example.mediamarkt.CatFire.CartActivity;

import static java.security.CryptoPrimitive.SIGNATURE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mediamarkt.Auth.User;
import com.example.mediamarkt.CatFire.Model.CartModel;
import com.example.mediamarkt.CatFire.Model.History;
import com.example.mediamarkt.CatFire.Model.Model;
import com.example.mediamarkt.CatFire.adapter.MyCartAdapter;
import com.example.mediamarkt.CatFire.eventbus.MyUpdateCartEvent;
import com.example.mediamarkt.CatFire.listener.LoadListenerCart;
import com.example.mediamarkt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements LoadListenerCart {

    @BindView(R.id.recycler_cart)
    RecyclerView recycler_cart;
    @BindView(R.id.mainLayout)
    ScrollView mainLayout;
    @BindView(R.id.btnback)
    ImageView btnBack;
    @BindView(R.id.txtTotal)
    TextView txtTotal;
    @BindView(R.id.buy)
    Button buy;
    @BindView(R.id.summ)
    LinearLayout summ;
    @BindView(R.id.animationView)
    LottieAnimationView animationView;
    LoadListenerCart cartLoadListener;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event) {
        loadCartFromFirebase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_cart);

        init();
        loadCartFromFirebase();

    }

    public int prov = 0;

    private void loadCartFromFirebase() {

        List<CartModel> cartModels = new ArrayList<>();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance()
                .getReference("Пользователи")
                .child(uid)
                .child("Cart")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                                CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                                cartModel.setKey(cartSnapshot.getKey());
                                cartModels.add(cartModel);
                            }
                            prov = 1;
                            cartLoadListener.onCartSuccess(cartModels);
                            buy.setVisibility(View.VISIBLE);
                            summ.setVisibility(View.VISIBLE);
                            animationView.setVisibility(View.INVISIBLE);
                        } else {
                            cartLoadListener.onCartFailed("Корзина пуста!");
                            buy.setVisibility(View.INVISIBLE);
                            summ.setVisibility(View.INVISIBLE);
                            animationView.setVisibility(View.VISIBLE);
                            if (prov == 1) {
                                finish();
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                                prov = 0;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartFailed(error.getMessage());
                    }
                });
    }

    private void deletefromFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference Cart = FirebaseDatabase.getInstance()
                .getReference("Пользователи")
                .child(uid)
                .child("Cart");
        Cart.removeValue();
        finish();
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    private void init() {
        ButterKnife.bind(this);

        cartLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(layoutManager);
        recycler_cart.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onCartSuccess(List<CartModel> cartModelList) {
        double sum = 0;
        for (CartModel cartModel : cartModelList) {
            sum += cartModel.getTotalPrice();
        }
        txtTotal.setText(new StringBuilder("₽ ").append(sum));
        MyCartAdapter adapter = new MyCartAdapter(this, cartModelList);
        recycler_cart.setAdapter(adapter);
    }

    @Override
    public void onCartFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    String countbuy;
    public void buy(View view) {
        ButterKnife.bind(this);

        cartLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(layoutManager);
        recycler_cart.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        //Получение количества товаров в чеке
        Bundle arguments = getIntent().getExtras();
        int quantity = arguments.getInt("quantity");

        //Получение дня
        Calendar calendar = new GregorianCalendar();
        Date trialTime = new Date();
        calendar.setTime(trialTime);
        Integer date = calendar.get(Calendar.DATE);
        Integer month = calendar.get(Calendar.MONTH) + 1;
        Integer year = calendar.get(Calendar.YEAR);
        String time = "Дата покупки: " + date+ "." + month+ "." + year;

        //Запись новых данных
        History history = new History();
        history.setDate(time);
        history.setWherebuy("Онлайн заказ");
        history.setPrice(txtTotal.getText().toString());
        history.setQuantity(String.valueOf(quantity) + " товара");


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Пользователи").child(uid).child("buy").push(); // Key
        ref.setValue(history);

        deletefromFirebase();

        Snackbar.make(mainLayout, "Спасибо за покупку!", Snackbar.LENGTH_SHORT).show();

        DatabaseReference productsRef = db.getReference("Пользователи").child(uid).child("buy");
        Query queryByProductSeasonCategory = productsRef;
        queryByProductSeasonCategory.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    long count = task.getResult().getChildrenCount();
                    Log.d("TAG", "count: " + count);
                    countbuy = String.valueOf(count); //Added

                    DatabaseReference ref1 = db.getReference("Пользователи").child(uid).child("level"); // Key
                    if (count > 3 && count <=5) {
                        ref1.setValue("Продвинутый");
                    }
                    if (count > 5) {
                        ref1.setValue("Постоянный покупатель");
                    }
                } else {
                    Log.d("TAG", task.getException().getMessage()); //Never ignore potential errors!
                }
            }
        });


    }
}