package com.senati.almacenabarrotessystem;

import dao.conexion_class;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Prueba unitaria por consola para validar la conexión JDBC con SQL Server.
 */
public class Test_Conexion {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("   PRUEBA DE CONEXIÓN A SQL SERVER - ALMACÉN ABARROTES    ");
        System.out.println("==========================================================");

        try (Connection cn = conexion_class.obtenerConexion()) {
            if (cn != null && !cn.isClosed()) {
                DatabaseMetaData meta = cn.getMetaData();
                System.out.println("[ÉXITO] Conexión establecida correctamente.");
                System.out.println(" -> Motor BD       : " + meta.getDatabaseProductName());
                System.out.println(" -> Versión Motor  : " + meta.getDatabaseProductVersion());
                System.out.println(" -> Driver JDBC    : " + meta.getDriverName() + " (" + meta.getDriverVersion() + ")");
                System.out.println(" -> Catálogo Activo: " + cn.getCatalog());
                System.out.println(" -> Usuario Conect.: " + meta.getUserName());
            } else {
                System.err.println("[FALLO] No se pudo establecer la conexión. Verifique usuario/clave en conexion_class.java");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR SQL] Detalle: " + e.getMessage());
        }
    }
}
