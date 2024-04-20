package com.example.gridlayout.Fragment.cart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.service.autofill.UserData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gridlayout.Fragment.ThanhToanActivity;
import com.example.gridlayout.Fragment.account.login.LoginActivity;
import com.example.gridlayout.R;
import com.example.gridlayout.dbmanager.AccountManager;
import com.example.gridlayout.dbmanager.CartDatabase;
//import com.example.gridlayout.dbmanager.CartManager;
import com.example.gridlayout.entities.CartItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;


public class CartFragment extends Fragment implements CartInterface{
    List<CartItem> cartItems;
    private CartAdapter adapter;
//    private CartManager cartManager;

//    private  CartViewModel cartViewModel;
    TextView txtTotalAllCart;
    private static final int MY_REQUESTCODE_Payment = 10;
    private static final int MY_REQUESTCODE = 12;
    private AccountManager accountManager;
    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.cart_view, container, false);
        ListView lsListView = (ListView) view.findViewById(R.id.productList);
        txtTotalAllCart = (TextView) view.findViewById(R.id.txtTotalAllCart);
        Button btnPayment = (Button) view.findViewById(R.id.btnPayment);
        accountManager = new AccountManager(getContext());
        //CartViewModel -- livedata
//        cartViewModel = new  ViewModelProvider(this).get(CartViewModel.class);
//        cartViewModel.getlstCartLiveData().observe(getViewLifecycleOwner(), new Observer<List<CartItem>>() {
//            @Override
//            public void onChanged(List<CartItem> cartItems) {
//                double totalAll = 0;
//                for(CartItem item : cartItems){
//                    totalAll += item.getTotalPrice();
//                }
//                NumberFormat format = NumberFormat.getCurrencyInstance();
//                format.setMaximumFractionDigits(0);
//                format.setCurrency(Currency.getInstance("VND"));
//                String totalCost = format.format(totalAll);
//                txtTotalAllCart.setText(totalCost);
//                adapter =
//                        new CartAdapter(getContext(),
//                                R.layout.product_item, cartItems);
//
//                lsListView.setAdapter(adapter);
//            }
//        });
//        CartItem cartItem = new CartItem(102,"Iphone",10000,10,1,"20240413015816134vivoy36.jpg");
//        CartDatabase.getInstance(getContext()).cartDAO().insertCart(cartItem);
        cartItems = CartDatabase.getInstance(getContext()).cartDAO().getListCartItem();
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CartItem> checkCart = CartDatabase.getInstance(getContext()).cartDAO().getListCartItem();
                if(checkCart.isEmpty()){
                    Toast.makeText(getContext(),"Bạn không có món nào để mua",Toast.LENGTH_SHORT).show();
                }else{
                    double total = 0;
                    for(CartItem i : checkCart){
                        total += i.getTotalPrice();
                    }
                    if(total > 0){
                        com.example.gridlayout.entities.Account account = accountManager.getAccount();
                        if(account != null){
                            Intent intent = new Intent(getContext(), ThanhToanActivity.class);
                            startActivityForResult(intent,MY_REQUESTCODE_Payment);
                        }else {
                            Toast.makeText(getContext(),"Bạn chưa đăng nhập để thanh toán",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivityForResult(intent,MY_REQUESTCODE);
                        }

                    }else {
                        Toast.makeText(getContext(),"Bạn không có sản phẩm nào để thanh toán",Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });


//        cartManager = new CartManager(getContext());
//        cartItems = cartManager.ListCartItem();
        double totalAll = 0;
        for(CartItem item : cartItems){
            totalAll += item.getTotalPrice();
        }
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        String totalCost = format.format(totalAll);
        txtTotalAllCart.setText(totalCost);
        Log.i("Item from db: ",cartItems.toString());
        adapter =
                new CartAdapter(this,getContext(),
                                R.layout.product_item, cartItems);
        lsListView.setAdapter(adapter);
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUESTCODE_Payment && resultCode == Activity.RESULT_OK){
            com.example.gridlayout.entities.Account account = accountManager.getAccount();
            CartDatabase.getInstance(getContext()).cartDAO().deleteAllCartPayed();
//            cartItems = CartDatabase.getInstance(getContext()).cartDAO().getListCartItem();
             adapter.setListItem();
                ///Xử lý sau thanh toán
        }
        if (requestCode == MY_REQUESTCODE && resultCode == Activity.RESULT_OK){
            //Xử lý sau khi đăng nhập thành công
            double totalAll = 0;
            for(CartItem item : cartItems){
                totalAll += item.getTotalPrice();
            }
            if(totalAll > 0){
                Intent intent = new Intent(getContext(), ThanhToanActivity.class);
                startActivityForResult(intent,MY_REQUESTCODE_Payment);
            }else {
                Toast.makeText(getContext(),"Không có sản phẩm để thanh toán",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void updatePrice(double total) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        String totalCost = format.format(total);
        txtTotalAllCart.setText(totalCost);
    }
}