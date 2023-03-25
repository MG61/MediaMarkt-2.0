package com.example.mediamarkt.Auth;

public class User {
    private String name, email, pass, phone, card, level;

    public User() {
    }

    public User(String name, String email, String pass, String phone, String card, String level) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
        this.card = card;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
