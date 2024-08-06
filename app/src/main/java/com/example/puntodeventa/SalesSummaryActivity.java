package com.example.puntodeventa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SalesSummaryActivity extends Activity {

    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;
    private ListView listViewVentas;
    private TextView textViewTotalVenta;
    private Button buttonInventario;
    private Button buttonSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_summary);

        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getReadableDatabase();

        listViewVentas = findViewById(R.id.listViewVentas);
        textViewTotalVenta = findViewById(R.id.textViewTotalVenta);
        buttonInventario = findViewById(R.id.buttonInventario);
        buttonSalir = findViewById(R.id.buttonSalir);

        loadSalesData();
        calculateTotal();

        buttonInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SalesSummaryActivity.this, InventarioActivity.class));
            }
        });

        buttonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadSalesData() {
        Cursor cursor = null;
        try {
            cursor = database.query("ventas",
                    new String[]{"cantidad", "precio", "importe"},
                    null, null, null, null, null);

            String[] fromColumns = {"cantidad", "precio", "importe"};
            int[] toViews = {R.id.textViewVentaCantidad, R.id.textViewVentaPrecio, R.id.textViewVentaImporte};

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    R.layout.item_venta, cursor, fromColumns, toViews, 0);
            listViewVentas.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void calculateTotal() {
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT SUM(importe) AS total FROM ventas", null);
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") double total = cursor.getDouble(cursor.getColumnIndex("total"));
                textViewTotalVenta.setText(getString(R.string.sales_total) + String.format("%.2f", total));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
