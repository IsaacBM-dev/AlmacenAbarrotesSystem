package controller;

import dao.ProductoDAO;
import model.AlertaResumen;
import model.Producto;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador de reglas de negocio para el Catálogo de Productos y Alertas.
 */
public class ProductoController {

    private final ProductoDAO productoDAO;

    public ProductoController() {
        this.productoDAO = new ProductoDAO();
    }

    /**
     * Lista todos los productos del almacén.
     *
     * @return Lista completa de productos.
     */
    public List<Producto> listarProductos() {
        return productoDAO.listar();
    }

    /**
     * Lista únicamente los productos con estado Activo (1) para el módulo de Movimientos.
     *
     * @return Lista de productos activos.
     */
    public List<Producto> listarProductosActivos() {
        return productoDAO.listarActivos();
    }

    /**
     * Busca productos por coincidencia en código, nombre o categoría.
     *
     * @param filtro Criterio de búsqueda.
     * @return Lista de productos filtrados.
     */
    public List<Producto> buscarProductos(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            return productoDAO.listar();
        }
        return productoDAO.buscar(filtro.trim());
    }

    /**
     * Obtiene un producto por su ID primario.
     *
     * @param idProducto ID del producto.
     * @return Objeto Producto o null.
     */
    public Producto obtenerProductoPorId(int idProducto) {
        return productoDAO.obtenerPorId(idProducto);
    }

    /**
     * Lista las alertas activas de stock mínimo o vencimiento próximo (<= 30 días).
     *
     * @return Lista de objetos AlertaResumen.
     */
    public List<AlertaResumen> listarAlertasInventario() {
        return productoDAO.listarAlertas();
    }

    /**
     * Valida las reglas de negocio y registra un nuevo producto en la base de datos.
     *
     * @param codigo            Código único del producto (ej. ABR-007).
     * @param nombre            Nombre descriptivo del producto.
     * @param categoria         Categoría del abarrote.
     * @param unidadMedida      Unidad de almacén (ej. SACO 50KG, CAJA X 24).
     * @param stockActual       Stock inicial (>= 0).
     * @param stockMinimo       Stock mínimo de seguridad (>= 0).
     * @param fechaVencimiento  Fecha de vencimiento opcional (puede ser null en no perecibles).
     * @param precioReferencial Precio mayorista referencial (> 0).
     * @param estado            Estado inicial (true = Activo).
     * @return "OK" si se registró correctamente, o mensaje de validación/error.
     */
    public String registrarProducto(String codigo, String nombre, String categoria, String unidadMedida,
                                    int stockActual, int stockMinimo, Date fechaVencimiento,
                                    double precioReferencial, boolean estado) {

        String validacion = validarDatosProducto(codigo, nombre, categoria, unidadMedida,
                stockActual, stockMinimo, precioReferencial);
        if (!"OK".equals(validacion)) {
            return validacion;
        }

        try {
            Producto p = new Producto();
            p.setCodigo(codigo.trim().toUpperCase());
            p.setNombre(nombre.trim());
            p.setCategoria(categoria.trim());
            p.setUnidad_medida(unidadMedida.trim().toUpperCase());
            p.setStock_actual(stockActual);
            p.setStock_minimo(stockMinimo);
            p.setFecha_vencimiento(fechaVencimiento);
            p.setPrecio_referencial(precioReferencial);
            p.setEstado(estado);

            productoDAO.registrar(p);
            return "OK";
        } catch (SQLException e) {
            return "Error en Base de Datos: " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado al registrar producto: " + e.getMessage();
        }
    }

    /**
     * Valida las reglas de negocio y actualiza un producto existente.
     *
     * @param idProducto        ID del producto a actualizar.
     * @param codigo            Código único del producto.
     * @param nombre            Nombre descriptivo.
     * @param categoria         Categoría.
     * @param unidadMedida      Unidad de almacén.
     * @param stockActual       Stock actual (>= 0).
     * @param stockMinimo       Stock mínimo (>= 0).
     * @param fechaVencimiento  Fecha de vencimiento (o null).
     * @param precioReferencial Precio referencial (> 0).
     * @param estado            Estado (true = Activo, false = Inactivo).
     * @return "OK" si se actualizó con éxito, o mensaje de error.
     */
    public String actualizarProducto(int idProducto, String codigo, String nombre, String categoria,
                                     String unidadMedida, int stockActual, int stockMinimo,
                                     Date fechaVencimiento, double precioReferencial, boolean estado) {

        if (idProducto <= 0) {
            return "Debe seleccionar un producto válido de la tabla para actualizar.";
        }

        String validacion = validarDatosProducto(codigo, nombre, categoria, unidadMedida,
                stockActual, stockMinimo, precioReferencial);
        if (!"OK".equals(validacion)) {
            return validacion;
        }

        try {
            Producto p = new Producto();
            p.setId_producto(idProducto);
            p.setCodigo(codigo.trim().toUpperCase());
            p.setNombre(nombre.trim());
            p.setCategoria(categoria.trim());
            p.setUnidad_medida(unidadMedida.trim().toUpperCase());
            p.setStock_actual(stockActual);
            p.setStock_minimo(stockMinimo);
            p.setFecha_vencimiento(fechaVencimiento);
            p.setPrecio_referencial(precioReferencial);
            p.setEstado(estado);

            productoDAO.actualizar(p);
            return "OK";
        } catch (SQLException e) {
            return "Error en Base de Datos: " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado al actualizar producto: " + e.getMessage();
        }
    }

    /**
     * Cambia el estado de un producto (Borrado lógico: Activo / Inactivo).
     *
     * @param idProducto  ID del producto.
     * @param nuevoEstado true = Activo, false = Inactivo.
     * @return "OK" si se actualizó correctamente, o mensaje de error.
     */
    public String cambiarEstadoProducto(int idProducto, boolean nuevoEstado) {
        if (idProducto <= 0) {
            return "Debe seleccionar un producto válido.";
        }
        boolean exito = productoDAO.cambiarEstado(idProducto, nuevoEstado);
        return exito ? "OK" : "No se pudo cambiar el estado del producto.";
    }

    /**
     * Método privado auxiliar para centralizar las reglas de validación de productos.
     */
    private String validarDatosProducto(String codigo, String nombre, String categoria,
                                        String unidadMedida, int stockActual, int stockMinimo,
                                        double precioReferencial) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return "El código del producto es obligatorio.";
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre del producto es obligatorio.";
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            return "Debe seleccionar una categoría válida.";
        }
        if (unidadMedida == null || unidadMedida.trim().isEmpty()) {
            return "Debe seleccionar una unidad de almacén.";
        }
        if (stockActual < 0) {
            return "El stock actual no puede ser un número negativo.";
        }
        if (stockMinimo < 0) {
            return "El stock mínimo no puede ser un número negativo.";
        }
        if (precioReferencial <= 0) {
            return "El precio referencial debe ser mayor a S/ 0.00.";
        }
        return "OK";
    }
}
