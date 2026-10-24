package view;

import controller.UsuarioController;
import model.Usuario;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class JPanel_Usuario extends javax.swing.JPanel {
    
    // Controlador y variable para guardar el ID del usuario seleccionado en la tabla
    private final UsuarioController usuarioController = new UsuarioController();
    private int idUsuarioSeleccionado = 0;

    /**
     * Creates new form JPanel_Usuario
     */
    public JPanel_Usuario() {
        initComponents();
        configurarBotonesIniciales();
        cargarTablaUsuarios();
    }
    
    // =========================================================================
    // MÉTODOS DE LÓGICA Y VALIDACIÓN DE USUARIOS
    // =========================================================================

    private void configurarBotonesIniciales() {
        jbn_actualizar.setEnabled(false);
        jbn_carbiarEstadoUsuario.setEnabled(false);
    }

    private void cargarTablaUsuarios() {
        DefaultTableModel modelo = (DefaultTableModel) jTable_listaUsuariosRegistrados.getModel();
        modelo.setRowCount(0);

        List<Usuario> lista = usuarioController.listarUsuarios();
        for (Usuario u : lista) {
            modelo.addRow(new Object[]{
                u.getId_usuario(),
                u.getNombre(),
                u.getUsername(),
                u.getRol(),
                u.isEstado() ? "ACTIVO" : "INACTIVO"
            });
        }
    }

    private void registrarUsuario() {
        String nombre = jtf_nombreCompleto1.getText();
        String username = jtf_username.getText();
        String clave = new String(jpf_password.getPassword());
        String rol = String.valueOf(jcb_rolSistema.getSelectedItem());
        boolean estado = jcb_estadoUsuario.isSelected();

        String respuesta = usuarioController.registrarUsuario(nombre, username, clave, rol, estado);
        if ("OK".equals(respuesta)) {
            JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTablaUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, respuesta, "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarUsuario() {
        if (idUsuarioSeleccionado <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione primero un usuario de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = jtf_nombreCompleto1.getText();
        String username = jtf_username.getText();
        String nuevaClave = new String(jpf_password.getPassword());
        String rol = String.valueOf(jcb_rolSistema.getSelectedItem());
        boolean estado = jcb_estadoUsuario.isSelected();

        String respuesta = usuarioController.actualizarUsuario(idUsuarioSeleccionado, nombre, username, nuevaClave, rol, estado);
        if ("OK".equals(respuesta)) {
            JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTablaUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, respuesta, "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cambiarEstadoUsuario() {
        if (idUsuarioSeleccionado <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione primero un usuario de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int fila = jTable_listaUsuariosRegistrados.getSelectedRow();
        boolean estadoActualTabla = fila >= 0 && "ACTIVO".equalsIgnoreCase(jTable_listaUsuariosRegistrados.getValueAt(fila, 4).toString());
        boolean nuevoEstado = !estadoActualTabla;

        String respuesta = usuarioController.cambiarEstadoUsuario(idUsuarioSeleccionado, nuevoEstado);
        if ("OK".equals(respuesta)) {
            JOptionPane.showMessageDialog(this, "Estado del usuario cambiado a: " + (nuevoEstado ? "ACTIVO" : "INACTIVO"), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTablaUsuarios();
        } else {
            JOptionPane.showMessageDialog(this, respuesta, "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void seleccionarFilaTabla() {
        int fila = jTable_listaUsuariosRegistrados.getSelectedRow();
        if (fila >= 0) {
            idUsuarioSeleccionado = Integer.parseInt(jTable_listaUsuariosRegistrados.getValueAt(fila, 0).toString());
            jtf_nombreCompleto1.setText(jTable_listaUsuariosRegistrados.getValueAt(fila, 1).toString());
            jtf_username.setText(jTable_listaUsuariosRegistrados.getValueAt(fila, 2).toString());
            jpf_password.setText("");
            jcb_rolSistema.setSelectedItem(jTable_listaUsuariosRegistrados.getValueAt(fila, 3).toString());
            boolean activo = "ACTIVO".equalsIgnoreCase(jTable_listaUsuariosRegistrados.getValueAt(fila, 4).toString());
            jcb_estadoUsuario.setSelected(activo);

            jbn_registrar.setEnabled(false);
            jbn_actualizar.setEnabled(true);
            jbn_carbiarEstadoUsuario.setEnabled(true);
        }
    }

    private void limpiarFormulario() {
        idUsuarioSeleccionado = 0;
        jtf_nombreCompleto1.setText("");
        jtf_username.setText("");
        jpf_password.setText("");
        jcb_rolSistema.setSelectedIndex(0);
        jcb_estadoUsuario.setSelected(true);
        jTable_listaUsuariosRegistrados.clearSelection();

        jbn_registrar.setEnabled(true);
        jbn_actualizar.setEnabled(false);
        jbn_carbiarEstadoUsuario.setEnabled(false);
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
        jPanel_datosUsuario = new javax.swing.JPanel();
        jLabel_nombreCompleto = new javax.swing.JLabel();
        jtf_username = new javax.swing.JTextField();
        jLabel_username = new javax.swing.JLabel();
        jtf_nombreCompleto1 = new javax.swing.JTextField();
        jLabel_contrasena = new javax.swing.JLabel();
        jpf_password = new javax.swing.JPasswordField();
        jLabel_rolSistema = new javax.swing.JLabel();
        jcb_rolSistema = new javax.swing.JComboBox<>();
        jcb_estadoUsuario = new javax.swing.JCheckBox();
        jbn_registrar = new javax.swing.JButton();
        jbn_carbiarEstadoUsuario = new javax.swing.JButton();
        jbn_actualizar = new javax.swing.JButton();
        jPanel_listaUsuariosRegistrados = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_listaUsuariosRegistrados = new javax.swing.JTable();

        jPanel_fondo.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_fondo.setForeground(new java.awt.Color(255, 255, 255));

        jPanel_datosUsuario.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_datosUsuario.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos del usuario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel_datosUsuario.setForeground(new java.awt.Color(255, 255, 255));

        jLabel_nombreCompleto.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_nombreCompleto.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_nombreCompleto.setText("Nombre Completo");

        jtf_username.setBackground(new java.awt.Color(255, 255, 255));
        jtf_username.setForeground(new java.awt.Color(0, 0, 0));

        jLabel_username.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_username.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_username.setText("Username (cuenta de acceso):");

        jtf_nombreCompleto1.setBackground(new java.awt.Color(255, 255, 255));
        jtf_nombreCompleto1.setForeground(new java.awt.Color(0, 0, 0));

        jLabel_contrasena.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_contrasena.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_contrasena.setText("Contraseña (dejar vacío al editar para mantener):");

        jpf_password.setBackground(new java.awt.Color(255, 255, 255));
        jpf_password.setForeground(new java.awt.Color(0, 0, 0));

        jLabel_rolSistema.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_rolSistema.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_rolSistema.setText("Rol del sistema:");

        jcb_rolSistema.setBackground(new java.awt.Color(255, 255, 255));
        jcb_rolSistema.setForeground(new java.awt.Color(0, 0, 0));
        jcb_rolSistema.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "ALMACENERO" }));

        jcb_estadoUsuario.setBackground(new java.awt.Color(255, 255, 255));
        jcb_estadoUsuario.setForeground(new java.awt.Color(0, 0, 0));
        jcb_estadoUsuario.setSelected(true);
        jcb_estadoUsuario.setText("Usuario Activo (Habilitado)");

        jbn_registrar.setBackground(new java.awt.Color(255, 255, 255));
        jbn_registrar.setForeground(new java.awt.Color(0, 0, 0));
        jbn_registrar.setText("Registrar");
        jbn_registrar.addActionListener(this::jbn_registrarActionPerformed);

        jbn_carbiarEstadoUsuario.setBackground(new java.awt.Color(255, 255, 255));
        jbn_carbiarEstadoUsuario.setForeground(new java.awt.Color(0, 0, 0));
        jbn_carbiarEstadoUsuario.setText("Activar/Inactivar");
        jbn_carbiarEstadoUsuario.addActionListener(this::jbn_carbiarEstadoUsuarioActionPerformed);

        jbn_actualizar.setBackground(new java.awt.Color(255, 255, 255));
        jbn_actualizar.setForeground(new java.awt.Color(0, 0, 0));
        jbn_actualizar.setText("Actualizar");
        jbn_actualizar.addActionListener(this::jbn_actualizarActionPerformed);

        javax.swing.GroupLayout jPanel_datosUsuarioLayout = new javax.swing.GroupLayout(jPanel_datosUsuario);
        jPanel_datosUsuario.setLayout(jPanel_datosUsuarioLayout);
        jPanel_datosUsuarioLayout.setHorizontalGroup(
            jPanel_datosUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_datosUsuarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_datosUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_nombreCompleto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtf_username)
                    .addComponent(jLabel_username, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_contrasena, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpf_password)
                    .addComponent(jLabel_rolSistema, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcb_rolSistema, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcb_estadoUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel_datosUsuarioLayout.createSequentialGroup()
                        .addComponent(jbn_registrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbn_actualizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbn_carbiarEstadoUsuario)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel_datosUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel_datosUsuarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jtf_nombreCompleto1, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel_datosUsuarioLayout.setVerticalGroup(
            jPanel_datosUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_datosUsuarioLayout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jLabel_nombreCompleto)
                .addGap(52, 52, 52)
                .addComponent(jLabel_username)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtf_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel_contrasena)
                .addGap(18, 18, 18)
                .addComponent(jpf_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel_rolSistema)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jcb_rolSistema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jcb_estadoUsuario)
                .addGap(66, 66, 66)
                .addGroup(jPanel_datosUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbn_registrar)
                    .addComponent(jbn_actualizar)
                    .addComponent(jbn_carbiarEstadoUsuario))
                .addContainerGap(95, Short.MAX_VALUE))
            .addGroup(jPanel_datosUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel_datosUsuarioLayout.createSequentialGroup()
                    .addGap(141, 141, 141)
                    .addComponent(jtf_nombreCompleto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(443, Short.MAX_VALUE)))
        );

        jPanel_listaUsuariosRegistrados.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_listaUsuariosRegistrados.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Listado de Usuarios Registrados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel_listaUsuariosRegistrados.setForeground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        jTable_listaUsuariosRegistrados.setBackground(new java.awt.Color(255, 255, 255));
        jTable_listaUsuariosRegistrados.setForeground(new java.awt.Color(0, 0, 0));
        jTable_listaUsuariosRegistrados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre Completo", "Username", "Rol", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_listaUsuariosRegistrados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_listaUsuariosRegistradosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_listaUsuariosRegistrados);

        javax.swing.GroupLayout jPanel_listaUsuariosRegistradosLayout = new javax.swing.GroupLayout(jPanel_listaUsuariosRegistrados);
        jPanel_listaUsuariosRegistrados.setLayout(jPanel_listaUsuariosRegistradosLayout);
        jPanel_listaUsuariosRegistradosLayout.setHorizontalGroup(
            jPanel_listaUsuariosRegistradosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_listaUsuariosRegistradosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel_listaUsuariosRegistradosLayout.setVerticalGroup(
            jPanel_listaUsuariosRegistradosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_listaUsuariosRegistradosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel_fondoLayout = new javax.swing.GroupLayout(jPanel_fondo);
        jPanel_fondo.setLayout(jPanel_fondoLayout);
        jPanel_fondoLayout.setHorizontalGroup(
            jPanel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_fondoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel_datosUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_listaUsuariosRegistrados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel_fondoLayout.setVerticalGroup(
            jPanel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_fondoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel_datosUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_listaUsuariosRegistrados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel_fondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_fondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbn_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_registrarActionPerformed
        // TODO add your handling code here:
        registrarUsuario();
    }//GEN-LAST:event_jbn_registrarActionPerformed

    private void jbn_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_actualizarActionPerformed
        // TODO add your handling code here:
        actualizarUsuario();
    }//GEN-LAST:event_jbn_actualizarActionPerformed

    private void jbn_carbiarEstadoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_carbiarEstadoUsuarioActionPerformed
        // TODO add your handling code here:
        cambiarEstadoUsuario();
    }//GEN-LAST:event_jbn_carbiarEstadoUsuarioActionPerformed

    private void jTable_listaUsuariosRegistradosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_listaUsuariosRegistradosMouseClicked
        // TODO add your handling code here:
        seleccionarFilaTabla();
    }//GEN-LAST:event_jTable_listaUsuariosRegistradosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel_contrasena;
    private javax.swing.JLabel jLabel_nombreCompleto;
    private javax.swing.JLabel jLabel_rolSistema;
    private javax.swing.JLabel jLabel_username;
    private javax.swing.JPanel jPanel_datosUsuario;
    private javax.swing.JPanel jPanel_fondo;
    private javax.swing.JPanel jPanel_listaUsuariosRegistrados;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_listaUsuariosRegistrados;
    private javax.swing.JButton jbn_actualizar;
    private javax.swing.JButton jbn_carbiarEstadoUsuario;
    private javax.swing.JButton jbn_registrar;
    private javax.swing.JCheckBox jcb_estadoUsuario;
    private javax.swing.JComboBox<String> jcb_rolSistema;
    private javax.swing.JPasswordField jpf_password;
    private javax.swing.JTextField jtf_nombreCompleto1;
    private javax.swing.JTextField jtf_username;
    // End of variables declaration//GEN-END:variables
}
