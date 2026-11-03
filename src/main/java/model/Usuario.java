package model;

/**
 * Entidad que representa a un usuario del sistema (ADMIN o ALMACENERO).
 */
public class Usuario {

    private int id_usuario;
    private String nombre;
    private String username;
    private String clave_hash;
    private String rol;
    private boolean estado;

    public Usuario() {
    }

    public Usuario(int id_usuario, String nombre, String username, String clave_hash, String rol, boolean estado) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.username = username;
        this.clave_hash = clave_hash;
        this.rol = rol;
        this.estado = estado;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClave_hash() {
        return clave_hash;
    }

    public void setClave_hash(String clave_hash) {
        this.clave_hash = clave_hash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre + " (" + username + " - " + rol + ")";
    }
}
