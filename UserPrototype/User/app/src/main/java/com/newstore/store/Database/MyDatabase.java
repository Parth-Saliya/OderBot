package com.newstore.store.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.newstore.store.Model.CartListModel;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {

    public double finalAmount = 0;

    private static final String DATABASE_NAME = "store.db";
    private static final String CART_LIST = "cart_list";

    private static final String query = "create table " + CART_LIST + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,PID TEXT,PNAME TEXT,PAMOUNT TEXT,QTY TEXT,TQTY TEXT,PIMG TEXT)";

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CART_LIST);
        onCreate(db);
    }


    //Cart
    private void cartInsert(String pId, String pName, String pAmount, String qty, String tQty, String pImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PID", pId);
        contentValues.put("PNAME", pName);
        contentValues.put("PAMOUNT", pAmount);
        contentValues.put("QTY", qty);
        contentValues.put("TQTY", tQty);
        contentValues.put("PIMG", pImage);

        db.insert(CART_LIST, null, contentValues);
    }

    private void cartUpdate(String pId, String pName, String pAmount, String qty, String tQty, String pImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("PID", pId);
        contentValues.put("PNAME", pName);
        contentValues.put("PAMOUNT", pAmount);
        contentValues.put("QTY", qty);
        contentValues.put("TQTY", tQty);
        contentValues.put("PIMG", pImage);

        db.update(CART_LIST, contentValues, "PID = ?", new String[]{pId});
    }

    public void cartInsertOrUpdate(String pId, String pName, String pAmount, String qty, String tQty, String pImage) {
        String query = "SELECT * FROM " + CART_LIST + " WHERE (PID = '" + pId + "')";
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cartUpdate(pId, pName, pAmount, qty, tQty, pImage);
        } else {
            cartInsert(pId, pName, pAmount, qty, tQty, pImage);
        }
    }

    public void carItemRemove(String pID) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + CART_LIST + " WHERE PID = '" + pID + "'");
    }

    public void cartDelete() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + CART_LIST + "");
    }

    public List<CartListModel> getCartList() {
        List<CartListModel> modelList = new ArrayList<>();
        String query = "select * from " + CART_LIST + " ";
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                CartListModel model = new CartListModel();
                model.setId(cursor.getString(0));
                model.setpId(cursor.getString(1));
                model.setpName(cursor.getString(2));
                model.setpAmount(cursor.getString(3));
                model.setpQty(cursor.getString(4));
                model.setpTQty(cursor.getString(5));
                model.setpImage(cursor.getString(6));

                modelList.add(model);
            } while (cursor.moveToNext());
        }
        Log.d("cart data", modelList.toString());
        return modelList;
    }

    public void getFinalAmount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT sum(PAMOUNT * QTY) as finalAmount FROM cart_list";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            finalAmount = cursor.getDouble(cursor.getColumnIndex("finalAmount"));
        }
    }

}
