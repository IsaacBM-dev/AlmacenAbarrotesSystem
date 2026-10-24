
package model;

import java.sql.Date;

/**
 * POJO para mostrar alertas de stock mínimo y vencimiento próximo en el Dashboard principal.
 */
public class AlertaResumen {

    private int id_producto;
    private String codigo;
    private String nombre;
    private String categoria;
    private String unidad_medida;
    private int stock_actual;
    private int stock_minimo;
    private Date fecha_vencimiento;
    private Integer dias_para_vencer;
    private String tipo_alerta;

    public AlertaResumen() {
    }

    public AlertaResumen(int id_producto, String codigo, String nombre, String categoria,
                         String unidad_medida, int stock_actual, int stock_minimo,
                         Date fecha_vencimiento, Integer dias_para_vencer, String tipo_alerta) {
        this.id_producto = id_producto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.unidad_medida = unidad_medida;
        this.stock_actual = stock_actual;
        this.stock_minimo = stock_minimo;
        this.fecha_vencimiento = fecha_vencimiento;
        this.dias_para_vencer = dias_para_vencer;
        this.tipo_alerta = tipo_alerta;
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

    public Integer getDias_para_vencer() {
        return dias_para_vencer;
    }

    public void setDias_para_vencer(Integer dias_para_vencer) {
        this.dias_para_vencer = dias_para_vencer;
    }

    public String getTipo_alerta() {
        return tipo_alerta;
    }

    public void setTipo_alerta(String tipo_alerta) {
        this.tipo_alerta = tipo_alerta;
    }
}
