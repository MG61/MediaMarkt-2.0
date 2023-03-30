package com.example.mediamarkt.CatFire.listener;

import com.example.mediamarkt.CatFire.Model.History;

import java.util.List;

public interface LoadListenerHistory {

    void onHistorySuccess(List<History> historyModelList);

    void onHistoryFailed(String message);
}
