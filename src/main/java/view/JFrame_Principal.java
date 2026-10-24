package view;

import controller.ProductoController;
import controller.Sesion;
import model.AlertaResumen;
import model.Usuario;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class JFrame_Principal extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JFrame_Principal.class.getName());
    private final ProductoController productoController = new ProductoController();

    private JPanel panelDerechoDinamico;
    private JPanel panelVistaDashboard;

    public JFrame_Principal() {
        initComponents();
        configurarContenedorDinamico();
        aplicarDatosSesionYPermisos();
        cargarAlertasDashboard();
    }
    
    /**
     * Prepara el panel derecho para intercambiar vistas sin agregar listeners por código.
     */
    private void configurarContenedorDinamico() {
        this.setLocationRelativeTo(null);

        panelVistaDashboard = new JPanel(new BorderLayout(0, 6));
        panelVistaDashboard.setBackground(Color.WHITE);
        panelVistaDashboard.add(jPanel3, BorderLayout.NORTH);
        panelVistaDashboard.add(jPanel4, BorderLayout.CENTER);

        panelDerechoDinamico = new JPanel(new BorderLayout());
        panelDerechoDinamico.setBackground(Color.WHITE);
        panelDerechoDinamico.add(panelVistaDashboard, BorderLayout.CENTER);

        jPanel_fondo.removeAll();
        jPanel_fondo.setLayout(new BorderLayout(6, 0));
        jPanel_fondo.add(jPanel1, BorderLayout.WEST);
        jPanel_fondo.add(panelDerechoDinamico, BorderLayout.CENTER);

        configurarColoresTablaAlertas();
    }

    private void aplicarDatosSesionYPermisos() {
        Usuario u = Sesion.getUsuarioActivo();
        if (u != null) {
            jLabel_admintradorGeneral.setForeground(Color.WHITE);
            jLabel_admintradorGeneral.setText(u.getNombre());
            jLabel_rol.setText("ROL: " + u.getRol());
            this.setTitle("AlmacenAbarrotesSystem - Panel Principal | Usuario: " + u.getNombre() + " [" + u.getRol() + "]");
        } else {
            jLabel_admintradorGeneral.setForeground(Color.WHITE);
            jLabel_admintradorGeneral.setText("Sin sesión activa");
            jLabel_rol.setText("ROL: N/A");
        }

        if (!Sesion.esAdmin()) {
            jbn_gestionUsuarios.setVisible(false);
        }
    }

    public final void cargarAlertasDashboard() {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);

        List<AlertaResumen> lista = productoController.listarAlertasInventario();
        for (AlertaResumen a : lista) {
            modelo.addRow(new Object[]{
                a.getId_producto(),
                a.getCodigo(),
                a.getNombre(),
                a.getCategoria(),
                a.getUnidad_medida(),
                a.getStock_actual(),
                a.getStock_minimo(),
                a.getFecha_vencimiento() != null ? a.getFecha_vencimiento().toString() : "NO PERECIBLE",
                a.getDias_para_vencer() != null ? a.getDias_para_vencer() + " días" : "N/A",
                a.getTipo_alerta()
            });
        }
        jLabel_alertasActivas.setText("Alertas Activas: " + lista.size());
    }

    private void configurarColoresTablaAlertas() {
        jTable1.setRowHeight(26);
        jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    Object valAlerta = table.getValueAt(row, 9);
                    String alerta = valAlerta != null ? valAlerta.toString() : "";
                    if (alerta.contains("CRÍTICO") || alerta.contains("VENCIDO") || alerta.contains("AGOTADO")) {
                        c.setBackground(new Color(255, 230, 230));
                        c.setForeground(new Color(140, 0, 0));
                    } else {
                        c.setBackground(new Color(255, 248, 225));
                        c.setForeground(new Color(120, 80, 0));
                    }
                }
                return c;
            }
        });
    }

    private void mostrarDashboard() {
        cargarAlertasDashboard();
        cambiarVista(panelVistaDashboard);
    }

    private void cambiarVista(JPanel nuevoPanel) {
        panelDerechoDinamico.removeAll();
        panelDerechoDinamico.add(nuevoPanel, BorderLayout.CENTER);
        panelDerechoDinamico.revalidate();
        panelDerechoDinamico.repaint();
    }

    private void cerrarSesion() {
        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea cerrar la sesión actual?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION
        );
        if (confirmar == JOptionPane.YES_OPTION) {
            Sesion.cerrarSesion();
            JFrame_Login login = new JFrame_Login();
            login.setVisible(true);
            this.dispose();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel_fondo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel_amacenAbarrotes = new javax.swing.JLabel();
        jLabel_admintradorGeneral = new javax.swing.JLabel();
        jLabel_rol = new javax.swing.JLabel();
        jbn_dashboardAlertas = new javax.swing.JButton();
        jbn_catalogo = new javax.swing.JButton();
        jbn_movimientos = new javax.swing.JButton();
        jbn_gestionUsuarios = new javax.swing.JButton();
        jbn_cerrarSesion = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel_monitorDeAlertas = new javax.swing.JLabel();
        jLabel_alertasActivas = new javax.swing.JLabel();
        jbn_actualizarAlertas = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel_fondo.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(24, 58, 108));
        jPanel1.setPreferredSize(new java.awt.Dimension(218, 480));

        jPanel2.setBackground(new java.awt.Color(38, 102, 144));

        jLabel_amacenAbarrotes.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel_amacenAbarrotes.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_amacenAbarrotes.setText("AMACEN ABARROTES");

        jLabel_admintradorGeneral.setText("Administrador general");

        jLabel_rol.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_rol.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel_rol.setForeground(new java.awt.Color(255, 255, 0));
        jLabel_rol.setText("ROL: ADMIN");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel_amacenAbarrotes)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_admintradorGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel_rol, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_amacenAbarrotes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_admintradorGeneral)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_rol)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jbn_dashboardAlertas.setBackground(new java.awt.Color(0, 153, 102));
        jbn_dashboardAlertas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbn_dashboardAlertas.setForeground(new java.awt.Color(255, 255, 255));
        jbn_dashboardAlertas.setText("Dashboard / Alertas");
        jbn_dashboardAlertas.addActionListener(this::jbn_dashboardAlertasActionPerformed);

        jbn_catalogo.setBackground(new java.awt.Color(0, 153, 102));
        jbn_catalogo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbn_catalogo.setForeground(new java.awt.Color(255, 255, 255));
        jbn_catalogo.setText("Catálogo de productos");
        jbn_catalogo.addActionListener(this::jbn_catalogoActionPerformed);

        jbn_movimientos.setBackground(new java.awt.Color(0, 153, 102));
        jbn_movimientos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbn_movimientos.setForeground(new java.awt.Color(255, 255, 255));
        jbn_movimientos.setText("Movimientos (Kardex)");
        jbn_movimientos.addActionListener(this::jbn_movimientosActionPerformed);

        jbn_gestionUsuarios.setBackground(new java.awt.Color(0, 153, 102));
        jbn_gestionUsuarios.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbn_gestionUsuarios.setForeground(new java.awt.Color(255, 255, 255));
        jbn_gestionUsuarios.setText("Gestión de usuarios");
        jbn_gestionUsuarios.addActionListener(this::jbn_gestionUsuariosActionPerformed);

        jbn_cerrarSesion.setBackground(new java.awt.Color(220, 54, 54));
        jbn_cerrarSesion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jbn_cerrarSesion.setForeground(new java.awt.Color(255, 255, 255));
        jbn_cerrarSesion.setText("Cerrar Sesión");
        jbn_cerrarSesion.addActionListener(this::jbn_cerrarSesionActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbn_dashboardAlertas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbn_catalogo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addComponent(jbn_movimientos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbn_gestionUsuarios, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbn_cerrarSesion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbn_dashboardAlertas, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbn_catalogo, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbn_movimientos, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbn_gestionUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbn_cerrarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel_monitorDeAlertas.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_monitorDeAlertas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel_monitorDeAlertas.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_monitorDeAlertas.setText("MONITOR DE ALERTAS: STOCK MÍNIMO Y PRÓXIMOS VENCIMIENTOS (<= 30 DÍAS)");

        jLabel_alertasActivas.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel_alertasActivas.setForeground(new java.awt.Color(255, 51, 51));
        jLabel_alertasActivas.setText("Alertas Activas: 4");

        jbn_actualizarAlertas.setBackground(new java.awt.Color(0, 102, 102));
        jbn_actualizarAlertas.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jbn_actualizarAlertas.setForeground(new java.awt.Color(0, 255, 0));
        jbn_actualizarAlertas.setText("Actualizar Alertas");
        jbn_actualizarAlertas.addActionListener(this::jbn_actualizarAlertasActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel_monitorDeAlertas)
                .addGap(99, 99, 99)
                .addComponent(jLabel_alertasActivas, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                .addComponent(jbn_actualizarAlertas)
                .addGap(23, 23, 23))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_monitorDeAlertas)
                    .addComponent(jLabel_alertasActivas)
                    .addComponent(jbn_actualizarAlertas))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Productos que requieren reposición inmediata o control de merma", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N

        jTable1.setBackground(new java.awt.Color(255, 255, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Código", "Producto", "Categoría", "Unidad Almacén", "Stock Actual", "Stock Mínimo", "F. Vencimiento", "Días p. Vencer", "Estado de Alerta"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel_fondoLayout = new javax.swing.GroupLayout(jPanel_fondo);
        jPanel_fondo.setLayout(jPanel_fondoLayout);
        jPanel_fondoLayout.setHorizontalGroup(
            jPanel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_fondoLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel_fondoLayout.setVerticalGroup(
            jPanel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
            .addGroup(jPanel_fondoLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbn_dashboardAlertasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_dashboardAlertasActionPerformed
          mostrarDashboard();
    }//GEN-LAST:event_jbn_dashboardAlertasActionPerformed

    private void jbn_catalogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_catalogoActionPerformed
        // TODO add your handling code here:
        cambiarVista(new JPanel_Producto());
    }//GEN-LAST:event_jbn_catalogoActionPerformed

    private void jbn_movimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_movimientosActionPerformed
        // TODO add your handling code here:
        cambiarVista(new JPanel_Movimiento());
    }//GEN-LAST:event_jbn_movimientosActionPerformed

    private void jbn_gestionUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_gestionUsuariosActionPerformed
        // TODO add your handling code here:
        if (Sesion.esAdmin()) {
            cambiarVista(new JPanel_Usuario());
        } else {
            JOptionPane.showMessageDialog(this,
                    "Acceso restringido: Solo el rol ADMIN puede gestionar usuarios.",
                    "Permiso Denegado",
                    JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jbn_gestionUsuariosActionPerformed

    private void jbn_cerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_cerrarSesionActionPerformed
        // TODO add your handling code here:
        cerrarSesion();
    }//GEN-LAST:event_jbn_cerrarSesionActionPerformed

    private void jbn_actualizarAlertasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_actualizarAlertasActionPerformed
        // TODO add your handling code here:
        cargarAlertasDashboard();
    }//GEN-LAST:event_jbn_actualizarAlertasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new JFrame_Principal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel_admintradorGeneral;
    private javax.swing.JLabel jLabel_alertasActivas;
    private javax.swing.JLabel jLabel_amacenAbarrotes;
    private javax.swing.JLabel jLabel_monitorDeAlertas;
    private javax.swing.JLabel jLabel_rol;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel_fondo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton jbn_actualizarAlertas;
    private javax.swing.JButton jbn_catalogo;
    private javax.swing.JButton jbn_cerrarSesion;
    private javax.swing.JButton jbn_dashboardAlertas;
    private javax.swing.JButton jbn_gestionUsuarios;
    private javax.swing.JButton jbn_movimientos;
    // End of variables declaration//GEN-END:variables
}
