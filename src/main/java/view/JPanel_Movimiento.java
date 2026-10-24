package view;

import controller.MovimientoController;
import controller.ProductoController;
import controller.Sesion;
import model.Movimiento;
import model.Producto;
import model.Usuario;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class JPanel_Movimiento extends javax.swing.JPanel {
    
    // Controladores y lista auxiliar para vincular el combo con los objetos Producto reales
    private final MovimientoController movimientoController = new MovimientoController();
    private final ProductoController productoController = new ProductoController();
    private List<Producto> listaProductosActivos = new ArrayList<>();

    /**
     * Creates new form JPanel_Movimiento
     */
    public JPanel_Movimiento() {
        initComponents();
        configurarColoresTablaKardex();
        cargarComboProductos();
        cargarTablaKardex();
    }
    
    // =========================================================================
    // MÉTODOS DE LÓGICA Y VALIDACIÓN DE KARDEX
    // =========================================================================

    private void cargarComboProductos() {
        jcb_seleccionarProductoActivo.removeAllItems();
        listaProductosActivos = productoController.listarProductosActivos();

        for (Producto p : listaProductosActivos) {
            jcb_seleccionarProductoActivo.addItem(p.toString());
        }
        actualizarEtiquetaStock();
    }

    private Producto obtenerProductoSeleccionado() {
        int index = jcb_seleccionarProductoActivo.getSelectedIndex();
        if (index >= 0 && index < listaProductosActivos.size()) {
            return listaProductosActivos.get(index);
        }
        return null;
    }

    private void actualizarEtiquetaStock() {
        Producto seleccionado = obtenerProductoSeleccionado();
        if (seleccionado != null) {
            jLabel_stockActualEmpaque.setText(String.format(
                    "Stock Actual: %d | Empaque: %s",
                    seleccionado.getStock_actual(),
                    seleccionado.getUnidad_medida()
            ));
        } else {
            jLabel_stockActualEmpaque.setText("Stock Actual: -- | Empaque: --");
        }
    }

    private void sugerirMotivoPorTipo() {
        String tipo = String.valueOf(jcb_tipoMovimiento.getSelectedItem());
        if ("ENTRADA".equalsIgnoreCase(tipo)) {
            jcb_motivoOperacion.setSelectedItem("COMPRA PROVEEDOR");
        } else {
            jcb_motivoOperacion.setSelectedItem("DESPACHO TIENDA");
        }
    }

    private void cargarTablaKardex() {
        DefaultTableModel modelo = (DefaultTableModel) jTable_historialKardex.getModel();
        modelo.setRowCount(0);

        List<Movimiento> historial = movimientoController.listarMovimientos();
        for (Movimiento m : historial) {
            modelo.addRow(new Object[]{
                m.getId_movimiento(),
                m.getFecha_registro(),
                m.getNombre_producto(),
                m.getTipo_movimiento(),
                m.getMotivo(),
                m.getCantidad(),
                m.getUsername(),
                m.getObservacion()
            });
        }
    }

    private void configurarColoresTablaKardex() {
        jTable_historialKardex.setRowHeight(26);
        jTable_historialKardex.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    Object valTipo = table.getValueAt(row, 3);
                    String tipo = valTipo != null ? valTipo.toString() : "";
                    if ("ENTRADA".equalsIgnoreCase(tipo)) {
                        c.setBackground(new Color(232, 248, 238));
                        c.setForeground(new Color(15, 95, 45));
                    } else {
                        c.setBackground(new Color(253, 236, 236));
                        c.setForeground(new Color(145, 25, 25));
                    }
                }
                return c;
            }
        });
    }

    private void registrarMovimiento() {
        Producto prodSeleccionado = obtenerProductoSeleccionado();
        if (prodSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto activo.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioActivo = Sesion.getUsuarioActivo();
        int idUsuario = (usuarioActivo != null) ? usuarioActivo.getId_usuario() : 1;

        int cantidad;
        try {
            cantidad = Integer.parseInt(jtf_cantidad.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero válido mayor a cero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tipo = String.valueOf(jcb_tipoMovimiento.getSelectedItem());
        String motivo = String.valueOf(jcb_motivoOperacion.getSelectedItem());
        String observacion = jtf_observacionGuiaLote.getText().trim();

        String respuesta = movimientoController.registrarMovimiento(
                prodSeleccionado.getId_producto(),
                idUsuario,
                tipo,
                motivo,
                cantidad,
                observacion
        );

        if ("OK".equals(respuesta)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Movimiento registrado y stock actualizado exitosamente.",
                    "Transacción Exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );
            limpiarFormulario();
            cargarComboProductos();
            cargarTablaKardex();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    respuesta,
                    "Operación Rechazada",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiarFormulario() {
        if (jcb_seleccionarProductoActivo.getItemCount() > 0) {
            jcb_seleccionarProductoActivo.setSelectedIndex(0);
        }
        jcb_tipoMovimiento.setSelectedIndex(0);
        jcb_motivoOperacion.setSelectedIndex(0);
        jtf_cantidad.setText("1");
        jtf_observacionGuiaLote.setText("");
        actualizarEtiquetaStock();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel_registrarOperacionKardek = new javax.swing.JPanel();
        jLabel_seleccionarProductoActivo = new javax.swing.JLabel();
        jcb_seleccionarProductoActivo = new javax.swing.JComboBox<>();
        jLabel_stockActualEmpaque = new javax.swing.JLabel();
        jLabel_tipoMovimiento = new javax.swing.JLabel();
        jcb_tipoMovimiento = new javax.swing.JComboBox<>();
        jLabel_motivoOperacion = new javax.swing.JLabel();
        jcb_motivoOperacion = new javax.swing.JComboBox<>();
        jLabel_cantidad = new javax.swing.JLabel();
        jtf_cantidad = new javax.swing.JTextField();
        jLabel_observacion = new javax.swing.JLabel();
        jbn_procesarMovimiento = new javax.swing.JButton();
        jbn_limpiar = new javax.swing.JButton();
        jtf_observacionGuiaLote = new javax.swing.JTextField();
        jPanel_historialKardex = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_historialKardex = new javax.swing.JTable();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jPanel_registrarOperacionKardek.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_registrarOperacionKardek.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registrar operación en Kardex", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel_registrarOperacionKardek.setForeground(new java.awt.Color(255, 255, 255));

        jLabel_seleccionarProductoActivo.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_seleccionarProductoActivo.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_seleccionarProductoActivo.setText("Seleccionar Producto Activo:");

        jcb_seleccionarProductoActivo.setBackground(new java.awt.Color(255, 255, 255));
        jcb_seleccionarProductoActivo.setForeground(new java.awt.Color(0, 0, 0));
        jcb_seleccionarProductoActivo.addActionListener(this::jcb_seleccionarProductoActivoActionPerformed);

        jLabel_stockActualEmpaque.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_stockActualEmpaque.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel_stockActualEmpaque.setForeground(new java.awt.Color(0, 51, 153));
        jLabel_stockActualEmpaque.setText("Stock Actual: 24 | Empaque: Caja X 12");

        jLabel_tipoMovimiento.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_tipoMovimiento.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_tipoMovimiento.setText("Tipo de movimiento");

        jcb_tipoMovimiento.setBackground(new java.awt.Color(255, 255, 255));
        jcb_tipoMovimiento.setForeground(new java.awt.Color(0, 0, 0));
        jcb_tipoMovimiento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ENTRADA", "SALIDA" }));
        jcb_tipoMovimiento.addActionListener(this::jcb_tipoMovimientoActionPerformed);

        jLabel_motivoOperacion.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_motivoOperacion.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_motivoOperacion.setText("Motivo de la Operación:");

        jcb_motivoOperacion.setBackground(new java.awt.Color(255, 255, 255));
        jcb_motivoOperacion.setForeground(new java.awt.Color(0, 0, 0));
        jcb_motivoOperacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "COMPRA PROVEEDOR", "DESPACHO TIENDA", "MERMA POR VENCIMIENTO", "MERMA POR RUPTURA", "DEVOLUCIÓN CLIENTE", "AJUSTE INVENTARIO" }));

        jLabel_cantidad.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_cantidad.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_cantidad.setText("Cantidad (en unidades de almacén):");

        jtf_cantidad.setBackground(new java.awt.Color(255, 255, 255));
        jtf_cantidad.setForeground(new java.awt.Color(0, 0, 0));
        jtf_cantidad.setText("1");

        jLabel_observacion.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_observacion.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_observacion.setText("Observación / N° Guía / Lote:");

        jbn_procesarMovimiento.setBackground(new java.awt.Color(0, 102, 153));
        jbn_procesarMovimiento.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbn_procesarMovimiento.setForeground(new java.awt.Color(255, 255, 255));
        jbn_procesarMovimiento.setText("Procesar Movimiento");
        jbn_procesarMovimiento.addActionListener(this::jbn_procesarMovimientoActionPerformed);

        jbn_limpiar.setBackground(new java.awt.Color(0, 102, 153));
        jbn_limpiar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbn_limpiar.setForeground(new java.awt.Color(255, 255, 255));
        jbn_limpiar.setText("Limpiar");
        jbn_limpiar.addActionListener(this::jbn_limpiarActionPerformed);

        jtf_observacionGuiaLote.setBackground(new java.awt.Color(255, 255, 255));
        jtf_observacionGuiaLote.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel_registrarOperacionKardekLayout = new javax.swing.GroupLayout(jPanel_registrarOperacionKardek);
        jPanel_registrarOperacionKardek.setLayout(jPanel_registrarOperacionKardekLayout);
        jPanel_registrarOperacionKardekLayout.setHorizontalGroup(
            jPanel_registrarOperacionKardekLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_registrarOperacionKardekLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_registrarOperacionKardekLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_seleccionarProductoActivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcb_seleccionarProductoActivo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_stockActualEmpaque, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_tipoMovimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcb_tipoMovimiento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_motivoOperacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcb_motivoOperacion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel_cantidad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtf_cantidad)
                    .addComponent(jLabel_observacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_registrarOperacionKardekLayout.createSequentialGroup()
                        .addComponent(jbn_procesarMovimiento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jbn_limpiar, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                    .addComponent(jtf_observacionGuiaLote))
                .addContainerGap())
        );
        jPanel_registrarOperacionKardekLayout.setVerticalGroup(
            jPanel_registrarOperacionKardekLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_registrarOperacionKardekLayout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(jLabel_seleccionarProductoActivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jcb_seleccionarProductoActivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel_stockActualEmpaque)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_tipoMovimiento)
                .addGap(18, 18, 18)
                .addComponent(jcb_tipoMovimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel_motivoOperacion)
                .addGap(18, 18, 18)
                .addComponent(jcb_motivoOperacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel_cantidad)
                .addGap(18, 18, 18)
                .addComponent(jtf_cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel_observacion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtf_observacionGuiaLote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel_registrarOperacionKardekLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbn_limpiar)
                    .addComponent(jbn_procesarMovimiento))
                .addGap(63, 63, 63))
        );

        jPanel_historialKardex.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_historialKardex.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Historial Transaccional del Kardex (Trazabilidad Completa)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel_historialKardex.setForeground(new java.awt.Color(255, 255, 255));

        jTable_historialKardex.setBackground(new java.awt.Color(255, 255, 255));
        jTable_historialKardex.setForeground(new java.awt.Color(0, 0, 0));
        jTable_historialKardex.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Mov.", "Fecha y Hora", "Producto (unidad)", "Tipo", "Motivo", "Cantidad", "Usuario", "Obserbación"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable_historialKardex);

        javax.swing.GroupLayout jPanel_historialKardexLayout = new javax.swing.GroupLayout(jPanel_historialKardex);
        jPanel_historialKardex.setLayout(jPanel_historialKardexLayout);
        jPanel_historialKardexLayout.setHorizontalGroup(
            jPanel_historialKardexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_historialKardexLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel_historialKardexLayout.setVerticalGroup(
            jPanel_historialKardexLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_historialKardexLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel_registrarOperacionKardek, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_historialKardex, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel_registrarOperacionKardek, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_historialKardex, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jcb_seleccionarProductoActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcb_seleccionarProductoActivoActionPerformed
        // TODO add your handling code here:
        actualizarEtiquetaStock();
       
    }//GEN-LAST:event_jcb_seleccionarProductoActivoActionPerformed

    private void jcb_tipoMovimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcb_tipoMovimientoActionPerformed
        // TODO add your handling code here:
        sugerirMotivoPorTipo();
        
    }//GEN-LAST:event_jcb_tipoMovimientoActionPerformed

    private void jbn_procesarMovimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_procesarMovimientoActionPerformed
        // TODO add your handling code here:
        registrarMovimiento();
        
    }//GEN-LAST:event_jbn_procesarMovimientoActionPerformed

    private void jbn_limpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_limpiarActionPerformed
        // TODO add your handling code here:
        limpiarFormulario();
        
    }//GEN-LAST:event_jbn_limpiarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel_cantidad;
    private javax.swing.JLabel jLabel_motivoOperacion;
    private javax.swing.JLabel jLabel_observacion;
    private javax.swing.JLabel jLabel_seleccionarProductoActivo;
    private javax.swing.JLabel jLabel_stockActualEmpaque;
    private javax.swing.JLabel jLabel_tipoMovimiento;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel_historialKardex;
    private javax.swing.JPanel jPanel_registrarOperacionKardek;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_historialKardex;
    private javax.swing.JButton jbn_limpiar;
    private javax.swing.JButton jbn_procesarMovimiento;
    private javax.swing.JComboBox<String> jcb_motivoOperacion;
    private javax.swing.JComboBox<String> jcb_seleccionarProductoActivo;
    private javax.swing.JComboBox<String> jcb_tipoMovimiento;
    private javax.swing.JTextField jtf_cantidad;
    private javax.swing.JTextField jtf_observacionGuiaLote;
    // End of variables declaration//GEN-END:variables
}
