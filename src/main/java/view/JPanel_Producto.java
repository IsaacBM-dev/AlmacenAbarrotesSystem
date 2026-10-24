package view;

import controller.ProductoController;
import controller.Sesion;
import model.Producto;
import java.sql.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class JPanel_Producto extends javax.swing.JPanel {
    
    // Controlador y variable para guardar el ID del producto seleccionado en la tabla
    private final ProductoController productoController = new ProductoController();
    private int idProductoSeleccionado = 0;

    /**
     * Creates new form JPanel_Producto
     */
    public JPanel_Producto() {
        initComponents();
        configurarSeguridadRol();
        cargarTablaProductos(productoController.listarProductos());
    }
    
    // =========================================================================
    // MÉTODOS DE LÓGICA Y VALIDACIÓN DE PRODUCTOS
    // =========================================================================

    private void configurarSeguridadRol() {
        // Estado inicial de botones de edición (hasta que seleccione una fila)
        jbn_actualizar.setEnabled(false);
        jbn_BajaAltaLogica.setEnabled(false);

        // Si el usuario en sesión es ALMACENERO, puede consultar y buscar pero no modificar el catálogo
        if (!Sesion.esAdmin()) {
            jbn_registrar.setEnabled(false);
            jbn_actualizar.setEnabled(false);
            jbn_BajaAltaLogica.setEnabled(false);
            jtf_codigoProducto.setEditable(false);
            jtf_nombreDescripcion.setEditable(false);
            jtf_stockActual.setEditable(false);
            jtf_stockMinimo.setEditable(false);
            jtf_fechaVencimiento.setEditable(false);
            jtf_precioReferencial.setEditable(false);
            jcb_categoria.setEnabled(false);
            jcb_unidadAlmacen.setEnabled(false);
            jcb_estadoProductoCatalogo.setEnabled(false);
        }
    }

    private void cargarTablaProductos(List<Producto> lista) {
        DefaultTableModel modelo = (DefaultTableModel) jTable_catalogoGeneralInventario.getModel();
        modelo.setRowCount(0);

        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                p.getId_producto(),
                p.getCodigo(),
                p.getNombre(),
                p.getCategoria(),
                p.getUnidad_medida(),
                p.getStock_actual(),
                p.getStock_minimo(),
                p.getFecha_vencimiento() != null ? p.getFecha_vencimiento().toString() : "SIN VENCIMIENTO",
                String.format("%.2f", p.getPrecio_referencial()),
                p.isEstado() ? "ACTIVO" : "INACTIVO"
            });
        }
    }

    private Date parsearFechaVencimiento(String textoFecha) throws IllegalArgumentException {
        if (textoFecha == null || textoFecha.trim().isEmpty() || "SIN VENCIMIENTO".equalsIgnoreCase(textoFecha.trim())) {
            return null;
        }
        return Date.valueOf(textoFecha.trim()); // Formato esperado: AAAA-MM-DD
    }

    private void registrarProducto() {
        try {
            String codigo = jtf_codigoProducto.getText();
            String nombre = jtf_nombreDescripcion.getText();
            String categoria = String.valueOf(jcb_categoria.getSelectedItem());
            String unidad = String.valueOf(jcb_unidadAlmacen.getSelectedItem());
            int stockActual = Integer.parseInt(jtf_stockActual.getText().trim());
            int stockMinimo = Integer.parseInt(jtf_stockMinimo.getText().trim());
            Date fechaVenc = parsearFechaVencimiento(jtf_fechaVencimiento.getText());
            double precio = Double.parseDouble(jtf_precioReferencial.getText().trim().replace(",", "."));
            boolean estado = jcb_estadoProductoCatalogo.isSelected();

            String res = productoController.registrarProducto(
                    codigo, nombre, categoria, unidad, stockActual, stockMinimo, fechaVenc, precio, estado
            );

            if ("OK".equals(res)) {
                JOptionPane.showMessageDialog(this, "Producto registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarTablaProductos(productoController.listarProductos());
            } else {
                JOptionPane.showMessageDialog(this, res, "Validación", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique que Stock Actual, Stock Mínimo y Precio sean números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use AAAA-MM-DD (ejemplo: 2027-05-30) o déjelo vacío.", "Fecha Inválida", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarProducto() {
        if (idProductoSeleccionado <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione primero un producto de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String codigo = jtf_codigoProducto.getText();
            String nombre = jtf_nombreDescripcion.getText();
            String categoria = String.valueOf(jcb_categoria.getSelectedItem());
            String unidad = String.valueOf(jcb_unidadAlmacen.getSelectedItem());
            int stockActual = Integer.parseInt(jtf_stockActual.getText().trim());
            int stockMinimo = Integer.parseInt(jtf_stockMinimo.getText().trim());
            Date fechaVenc = parsearFechaVencimiento(jtf_fechaVencimiento.getText());
            double precio = Double.parseDouble(jtf_precioReferencial.getText().trim().replace(",", "."));
            boolean estado = jcb_estadoProductoCatalogo.isSelected();

            String res = productoController.actualizarProducto(
                    idProductoSeleccionado, codigo, nombre, categoria, unidad,
                    stockActual, stockMinimo, fechaVenc, precio, estado
            );

            if ("OK".equals(res)) {
                JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarTablaProductos(productoController.listarProductos());
            } else {
                JOptionPane.showMessageDialog(this, res, "Validación", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique que los stocks y el precio sean números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use AAAA-MM-DD (ejemplo: 2027-05-30) o déjelo vacío.", "Fecha Inválida", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstadoLogico() {
        if (idProductoSeleccionado <= 0) {
            JOptionPane.showMessageDialog(this, "Seleccione primero un producto de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int fila = jTable_catalogoGeneralInventario.getSelectedRow();
        boolean estadoActualTabla = fila >= 0 && "ACTIVO".equalsIgnoreCase(jTable_catalogoGeneralInventario.getValueAt(fila, 9).toString());
        boolean nuevoEstado = !estadoActualTabla;

        String res = productoController.cambiarEstadoProducto(idProductoSeleccionado, nuevoEstado);
        if ("OK".equals(res)) {
            JOptionPane.showMessageDialog(this, "Estado del producto cambiado a: " + (nuevoEstado ? "ACTIVO" : "INACTIVO"), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTablaProductos(productoController.listarProductos());
        } else {
            JOptionPane.showMessageDialog(this, res, "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscarProductos() {
        String filtro = jtf_buscar.getText();
        cargarTablaProductos(productoController.buscarProductos(filtro));
    }

    private void seleccionarFilaTabla() {
        int fila = jTable_catalogoGeneralInventario.getSelectedRow();
        if (fila >= 0) {
            idProductoSeleccionado = Integer.parseInt(jTable_catalogoGeneralInventario.getValueAt(fila, 0).toString());
            jtf_codigoProducto.setText(jTable_catalogoGeneralInventario.getValueAt(fila, 1).toString());
            jtf_nombreDescripcion.setText(jTable_catalogoGeneralInventario.getValueAt(fila, 2).toString());
            jcb_categoria.setSelectedItem(jTable_catalogoGeneralInventario.getValueAt(fila, 3).toString());
            jcb_unidadAlmacen.setSelectedItem(jTable_catalogoGeneralInventario.getValueAt(fila, 4).toString());
            jtf_stockActual.setText(jTable_catalogoGeneralInventario.getValueAt(fila, 5).toString());
            jtf_stockMinimo.setText(jTable_catalogoGeneralInventario.getValueAt(fila, 6).toString());

            String fechaTxt = jTable_catalogoGeneralInventario.getValueAt(fila, 7).toString();
            jtf_fechaVencimiento.setText("SIN VENCIMIENTO".equalsIgnoreCase(fechaTxt) ? "" : fechaTxt);

            jtf_precioReferencial.setText(jTable_catalogoGeneralInventario.getValueAt(fila, 8).toString());
            boolean activo = "ACTIVO".equalsIgnoreCase(jTable_catalogoGeneralInventario.getValueAt(fila, 9).toString());
            jcb_estadoProductoCatalogo.setSelected(activo);

            if (Sesion.esAdmin()) {
                jbn_registrar.setEnabled(false);
                jbn_actualizar.setEnabled(true);
                jbn_BajaAltaLogica.setEnabled(true);
            }
        }
    }

    private void limpiarFormulario() {
        idProductoSeleccionado = 0;
        jtf_codigoProducto.setText("");
        jtf_nombreDescripcion.setText("");
        jcb_categoria.setSelectedIndex(0);
        jcb_unidadAlmacen.setSelectedIndex(0);
        jtf_stockActual.setText("0");
        jtf_stockMinimo.setText("5");
        jtf_fechaVencimiento.setText("");
        jtf_precioReferencial.setText("0.00");
        jcb_estadoProductoCatalogo.setSelected(true);
        jTable_catalogoGeneralInventario.clearSelection();

        if (Sesion.esAdmin()) {
            jbn_registrar.setEnabled(true);
            jbn_actualizar.setEnabled(false);
            jbn_BajaAltaLogica.setEnabled(false);
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
        jPanel_fichaTecnicaAbarrote = new javax.swing.JPanel();
        jLabel_codigoProducto = new javax.swing.JLabel();
        jtf_codigoProducto = new javax.swing.JTextField();
        jLabel_nombreDescripcion = new javax.swing.JLabel();
        jtf_nombreDescripcion = new javax.swing.JTextField();
        jLabel_categoria = new javax.swing.JLabel();
        jcb_categoria = new javax.swing.JComboBox<>();
        jLabel_unidadAlmacen = new javax.swing.JLabel();
        jcb_unidadAlmacen = new javax.swing.JComboBox<>();
        jLabel_stockMinimo = new javax.swing.JLabel();
        jLabel_stockActual = new javax.swing.JLabel();
        jtf_stockActual = new javax.swing.JTextField();
        jtf_stockMinimo = new javax.swing.JTextField();
        jLabel_fechaVencimiento = new javax.swing.JLabel();
        jtf_fechaVencimiento = new javax.swing.JTextField();
        jLabel_precioReferencial = new javax.swing.JLabel();
        jtf_precioReferencial = new javax.swing.JTextField();
        jcb_estadoProductoCatalogo = new javax.swing.JCheckBox();
        jbn_registrar = new javax.swing.JButton();
        jbn_actualizar = new javax.swing.JButton();
        jbn_BajaAltaLogica = new javax.swing.JButton();
        jPanel_catalogoGeneralInventario = new javax.swing.JPanel();
        jScrollPane_catalogoGeneralInventario = new javax.swing.JScrollPane();
        jTable_catalogoGeneralInventario = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel_buscar = new javax.swing.JLabel();
        jtf_buscar = new javax.swing.JTextField();
        jbn_buscar = new javax.swing.JButton();
        jbn_verTodos = new javax.swing.JButton();

        jPanel_fondo.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_fondo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel_fondo.setForeground(new java.awt.Color(255, 255, 255));

        jPanel_fichaTecnicaAbarrote.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_fichaTecnicaAbarrote.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ficha técnica del abarrote", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N

        jLabel_codigoProducto.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_codigoProducto.setText("Código de producto:");

        jtf_codigoProducto.setBackground(new java.awt.Color(255, 255, 255));
        jtf_codigoProducto.setForeground(new java.awt.Color(0, 0, 0));

        jLabel_nombreDescripcion.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_nombreDescripcion.setText("Nombre / Descripcion Comercial:");

        jtf_nombreDescripcion.setBackground(new java.awt.Color(255, 255, 255));
        jtf_nombreDescripcion.setForeground(new java.awt.Color(0, 0, 0));
        jtf_nombreDescripcion.addActionListener(this::jtf_nombreDescripcionActionPerformed);

        jLabel_categoria.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_categoria.setText("Categoría:");

        jcb_categoria.setBackground(new java.awt.Color(255, 255, 255));
        jcb_categoria.setForeground(new java.awt.Color(0, 0, 0));
        jcb_categoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Granos", "Lácteos", "Enlatados", "Aceites", "Limpieza", "Embutidos", "Bebidas" }));

        jLabel_unidadAlmacen.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_unidadAlmacen.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_unidadAlmacen.setText("Unidad de almacén (empaque único):");

        jcb_unidadAlmacen.setBackground(new java.awt.Color(255, 255, 255));
        jcb_unidadAlmacen.setForeground(new java.awt.Color(0, 0, 0));
        jcb_unidadAlmacen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SACO 50KG", "CAJA X 24", "PAQUETE X 12", "CAJA X 12", "SACO 15KG", "UNIDAD" }));

        jLabel_stockMinimo.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_stockMinimo.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_stockMinimo.setText("Stock Mínimo:");

        jLabel_stockActual.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_stockActual.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_stockActual.setText("Stock Actual:");

        jtf_stockActual.setBackground(new java.awt.Color(255, 255, 255));
        jtf_stockActual.setForeground(new java.awt.Color(0, 0, 0));
        jtf_stockActual.setText("0");

        jtf_stockMinimo.setBackground(new java.awt.Color(255, 255, 255));
        jtf_stockMinimo.setForeground(new java.awt.Color(0, 0, 0));
        jtf_stockMinimo.setText("5");

        jLabel_fechaVencimiento.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_fechaVencimiento.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_fechaVencimiento.setText("Fecha vencimiento (AAAA-MM-DD o vacío si no vence):");

        jtf_fechaVencimiento.setBackground(new java.awt.Color(255, 255, 255));
        jtf_fechaVencimiento.setForeground(new java.awt.Color(0, 0, 0));

        jLabel_precioReferencial.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_precioReferencial.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_precioReferencial.setText("Precio Referencial Mayorista (S/.):");

        jtf_precioReferencial.setBackground(new java.awt.Color(255, 255, 255));
        jtf_precioReferencial.setForeground(new java.awt.Color(0, 0, 0));
        jtf_precioReferencial.setText("0.00");

        jcb_estadoProductoCatalogo.setBackground(new java.awt.Color(255, 255, 255));
        jcb_estadoProductoCatalogo.setForeground(new java.awt.Color(0, 0, 0));
        jcb_estadoProductoCatalogo.setSelected(true);
        jcb_estadoProductoCatalogo.setText("Producto Activo en Catálogo");

        jbn_registrar.setBackground(new java.awt.Color(255, 255, 255));
        jbn_registrar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbn_registrar.setForeground(new java.awt.Color(0, 0, 0));
        jbn_registrar.setText("Registrar");
        jbn_registrar.addActionListener(this::jbn_registrarActionPerformed);

        jbn_actualizar.setBackground(new java.awt.Color(255, 255, 255));
        jbn_actualizar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbn_actualizar.setForeground(new java.awt.Color(0, 0, 0));
        jbn_actualizar.setText("Actualizar");
        jbn_actualizar.addActionListener(this::jbn_actualizarActionPerformed);

        jbn_BajaAltaLogica.setBackground(new java.awt.Color(255, 255, 255));
        jbn_BajaAltaLogica.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbn_BajaAltaLogica.setForeground(new java.awt.Color(0, 0, 0));
        jbn_BajaAltaLogica.setText("Dar Alta/Baja");
        jbn_BajaAltaLogica.addActionListener(this::jbn_BajaAltaLogicaActionPerformed);

        javax.swing.GroupLayout jPanel_fichaTecnicaAbarroteLayout = new javax.swing.GroupLayout(jPanel_fichaTecnicaAbarrote);
        jPanel_fichaTecnicaAbarrote.setLayout(jPanel_fichaTecnicaAbarroteLayout);
        jPanel_fichaTecnicaAbarroteLayout.setHorizontalGroup(
            jPanel_fichaTecnicaAbarroteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_fichaTecnicaAbarroteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_fichaTecnicaAbarroteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtf_codigoProducto)
                    .addComponent(jtf_nombreDescripcion)
                    .addGroup(jPanel_fichaTecnicaAbarroteLayout.createSequentialGroup()
                        .addGroup(jPanel_fichaTecnicaAbarroteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel_precioReferencial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jtf_fechaVencimiento)
                            .addComponent(jLabel_codigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel_fichaTecnicaAbarroteLayout.createSequentialGroup()
                                .addComponent(jtf_stockActual, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(60, 60, 60)
                                .addComponent(jtf_stockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel_categoria, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcb_categoria, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_unidadAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcb_unidadAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel_fichaTecnicaAbarroteLayout.createSequentialGroup()
                                .addGap(160, 160, 160)
                                .addComponent(jLabel_stockMinimo))
                            .addComponent(jLabel_nombreDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel_stockActual)
                            .addComponent(jLabel_fechaVencimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jtf_precioReferencial)
                            .addComponent(jcb_estadoProductoCatalogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel_fichaTecnicaAbarroteLayout.createSequentialGroup()
                                .addComponent(jbn_registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbn_actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbn_BajaAltaLogica)))
                        .addGap(0, 6, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel_fichaTecnicaAbarroteLayout.setVerticalGroup(
            jPanel_fichaTecnicaAbarroteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_fichaTecnicaAbarroteLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jLabel_codigoProducto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtf_codigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel_nombreDescripcion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jtf_nombreDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel_categoria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jcb_categoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel_unidadAlmacen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jcb_unidadAlmacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel_fichaTecnicaAbarroteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_stockActual)
                    .addComponent(jLabel_stockMinimo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_fichaTecnicaAbarroteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtf_stockActual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtf_stockMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel_fechaVencimiento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtf_fechaVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel_precioReferencial)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtf_precioReferencial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jcb_estadoProductoCatalogo)
                .addGap(37, 37, 37)
                .addGroup(jPanel_fichaTecnicaAbarroteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbn_registrar)
                    .addComponent(jbn_actualizar)
                    .addComponent(jbn_BajaAltaLogica))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel_catalogoGeneralInventario.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_catalogoGeneralInventario.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Catálogo General de Inventario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel_catalogoGeneralInventario.setForeground(new java.awt.Color(153, 153, 153));

        jScrollPane_catalogoGeneralInventario.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane_catalogoGeneralInventario.setForeground(new java.awt.Color(255, 255, 255));

        jTable_catalogoGeneralInventario.setBackground(new java.awt.Color(255, 255, 255));
        jTable_catalogoGeneralInventario.setForeground(new java.awt.Color(0, 0, 0));
        jTable_catalogoGeneralInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Código", "Producto", "Categoria", "Unidad Almacén", "Stock Actual", "Stock Mínimo", "F. Vencimiento", "Precio (S/)", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_catalogoGeneralInventario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_catalogoGeneralInventarioMouseClicked(evt);
            }
        });
        jScrollPane_catalogoGeneralInventario.setViewportView(jTable_catalogoGeneralInventario);

        javax.swing.GroupLayout jPanel_catalogoGeneralInventarioLayout = new javax.swing.GroupLayout(jPanel_catalogoGeneralInventario);
        jPanel_catalogoGeneralInventario.setLayout(jPanel_catalogoGeneralInventarioLayout);
        jPanel_catalogoGeneralInventarioLayout.setHorizontalGroup(
            jPanel_catalogoGeneralInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_catalogoGeneralInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane_catalogoGeneralInventario)
                .addContainerGap())
        );
        jPanel_catalogoGeneralInventarioLayout.setVerticalGroup(
            jPanel_catalogoGeneralInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_catalogoGeneralInventario, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel_buscar.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_buscar.setForeground(new java.awt.Color(0, 0, 0));
        jLabel_buscar.setText("Buscar por Código / Nombre / Categoría:");

        jtf_buscar.setBackground(new java.awt.Color(255, 255, 255));
        jtf_buscar.setForeground(new java.awt.Color(0, 0, 0));

        jbn_buscar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbn_buscar.setForeground(new java.awt.Color(255, 255, 255));
        jbn_buscar.setText("Buscar");
        jbn_buscar.addActionListener(this::jbn_buscarActionPerformed);

        jbn_verTodos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbn_verTodos.setForeground(new java.awt.Color(255, 255, 255));
        jbn_verTodos.setText("Ver Todos");
        jbn_verTodos.addActionListener(this::jbn_verTodosActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_buscar)
                .addGap(18, 18, 18)
                .addComponent(jtf_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbn_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbn_verTodos)
                .addContainerGap(88, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_buscar)
                    .addComponent(jtf_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbn_buscar)
                    .addComponent(jbn_verTodos))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel_fondoLayout = new javax.swing.GroupLayout(jPanel_fondo);
        jPanel_fondo.setLayout(jPanel_fondoLayout);
        jPanel_fondoLayout.setHorizontalGroup(
            jPanel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_fondoLayout.createSequentialGroup()
                .addComponent(jPanel_fichaTecnicaAbarrote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel_catalogoGeneralInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel_fondoLayout.setVerticalGroup(
            jPanel_fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_fichaTecnicaAbarrote, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_fondoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel_catalogoGeneralInventario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_fondo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_fondo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtf_nombreDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtf_nombreDescripcionActionPerformed
        // TODO add your handling code here:
        jcb_categoria.requestFocus();
    }//GEN-LAST:event_jtf_nombreDescripcionActionPerformed

    private void jbn_registrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_registrarActionPerformed
        // TODO add your handling code here:
        registrarProducto();
        
    }//GEN-LAST:event_jbn_registrarActionPerformed

    private void jbn_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_actualizarActionPerformed
        // TODO add your handling code here:
        actualizarProducto();
        
    }//GEN-LAST:event_jbn_actualizarActionPerformed

    private void jbn_BajaAltaLogicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_BajaAltaLogicaActionPerformed
        // TODO add your handling code here:
        cambiarEstadoLogico();
    }//GEN-LAST:event_jbn_BajaAltaLogicaActionPerformed

    private void jTable_catalogoGeneralInventarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_catalogoGeneralInventarioMouseClicked
        // TODO add your handling code here:
        seleccionarFilaTabla();
    }//GEN-LAST:event_jTable_catalogoGeneralInventarioMouseClicked

    private void jbn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_buscarActionPerformed
        // TODO add your handling code here:
        buscarProductos();
        
    }//GEN-LAST:event_jbn_buscarActionPerformed

    private void jbn_verTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbn_verTodosActionPerformed
        // TODO add your handling code here:
        jtf_buscar.setText("");
        cargarTablaProductos(productoController.listarProductos());
        limpiarFormulario();
    }//GEN-LAST:event_jbn_verTodosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel_buscar;
    private javax.swing.JLabel jLabel_categoria;
    private javax.swing.JLabel jLabel_codigoProducto;
    private javax.swing.JLabel jLabel_fechaVencimiento;
    private javax.swing.JLabel jLabel_nombreDescripcion;
    private javax.swing.JLabel jLabel_precioReferencial;
    private javax.swing.JLabel jLabel_stockActual;
    private javax.swing.JLabel jLabel_stockMinimo;
    private javax.swing.JLabel jLabel_unidadAlmacen;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel_catalogoGeneralInventario;
    private javax.swing.JPanel jPanel_fichaTecnicaAbarrote;
    private javax.swing.JPanel jPanel_fondo;
    private javax.swing.JScrollPane jScrollPane_catalogoGeneralInventario;
    private javax.swing.JTable jTable_catalogoGeneralInventario;
    private javax.swing.JButton jbn_BajaAltaLogica;
    private javax.swing.JButton jbn_actualizar;
    private javax.swing.JButton jbn_buscar;
    private javax.swing.JButton jbn_registrar;
    private javax.swing.JButton jbn_verTodos;
    private javax.swing.JComboBox<String> jcb_categoria;
    private javax.swing.JCheckBox jcb_estadoProductoCatalogo;
    private javax.swing.JComboBox<String> jcb_unidadAlmacen;
    private javax.swing.JTextField jtf_buscar;
    private javax.swing.JTextField jtf_codigoProducto;
    private javax.swing.JTextField jtf_fechaVencimiento;
    private javax.swing.JTextField jtf_nombreDescripcion;
    private javax.swing.JTextField jtf_precioReferencial;
    private javax.swing.JTextField jtf_stockActual;
    private javax.swing.JTextField jtf_stockMinimo;
    // End of variables declaration//GEN-END:variables
}
