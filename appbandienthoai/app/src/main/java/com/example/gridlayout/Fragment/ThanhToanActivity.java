package com.example.gridlayout.Fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gridlayout.R;
import com.example.gridlayout.api.BillService;
import com.example.gridlayout.api.CartService;
import com.example.gridlayout.api.ProductService;
import com.example.gridlayout.dbmanager.AccountManager;
import com.example.gridlayout.dbmanager.CartDatabase;
import com.example.gridlayout.entities.Account;
import com.example.gridlayout.entities.Bill;
import com.example.gridlayout.entities.CartItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ThanhToanActivity extends AppCompatActivity {

    EditText editTextName, editTextSdt, editTextAddress;
    TextView total;
    Button btnTroVe,btnThanhToan;
    AccountManager accountManager;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://backendphonestore.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private BillService apiservice = retrofit.create(BillService.class);
    private CartService apiserviceCart = retrofit.create(CartService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutthanhtoan);
        editTextName = (EditText)  findViewById(R.id.editTextName);
        editTextSdt = (EditText) findViewById(R.id.editTextSDT);
        editTextAddress = (EditText) findViewById(R.id.editTextDiaChi);
        btnTroVe = (Button) findViewById(R.id.btnTroVe);
        total = (TextView) findViewById(R.id.txtTotal);
        btnThanhToan = (Button) findViewById(R.id.btnThanhToan);
        accountManager = new AccountManager(this);


        List<CartItem> cartItems=  CartDatabase.getInstance(this).cartDAO().getListCartItem();
        double totalPrice = 0;
        for (CartItem i: cartItems){
            totalPrice += i.getTotalPrice();
        }

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        String price = format.format(totalPrice);
        total.setText(""+price);
        btnTroVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextName.getText().toString().replaceAll(" ","") == ""){
                    Toast.makeText(v.getContext(),"Chưa nhập tên người nhận",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editTextSdt.getText().toString().replaceAll(" ","") == ""){
                    Toast.makeText(v.getContext(),"Chưa nhập số điện thoại",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(editTextAddress.getText().toString().replaceAll(" ","") == ""){
                    Toast.makeText(v.getContext(),"Chưa nhập địa chỉ giao hàng",Toast.LENGTH_SHORT).show();
                    return;
                }
                Account account = accountManager.getAccount();
                if(account != null){
                    List<CartItem> items = new ArrayList<>();
                    double totalPrice = 0;
                    for (CartItem i: cartItems){
                        if(i.getQuantity() > 0){
                            items.add(i);
                            totalPrice += i.getTotalPrice();
                        }

                    }
                    Log.i("Payment: ","Có account");
                    Bill bill = new Bill(account.getUsername(),editTextName.getText().toString(),
                            editTextSdt.getText().toString(), editTextAddress.getText().toString(),
                            totalPrice,"Tiền mặt",false,
                            items);
                    Log.i("Payment: ","Call api bill");
                    Call<Bill> call = apiservice.createBill(bill);
                    call.enqueue(new Callback<Bill>() {
                        @Override
                        public void onResponse(Call<Bill> call, Response<Bill> response) {
                            if (response.isSuccessful()){
                                Log.i("Payment: ","Tạo hóa đơn thành công");
                                List<CartItem> items = CartDatabase.getInstance(v.getContext()).cartDAO().getListCartItem();
                                Log.i("Payment: ","call api xóa item cart trong db sau tt");

                                Call<CartItem> call1 = apiserviceCart.deleteCartAfterPay(account.getUsername());
                                call1.enqueue(new Callback<CartItem>() {
                                    @Override
                                    public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                                        if(response.isSuccessful()){
                                            Log.i("Payment: ","Xử lí item thành công ở db và sqlite");
                                            try {
                                                Log.i("Payment: ","Tạo result và kết thúc phiên");
                                                Toast.makeText(v.getContext(),"Thanh toán thành công", Toast.LENGTH_SHORT).show();
                                                Intent intentResult = new Intent();
                                                setResult(Activity.RESULT_OK,intentResult);

                                            }catch (Exception e){
                                                Log.i("Payment: ","Lỗi tạo result "+e.getMessage());
                                            }
                                            finish();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<CartItem> call, Throwable t) {
                                        Log.i("Lỗi thanh toán: ","api lỗi khi xóa full itemcart sau thanh toán "+ t.toString());
                                    }
                                });

                            }
                        }

                        @Override
                        public void onFailure(Call<Bill> call, Throwable t) {
                            Toast.makeText(v.getContext(), "Thanh toán lỗi",Toast.LENGTH_SHORT).show();
                            Log.i("Payment: ","Lỗi thanh toán api "+ t.toString());
                        }
                    });


                }
            }
        });

    }
}