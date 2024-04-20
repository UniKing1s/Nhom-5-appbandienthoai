package com.example.gridlayout.dbmanager;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gridlayout.entities.CartItem;

import java.util.List;

@Dao
public interface CartDAO {
    @Insert
    void insertCart(CartItem cartItem);
    @Query("Select * from Cart")
    List<CartItem> getListCartItem();

    @Query("Select * from Cart where masp= :masp")
    List<CartItem> checkCart(int masp);

    @Update
    void UpdateCart(CartItem item);
    @Query("Update Cart set quantity = quantity + :quantity, totalPrice = :total where masp = :masp")
    void updateCartQuantity(int masp, int quantity, float total);

    @Query("Delete from Cart where masp = :masp")
    void deleteCartItem(int masp);

    @Query("Delete from Cart where quantity > 0 ")
    void deleteAllCartPayed();
    @Query("Delete from Cart")
    void deleteAll();
    @Query("Update Cart set username = :username ")
    void updateUserForCarts(String username);
}
