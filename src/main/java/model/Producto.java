package model;

import java.sql.Date;

/**
 * Entidad que representa un producto del catálogo mayorista de abarrotes.
 */
public class Producto {

    private int id_producto;
    private String codigo;
    private String nombre;
    private String categoria;
    private String unidad_medida;
    private int stock_actual;
    private int stock_minimo;
    private Date fecha_vencimiento;
    private double precio_referencial;
    private boolean estado;

    public Producto() {
    }

    public Producto(int id_producto, String codigo, String nombre, String categoria,
                    String unidad_medida, int stock_actual, int stock_minimo,
                    Date fecha_vencimiento, double precio_referencial, boolean estado) {
        this.id_producto = id_producto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.unidad_medida = unidad_medida;
        this.stock_actual = stock_actual;
        this.stock_minimo = stock_minimo;
        this.fecha_vencimiento = fecha_vencimiento;
        this.precio_referencial = precio_referencial;
        this.estado = estado;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public int getStock_actual() {
        return stock_actual;
    }

    public void setStock_actual(int stock_actual) {
        this.stock_actual = stock_actual;
    }

    public int getStock_minimo() {
        return stock_minimo;
    }

    public void setStock_minimo(int stock_minimo) {
        this.stock_minimo = stock_minimo;
    }

    public Date getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(Date fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public double getPrecio_referencial() {
        return precio_referencial;
    }

    public void setPrecio_referencial(double precio_referencial) {
        this.precio_referencial = precio_referencial;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " [" + unidad_medida + "] (Stock: " + stock_actual + ")";
    }
}
