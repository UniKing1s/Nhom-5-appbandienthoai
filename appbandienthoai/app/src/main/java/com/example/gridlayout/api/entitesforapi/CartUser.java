package com.example.gridlayout.api.entitesforapi;

import com.example.gridlayout.entities.CartItem;

import java.util.List;

public class CartUser {
    private String username;
    private List<CartItem> cart;

    public CartUser(String username, List<CartItem> cart) {
        this.username = username;
        this.cart = cart;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CartItem> getCart() {
        return cart;
    }

    public void setCart(List<CartItem> cart) {
        this.cart = cart;
    }
}
