package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de proveer la conexión JDBC hacia Microsoft SQL Server.
 */
public class conexion_class {

    private static final String HOST = "localhost";
    private static final String PORT = "1433";
    private static final String DB_NAME = "BD_AlmacenAbarrotes";
    private static final String USER = "sa";
    private static final String PASSWORD = "*****************"; // <-- Cambia aquí por tu clave de SQL Server

    private static final String URL = "jdbc:sqlserver://" + HOST + ":" + PORT + ";"
            + "databaseName=" + DB_NAME + ";"
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "loginTimeout=15;";

    /**
     * Obtiene una nueva conexión activa a la base de datos BD_AlmacenAbarrotes.
     *
     * @return Objeto Connection activo o null si ocurre un error.
     */
    public static Connection obtenerConexion() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Error crítico: No se encontró el driver JDBC de SQL Server. " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.err.println("Error de conexión a SQL Server (" + DB_NAME + "): " + e.getMessage());
            return null;
        }
    }
}
