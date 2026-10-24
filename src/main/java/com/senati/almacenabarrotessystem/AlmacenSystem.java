package com.senati.almacenabarrotessystem;

import com.formdev.flatlaf.FlatIntelliJLaf;
import view.JFrame_Login;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal de arranque para AlmacenAbarrotesSystem.
 */
public class AlmacenSystem {

    public static void main(String[] args) {
        // 1. Inicializar Look and Feel moderno FlatLaf antes de crear cualquier componente Swing
        try {
            FlatIntelliJLaf.setup();
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("ScrollBar.showButtons", true);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", true);
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el tema FlatLaf: " + e.getMessage());
        }

        // 2. Desplegar la ventana de Login en el hilo de eventos de Swing (EDT)
        SwingUtilities.invokeLater(() -> {
            JFrame_Login loginFrame = new JFrame_Login();
            loginFrame.setVisible(true);
        });
    }
}
