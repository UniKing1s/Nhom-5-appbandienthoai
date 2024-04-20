package com.example.gridlayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gridlayout.Fragment.account.Account;
import com.example.gridlayout.Fragment.cart.CartFragment;
import com.example.gridlayout.Fragment.home.HomeFragment;
import com.example.gridlayout.dbmanager.CartDatabase;
import com.example.gridlayout.entities.CartItem;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private Button iconHome;
    private Button iconCart;
    private Button iconAccount;
    private TextView txtHeader;
//    private TextView txtAmountCart;
//    CartManager cartManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new HomeFragment(),0);
        iconHome = findViewById(R.id.btnHome);
        iconCart = findViewById(R.id.btnCart);
        iconAccount = findViewById(R.id.btnAccount);
        txtHeader = findViewById(R.id.txtHeader);
//        txtAmountCart = findViewById(R.id.txtAmountCart);
//        cartManager = new CartManager(this);
//        List<CartItem> items = cartManager.ListCartItem();
            List<CartItem> items = CartDatabase.getInstance(this).cartDAO().getListCartItem();
//        txtAmountCart.setText(""+items.size());
//        loadFragment(new HomeFragment(),0);
        setListener();
    }
    private void setListener(){
        iconHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment(),1 );
                txtHeader.setText("Trang chủ");
            }
        });
        iconCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtHeader.setText("Giỏ hàng");
                loadFragment(new CartFragment(),2);
            }
        });
        iconAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtHeader.setText("Tài khoản");
                loadFragment(new Account(),3 );
            }
        });
    }

    private void loadFragment(Fragment fragment, int flag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag == 0){
            ft.add(R.id.fragmentBox,fragment);
        }else{
            ft.replace(R.id.fragmentBox,fragment);
        }
        ft.commit();

    }
}