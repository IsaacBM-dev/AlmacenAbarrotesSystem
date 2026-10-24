
package model;

import java.sql.Timestamp;

/**
 * Entidad que representa una transacción de inventario (ENTRADA o SALIDA) en el Kardex.
 */
public class Movimiento {

    private int id_movimiento;
    private int id_producto;
    private String nombre_producto;
    private int id_usuario;
    private String username;
    private String tipo_movimiento;
    private String motivo;
    private int cantidad;
    private Timestamp fecha_registro;
    private String observacion;

    public Movimiento() {
    }

    public Movimiento(int id_movimiento, int id_producto, String nombre_producto,
                      int id_usuario, String username, String tipo_movimiento,
                      String motivo, int cantidad, Timestamp fecha_registro, String observacion) {
        this.id_movimiento = id_movimiento;
        this.id_producto = id_producto;
        this.nombre_producto = nombre_producto;
        this.id_usuario = id_usuario;
        this.username = username;
        this.tipo_movimiento = tipo_movimiento;
        this.motivo = motivo;
        this.cantidad = cantidad;
        this.fecha_registro = fecha_registro;
        this.observacion = observacion;
    }

    public int getId_movimiento() {
        return id_movimiento;
    }

    public void setId_movimiento(int id_movimiento) {
        this.id_movimiento = id_movimiento;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTipo_movimiento() {
        return tipo_movimiento;
    }

    public void setTipo_movimiento(String tipo_movimiento) {
        this.tipo_movimiento = tipo_movimiento;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Timestamp getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Timestamp fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
