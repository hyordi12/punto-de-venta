package com.example.puntodeventa;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class InventarioActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Button buttonAgregarCategoria, buttonActualizarCategoria, buttonEliminarCategoria;
    private Button buttonInsertarProducto, buttonActualizarProducto, buttonEliminarProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        dbHelper = new DatabaseHelper(this);

        buttonAgregarCategoria = findViewById(R.id.buttonAgregarCategoria);
        buttonActualizarCategoria = findViewById(R.id.buttonActualizarCategoria);
        buttonEliminarCategoria = findViewById(R.id.buttonEliminarCategoria);

        buttonInsertarProducto = findViewById(R.id.buttonInsertarProducto);
        buttonActualizarProducto = findViewById(R.id.buttonActualizarProducto);
        buttonEliminarProducto = findViewById(R.id.buttonEliminarProducto);

        buttonAgregarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog();
            }
        });

        buttonActualizarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateCategoryDialog();
            }
        });

        buttonEliminarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteCategoryDialog();
            }
        });

        buttonInsertarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInsertProductDialog();
            }
        });

        buttonActualizarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateProductDialog();
            }
        });

        buttonEliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteProductDialog();
            }
        });
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Categoría");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categoryName = input.getText().toString();
                if (!categoryName.isEmpty()) {
                    insertCategory(categoryName);
                } else {
                    Toast.makeText(InventarioActivity.this, "El nombre de la categoría no puede estar vacío", Toast.LENGTH_SHORT).show();
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

    private void insertCategory(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", name);

        long newRowId = db.insert("categorias", null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Categoría agregada con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al agregar la categoría", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUpdateCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar Categoría");

        final EditText inputId = new EditText(this);
        inputId.setHint("ID de Categoría");
        builder.setView(inputId);

        final EditText inputName = new EditText(this);
        inputName.setHint("Nuevo nombre");
        builder.setView(inputName);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categoryId = inputId.getText().toString();
                String newName = inputName.getText().toString();
                if (!categoryId.isEmpty() && !newName.isEmpty()) {
                    updateCategory(Integer.parseInt(categoryId), newName);
                } else {
                    Toast.makeText(InventarioActivity.this, "Todos los campos deben estar llenos", Toast.LENGTH_SHORT).show();
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

    private void updateCategory(int id, String newName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", newName);

        int rowsUpdated = db.update("categorias", values, "id = ?", new String[]{String.valueOf(id)});
        if (rowsUpdated > 0) {
            Toast.makeText(this, "Categoría actualizada con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al actualizar la categoría", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Categoría");

        final EditText input = new EditText(this);
        input.setHint("ID de Categoría");
        builder.setView(input);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categoryId = input.getText().toString();
                if (!categoryId.isEmpty()) {
                    deleteCategory(Integer.parseInt(categoryId));
                } else {
                    Toast.makeText(InventarioActivity.this, "El ID de la categoría no puede estar vacío", Toast.LENGTH_SHORT).show();
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

    private void deleteCategory(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("categorias", "id = ?", new String[]{String.valueOf(id)});
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Categoría eliminada con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al eliminar la categoría", Toast.LENGTH_SHORT).show();
        }
    }

    private void showInsertProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Insertar Producto");

        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_insert_product, null);
        builder.setView(dialogView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText inputName = dialogView.findViewById(R.id.ProductName);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText inputPrice = dialogView.findViewById(R.id.ProductPrice);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText inputQuantity = dialogView.findViewById(R.id.ProductQuantity);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText inputImage = dialogView.findViewById(R.id.ProductImage);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final Spinner spinnerCategory = dialogView.findViewById(R.id.SpinnerCategory);

        loadCategoriesIntoSpinner(spinnerCategory);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = inputName.getText().toString();
                String priceStr = inputPrice.getText().toString();
                String quantityStr = inputQuantity.getText().toString();
                String imageStr = inputImage.getText().toString(); // Convert image to BLOB

                if (!name.isEmpty() && !priceStr.isEmpty() && !quantityStr.isEmpty()) {
                    double price = Double.parseDouble(priceStr);
                    int quantity = Integer.parseInt(quantityStr);
                    int selectedCategoryId = (int) spinnerCategory.getTag(); // Get selected category ID
                    insertProduct(name, price, quantity, imageStr, selectedCategoryId); // Include selected category ID
                } else {
                    Toast.makeText(InventarioActivity.this, "Todos los campos deben estar llenos", Toast.LENGTH_SHORT).show();
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

    private void loadCategoriesIntoSpinner(Spinner spinner) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("categorias", new String[]{"id", "nombre"}, null, null, null, null, null);

        // Create a list of categories
        List<String> categories = new ArrayList<>();
        final List<Integer> categoryIds = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            categories.add(name);
            categoryIds.add(id);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Save the selected category ID in the tag of the spinner
                spinner.setTag(categoryIds.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void insertProduct(String name, double price, int quantity, String image, int categoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", name);
        values.put("precio", price);
        values.put("cantidad", quantity);
        values.put("imagen", (byte[]) null);
        values.put("fecha_guardado", System.currentTimeMillis());
        values.put("categoria_id", categoryId);

        long newRowId = db.insert("productos", null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Producto insertado con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al insertar el producto", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUpdateProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Actualizar Producto");

        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_product, null);
        builder.setView(dialogView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText inputId = dialogView.findViewById(R.id.TextProductId);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText inputPrice = dialogView.findViewById(R.id.TextProductPrice);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText inputQuantity = dialogView.findViewById(R.id.editProductQuantity);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String idStr = inputId.getText().toString();
                String priceStr = inputPrice.getText().toString();
                String quantityStr = inputQuantity.getText().toString();

                if (!idStr.isEmpty() && !priceStr.isEmpty() && !quantityStr.isEmpty()) {
                    int id = Integer.parseInt(idStr);
                    double price = Double.parseDouble(priceStr);
                    int quantity = Integer.parseInt(quantityStr);
                    updateProduct(id, price, quantity);
                } else {
                    Toast.makeText(InventarioActivity.this, "Todos los campos deben estar llenos", Toast.LENGTH_SHORT).show();
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

    private void updateProduct(int id, double price, int quantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("precio", price);
        values.put("cantidad", quantity);

        int rowsUpdated = db.update("productos", values, "id = ?", new String[]{String.valueOf(id)});
        if (rowsUpdated > 0) {
            Toast.makeText(this, "Producto actualizado con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Producto");

        final EditText input = new EditText(this);
        input.setHint("ID de Producto");
        builder.setView(input);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String productId = input.getText().toString();
                if (!productId.isEmpty()) {
                    deleteProduct(Integer.parseInt(productId));
                } else {
                    Toast.makeText(InventarioActivity.this, "El ID del producto no puede estar vacío", Toast.LENGTH_SHORT).show();
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

    private void deleteProduct(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete("productos", "id = ?", new String[]{String.valueOf(id)});
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Producto eliminado con éxito", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
        }
    }
}
