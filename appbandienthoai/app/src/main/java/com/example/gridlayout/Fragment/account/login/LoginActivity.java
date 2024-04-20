package com.example.gridlayout.Fragment.account.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.gridlayout.Fragment.home.HomeRecycleViewAdapter;
import com.example.gridlayout.MainActivity;
import com.example.gridlayout.api.AccountService;
import com.example.gridlayout.api.CartService;
import com.example.gridlayout.api.ProductService;
import com.example.gridlayout.api.entitesforapi.CartUser;
import com.example.gridlayout.dbmanager.AccountManager;
import com.example.gridlayout.dbmanager.CartDatabase;
import com.example.gridlayout.entities.Account;
import com.example.gridlayout.entities.CartItem;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.view.WindowCompat;



import com.example.gridlayout.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    EditText edittk, editTextPassword;
    Button btnLogin;
    AccountManager accountManager;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://backendphonestore.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private AccountService apiservice = retrofit.create(AccountService.class);
    private CartService apiserviceCart = retrofit.create(CartService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);
        accountManager = new AccountManager(this);
        edittk = findViewById(R.id.edittk);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account account = new Account(edittk.getText().toString(), editTextPassword.getText().toString());
                Call<Account> call = apiservice.login(account);
                call.enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if(response.isSuccessful()){
                            accountManager.AddAccount(account);
//                            CartDatabase.getInstance(v.getContext()).cartDAO().updateUserForCarts(account.getUsername());

                            List<CartItem> items = CartDatabase.getInstance(v.getContext()).cartDAO().getListCartItem();
//                            CartUser cartUser = new CartUser(account.getUsername(),items);
                            if(items.isEmpty()){
                                Call<List<CartItem>> call1 = apiserviceCart.getCarts(account.getUsername());
                                call1.enqueue(new Callback<List<CartItem>>() {
                                    @Override
                                    public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                                        if(response.isSuccessful()){
                                            List<CartItem> cartItems = response.body();
//                                            CartDatabase.getInstance(v.getContext()).cartDAO().deleteAll();
                                            for(CartItem i : cartItems){
                                                CartDatabase.getInstance(v.getContext()).cartDAO().insertCart(i);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<CartItem>> call, Throwable t) {

                                    }
                                });
                            }else {
                                CartDatabase.getInstance(v.getContext()).cartDAO().updateUserForCarts(account.getUsername());
                                List<CartItem> itemCarts = CartDatabase.getInstance(v.getContext()).cartDAO().getListCartItem();
                                Call<List<CartItem>> cartUserCall = apiserviceCart.storeCart(itemCarts);
                                cartUserCall.enqueue(new Callback<List<CartItem>>() {
                                    @Override
                                    public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                                        if(response.isSuccessful()){
                                            List<CartItem> cartItems = response.body();
                                            CartDatabase.getInstance(v.getContext()).cartDAO().deleteAll();
                                            for(CartItem i : cartItems){
                                                CartDatabase.getInstance(v.getContext()).cartDAO().insertCart(i);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<CartItem>> call, Throwable t) {

                                    }
                                });
                            }



                            Toast.makeText(v.getContext(),"Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intentResult = new Intent();
                            setResult(Activity.RESULT_OK,intentResult);
                            finish();
                        }else {
                            Toast.makeText(v.getContext(),"Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        Toast.makeText(v.getContext(),"Sai thông tin đăng nhập", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}