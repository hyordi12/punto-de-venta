package com.example.puntodeventa;

public class Venta {
    private int id;
    private int productoId;
    private int cantidad;
    private double precio;
    private double importe;

    public Venta(int id, int productoId, int cantidad, double precio, double importe) {
        this.id = id;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precio = precio;
        this.importe = importe;
    }

    public int getId() { return id; }
    public int getProductoId() { return productoId; }
    public int getCantidad() { return cantidad; }
    public double getPrecio() { return precio; }
    public double getImporte() { return importe; }
}

