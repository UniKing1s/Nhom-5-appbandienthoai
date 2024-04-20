package com.example.gridlayout.api;

import com.example.gridlayout.api.entitesforapi.ProductSearch;
import com.example.gridlayout.entities.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductService {
    @GET("products")
    Call<List<Product>> getAllProducts();
    @POST("products")
    Call<Product> insertProduct(@Body Product product);
    @PUT("products/{id}")
    Call<Product> updateProduct(@Path("id") int id , @Body Product product);
    @DELETE("products/{id}")
    Call<Product> deleteProduct(@Path("id") int id);
    @POST("products/search")
    Call<List<Product>> searchProduct(@Body ProductSearch data);
}
