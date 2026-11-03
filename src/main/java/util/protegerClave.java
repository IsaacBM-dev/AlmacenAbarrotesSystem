package util;

import org.mindrot.jbcrypt.BCrypt;

public class protegerClave {

    // Factor de costo estándar para el algoritmo Blowfish de BCrypt
    private static final int WORKLOAD = 10;

    /**
     * Genera un hash BCrypt seguro a partir de una contraseña en texto plano.
     *
     * @param claveTextoPlano Contraseña ingresada por el usuario.
     * @return Cadena con el hash BCrypt generado.
     */
    public static String encriptar(String claveTextoPlano) {
        if (claveTextoPlano == null || claveTextoPlano.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña a encriptar no puede estar vacía.");
        }
        String salt = BCrypt.gensalt(WORKLOAD);
        return BCrypt.hashpw(claveTextoPlano, salt);
    }

    /**
     * Verifica si una contraseña en texto plano coincide con un hash BCrypt almacenado en BD.
     *
     * @param claveTextoPlano Contraseña ingresada en el Login.
     * @param hashAlmacenado  Hash recuperado desde SQL Server (columna clave_hash).
     * @return true si coinciden, false en caso contrario.
     */
    public static boolean verificar(String claveTextoPlano, String hashAlmacenado) {
        if (claveTextoPlano == null || hashAlmacenado == null || !hashAlmacenado.startsWith("$2a$")) {
            return false;
        }
        try {
            return BCrypt.checkpw(claveTextoPlano, hashAlmacenado);
        } catch (Exception e) {
            System.err.println("Error al verificar hash BCrypt: " + e.getMessage());
            return false;
        }
    }
}
