package com.example.lab_00.appku;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MataKuliahHelper extends SQLiteOpenHelper {
    private static String db_name = "ipb";
    private static SQLiteDatabase.CursorFactory factory = null;
    private static int version = 1;
    private String tbl_name = "tbl_mata_kuliah";

    public MataKuliahHelper(Context context) {
        super(context, db_name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + this.tbl_name + " (id_mata_kuliah VARCHAR(6), mata_kuliah TEXT, sks VARCHAR(6))");
        db.execSQL("INSERT INTO " + this.tbl_name + " VALUES ('INF301', 'Pemrograman Mobile', '3(1-2)')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + this.tbl_name);
        onCreate(db);
    }
}
