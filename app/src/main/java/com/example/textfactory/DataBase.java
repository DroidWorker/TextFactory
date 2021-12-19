package com.example.textfactory;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBase {
    Context appContext = null;
    SQLiteDatabase db = null;

    public DataBase(Context appContext)
    {
        this.appContext = appContext;

        db= appContext.openOrCreateDatabase("FontBuilder.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS fonts (name TEXT, age INTEGER)");
        //|font uri|creation date|font name|
    }


}