package com.example.puntodeventa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Spinner spinnerCategorias;
    private ListView listViewProductos;
    private Button buttonFinalizarVenta;
    private Button buttonInventario;
    private Button buttonSalir;
    private List<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        spinnerCategorias = findViewById(R.id.spinnerCategorias);
        listViewProductos = findViewById(R.id.listViewProductos);
        buttonFinalizarVenta = findViewById(R.id.buttonFinalizarVenta);
        buttonInventario = findViewById(R.id.buttonInventario);
        buttonSalir = findViewById(R.id.buttonSalir); // Inicialización

        loadCategories();
        loadProducts();

        listViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showQuantityDialog(productList.get(position));
            }
        });

        buttonFinalizarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizeSale();
            }
        });

        buttonInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToInventario();
            }
        });


        buttonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSalesSummary();
            }
        });
    }

    @SuppressLint("Range")
    private void loadCategories() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM categorias", null);
        List<String> categories = new ArrayList<>();
        while (cursor.moveToNext()) {
            categories.add(cursor.getString(cursor.getColumnIndex("nombre")));
        }
        cursor.close();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(categoryAdapter);
    }

    private void loadProducts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM productos", null);
        productList.clear();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
            @SuppressLint("Range") double precio = cursor.getDouble(cursor.getColumnIndex("precio"));
            @SuppressLint("Range") byte[] imagen = cursor.getBlob(cursor.getColumnIndex("imagen"));
            @SuppressLint("Range") Product product = new Product(cursor.getInt(cursor.getColumnIndex("id")), nombre, precio, imagen);
            productList.add(product);
        }
        cursor.close();
        productAdapter = new ProductAdapter(this, productList);
        listViewProductos.setAdapter(productAdapter);
    }

    private void showQuantityDialog(final Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cantidad");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int quantity = Integer.parseInt(input.getText().toString());
                    if (quantity > 0) {
                        saveSale(product, quantity);
                    } else {
                        Toast.makeText(MainActivity.this, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void saveSale(Product product, int quantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("producto_id", product.getId());
        values.put("cantidad", quantity);
        values.put("precio", product.getPrice());
        values.put("importe", product.getPrice() * quantity);
        values.put("fecha_venta", System.currentTimeMillis());

        long newRowId = db.insert("ventas", null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Venta registrada con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al registrar la venta", Toast.LENGTH_SHORT).show();
        }
    }

    private void finalizeSale() {

        Intent intent = new Intent(MainActivity.this, SalesSummaryActivity.class);
        startActivity(intent);
    }

    private void navigateToInventario() {
        Intent intent = new Intent(MainActivity.this, InventarioActivity.class);
        startActivity(intent);
    }


    private void navigateToSalesSummary() {
        Intent intent = new Intent(MainActivity.this, VentasActivity.class);
        startActivity(intent);
    }
}
