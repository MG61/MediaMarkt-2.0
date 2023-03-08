package com.example.mediamarkt.CatFire.listener;

import com.example.mediamarkt.CatFire.Model.CartModel;
import com.example.mediamarkt.CatFire.Model.Model;

import java.util.List;

public interface LoadListenerCart {
    void onCartSuccess(List<CartModel> cartModelList);

    void onCartFailed(String message);
}
