package com.senati.almacenabarrotessystem;

import javax.swing.SwingUtilities;
import util.GestorTema;
import view.JFrame_Login;
//import com.formdev.flatlaf.FlatIntelliJLaf;
//import view.JFrame_Login;
//import javax.swing.SwingUtilities;
//import javax.swing.UIManager;

/**
 * Clase principal de arranque para AlmacenAbarrotesSystem
 */
public class AlmacenSystem {

    public static void main(String[] args) {
        
        // 1. Inicializa en modo Light Owl por defecto
        GestorTema.inicializarTema();

        // 2. Abre el Login
        SwingUtilities.invokeLater(() -> {
            new JFrame_Login().setVisible(true);
        });
    }
        /**
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

        SwingUtilities.invokeLater(() -> {
            JFrame_Login loginFrame = new JFrame_Login();
            loginFrame.setVisible(true);
        });
        */
    
}
