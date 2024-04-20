package com.example.gridlayout.Fragment.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gridlayout.MainActivity;
import com.example.gridlayout.R;
//import com.example.gridlayout.dbmanager.CartManager;
import com.example.gridlayout.api.CartService;
import com.example.gridlayout.api.ProductService;
import com.example.gridlayout.dbmanager.AccountManager;
import com.example.gridlayout.dbmanager.CartDatabase;
import com.example.gridlayout.entities.Account;
import com.example.gridlayout.entities.CartItem;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetaileActivity extends AppCompatActivity {
    TextView txtName,txtPrice,txtPriceSale, txtSale, txtDecrib, txtQuantity;
    ImageView productImg;
    Button btnAddToCart;
    AccountManager accountManager;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://backendphonestore.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private CartService apiservice = retrofit.create(CartService.class);
//    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detaile);
//        cartManager = new CartManager(this);
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtDecrib = findViewById(R.id.txtDecribtion);
        txtSale = findViewById(R.id.txtSale);
        txtPriceSale = findViewById(R.id.txtPriceSale);
        txtQuantity = findViewById(R.id.txtQuantity);
        productImg = findViewById(R.id.productImg);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        Button btnBack = findViewById(R.id.btnBack);
        accountManager = new AccountManager(this);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroy();
            }
        });
        LinearLayout lineSaleAmount = (LinearLayout) findViewById(R.id.lineSaleAmount);
        LinearLayout lineSale = (LinearLayout) findViewById(R.id.lineSale);
        Intent intent = getIntent();

        txtName.setText(intent.getExtras().getString("name"));
        double price = intent.getExtras().getDouble("price");

        txtQuantity.setText(""+intent.getExtras().getInt("quantity"));
        int sale = intent.getExtras().getInt("sale");

        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        double priceAfterCal = price * ((100 - Double.parseDouble(""+sale))/100);
        if (sale > 0){
            String cost =  format.format(price);
            String saleCost = format.format(priceAfterCal);
            String yourTextWithStrikethrough = "<s>"+cost+"</s>";
            txtPrice.setText(Html.fromHtml(yourTextWithStrikethrough));
            txtSale.setText(""+sale+" %");
            txtPriceSale.setText(saleCost);
            lineSale.setVisibility(View.VISIBLE);
            lineSaleAmount.setVisibility(View.VISIBLE);
        }else {
            String cost =  format.format(price);
            txtPrice.setText(cost);
            lineSale.setVisibility(View.INVISIBLE);
            lineSaleAmount.setVisibility(View.INVISIBLE);
        }

        String img = intent.getExtras().getString("img");
        Picasso.get()
                .load("https://backendphonestore.onrender.com/images/"+img)
                .into(productImg);
        String decribtion = intent.getExtras().getString("decribtion");
        String newS = "";
        String[] parts = decribtion.split("\n");
        for (String part : parts) {
            String[] line = part.split(":");
            String yourTextWithStrikethrough = "<b>"+line[0]+"</b>";
            newS += yourTextWithStrikethrough +" : "+line[1]+ "\n";
        }
        txtDecrib.setText(Html.fromHtml(newS));



        /// listener
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem item = new CartItem(intent.getExtras().getInt("masp"),
                        intent.getExtras().getString("name"),
                        intent.getExtras().getDouble("price"),
                        intent.getExtras().getInt("sale"),
                        1,
                        intent.getExtras().getString("img"));

//                CartItem itemfromData = cartManager.getCartItem(item.getMasp());
                Account account = accountManager.getAccount();
                if(account != null){
                    item.setUsername(account.getUsername());
                    Call<CartItem> call = apiservice.addtoCart(item);
                    call.enqueue(new Callback<CartItem>() {
                        @Override
                        public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                            if (response.isSuccessful()){
                                Toast.makeText(v.getContext(),"Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CartItem> call, Throwable t) {

                        }
                    });
                }
                List<CartItem> itemfromData = CartDatabase.getInstance(v.getContext()).cartDAO().checkCart(item.getMasp());
//                Log.i("Sản phẩm : ",itemfromData.getName() +" : "+itemfromData.getQuantity());
                if (itemfromData.isEmpty()){
//                    cartManager.AddCartItem(item);
                    item.calTotalPrice();
                    CartDatabase.getInstance(v.getContext()).cartDAO().insertCart(item);
                }else {
//                    item.setQuantity(item.getQuantity() + itemfromData.getQuantity());
                    item.setQuantity(item.getQuantity() + itemfromData.get(0).getQuantity());
                    item.calTotalPrice();
                    CartDatabase.getInstance(v.getContext()).cartDAO().UpdateCart(item);
//                    cartManager.EditCart(item);

                }
                Toast.makeText(v.getContext(),"Thêm sản phẩm vào giỏ thành công", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void destroy(){
        this.onBackPressed();
    }
}