package com.example.gridlayout.dbmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.gridlayout.entities.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountManager extends SQLiteOpenHelper {
    String DB_NAME="ShopLogin";
    private String TABLE_NAME = "Account";
    private Context context;
    public AccountManager(Context context) {
        super(context,"ShopLogin",null,1);
        this.context=context;
    }
    //Tạo các table trong CSDL
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE Account(username nvarchar(50) primary key,"
                +"password nvarchar(50))";
        db.execSQL(sql);
    }
    //Tạo CSDL lúc update phiên bản
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
    public boolean AddAccount(Account account)
    {
        long kq=0;
        try {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues prams=new ContentValues();
            prams.put("username",account.getUsername());
            prams.put("password",account.getPassword());
            kq = db.insert(TABLE_NAME, null, prams);
            db.close();
        }catch (Exception ex)
        {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return kq>0;
    }
    public Account getAccount() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, null, null
                    , null,null, null, null);
            if(cursor != null)
                cursor.moveToFirst();
            Account account = new Account(cursor.getString(0), cursor.getString(1));
            return account;
        }catch (Exception e){
            return null;
        }
    }
    public void deleteAccount(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "username=?", new String[] { username });
        db.close();
    }
}
