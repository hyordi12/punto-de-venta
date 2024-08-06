package com.example.puntodeventa;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_product, parent, false);
        }

        Product product = getItem(position);

        TextView nameTextView = convertView.findViewById(R.id.textViewProductName);
        TextView priceTextView = convertView.findViewById(R.id.textViewProductPrice);
        ImageView productImageView = convertView.findViewById(R.id.imageViewProduct);

        nameTextView.setText(product.getName());
        priceTextView.setText("$" + product.getPrice());
        if (product.getImage() != null) {
            productImageView.setImageBitmap(BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length));
        } else {
            productImageView.setImageResource(R.drawable.fnd);
        }

        return convertView;
    }
}

