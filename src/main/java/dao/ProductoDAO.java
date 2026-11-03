package dao;

import model.AlertaResumen;
import model.Producto;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión del catálogo de Productos y consulta de Alertas de Inventario.
 */
public class ProductoDAO {

    /**
     * Lista todos los productos del catálogo.
     *
     * @return Lista de objetos Producto.
     */
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "{CALL dbo.sp_Producto_Listar()}";

        try (Connection cn = conexion_class.obtenerConexion();
             CallableStatement cs = cn != null ? cn.prepareCall(sql) : null;
             ResultSet rs = cs != null ? cs.executeQuery() : null) {

            if (rs == null) {
                return lista;
            }

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error en ProductoDAO.listar: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Lista únicamente los productos activos (estado = 1) para cargarlos en los combos de Movimientos.
     *
     * @return Lista de productos activos.
     */
    public List<Producto> listarActivos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id_producto, codigo, nombre, categoria, unidad_medida, "
                   + "stock_actual, stock_minimo, fecha_vencimiento, precio_referencial, estado "
                   + "FROM dbo.Producto WHERE estado = 1 ORDER BY nombre ASC";

        try (Connection cn = conexion_class.obtenerConexion();
             PreparedStatement ps = cn != null ? cn.prepareStatement(sql) : null;
             ResultSet rs = ps != null ? ps.executeQuery() : null) {

            if (rs == null) {
                return lista;
            }

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error en ProductoDAO.listarActivos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca productos por código, nombre o categoría.
     *
     * @param filtro Texto de búsqueda.
     * @return Lista de productos coincidentes.
     */
    public List<Producto> buscar(String filtro) {
        List<Producto> lista = new ArrayList<>();
        String sql = "{CALL dbo.sp_Producto_Buscar(?)}";

        try (Connection cn = conexion_class.obtenerConexion();
             CallableStatement cs = cn != null ? cn.prepareCall(sql) : null) {

            if (cn == null || cs == null) {
                return lista;
            }

            cs.setString(1, filtro != null ? filtro.trim() : "");
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en ProductoDAO.buscar: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene un producto específico por su ID primario (útil para validar stock actual antes de una salida).
     *
     * @param idProducto ID del producto.
     * @return Objeto Producto o null si no existe.
     */
    public Producto obtenerPorId(int idProducto) {
        String sql = "SELECT id_producto, codigo, nombre, categoria, unidad_medida, "
                   + "stock_actual, stock_minimo, fecha_vencimiento, precio_referencial, estado "
                   + "FROM dbo.Producto WHERE id_producto = ?";

        try (Connection cn = conexion_class.obtenerConexion();
             PreparedStatement ps = cn != null ? cn.prepareStatement(sql) : null) {

            if (cn == null || ps == null) {
                return null;
            }

            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en ProductoDAO.obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    /**
     * Registra un nuevo producto en el catálogo.
     *
     * @param p Objeto Producto a insertar.
     * @return true si se registró con éxito.
     * @throws SQLException Si el código está duplicado o falla alguna restricción CHECK.
     */
    public boolean registrar(Producto p) throws SQLException {
        String sql = "{CALL dbo.sp_Producto_Registrar(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection cn = conexion_class.obtenerConexion();
             CallableStatement cs = cn != null ? cn.prepareCall(sql) : null) {

            if (cn == null || cs == null) {
                throw new SQLException("No hay conexión disponible con SQL Server.");
            }

            cs.setString(1, p.getCodigo());
            cs.setString(2, p.getNombre());
            cs.setString(3, p.getCategoria());
            cs.setString(4, p.getUnidad_medida());
            cs.setInt(5, p.getStock_actual());
            cs.setInt(6, p.getStock_minimo());
            if (p.getFecha_vencimiento() != null) {
                cs.setDate(7, p.getFecha_vencimiento());
            } else {
                cs.setNull(7, Types.DATE);
            }
            cs.setDouble(8, p.getPrecio_referencial());
            cs.setBoolean(9, p.isEstado());

            cs.executeUpdate();
            return true;
        }
    }

    /**
     * Actualiza un producto existente.
     *
     * @param p Objeto Producto con los datos modificados.
     * @return true si se actualizó correctamente.
     * @throws SQLException Si ocurre un error en SQL Server.
     */
    public boolean actualizar(Producto p) throws SQLException {
        String sql = "{CALL dbo.sp_Producto_Actualizar(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection cn = conexion_class.obtenerConexion();
             CallableStatement cs = cn != null ? cn.prepareCall(sql) : null) {

            if (cn == null || cs == null) {
                throw new SQLException("No hay conexión disponible con SQL Server.");
            }

            cs.setInt(1, p.getId_producto());
            cs.setString(2, p.getCodigo());
            cs.setString(3, p.getNombre());
            cs.setString(4, p.getCategoria());
            cs.setString(5, p.getUnidad_medida());
            cs.setInt(6, p.getStock_actual());
            cs.setInt(7, p.getStock_minimo());
            if (p.getFecha_vencimiento() != null) {
                cs.setDate(8, p.getFecha_vencimiento());
            } else {
                cs.setNull(8, Types.DATE);
            }
            cs.setDouble(9, p.getPrecio_referencial());
            cs.setBoolean(10, p.isEstado());

            cs.executeUpdate();
            return true;
        }
    }

    /**
     * Realiza el borrado lógico o reactivación de un producto.
     *
     * @param idProducto ID del producto.
     * @param nuevoEstado true = Activo, false = Inactivo.
     * @return true si la operación fue exitosa.
     */
    public boolean cambiarEstado(int idProducto, boolean nuevoEstado) {
        String sql = "{CALL dbo.sp_Producto_CambiarEstado(?, ?)}";
        try (Connection cn = conexion_class.obtenerConexion();
             CallableStatement cs = cn != null ? cn.prepareCall(sql) : null) {

            if (cn == null || cs == null) {
                return false;
            }

            cs.setInt(1, idProducto);
            cs.setBoolean(2, nuevoEstado);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en ProductoDAO.cambiarEstado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene la lista de productos con stock bajo (stock_actual <= stock_minimo)
     * o próximos a vencer (<= 30 días) para el Dashboard principal.
     *
     * @return Lista de objetos AlertaResumen.
     */
    public List<AlertaResumen> listarAlertas() {
        List<AlertaResumen> alertas = new ArrayList<>();
        String sql = "{CALL dbo.sp_Producto_ListarAlertas()}";

        try (Connection cn = conexion_class.obtenerConexion();
             CallableStatement cs = cn != null ? cn.prepareCall(sql) : null;
             ResultSet rs = cs != null ? cs.executeQuery() : null) {

            if (rs == null) {
                return alertas;
            }

            while (rs.next()) {
                AlertaResumen a = new AlertaResumen();
                a.setId_producto(rs.getInt("id_producto"));
                a.setCodigo(rs.getString("codigo"));
                a.setNombre(rs.getString("nombre"));
                a.setCategoria(rs.getString("categoria"));
                a.setUnidad_medida(rs.getString("unidad_medida"));
                a.setStock_actual(rs.getInt("stock_actual"));
                a.setStock_minimo(rs.getInt("stock_minimo"));
                a.setFecha_vencimiento(rs.getDate("fecha_vencimiento"));

                int dias = rs.getInt("dias_para_vencer");
                if (rs.wasNull()) {
                    a.setDias_para_vencer(null);
                } else {
                    a.setDias_para_vencer(dias);
                }

                a.setTipo_alerta(rs.getString("tipo_alerta"));
                alertas.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Error en ProductoDAO.listarAlertas: " + e.getMessage());
        }
        return alertas;
    }

    /**
     * Método auxiliar privado para mapear una fila del ResultSet a un objeto Producto.
     */
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId_producto(rs.getInt("id_producto"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setCategoria(rs.getString("categoria"));
        p.setUnidad_medida(rs.getString("unidad_medida"));
        p.setStock_actual(rs.getInt("stock_actual"));
        p.setStock_minimo(rs.getInt("stock_minimo"));
        p.setFecha_vencimiento(rs.getDate("fecha_vencimiento"));
        p.setPrecio_referencial(rs.getDouble("precio_referencial"));
        p.setEstado(rs.getBoolean("estado"));
        return p;
    }
}
