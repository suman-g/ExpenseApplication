package com.example.expenseapp;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;


public class Expense implements Serializable {

    String name;
    String category;
    String amount;
    String date;
    String image;

    public Expense(String name, String category, String amount, String date, String image) {
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", amount='" + amount + '\'' +
                ", date='" + date + '\'' +
                ", image=" + image +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
