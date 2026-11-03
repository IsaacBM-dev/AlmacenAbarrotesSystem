package controller;

import model.Usuario;

/**
 * Gestor estático de la sesión activa del usuario en el sistema.
 */
public class Sesion {

    private static Usuario usuarioActivo;

    private Sesion() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Inicia la sesión almacenando el usuario autenticado.
     *
     * @param usuario Objeto Usuario autenticado.
     */
    public static void iniciarSesion(Usuario usuario) {
        usuarioActivo = usuario;
    }

    /**
     * Obtiene el usuario actualmente autenticado en el sistema.
     *
     * @return Objeto Usuario activo o null si no hay sesión.
     */
    public static Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    /**
     * Verifica si existe una sesión activa.
     *
     * @return true si hay un usuario logueado, false en caso contrario.
     */
    public static boolean haySesionActiva() {
        return usuarioActivo != null;
    }

    /**
     * Verifica si el usuario en sesión posee el rol ADMIN.
     *
     * @return true si es ADMIN, false si es ALMACENERO o no hay sesión.
     */
    public static boolean esAdmin() {
        return usuarioActivo != null && "ADMIN".equalsIgnoreCase(usuarioActivo.getRol());
    }

    /**
     * Limpia los datos del usuario al cerrar sesión.
     */
    public static void cerrarSesion() {
        usuarioActivo = null;
    }
}
