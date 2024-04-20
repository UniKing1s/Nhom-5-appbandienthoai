package com.example.gridlayout.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cart")
public class CartItem {
//    @PrimaryKey
    private String username;
    @PrimaryKey
    private int masp;

    private String name;
    private double price;
    private int sale;
    private int quantity;
    private double totalPrice;
    private String img;

    public CartItem(int masp, String name, double price, int sale, int quantity, String img) {
        this.masp = masp;
        this.name = name;
        this.price = price;
        this.sale = sale;
        this.quantity = quantity;
//        this.totalPrice = totalPrice;
        this.img = img;
    }

    public CartItem() {
    }
    public double calTotalPrice(){
        this.totalPrice = this.price* this.quantity *(100 - Double.parseDouble(""+this.sale))/100;
        return this.totalPrice;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
