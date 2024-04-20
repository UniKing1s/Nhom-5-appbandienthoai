package com.example.gridlayout.Fragment.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gridlayout.R;
import com.example.gridlayout.api.ProductService;
import com.example.gridlayout.api.entitesforapi.ProductSearch;
import com.example.gridlayout.entities.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {
    private View view;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://backendphonestore.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private ProductService apiservice = retrofit.create(ProductService.class);
    private List<Product> productListData;
    private HomeRecycleViewAdapter adapter;
    private int pgNumber = 1;
    //Recycle view
    private RecyclerView recyclerView;



    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
//        EditText pageNumber = (EditText) view.findViewById(R.id.editTextPageNumber);
//        Button previousPageBtn = (Button) view.findViewById(R.id.btnPrevious);
//        Button nextPageBtn = (Button) view.findViewById(R.id.btnNext);
        Button search = (Button) view.findViewById(R.id.btnSearch);
        EditText txtSearch = (EditText) view.findViewById(R.id.editTextSearch);

//        pageNumber.setText(""+pgNumber);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        loadListItem(recyclerView);



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = txtSearch.getText().toString();
                filterList(text);
            }
        });
//        previousPageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                String pageNum = pageNumber.getText().toString();
////                int number = Integer.parseInt(pageNum);
//                if(pgNumber > 1){
//                    pgNumber --;
//                    pageNumber.setText(""+pgNumber);
//                }
//            }
//        });
//        nextPageBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                String pageNum = pageNumber.getText().toString();
////                int number = Integer.parseInt(pageNum);
//                pgNumber ++;
//                pageNumber.setText(""+pgNumber);
////                if(pageNum != "0"){
////
////                }
//            }
//        });
//        pageNumber.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////                pageNumber.setText("");
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                try {
//                    int number = Integer.parseInt(pageNumber.getText().toString());
//                    if(number >= 1){
//                        pgNumber = number;
//                        Toast.makeText(getContext(),"Trang: "+ number,Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        pageNumber.setText(""+pgNumber);
//                    }
//                }catch (Exception e){
//                    pageNumber.setText(""+pgNumber);
//                }
//            }
//        });
        return view;
    }
    private void filterList(String newText){

//                = new ArrayList<>();
//        for (Product pro : productListData){
//            if(pro.getName().toLowerCase().contains(newText.toLowerCase())){
//                filterLst.add(pro);
//            }
//        }
        ProductSearch productSearch = new ProductSearch(newText);
        Call<List<Product>> call = apiservice.searchProduct(productSearch);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> filterLst;
                if(response.isSuccessful()){
                    filterLst = response.body();
                    adapter =
                    new HomeRecycleViewAdapter(getContext(), filterLst);
                    recyclerView.setAdapter(adapter);
                }else {
                    Toast.makeText(getContext(),"Không có sản phẩm tương ứng",Toast.LENGTH_SHORT).show();
                    loadListItem(recyclerView);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(),"Không có sản phẩm tương ứng",Toast.LENGTH_SHORT).show();
                loadListItem(recyclerView);
            }
        });
//        if (filterLst.isEmpty()){
//            Toast.makeText(getContext(),"Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
//        }else {
//            adapter =
//                    new HomeRecycleViewAdapter(getContext(), filterLst);
//            recyclerView.setAdapter(adapter);
//        }
    }
    private void loadListItem(RecyclerView recyclerView_product){
        Call<List<Product>> call = apiservice.getAllProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                    productListData = response.body();
                    adapter =
                            new HomeRecycleViewAdapter(getContext(), productListData);
                    recyclerView_product.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.i("Lỗi: ",t.toString());
            }
        });
    }
}