package com.example.gridlayout.api;
import com.example.gridlayout.api.entitesforapi.CartUser;
import com.example.gridlayout.entities.CartItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartService {
    @POST("carts/")
    Call<List<CartItem>> storeCart(@Body List<CartItem> cartItems);
    @GET("carts/{username}")
    Call<List<CartItem>> getCarts(@Path("username") String username);
    @POST("carts/updateCart")
    Call<CartItem> updateCartDb(@Body CartItem cartItem);
    @POST("carts/delete")
    Call<CartItem> deleteCart(@Body CartItem cartItem);
    @POST("carts/addToCart")
    Call<CartItem> addtoCart(@Body CartItem cartItem);
    @DELETE("carts/afterPayed/{username}")
    Call<CartItem> deleteCartAfterPay(@Path("username") String username);
}
