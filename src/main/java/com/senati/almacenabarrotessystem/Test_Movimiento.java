package com.senati.almacenabarrotessystem;

import controller.MovimientoController;
import controller.ProductoController;
import model.Movimiento;
import model.Producto;
import java.util.List;

/**
 * Prueba por consola de Movimientos (Kardex) y validación de Stock no negativo.
 */
public class Test_Movimiento {

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("     PRUEBA TRANSACCIONAL DE MOVIMIENTOS Y KARDEX         ");
        System.out.println("==========================================================");

        MovimientoController movController = new MovimientoController();
        ProductoController prodController = new ProductoController();

        int idProductoPrueba = 1; // ABR-001 (Arroz Extra Costeño)
        int idUsuarioAdmin = 1;   // admin

        Producto antes = prodController.obtenerProductoPorId(idProductoPrueba);
        if (antes == null) {
            System.err.println("No se encontró el producto con ID 1. Ejecute primero BD_AlmacenAbarrotes.sql");
            return;
        }

        System.out.println("Producto seleccionado : " + antes.getNombre() + " [" + antes.getUnidad_medida() + "]");
        System.out.println("Stock Inicial         : " + antes.getStock_actual());

        // 1. Registrar una ENTRADA de 10 unidades
        String resEntrada = movController.registrarMovimiento(
                idProductoPrueba, idUsuarioAdmin, "ENTRADA", "COMPRA PROVEEDOR", 10, "Ingreso por Guía #001-4589"
        );
        System.out.println("\n1. Registro de ENTRADA (+10): " + resEntrada);
        Producto despuesEntrada = prodController.obtenerProductoPorId(idProductoPrueba);
        System.out.println("   Nuevo Stock tras ENTRADA : " + despuesEntrada.getStock_actual());

        // 2. Intentar registrar una SALIDA excesiva (Ej. 99999 unidades) -> Debe ser bloqueada
        String resSalidaInvalida = movController.registrarMovimiento(
                idProductoPrueba, idUsuarioAdmin, "SALIDA", "DESPACHO TIENDA", 99999, "Intento de sobregiro de stock"
        );
        System.out.println("\n2. Prueba de bloqueo por Stock Negativo (SALIDA de 99999):");
        System.out.println("   Respuesta del Controlador: " + resSalidaInvalida);

        // 3. Registrar una SALIDA válida de 3 unidades
        String resSalidaValida = movController.registrarMovimiento(
                idProductoPrueba, idUsuarioAdmin, "SALIDA", "DESPACHO TIENDA", 3, "Despacho normal validado"
        );
        System.out.println("\n3. Registro de SALIDA válida (-3): " + resSalidaValida);
        Producto despuesSalida = prodController.obtenerProductoPorId(idProductoPrueba);
        System.out.println("   Stock Final tras SALIDA       : " + despuesSalida.getStock_actual());

        // 4. Mostrar los últimos movimientos del Kardex
        System.out.println("\n4. Historial reciente del Kardex:");
        List<Movimiento> historial = movController.listarMovimientos();
        for (Movimiento m : historial) {
            System.out.printf("   -> #%d | %s | %-7s | Cant: %3d | Motivo: %-20s | Prod: %s | Usuario: %s%n",
                    m.getId_movimiento(),
                    m.getFecha_registro(),
                    m.getTipo_movimiento(),
                    m.getCantidad(),
                    m.getMotivo(),
                    m.getNombre_producto(),
                    m.getUsername());
        }
    }
}
