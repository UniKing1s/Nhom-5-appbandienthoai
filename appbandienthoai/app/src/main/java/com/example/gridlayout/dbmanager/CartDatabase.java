package com.example.gridlayout.dbmanager;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gridlayout.entities.CartItem;

@Database(entities = {CartItem.class},version = 2)
public abstract class CartDatabase extends RoomDatabase
{
    private static final String DATABASE_Name = "Cart.db";
    private  static CartDatabase instance;
//    synchronized tuần tự
    public static synchronized CartDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), CartDatabase.class, DATABASE_Name)
                    .allowMainThreadQueries()
                    .build();

        }
        return instance;
    }
    public abstract CartDAO cartDAO();
}
