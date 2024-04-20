package com.example.gridlayout.api;
import com.example.gridlayout.entities.Bill;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface BillService {
    @POST("bills/")
    Call<Bill> createBill(@Body Bill bill);
    @GET("bills/{username}")
    Call<List<Bill>> getBillByUser(@Path("username") String username);
}
