package com.senati.almacenabarrotessystem;

import util.protegerClave;
import dao.conexion_class;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Test_Hash {

    public static void main(String[] args) {
        String clavePlana = "admin123";
        String hashReal = protegerClave.encriptar(clavePlana);

        System.out.println("Hash BCrypt generado para 'admin123': " + hashReal);
        System.out.println("Verificación local: " + protegerClave.verificar(clavePlana, hashReal));

        String sql = "UPDATE dbo.Usuario SET clave_hash = ? WHERE username IN ('admin', 'almacenero1')";

        try (Connection cn = conexion_class.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, hashReal);
            int filas = ps.executeUpdate();
            System.out.println("[ÉXITO] Se actualizaron " + filas + " usuarios en SQL Server con la clave: " + clavePlana);

        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo actualizar la clave: " + e.getMessage());
        }
    }
}
