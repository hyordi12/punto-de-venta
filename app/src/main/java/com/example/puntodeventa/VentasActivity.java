package com.example.puntodeventa;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class VentasActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listViewVentas;
    private TextView textViewTotalVenta;
    private VentaAdapter ventaAdapter;
    private List<Venta> ventaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        dbHelper = new DatabaseHelper(this);

        listViewVentas = findViewById(R.id.listViewVentas);
        textViewTotalVenta = findViewById(R.id.textViewTotalVenta);

        loadVentas();
        calculateTotal();
    }

    private void loadVentas() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ventas", null);
        ventaList.clear();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int productoId = cursor.getInt(cursor.getColumnIndex("producto_id"));
            @SuppressLint("Range") int cantidad = cursor.getInt(cursor.getColumnIndex("cantidad"));
            @SuppressLint("Range") double precio = cursor.getDouble(cursor.getColumnIndex("precio"));
            @SuppressLint("Range") double importe = cursor.getDouble(cursor.getColumnIndex("importe"));
            @SuppressLint("Range") Venta venta = new Venta(cursor.getInt(cursor.getColumnIndex("id")), productoId, cantidad, precio, importe);
            ventaList.add(venta);
        }
        cursor.close();
        ventaAdapter = new VentaAdapter(this, ventaList);
        listViewVentas.setAdapter(ventaAdapter);
    }

    private void calculateTotal() {
        double total = 0;
        for (Venta venta : ventaList) {
            total += venta.getImporte();
        }
        textViewTotalVenta.setText("Total: $" + total);
    }
}

