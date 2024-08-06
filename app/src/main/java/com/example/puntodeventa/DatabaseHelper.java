package com.example.puntodeventa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "posapp.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_CATEGORIAS_TABLE = "CREATE TABLE categorias (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre TEXT);";

    private static final String CREATE_PRODUCTOS_TABLE = "CREATE TABLE productos (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre TEXT, " +
            "precio REAL, " +
            "cantidad INTEGER, " +
            "imagen BLOB, " +
            "fecha_guardado TEXT, " +
            "categoria_id INTEGER, " +
            "FOREIGN KEY(categoria_id) REFERENCES categorias(id));";

    private static final String CREATE_VENTAS_TABLE = "CREATE TABLE ventas (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "producto_id INTEGER, " +
            "cantidad INTEGER, " +
            "precio REAL, " +
            "importe REAL, " +
            "fecha_venta TEXT, " +
            "FOREIGN KEY(producto_id) REFERENCES productos(id));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORIAS_TABLE);
        db.execSQL(CREATE_PRODUCTOS_TABLE);
        db.execSQL(CREATE_VENTAS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ventas");
        db.execSQL("DROP TABLE IF EXISTS productos");
        db.execSQL("DROP TABLE IF EXISTS categorias");
        onCreate(db);
    }
}

