package UI;

import dao.PedidoDAO;
import java.awt.*;
import javax.swing.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;

/**
 * Panel para gestionar los pedidos registrados en SpeedFast.
 *
 * Permite consultar, actualizar y eliminar pedidos almacenados en la base de datos.
 */
public class PanelListaPedidos extends JPanel {

    private final PedidoDAO pedidoDAO;

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;

    private JTextField txtDireccion;
    private JTextField txtDistancia;
    private JComboBox<String> cmbTipo;

    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private int idSeleccionado = -1;

    /**
     * Constructor del panel de gestión de pedidos.
     */
    public PanelListaPedidos() {
        pedidoDAO = new PedidoDAO();

        configurarPanel();
        crearComponentes();
        configurarEventos();
        refrescarTabla();
    }

    /**
     * Configura la distribución general del panel.
     */
    private void configurarPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /**
     * Crea y organiza los componentes gráficos.
     */
    private void crearComponentes() {

        JLabel lblTitulo = new JLabel("Gestión de pedidos");

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new javax.swing.BoxLayout(panelFormulario, javax.swing.BoxLayout.Y_AXIS));

        JPanel panelDatos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel lblDireccion = new JLabel("Dirección:");
        JLabel lblDistancia = new JLabel("Distancia:");
        JLabel lblTipo = new JLabel("Tipo:");
        JLabel lblEstado = new JLabel("Estado:");

        txtDireccion = new JTextField(15);
        txtDistancia = new JTextField(8);

        cmbTipo = new JComboBox<>(new String[]{"COMIDA", "ENCOMIENDA", "EXPRESS"});


        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelDatos.add(lblDireccion);
        panelDatos.add(txtDireccion);
        panelDatos.add(lblDistancia);
        panelDatos.add(txtDistancia);
        panelDatos.add(lblTipo);
        panelDatos.add(cmbTipo);
        panelDatos.add(lblEstado);

        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        panelFormulario.add(panelDatos);
        panelFormulario.add(panelBotones);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Dirección", "Distancia", "Tipo", "Estado"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabla = new JScrollPane(tablaPedidos);

        add(scrollTabla, BorderLayout.CENTER);
    }

    /**
     * Configura los eventos de los componentes.
     */
    private void configurarEventos() {

        btnActualizar.addActionListener(e -> actualizarPedido());
        btnEliminar.addActionListener(e -> eliminarPedido());
        btnLimpiar.addActionListener(e -> limpiarSeleccion());

        tablaPedidos.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                cargarPedidoSeleccionado();
            }
        });
    }

    /**
     * Actualiza los datos del pedido seleccionado.
     */
    private void actualizarPedido() {

        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un pedido de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String direccion = txtDireccion.getText().trim();
        String textoDistancia = txtDistancia.getText().trim().replace(",", ".");

        if (direccion.isEmpty() || textoDistancia.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes completar todos los campos.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double distanciaKm;

        try {
            distanciaKm = Double.parseDouble(textoDistancia);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La distancia debe ser un número válido.", "Distancia inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (distanciaKm <= 0) {
            JOptionPane.showMessageDialog(this, "La distancia debe ser mayor que 0.", "Distancia inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipo = cmbTipo.getSelectedItem().toString();

        Pedido pedido;

        switch (tipo) {
            case "COMIDA":
                pedido = new PedidoComida(idSeleccionado, direccion, distanciaKm);
                break;

            case "ENCOMIENDA":
                pedido = new PedidoEncomienda(idSeleccionado, direccion, distanciaKm);
                break;

            case "EXPRESS":
                pedido = new PedidoExpress(idSeleccionado, direccion, distanciaKm);
                break;

            default:
                JOptionPane.showMessageDialog(this, "Tipo de pedido no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }


        if (pedidoDAO.update(pedido)) {
            JOptionPane.showMessageDialog(this, "Pedido actualizado correctamente.", "Actualización exitosa", JOptionPane.INFORMATION_MESSAGE);

            refrescarTabla();
            limpiarSeleccion();

        } else {
            JOptionPane.showMessageDialog(this, "No fue posible actualizar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina el pedido seleccionado.
     */
    private void eliminarPedido() {

        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un pedido de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar el pedido seleccionado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        if (pedidoDAO.delete(idSeleccionado)) {
            JOptionPane.showMessageDialog(this, "Pedido eliminado correctamente.", "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);

            refrescarTabla();
            limpiarSeleccion();

        } else {
            JOptionPane.showMessageDialog(this, "No fue posible eliminar el pedido. Puede tener entregas asociadas.", "Error al eliminar", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga en el formulario los datos del pedido seleccionado en la tabla.
     */
    private void cargarPedidoSeleccionado() {

        int fila = tablaPedidos.getSelectedRow();

        if (fila == -1) {
            return;
        }

        idSeleccionado = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

        txtDireccion.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtDistancia.setText(modeloTabla.getValueAt(fila, 2).toString());

        cmbTipo.setSelectedItem(modeloTabla.getValueAt(fila, 3).toString());

    }

    /**
     * Actualiza la tabla con los pedidos almacenados en la base de datos.
     */
    public final void refrescarTabla() {

        modeloTabla.setRowCount(0);

        List<Pedido> pedidos = pedidoDAO.readAll();

        for (Pedido pedido : pedidos) {
            modeloTabla.addRow(new Object[]{pedido.getIdPedido(), pedido.getDireccionEntrega(), pedido.getDistanciaKm(), pedido.getTipoPedido(), pedido.getEstado()});
        }
    }

    /**
     * Limpia el formulario y la selección actual.
     */
    private void limpiarSeleccion() {

        idSeleccionado = -1;
        txtDireccion.setText("");
        txtDistancia.setText("");
        cmbTipo.setSelectedIndex(0);

        tablaPedidos.clearSelection();
        txtDireccion.requestFocus();
    }
}