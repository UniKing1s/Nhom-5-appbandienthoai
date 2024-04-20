package com.example.gridlayout.Fragment.home;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gridlayout.R;
import com.example.gridlayout.api.CartService;
import com.example.gridlayout.dbmanager.AccountManager;
import com.example.gridlayout.dbmanager.CartDatabase;
import com.example.gridlayout.entities.Account;
import com.example.gridlayout.entities.CartItem;
import com.example.gridlayout.entities.Product;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeRecycleViewAdapter extends RecyclerView.Adapter<HomeRecycleViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Product> mlstPro;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://backendphonestore.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private CartService apiservice = retrofit.create(CartService.class);
    AccountManager accountManager;
//    CartManager cartManager;
    public HomeRecycleViewAdapter(Context context, List<Product> mlstPro) {
        this.mContext = context;
        checkData(mlstPro);
    }
    public void checkData(List<Product> lst){
        List<Product> productCanShow = new ArrayList<>();
        for(Product pro : lst){
            if (pro.getQuantity() > 0){
                productCanShow.add(pro);
            }
        }
        this.mlstPro = productCanShow;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
//        cartManager = new CartManager(mContext);
        view = mInflater.inflate(R.layout.card_view_item_product,parent,false);
        accountManager = new AccountManager(mContext);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ///Sử dụng edit giá ra VND
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        //
        Product product = mlstPro.get(position);

        holder.txt_name.setText(product.getName());
        if (mlstPro.get(position).getSale() > 0){
            holder.txt_sale.setText("Sale: "+product.getSale() + "%");
            double price = product.getPrice() * ((100 - Double.parseDouble(""+product.getSale()))/100);
            String cost = format.format(price);
            String yourTextWithStrikethrough = "<s>"+product.getPrice()+"</s>";
            holder.txt_price.setText(Html.fromHtml(yourTextWithStrikethrough));
            holder.txt_priceSale.setText(cost);
            holder.txt_sale.setVisibility(View.VISIBLE);
            holder.txt_priceSale.setVisibility(View.VISIBLE);
        }else {
            String cost = format.format(product.getPrice());
            holder.txt_price.setText(cost);
            holder.txt_sale.setText(" ");
            holder.txt_sale.setVisibility(View.INVISIBLE);
            holder.txt_priceSale.setVisibility(View.INVISIBLE);
        }
//        holder.txt_price.setText(""+product.getPrice());
        Picasso.get()
                .load("https://backendphonestore.onrender.com/images/"+mlstPro.get(position).getImg())
                .into(holder.img_view);
        final int slot = position;
        holder.img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,"item: "+ mlstPro.get(slot).getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,ProductDetaileActivity.class);
                intent.putExtra("masp", product.getMasp());
                intent.putExtra("price",product.getPrice());
                intent.putExtra("sale",product.getSale());
                intent.putExtra("name",product.getName());
                intent.putExtra("img",product.getImg());
                intent.putExtra("decribtion",product.getDecribtion());
                intent.putExtra("quantity",product.getQuantity());
                intent.putExtra("status",product.getStatus());
                intent.putExtra("type",product.getType());
                mContext.startActivity(intent);
            }
        });
        holder.img_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem cartItem = new CartItem(product.getMasp(),product.getName(),product.getPrice(),product.getSale(),1,product.getImg());
                Account account = accountManager.getAccount();
                if (account != null) {
                    cartItem.calTotalPrice();
                    cartItem.setUsername(account.getUsername());
                    Call<CartItem> call = apiservice.addtoCart(cartItem);
                    call.enqueue(new Callback<CartItem>() {
                        @Override
                        public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(mContext, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                Log.i("Lỗi api: " ,"addto cart không lỗi");
                            }

                        }

                        @Override
                        public void onFailure(Call<CartItem> call, Throwable t) {
                            Log.i("Lỗi api: " ,"addto cart "+ t.toString());
                        }
                    });
                }
//                cartManager.AddCartItem(cartItem);
                List<CartItem> checkCart = CartDatabase.getInstance(v.getContext()).cartDAO().checkCart(cartItem.getMasp());
                if (checkCart.isEmpty()){
                    cartItem.calTotalPrice();
                    CartDatabase.getInstance(v.getContext()).cartDAO().insertCart(cartItem);
                }else {
                    cartItem.setQuantity(cartItem.getQuantity() + checkCart.get(0).getQuantity());
                    cartItem.calTotalPrice();
                    CartDatabase.getInstance(mContext).cartDAO().UpdateCart(cartItem);
                }

                Toast.makeText(mContext, "Thêm sản phẩm vào giỏ hàng thành công",Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return mlstPro.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img_view;
        TextView txt_name;
        TextView txt_price;
        TextView txt_sale;
        ImageView img_add_to_cart;
        TextView txt_priceSale;
        public MyViewHolder(View itemView){
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txtName);
            img_view = (ImageView) itemView.findViewById(R.id.img_view);
            txt_price = (TextView) itemView.findViewById(R.id.txtPrice);
            txt_sale = (TextView) itemView.findViewById(R.id.txtSale);
            img_add_to_cart = (ImageView) itemView.findViewById(R.id.img_aka_add_to_cart);
            txt_priceSale = (TextView) itemView.findViewById(R.id.txtPriceSale);
        }
    }
}
