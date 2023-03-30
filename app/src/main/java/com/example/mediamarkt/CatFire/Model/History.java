package com.example.mediamarkt.CatFire.Model;

public class History {
    private String key, date, wherebuy, price, quantity;

    public History(String key, String date, String wherebuy, String price, String quantity) {
        this.key = key;
        this.date = date;
        this.wherebuy = wherebuy;
        this.price = price;
        this.quantity = quantity;
    }

    public History() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWherebuy() {
        return wherebuy;
    }

    public void setWherebuy(String wherebuy) {
        this.wherebuy = wherebuy;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
