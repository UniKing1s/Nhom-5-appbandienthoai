package com.example.gridlayout.Fragment.account.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gridlayout.R;
import com.example.gridlayout.api.AccountService;
import com.example.gridlayout.api.CartService;
import com.example.gridlayout.dbmanager.AccountManager;
import com.example.gridlayout.dbmanager.CartDatabase;
import com.example.gridlayout.entities.Account;
import com.example.gridlayout.entities.CartItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    EditText editTDN, editMK, editNLMK;
    Button btnDK;
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
        setContentView(R.layout.register_form);
        editTDN = findViewById(R.id.editTDN);
        editMK = findViewById(R.id.editMK);
        editNLMK = findViewById(R.id.editNMK);
        btnDK = findViewById(R.id.btnDK);
        accountManager = new AccountManager(this);

        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTDN.getText().toString().replaceAll(" ","").equals("")){
                    Toast.makeText(v.getContext(),"Chưa nhập tên đăng nhập",Toast.LENGTH_SHORT).show();
                }
                if(editMK.getText().toString().replaceAll(" ","").equals("")){
                    Toast.makeText(v.getContext(),"Chưa nhập mật khẩu",Toast.LENGTH_SHORT).show();
                }
                if(editNLMK.getText().toString().replaceAll(" ","").equals("")){
                    Toast.makeText(v.getContext(),"Chưa nhập lại mật khẩu",Toast.LENGTH_SHORT).show();
                }
                if(editTDN.getText().toString().length() >5 || editTDN.getText().toString().length() <=12){
                    if(editMK.getText().toString().length() >5 || editMK.getText().toString().length() <=12){
                        if(editMK.getText().toString().equals(editNLMK.getText().toString())){
                            Account account = new Account(editTDN.getText().toString(), editMK.getText().toString());
                            Call<Account> call = apiservice.register(account);
                            call.enqueue(new Callback<Account>() {
                                @Override
                                public void onResponse(Call<Account> call, Response<Account> response) {
                                    if(response.isSuccessful()){
                                        accountManager.AddAccount(account);
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
                                        Toast.makeText(v.getContext(),"Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                        Intent intentResult = new Intent();
                                        setResult(Activity.RESULT_OK,intentResult);
                                        finish();
                                    }
                                    Toast.makeText(v.getContext(),"Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Account> call, Throwable t) {
//                                    Toast.makeText(v.getContext(),"Tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            Toast.makeText(v.getContext(),"Nhập lại mật khẩu không trùng khớp",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(v.getContext(),"Mật khẩu phải từ 6 - 12 ký tự",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(v.getContext(),"Tài khoản phải từ 6 - 12 ký tự",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}