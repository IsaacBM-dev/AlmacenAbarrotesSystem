package dao;

import model.Usuario;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de Usuarios en SQL Server.
 */
public class UsuarioDAO {

    /**
     * Obtiene un usuario según su username para validar el login y su hash BCrypt.
     *
     * @param username Nombre de usuario ingresado.
     * @return Objeto Usuario si existe, o null si no se encuentra.
     */
    public Usuario obtenerPorUsername(String username) {
        String sql = "{CALL dbo.sp_Usuario_ObtenerPorUsername(?)}";
        try (Connection cn = conexion_class.obtenerConexion();
             CallableStatement cs = cn != null ? cn.prepareCall(sql) : null) {

            if (cn == null || cs == null) {
                return null;
            }

            cs.setString(1, username);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId_usuario(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setUsername(rs.getString("username"));
                    u.setClave_hash(rs.getString("clave_hash"));
                    u.setRol(rs.getString("rol"));
                    u.setEstado(rs.getBoolean("estado"));
                    return u;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.obtenerPorUsername: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lista todos los usuarios registrados en el sistema.
     *
     * @return Lista de objetos Usuario.
     */
    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "{CALL dbo.sp_Usuario_Listar()}";

        try (Connection cn = conexion_class.obtenerConexion();
             CallableStatement cs = cn != null ? cn.prepareCall(sql) : null;
             ResultSet rs = cs != null ? cs.executeQuery() : null) {

            if (rs == null) {
                return lista;
            }

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId_usuario(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setUsername(rs.getString("username"));
                u.setClave_hash(rs.getString("clave_hash"));
                u.setRol(rs.getString("rol"));
                u.setEstado(rs.getBoolean("estado"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.listar: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param u Objeto Usuario con el hash BCrypt ya generado.
     * @return true si se registró correctamente, false si ocurrió un error.
     * @throws SQLException Si el procedimiento almacenado lanza una regla de negocio (ej. username duplicado).
     */
    public boolean registrar(Usuario u) throws SQLException {
        String sql = "{CALL dbo.sp_Usuario_Registrar(?, ?, ?, ?, ?)}";
        try (Connection cn = conexion_class.obtenerConexion();
             CallableStatement cs = cn != null ? cn.prepareCall(sql) : null) {

            if (cn == null || cs == null) {
                throw new SQLException("No hay conexión disponible con SQL Server.");
            }

            cs.setString(1, u.getNombre());
            cs.setString(2, u.getUsername());
            cs.setString(3, u.getClave_hash());
            cs.setString(4, u.getRol());
            cs.setBoolean(5, u.isEstado());

            cs.executeUpdate();
            return true;
        }
    }

    /**
     * Actualiza los datos de un usuario existente.
     * Si clave_hash es null o vacío, el SP conserva la contraseña anterior.
     *
     * @param u Objeto Usuario con los datos actualizados.
     * @return true si la actualización fue exitosa.
     * @throws SQLException Si ocurre un error de validación en base de datos.
     */
    public boolean actualizar(Usuario u) throws SQLException {
        String sql = "{CALL dbo.sp_Usuario_Actualizar(?, ?, ?, ?, ?, ?)}";
        try (Connection cn = conexion_class.obtenerConexion();
             CallableStatement cs = cn != null ? cn.prepareCall(sql) : null) {

            if (cn == null || cs == null) {
                throw new SQLException("No hay conexión disponible con SQL Server.");
            }

            cs.setInt(1, u.getId_usuario());
            cs.setString(2, u.getNombre());
            cs.setString(3, u.getUsername());
            if (u.getClave_hash() == null || u.getClave_hash().trim().isEmpty()) {
                cs.setNull(4, java.sql.Types.VARCHAR);
            } else {
                cs.setString(4, u.getClave_hash());
            }
            cs.setString(5, u.getRol());
            cs.setBoolean(6, u.isEstado());

            cs.executeUpdate();
            return true;
        }
    }

    /**
     * Cambia el estado (Activo/Inactivo) de un usuario (Borrado lógico).
     *
     * @param idUsuario ID del usuario.
     * @param nuevoEstado true = Activo (1), false = Inactivo (0).
     * @return true si se actualizó correctamente.
     */
    public boolean cambiarEstado(int idUsuario, boolean nuevoEstado) {
        String sql = "{CALL dbo.sp_Usuario_CambiarEstado(?, ?)}";
        try (Connection cn = conexion_class.obtenerConexion();
             CallableStatement cs = cn != null ? cn.prepareCall(sql) : null) {

            if (cn == null || cs == null) {
                return false;
            }

            cs.setInt(1, idUsuario);
            cs.setBoolean(2, nuevoEstado);
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.cambiarEstado: " + e.getMessage());
            return false;
        }
    }
}
