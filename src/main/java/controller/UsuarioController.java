package controller;

import dao.UsuarioDAO;
import model.Usuario;
import util.protegerClave;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador de reglas de negocio para la gestión de Usuarios y Autenticación.
 */
public class UsuarioController {

    private final UsuarioDAO usuarioDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Valida las credenciales de acceso usando BCrypt e inicia la sesión si son correctas.
     *
     * @param username   Nombre de usuario.
     * @param clavePlana Contraseña en texto plano ingresada en el formulario.
     * @return "OK" si el acceso es válido, o un mensaje descriptivo del error.
     */
    public String login(String username, String clavePlana) {
        if (username == null || username.trim().isEmpty()) {
            return "Debe ingresar el nombre de usuario.";
        }
        if (clavePlana == null || clavePlana.trim().isEmpty()) {
            return "Debe ingresar la contraseña.";
        }

        Usuario u = usuarioDAO.obtenerPorUsername(username.trim());
        if (u == null) {
            return "Usuario o contraseña incorrectos.";
        }

        if (!u.isEstado()) {
            return "La cuenta del usuario '" + u.getUsername() + "' se encuentra INACTIVA. Contacte al Administrador.";
        }

        boolean claveCorrecta = protegerClave.verificar(clavePlana, u.getClave_hash());
        if (!claveCorrecta) {
            return "Usuario o contraseña incorrectos.";
        }

        Sesion.iniciarSesion(u);
        return "OK";
    }

    /**
     * Lista todos los usuarios registrados.
     *
     * @return Lista de objetos Usuario.
     */
    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listar();
    }

    /**
     * Valida y registra un nuevo usuario encriptando su contraseña con BCrypt.
     *
     * @param nombre     Nombre completo del usuario.
     * @param username   Nombre de cuenta único.
     * @param clavePlana Contraseña en texto plano.
     * @param rol        Rol asignado ("ADMIN" o "ALMACENERO").
     * @param estado     Estado inicial (true = Activo, false = Inactivo).
     * @return "OK" si se registró con éxito, o el mensaje de error correspondiente.
     */
    public String registrarUsuario(String nombre, String username, String clavePlana, String rol, boolean estado) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre completo del usuario es obligatorio.";
        }
        if (username == null || username.trim().isEmpty()) {
            return "El username es obligatorio.";
        }
        if (username.trim().contains(" ")) {
            return "El username no debe contener espacios en blanco.";
        }
        if (clavePlana == null || clavePlana.trim().length() < 4) {
            return "La contraseña es obligatoria y debe tener al menos 4 caracteres.";
        }
        if (rol == null || (!rol.equals("ADMIN") && !rol.equals("ALMACENERO"))) {
            return "El rol seleccionado no es válido (debe ser ADMIN o ALMACENERO).";
        }

        try {
            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre.trim());
            nuevo.setUsername(username.trim());
            nuevo.setClave_hash(protegerClave.encriptar(clavePlana.trim()));
            nuevo.setRol(rol);
            nuevo.setEstado(estado);

            usuarioDAO.registrar(nuevo);
            return "OK";
        } catch (SQLException e) {
            return "Error en Base de Datos: " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado al registrar usuario: " + e.getMessage();
        }
    }

    /**
     * Valida y actualiza un usuario existente. Si nuevaClavePlana está vacía, mantiene su clave actual.
     *
     * @param idUsuario       ID del usuario a modificar.
     * @param nombre          Nombre completo.
     * @param username        Nombre de cuenta.
     * @param nuevaClavePlana Nueva contraseña opcional (puede ir vacía si no se desea cambiar).
     * @param rol             Rol ("ADMIN" o "ALMACENERO").
     * @param estado          Estado (true = Activo, false = Inactivo).
     * @return "OK" si se actualizó con éxito, o el mensaje de error.
     */
    public String actualizarUsuario(int idUsuario, String nombre, String username, String nuevaClavePlana, String rol, boolean estado) {
        if (idUsuario <= 0) {
            return "Debe seleccionar un usuario válido de la tabla.";
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            return "El nombre completo del usuario es obligatorio.";
        }
        if (username == null || username.trim().isEmpty()) {
            return "El username es obligatorio.";
        }
        if (username.trim().contains(" ")) {
            return "El username no debe contener espacios en blanco.";
        }
        if (rol == null || (!rol.equals("ADMIN") && !rol.equals("ALMACENERO"))) {
            return "El rol seleccionado no es válido.";
        }

        String hashParaActualizar = null;
        if (nuevaClavePlana != null && !nuevaClavePlana.trim().isEmpty()) {
            if (nuevaClavePlana.trim().length() < 4) {
                return "Si desea cambiar la contraseña, debe tener al menos 4 caracteres.";
            }
            hashParaActualizar = protegerClave.encriptar(nuevaClavePlana.trim());
        }

        try {
            Usuario u = new Usuario();
            u.setId_usuario(idUsuario);
            u.setNombre(nombre.trim());
            u.setUsername(username.trim());
            u.setClave_hash(hashParaActualizar);
            u.setRol(rol);
            u.setEstado(estado);

            usuarioDAO.actualizar(u);
            return "OK";
        } catch (SQLException e) {
            return "Error en Base de Datos: " + e.getMessage();
        } catch (Exception e) {
            return "Error inesperado al actualizar usuario: " + e.getMessage();
        }
    }

    /**
     * Cambia el estado (Activo/Inactivo) de un usuario aplicando borrado lógico.
     *
     * @param idUsuario   ID del usuario.
     * @param nuevoEstado true para activar, false para inactivar.
     * @return "OK" si se ejecutó correctamente, o mensaje de error.
     */
    public String cambiarEstadoUsuario(int idUsuario, boolean nuevoEstado) {
        if (idUsuario <= 0) {
            return "ID de usuario inválido.";
        }
        if (!nuevoEstado && Sesion.getUsuarioActivo() != null
                && Sesion.getUsuarioActivo().getId_usuario() == idUsuario) {
            return "Por seguridad, no puede desactivar su propio usuario en sesión.";
        }

        boolean exito = usuarioDAO.cambiarEstado(idUsuario, nuevoEstado);
        return exito ? "OK" : "No se pudo cambiar el estado del usuario.";
    }
}
