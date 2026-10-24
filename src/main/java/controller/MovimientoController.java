package controller;

import dao.MovimientoDAO;
import dao.ProductoDAO;
import model.Movimiento;
import model.Producto;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador de reglas de negocio para el registro de Movimientos (Kardex) y control de Stock.
 */
public class MovimientoController {

    private final MovimientoDAO movimientoDAO;
    private final ProductoDAO productoDAO;

    public MovimientoController() {
        this.movimientoDAO = new MovimientoDAO();
        this.productoDAO = new ProductoDAO();
    }

    /**
     * Lista el historial completo de movimientos del Kardex.
     *
     * @return Lista de objetos Movimiento.
     */
    public List<Movimiento> listarMovimientos() {
        return movimientoDAO.listar();
    }

    /**
     * Registra un movimiento de inventario (ENTRADA o SALIDA) verificando previamente
     * que el producto esté activo y que el stock nunca quede en negativo.
     *
     * @param idProducto     ID del producto afectado.
     * @param idUsuario      ID del usuario que registra la operación.
     * @param tipoMovimiento "ENTRADA" o "SALIDA".
     * @param motivo         Motivo del movimiento (ej. COMPRA PROVEEDOR, DESPACHO TIENDA, MERMA).
     * @param cantidad       Cantidad de unidades de almacén (> 0).
     * @param observacion    Comentario o referencia documental (guía, factura, lote).
     * @return "OK" si se procesó la transacción y se actualizó el stock, o mensaje de error.
     */
    public String registrarMovimiento(int idProducto, int idUsuario, String tipoMovimiento,
                                      String motivo, int cantidad, String observacion) {
        if (idProducto <= 0) {
            return "Debe seleccionar un producto válido.";
        }
        if (idUsuario <= 0) {
            return "No se identificó al usuario en sesión. Vuelva a iniciar sesión.";
        }
        if (tipoMovimiento == null || (!tipoMovimiento.equals("ENTRADA") && !tipoMovimiento.equals("SALIDA"))) {
            return "El tipo de movimiento debe ser ENTRADA o SALIDA.";
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            return "Debe especificar el motivo del movimiento.";
        }
        if (cantidad <= 0) {
            return "La cantidad del movimiento debe ser un número entero mayor a cero (0).";
        }

        // Consulta en tiempo real a la BD para validar existencia, estado y stock disponible
        Producto prodActual = productoDAO.obtenerPorId(idProducto);
        if (prodActual == null) {
            return "El producto seleccionado no existe en la base de datos.";
        }
        if (!prodActual.isEstado()) {
            return "El producto '" + prodActual.getNombre() + "' está INACTIVO y no admite movimientos.";
        }

        // REGLA CRÍTICA 1: Un producto nunca puede quedar con stock_actual negativo
        if ("SALIDA".equalsIgnoreCase(tipoMovimiento)) {
            if (cantidad > prodActual.getStock_actual()) {
                return String.format(
                        "Operación rechazada: Stock insuficiente para '%s'.\nStock disponible: %d (%s) | Cantidad solicitada: %d",
                        prodActual.getNombre(),
                        prodActual.getStock_actual(),
                        prodActual.getUnidad_medida(),
                        cantidad
                );
            }
        }

        try {
            Movimiento m = new Movimiento();
            m.setId_producto(idProducto);
            m.setId_usuario(idUsuario);
            m.setTipo_movimiento(tipoMovimiento.toUpperCase());
            m.setMotivo(motivo.trim().toUpperCase());
            m.setCantidad(cantidad);
            m.setObservacion(observacion != null ? observacion.trim() : "");

            movimientoDAO.registrar(m);
            return "OK";
        } catch (SQLException e) {
            return "Error Transaccional en SQL Server: " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado al registrar movimiento: " + e.getMessage();
        }
    }
}
