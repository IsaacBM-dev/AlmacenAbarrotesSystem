package dao;

import model.Movimiento;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para el registro transaccional de Movimientos (Kardex) y consulta de historial.
 */
public class MovimientoDAO {

    /**
     * Registra un movimiento (ENTRADA o SALIDA) y actualiza automáticamente el stock_actual
     * del producto mediante el procedimiento almacenado transaccional sp_Movimiento_Registrar.
     *
     * @param m Objeto Movimiento con los datos de la operación.
     * @return true si el movimiento y la actualización de stock se confirmaron (COMMIT).
     * @throws SQLException Si el stock es insuficiente o falla alguna validación en SQL Server.
     */
    public boolean registrar(Movimiento m) throws SQLException {
        String sql = "{CALL dbo.sp_Movimiento_Registrar(?, ?, ?, ?, ?, ?)}";

        try (Connection cn = conexion_class.obtenerConexion();
             CallableStatement cs = cn != null ? cn.prepareCall(sql) : null) {

            if (cn == null || cs == null) {
                throw new SQLException("No se pudo establecer conexión con la base de datos.");
            }

            cs.setInt(1, m.getId_producto());
            cs.setInt(2, m.getId_usuario());
            cs.setString(3, m.getTipo_movimiento());
            cs.setString(4, m.getMotivo());
            cs.setInt(5, m.getCantidad());

            if (m.getObservacion() != null && !m.getObservacion().trim().isEmpty()) {
                cs.setString(6, m.getObservacion().trim());
            } else {
                cs.setNull(6, Types.VARCHAR);
            }

            cs.executeUpdate();
            return true;
        }
    }

    /**
     * Lista el historial completo de movimientos del Kardex ordenado del más reciente al más antiguo.
     *
     * @return Lista de objetos Movimiento.
     */
    public List<Movimiento> listar() {
        List<Movimiento> lista = new ArrayList<>();
        String sql = "{CALL dbo.sp_Movimiento_Listar()}";

        try (Connection cn = conexion_class.obtenerConexion();
             CallableStatement cs = cn != null ? cn.prepareCall(sql) : null;
             ResultSet rs = cs != null ? cs.executeQuery() : null) {

            if (rs == null) {
                return lista;
            }

            while (rs.next()) {
                Movimiento m = new Movimiento();
                m.setId_movimiento(rs.getInt("id_movimiento"));
                m.setId_producto(rs.getInt("id_producto"));
                m.setNombre_producto(rs.getString("nombre_producto"));
                m.setId_usuario(rs.getInt("id_usuario"));
                m.setUsername(rs.getString("username"));
                m.setTipo_movimiento(rs.getString("tipo_movimiento"));
                m.setMotivo(rs.getString("motivo"));
                m.setCantidad(rs.getInt("cantidad"));
                m.setFecha_registro(rs.getTimestamp("fecha_registro"));
                m.setObservacion(rs.getString("observacion"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error en MovimientoDAO.listar: " + e.getMessage());
        }
        return lista;
    }
}
