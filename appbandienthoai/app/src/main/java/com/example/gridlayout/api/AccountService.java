package com.example.gridlayout.api;
import com.example.gridlayout.entities.Account;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface AccountService {
    @POST("accounts/login")
    Call<Account> login(@Body Account account);

    @POST("accounts/")
    Call<Account> register(@Body Account account);

}
