package com.senati.almacenabarrotessystem;

import controller.ProductoController;
import model.AlertaResumen;
import model.Producto;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Prueba rápida por consola para el CRUD de Productos y consulta de Alertas.
 */
public class Test_Producto {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("        PRUEBA DE CATÁLOGO DE PRODUCTOS Y ALERTAS         ");
        System.out.println("==========================================================");

        ProductoController controller = new ProductoController();

        // 1. Intentar registrar un producto de prueba (si ya existe el código, mostrará el mensaje controlado)
        Date vencimientoPrueba = Date.valueOf(LocalDate.now().plusDays(20)); // Vence en 20 días (generará alerta)
        String resultadoRegistro = controller.registrarProducto(
                "ABR-TEST01",
                "Fideos Spaghetti Don Vittorio 500g",
                "Granos",
                "PAQUETE X 12",
                6,  // Stock actual menor al mínimo (8)
                8,  // Stock mínimo
                vencimientoPrueba,
                46.50,
                true
        );
        System.out.println("1. Resultado Registro Producto Prueba: " + resultadoRegistro);

        // 2. Listar catálogo completo
        System.out.println("\n2. Listado general de productos en almacén:");
        List<Producto> productos = controller.listarProductos();
        for (Producto p : productos) {
            System.out.printf("   -> ID: %d | Cód: %-10s | %-35s | Unidad: %-12s | Stock: %3d/%3d | S/ %.2f | Activo: %s%n",
                    p.getId_producto(), p.getCodigo(), p.getNombre(), p.getUnidad_medida(),
                    p.getStock_actual(), p.getStock_minimo(), p.getPrecio_referencial(),
                    p.isEstado() ? "SÍ" : "NO");
        }

        // 3. Listar alertas para el Dashboard
        System.out.println("\n3. Listado de Alertas Activas (Stock Bajo / Vencimiento <= 30 días):");
        List<AlertaResumen> alertas = controller.listarAlertasInventario();
        for (AlertaResumen a : alertas) {
            System.out.printf("   [!] %-38s | %s (%s) | Stock: %d (Mín: %d) | Vence en: %s días%n",
                    a.getTipo_alerta(), a.getNombre(), a.getCodigo(),
                    a.getStock_actual(), a.getStock_minimo(),
                    a.getDias_para_vencer() != null ? a.getDias_para_vencer().toString() : "N/A");
        }
    }
}
