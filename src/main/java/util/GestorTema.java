package util;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTLightOwlIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTNightOwlIJTheme;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import java.awt.Image;
import javax.swing.ImageIcon;

public class GestorTema {

    private static boolean modoOscuro = false; // false = Light Owl | true = Night Owl

    // Colores para los botones del menú lateral en JFrame_Principal
    public static final Color COLOR_MENU_INACTIVO = new Color(0, 153, 102); // Verde original
    public static final Color COLOR_MENU_ACTIVO   = new Color(255, 153, 0); // Naranja resaltado

    private static final Color BG_LIGHT = new Color(255, 255, 255);
    private static final Color FG_LIGHT = new Color(17, 17, 17);

    private static final Color BG_NIGHT = new Color(1, 22, 39);             // Fondo Night Owl (#011627)
    private static final Color BG_NIGHT_INPUT = new Color(11, 37, 58);      // Cajas de texto Night Owl
    private static final Color FG_NIGHT = new Color(214, 222, 235);         // Texto claro Night Owl

    public static void inicializarTema() {
        aplicarTema(false);
    }

    public static void alternarTema() {
        aplicarTema(!modoOscuro);
    }

    public static boolean esModoOscuro() {
        return modoOscuro;
    }

    public static void aplicarTema(boolean activarOscuro) {
        try {
            modoOscuro = activarOscuro;
            if (modoOscuro) {
                FlatMTNightOwlIJTheme.setup();
            } else {
                FlatMTLightOwlIJTheme.setup();
            }

            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("PasswordField.showRevealButton", true);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", true);

            FlatLaf.updateUI();

            for (Window w : Window.getWindows()) {
                if (w.isDisplayable()) {
                    adaptarContenedor(w);
                    w.repaint();
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cambiar el tema FlatLaf: " + e.getMessage());
        }
    }

    public static void adaptarContenedor(Container contenedor) {
        Color bgPanel = modoOscuro ? BG_NIGHT : BG_LIGHT;
        Color bgInput = modoOscuro ? BG_NIGHT_INPUT : BG_LIGHT;
        Color fgTexto = modoOscuro ? FG_NIGHT : FG_LIGHT;

        if (contenedor instanceof JPanel panel) {
            Color bgActual = panel.getBackground();
            if (!esColorInstitucionalFijo(bgActual)) {
                panel.setBackground(bgPanel);
            }
            if (panel.getBorder() instanceof TitledBorder tb) {
                tb.setTitleColor(fgTexto);
            }
        }

        for (Component comp : contenedor.getComponents()) {
            if (comp instanceof JPanel subPanel) {
                adaptarContenedor(subPanel);
            } else if (comp instanceof JScrollPane scroll) {
                scroll.getViewport().setBackground(bgPanel);
                adaptarContenedor(scroll);
            } else if (comp instanceof JTable tabla) {
                tabla.setBackground(bgPanel);
                tabla.setForeground(fgTexto);
                tabla.getTableHeader().setBackground(modoOscuro ? new Color(18, 45, 66) : new Color(230, 236, 245));
                tabla.getTableHeader().setForeground(fgTexto);
            } else if (comp instanceof JLabel label) {
                Color fg = label.getForeground();
                if (!esColorTextoEspecial(fg, contenedor)) {
                    label.setForeground(fgTexto);
                }
            } else if (comp instanceof JTextField || comp instanceof JPasswordField) {
                comp.setBackground(bgInput);
                comp.setForeground(fgTexto);
            } else if (comp instanceof JComboBox<?> combo) {
                combo.setBackground(bgInput);
                combo.setForeground(fgTexto);
            } else if (comp instanceof JCheckBox check) {
                check.setBackground(bgPanel);
                check.setForeground(fgTexto);
            } else if (comp instanceof JButton btn) {
                ajustarIconoBoton(btn);
                Color bgBtn = btn.getBackground();
                if (bgBtn.equals(Color.WHITE) || bgBtn.equals(BG_NIGHT_INPUT)) {
                    btn.setBackground(modoOscuro ? BG_NIGHT_INPUT : Color.WHITE);
                    btn.setForeground(fgTexto);
                }
            } else if (comp instanceof Container subContenedor) {
                adaptarContenedor(subContenedor);
            }
        }
    }

    public static void marcarBotonActivo(JButton botonActivo, JButton... todosLosBotonesMenu) {
        for (JButton btn : todosLosBotonesMenu) {
            if (btn == null) continue;
            if (btn == botonActivo) {
                btn.setBackground(COLOR_MENU_ACTIVO);
                btn.setForeground(new Color(20, 20, 20));
            } else {
                btn.setBackground(COLOR_MENU_INACTIVO);
                btn.setForeground(Color.WHITE);
            }
        }
    }

    public static String obtenerTextoBotonTema() {
        return modoOscuro ? "Modo Claro" : "Modo Oscuro";
    }

    private static boolean esColorInstitucionalFijo(Color c) {
        if (c == null) return false;
        return (c.getRed() == 24 && c.getGreen() == 58 && c.getBlue() == 108)
                || (c.getRed() == 38 && c.getGreen() == 102 && c.getBlue() == 144);
    }

    private static boolean esColorTextoEspecial(Color fg, Container padre) {
        if (fg == null) return false;
        if (padre instanceof JPanel p && esColorInstitucionalFijo(p.getBackground())) {
            return true;
        }
        return fg.equals(new Color(255, 51, 51)) || fg.equals(new Color(255, 255, 0));
    }
    
    /**
     * Redimensiona automáticamente el ícono de un botón según su altura
     * sin que tengas que editar las imágenes manualmente.
     */
    public static void ajustarIconoBoton(JButton btn) {
        if (btn.getIcon() instanceof ImageIcon iconoOriginal) {
            // Evitamos re-escalar si ya tiene tamaño de ícono pequeño (<= 24px)
            if (iconoOriginal.getIconWidth() <= 24 && iconoOriginal.getIconHeight() <= 24) {
                return;
            }

            // Calculamos el tamaño ideal según la altura del botón (dejando 10px de margen)
            int altoBoton = btn.getHeight() > 0 ? btn.getHeight() : btn.getPreferredSize().height;
            int tamano = (altoBoton > 16) ? (altoBoton - 10) : 20;

            // Si el botón además tiene texto (ej. "Ingresar al Sistema"), limitamos el ícono a 22x22 px
            if (btn.getText() != null && !btn.getText().trim().isEmpty() && tamano > 22) {
                tamano = 22;
            }

            Image imgEscalada = iconoOriginal.getImage().getScaledInstance(tamano, tamano, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(imgEscalada));
        }
    }

}
