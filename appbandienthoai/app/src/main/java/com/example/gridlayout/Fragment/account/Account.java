package com.example.gridlayout.Fragment.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
//import com.example.gridlayout.entities.Account;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gridlayout.Fragment.account.login.LoginActivity;
import com.example.gridlayout.Fragment.account.register.RegisterActivity;
import com.example.gridlayout.Fragment.home.ProductDetaileActivity;
import com.example.gridlayout.R;
import com.example.gridlayout.dbmanager.AccountManager;
import com.example.gridlayout.dbmanager.CartDatabase;

public class Account extends Fragment {
    private static final int MY_REQUESTCODE = 10;
    AccountManager accountManager;
    public Account() {
        // Required empty public constructor
    }
    TextView txtInfo;
    LinearLayout lineLogin,lineRegister,lineLogout,lineInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.account_xml, container, false);
        lineLogin = view.findViewById(R.id.lineDangNhap);
        lineRegister = view.findViewById(R.id.lineDangKy);
        lineLogout = view.findViewById(R.id.lineDangXuat);
        lineInfo = view.findViewById(R.id.lineInfo);
        txtInfo = view.findViewById(R.id.txtInfo);
        accountManager = new AccountManager(getContext());
        com.example.gridlayout.entities.Account account = accountManager.getAccount();
        if(account != null){
            lineInfo.setVisibility(View.VISIBLE);
            lineLogin.setVisibility(View.GONE);
            lineRegister.setVisibility(View.GONE);
            lineLogout.setVisibility(View.VISIBLE);
            txtInfo.setText(account.getUsername());
        }else {
            lineLogout.setVisibility(View.GONE);
            lineInfo.setVisibility(View.GONE);
            lineLogin.setVisibility(View.VISIBLE);
            lineRegister.setVisibility(View.VISIBLE);
        }
        lineLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {view_loginform();
            }
        });
        lineRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_registerform();
            }
        });
        lineLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartDatabase.getInstance(getContext()).cartDAO().deleteAll();
                accountManager.deleteAccount(account.getUsername());
                lineInfo.setVisibility(View.GONE);
                lineLogin.setVisibility(View.VISIBLE);
                lineRegister.setVisibility(View.VISIBLE);
                lineLogout.setVisibility(View.GONE);
            }
        });
        return view;
    }



    private void view_loginform(){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivityForResult(intent,MY_REQUESTCODE);
    }
    private void view_registerform(){
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivityForResult(intent,MY_REQUESTCODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUESTCODE && resultCode == Activity.RESULT_OK){
            com.example.gridlayout.entities.Account account = accountManager.getAccount();
            if(account != null){
                lineInfo.setVisibility(View.VISIBLE);
                lineLogin.setVisibility(View.GONE);
                lineRegister.setVisibility(View.GONE);
                lineLogout.setVisibility(View.VISIBLE);
                txtInfo.setText(account.getUsername());
            }else {
                lineLogout.setVisibility(View.GONE);
                lineInfo.setVisibility(View.GONE);
                lineLogin.setVisibility(View.VISIBLE);
                lineRegister.setVisibility(View.VISIBLE);
            }
        }
    }
}