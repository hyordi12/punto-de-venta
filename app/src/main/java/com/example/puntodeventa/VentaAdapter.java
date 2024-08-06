package com.example.puntodeventa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class VentaAdapter extends ArrayAdapter<Venta> {

    public VentaAdapter(Context context, List<Venta> ventas) {
        super(context, 0, ventas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_venta, parent, false);
        }

        Venta venta = getItem(position);

        TextView cantidadTextView = convertView.findViewById(R.id.textViewVentaCantidad);
        TextView precioTextView = convertView.findViewById(R.id.textViewVentaPrecio);
        TextView importeTextView = convertView.findViewById(R.id.textViewVentaImporte);

        cantidadTextView.setText("Cantidad: " + venta.getCantidad());
        precioTextView.setText("Precio: $" + venta.getPrecio());
        importeTextView.setText("Importe: $" + venta.getImporte());

        return convertView;
    }
}

