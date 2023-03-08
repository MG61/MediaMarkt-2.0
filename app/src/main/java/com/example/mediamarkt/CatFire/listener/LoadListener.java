package com.example.mediamarkt.CatFire.listener;

import com.example.mediamarkt.CatFire.Model.Model;

import java.util.List;

public interface LoadListener {
    void onLoadSuccess(List<Model> ModelList);

    void onLoadFailed(String message);
}
