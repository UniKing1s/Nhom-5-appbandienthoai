package com.example.gridlayout.Fragment.cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.gridlayout.R;
import com.example.gridlayout.api.AccountService;
import com.example.gridlayout.api.CartService;
import com.example.gridlayout.dbmanager.AccountManager;
import com.example.gridlayout.dbmanager.CartDatabase;
//import com.example.gridlayout.dbmanager.CartManager;
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

public class CartAdapter extends ArrayAdapter<CartItem> {
    private Context context;
    private  int resource;
    private List<CartItem> lstItem;
    private boolean dialogOpen = false;
//    private CartManager cartManager;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://backendphonestore.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private CartService apiservice = retrofit.create(CartService.class);
    private AccountManager accountManager;
    private CartInterface cartInterface;

    public CartAdapter(CartInterface cartInterface, Context context, int resource, List<CartItem> lstItem){
        super(context, resource,lstItem);
        this.context = context;
        this.resource = resource;
        this.lstItem = lstItem;
        this.cartInterface = cartInterface;
//        cartManager = new CartManager(this.context);
    }
    public void setListItem(){
        List<CartItem> itemsToRemove = new ArrayList<>();
        for(CartItem i : this.lstItem){
            if(i.getQuantity() > 0){
                itemsToRemove.add(i);

            }
        }
        this.lstItem.removeAll(itemsToRemove);
        notifyDataSetChanged();
        double total = 0;
        for (CartItem i : lstItem){
            total += i.getTotalPrice();
        }
        cartInterface.updatePrice(total);

    }

    @NonNull
    @Override
    public View getView(int position, View current, ViewGroup parent) {
        ViewHolder viewHolder;
        if (current == null){
            Log.i("Item trong adapter: ", lstItem.toString());
            current = LayoutInflater.from(context).inflate(resource,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.imgView = (ImageView) current.findViewById(R.id.imgViewImg);
            viewHolder.txtName = (TextView) current.findViewById(R.id.txtName);
            viewHolder.txtPrice = (TextView) current.findViewById(R.id.txtPrice);
            viewHolder.txtSale = (TextView) current.findViewById(R.id.txtSale);
            viewHolder.txtQuantity = (TextView) current.findViewById(R.id.txtQuantity);
            viewHolder.txtPriceSaleAfterCal = (TextView) current.findViewById(R.id.txtPriceSaleAfterCal);
            viewHolder.linePriceSaleAfterCal = (LinearLayout) current.findViewById(R.id.linePriceSaleAfterCal);
            //Lưu lại viewHolder
            viewHolder.txtTotalPrice = (TextView) current.findViewById(R.id.txtTotalPrice);
            current.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) current.getTag();
        }
        CartItem item = lstItem.get(position);
        viewHolder.txtName.setText(item.getName());
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("VND"));
        viewHolder.txtQuantity.setText(""+item.getQuantity());
        accountManager = new AccountManager(context);

        if(item.getSale() > 0){
            String cost = format.format(item.getPrice());
            double saleC = (item.getPrice()*(100 - Double.parseDouble(""+item.getSale()))/100);
            String saleCost = format.format(saleC);
            viewHolder.txtPrice.setText(Html.fromHtml("<s>"+cost+"</s>"));
            viewHolder.txtPriceSaleAfterCal.setText(saleCost);
            viewHolder.txtSale.setText("Sale: " + item.getSale() +" %");
            viewHolder.linePriceSaleAfterCal.setVisibility(View.VISIBLE);
            viewHolder.txtSale.setVisibility(View.VISIBLE);
        }else {
            String cost = format.format(item.getPrice());
            viewHolder.txtPrice.setText(cost);
            viewHolder.linePriceSaleAfterCal.setVisibility(View.INVISIBLE);
            viewHolder.txtSale.setVisibility(View.INVISIBLE);
        }
//        DecimalFormat formatter = new DecimalFormat("###,###,###");
////        String cost = formatter.format((product.getPrice())+" VNĐ");
        String total = format.format(item.getTotalPrice());
        viewHolder.txtTotalPrice.setText(total);
        Log.i("Item 1: ",lstItem.get(position).getImg());
        Picasso.get()
                .load("https://backendphonestore.onrender.com/images/"+lstItem.get(position).getImg())
                .into(viewHolder.imgView);




        ///Event handle cho các nút

        viewHolder.btnCong = current.findViewById(R.id.btnCong);
        viewHolder.btnTru = current.findViewById(R.id.btnTru);
        viewHolder.btnXoa = current.findViewById(R.id.btnXoa);
        Account accounts = accountManager.getAccount();
        viewHolder.btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                item.setQuantity(item.getQuantity()+1);
                viewHolder.txtQuantity.setText(""+ item.getQuantity());
                item.calTotalPrice();
                String totalAfterAdd = format.format(item.getTotalPrice());
                viewHolder.txtTotalPrice.setText(totalAfterAdd);
                CartDatabase.getInstance(getContext()).cartDAO().UpdateCart(item);
                if(accounts != null){
                    item.setUsername(accounts.getUsername());
                    Call<CartItem> call = apiservice.updateCartDb(item);
                    call.enqueue(new Callback<CartItem>() {
                        @Override
                        public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                            Toast.makeText(context,"Tăng thành công",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<CartItem> call, Throwable t) {
                            Toast.makeText(context,"Tăng thất bại",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                double total = 0;
                for (CartItem i : lstItem){
                    total += i.getTotalPrice();
                }
                cartInterface.updatePrice(total);
//                cartManager.EditCart(item);
            }
        });
        viewHolder.btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getQuantity() > 0){
                    item.setQuantity(item.getQuantity()-1);
                    viewHolder.txtQuantity.setText(""+ item.getQuantity());
                    item.calTotalPrice();
                    String totalAfterAdd = format.format(item.getTotalPrice());
                    viewHolder.txtTotalPrice.setText(totalAfterAdd);
//                    cartManager.EditCart(item);
                    CartDatabase.getInstance(getContext()).cartDAO().UpdateCart(item);
                    if(accounts != null){
                        item.setUsername(accounts.getUsername());
                        Call<CartItem> call = apiservice.updateCartDb(item);
                        call.enqueue(new Callback<CartItem>() {
                            @Override
                            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                                Toast.makeText(context,"Giảm thành công",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<CartItem> call, Throwable t) {
                                Toast.makeText(context,"Giảm thất bại",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    double total = 0;
                    for (CartItem i : lstItem){
                        total += i.getTotalPrice();
                    }
                    cartInterface.updatePrice(total);
                }
            }
        });
        viewHolder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dialogOpen){
                    showDialogDelete(item,position);
                    dialogOpen = true;
                }


            }
        });
        return current;
    }
    public void showDialogDelete(CartItem item, int position){
//        AlertDialog.Builder buider=new AlertDialog.Builder(getContext());
//        LayoutInflater inflater= getLayoutInflater();
//        View alerlayout=inflater.inflate(R.layout.delete_dialog, null);
//        TextView textView = (TextView) alerlayout.findViewById(R.id.textView);
//        buider.setView(alerlayout);
//        textView.setText("Bạn có chắc là muốn xóa sản phẩm "+ item.getName());
//        buider.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////                        product.setId(product.getId());
//                deleteProduct(product.getId(),product);
//            }
//        });
//        buider.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        buider.show();
//        Context context = this;

// Create an AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo!");
        builder.setMessage("Bạn có chắc muốn xóa sản phẩm khỏi giỏ hàng?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                cartManager.deleteItem(item.getMasp());
                try{
                    Account account = accountManager.getAccount();
                    if(account != null){
                        lstItem.get(position).setName(account.getUsername());
                        Call<CartItem> cartItemCall = apiservice.deleteCart(lstItem.get(position));
                        cartItemCall.enqueue(new Callback<CartItem>() {
                            @Override
                            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(context,"Xóa sản phẩm thành công",Toast.LENGTH_SHORT).show();
//                                    CartDatabase.getInstance(getContext()).cartDAO().deleteCartItem(item.getMasp());
//                                    lstItem.remove(position);
//                                    notifyDataSetChanged();
                                }else {
                                    Toast.makeText(context,"Không thể xóa sản phẩm thành công",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<CartItem> call, Throwable t) {
                                Toast.makeText(context,"Xóa sản phẩm thất bại",Toast.LENGTH_SHORT).show();
                                Log.i("Lỗi : ",t.toString());
                            }
                        });
                    }





                }catch (Exception e){
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    Log.i("Lỗi E: ",e.toString());
                }
                CartDatabase.getInstance(getContext()).cartDAO().deleteCartItem(item.getMasp());
                lstItem.remove(position);
                notifyDataSetChanged();
                double total = 0;
                for (CartItem i : lstItem){
                    total += i.getTotalPrice();
                }
                cartInterface.updatePrice(total);
//                Toast.makeText(context, "Xóa sản phẩm khỏi giỏ hàng thành công",Toast.LENGTH_SHORT).show();
                dialogOpen = false;
//                CartAdapter.this.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform action on negative button click
                dialogOpen = false;
            }
        });

// Create and show the dialog
        builder.create().show();
    }

    public class ViewHolder
    {
        ImageView imgView;
        TextView txtName, txtPrice, txtSale,txtPriceSaleAfterCal,txtTotalPrice, txtQuantity;
        Button btnCong, btnTru, btnXoa;
        LinearLayout linePriceSaleAfterCal;
        public ViewHolder() {
        }
    }

}

