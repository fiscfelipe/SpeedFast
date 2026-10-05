package UI;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import java.awt.*;
import javax.swing.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.Entrega;
import model.Pedido;
import model.Repartidor;

/**
 * Panel para gestionar las entregas registradas en SpeedFast.
 *
 * Permite crear, consultar, actualizar y eliminar entregas, seleccionando pedidos y repartidores almacenados en la base de datos.
 */
public class PanelGestionEntregas extends JPanel {

    private final EntregaDAO entregaDAO;
    private final PedidoDAO pedidoDAO;
    private final RepartidorDAO repartidorDAO;

    private JComboBox<PedidoItem> cmbPedido;
    private JComboBox<RepartidorItem> cmbRepartidor;
    private JTextField txtFecha;
    private JTextField txtHora;

    private JTable tablaEntregas;
    private DefaultTableModel modeloTabla;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private int idSeleccionado = -1;

    /**
     * Constructor del panel de gestión de entregas.
     */
    public PanelGestionEntregas() {
        entregaDAO = new EntregaDAO();
        pedidoDAO = new PedidoDAO();
        repartidorDAO = new RepartidorDAO();

        configurarPanel();
        crearComponentes();
        configurarEventos();
        refrescarDatos();
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

        JLabel lblTitulo = new JLabel("Gestión de entregas");

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new javax.swing.BoxLayout(panelFormulario, javax.swing.BoxLayout.Y_AXIS));

        JPanel panelDatos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel lblPedido = new JLabel("Pedido:");
        JLabel lblRepartidor = new JLabel("Repartidor:");
        JLabel lblFecha = new JLabel("Fecha:");
        JLabel lblHora = new JLabel("Hora:");

        cmbPedido = new JComboBox<>();
        cmbRepartidor = new JComboBox<>();

        txtFecha = new JTextField(10);
        txtHora = new JTextField(8);

        txtFecha.setToolTipText("Formato: AAAA-MM-DD");
        txtHora.setToolTipText("Formato: HH:MM:SS");

        btnRegistrar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelDatos.add(lblPedido);
        panelDatos.add(cmbPedido);
        panelDatos.add(lblRepartidor);
        panelDatos.add(cmbRepartidor);
        panelDatos.add(lblFecha);
        panelDatos.add(txtFecha);
        panelDatos.add(lblHora);
        panelDatos.add(txtHora);

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        panelFormulario.add(panelDatos);
        panelFormulario.add(panelBotones);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Pedido", "Repartidor", "Fecha", "Hora"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEntregas = new JTable(modeloTabla);
        tablaEntregas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabla = new JScrollPane(tablaEntregas);

        add(scrollTabla, BorderLayout.CENTER);
    }

    /**
     * Configura los eventos de los componentes.
     */
    private void configurarEventos() {

        btnRegistrar.addActionListener(e -> registrarEntrega());
        btnActualizar.addActionListener(e -> actualizarEntrega());
        btnEliminar.addActionListener(e -> eliminarEntrega());
        btnLimpiar.addActionListener(e -> limpiarSeleccion());

        tablaEntregas.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                cargarEntregaSeleccionada();
            }
        });
    }

    /**
     * Registra una nueva entrega.
     */
    private void registrarEntrega() {

        Entrega entrega = obtenerEntregaFormulario();

        if (entrega == null) {
            return;
        }

        if (entregaDAO.create(entrega)) {
            JOptionPane.showMessageDialog(this, "Entrega #" + entrega.getId() + " registrada correctamente.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

            refrescarDatos();
            limpiarSeleccion();

        } else {
            JOptionPane.showMessageDialog(this, "No fue posible registrar la entrega.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza la entrega seleccionada.
     */
    private void actualizarEntrega() {

        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar una entrega de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Entrega datos = obtenerEntregaFormulario();

        if (datos == null) {
            return;
        }

        Entrega entrega = new Entrega(idSeleccionado, datos.getIdPedido(), datos.getIdRepartidor(), datos.getFecha(), datos.getHora());

        if (entregaDAO.update(entrega)) {
            JOptionPane.showMessageDialog(this, "Entrega actualizada correctamente.", "Actualización exitosa", JOptionPane.INFORMATION_MESSAGE);

            refrescarDatos();
            limpiarSeleccion();

        } else {
            JOptionPane.showMessageDialog(this, "No fue posible actualizar la entrega.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina la entrega seleccionada.
     */
    private void eliminarEntrega() {

        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar una entrega de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar la entrega seleccionada?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        if (entregaDAO.delete(idSeleccionado)) {
            JOptionPane.showMessageDialog(this, "Entrega eliminada correctamente.", "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);

            refrescarDatos();
            limpiarSeleccion();

        } else {
            JOptionPane.showMessageDialog(this, "No fue posible eliminar la entrega.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene y valida los datos ingresados en el formulario.
     *
     * @return entrega construida o null si los datos son inválidos
     */
    private Entrega obtenerEntregaFormulario() {

        PedidoItem pedidoItem = (PedidoItem) cmbPedido.getSelectedItem();
        RepartidorItem repartidorItem = (RepartidorItem) cmbRepartidor.getSelectedItem();

        if (pedidoItem == null || repartidorItem == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un pedido y un repartidor.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String textoFecha = txtFecha.getText().trim();
        String textoHora = txtHora.getText().trim();

        if (textoFecha.isEmpty() || textoHora.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes ingresar la fecha y la hora.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        LocalDate fecha;
        LocalTime hora;

        try {
            fecha = LocalDate.parse(textoFecha);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "La fecha debe tener formato AAAA-MM-DD.", "Fecha inválida", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        try {
            hora = LocalTime.parse(textoHora);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "La hora debe tener formato HH:MM o HH:MM:SS.", "Hora inválida", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return new Entrega(pedidoItem.getId(), repartidorItem.getId(), fecha, hora);
    }

    /**
     * Carga los datos de la entrega seleccionada en el formulario.
     */
    private void cargarEntregaSeleccionada() {

        int fila = tablaEntregas.getSelectedRow();

        if (fila == -1) {
            return;
        }

        idSeleccionado = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        int idPedido = Integer.parseInt(modeloTabla.getValueAt(fila, 1).toString());
        int idRepartidor = Integer.parseInt(modeloTabla.getValueAt(fila, 2).toString());

        seleccionarPedido(idPedido);
        seleccionarRepartidor(idRepartidor);
        
        cmbPedido.setEnabled(false);

        txtFecha.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtHora.setText(modeloTabla.getValueAt(fila, 4).toString());
    }

    /**
     * Actualiza la tabla y los JComboBox utilizando la información almacenada en la base de datos.
     */
    public final void refrescarDatos() {
        refrescarPedidos();
        refrescarRepartidores();
        refrescarTabla();
    }

    /**
     * Carga los pedidos desde la base de datos.
     */
    private void refrescarPedidos() {

        cmbPedido.removeAllItems();

        List<Pedido> pedidos = pedidoDAO.readAll();

        for (Pedido pedido : pedidos) {
            cmbPedido.addItem(new PedidoItem(pedido.getIdPedido(), pedido.getDireccionEntrega()));
        }
    }

    /**
     * Carga los repartidores desde la base de datos.
     */
    private void refrescarRepartidores() {

        cmbRepartidor.removeAllItems();

        List<Repartidor> repartidores = repartidorDAO.readAll();

        for (Repartidor repartidor : repartidores) {
            cmbRepartidor.addItem(new RepartidorItem(repartidor.getId(), repartidor.getNombre()));
        }
    }

    /**
     * Actualiza la tabla de entregas.
     */
    private void refrescarTabla() {

        modeloTabla.setRowCount(0);

        List<Entrega> entregas = entregaDAO.readAll();

        for (Entrega entrega : entregas) {
            modeloTabla.addRow(new Object[]{entrega.getId(), entrega.getIdPedido(), entrega.getIdRepartidor(), entrega.getFecha(), entrega.getHora()});
        }
    }

    /**
     * Selecciona en el JComboBox el pedido correspondiente al ID.
     */
    private void seleccionarPedido(int idPedido) {

        for (int i = 0; i < cmbPedido.getItemCount(); i++) {

            PedidoItem item = cmbPedido.getItemAt(i);

            if (item.getId() == idPedido) {
                cmbPedido.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     * Selecciona en el JComboBox el repartidor correspondiente al ID.
     */
    private void seleccionarRepartidor(int idRepartidor) {

        for (int i = 0; i < cmbRepartidor.getItemCount(); i++) {

            RepartidorItem item = cmbRepartidor.getItemAt(i);

            if (item.getId() == idRepartidor) {
                cmbRepartidor.setSelectedIndex(i);
                return;
            }
        }
    }

    /**
     * Limpia el formulario y la selección actual.
     */
    private void limpiarSeleccion() {

        idSeleccionado = -1;
        
        cmbPedido.setEnabled(true);

        if (cmbPedido.getItemCount() > 0) {
            cmbPedido.setSelectedIndex(0);
        }

        if (cmbRepartidor.getItemCount() > 0) {
            cmbRepartidor.setSelectedIndex(0);
        }

        txtFecha.setText(LocalDate.now().toString());
        txtHora.setText(LocalTime.now().withNano(0).toString());

        tablaEntregas.clearSelection();
    }

    /**
     * Elemento utilizado para mostrar pedidos en el JComboBox conservando internamente su identificador.
     */
    private static class PedidoItem {

        private final int id;
        private final String direccion;

        public PedidoItem(int id, String direccion) {
            this.id = id;
            this.direccion = direccion;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return id + " - " + direccion;
        }
    }

    /**
     * Elemento utilizado para mostrar repartidores en el JComboBox conservando internamente su identificador.
     */
    private static class RepartidorItem {

        private final int id;
        private final String nombre;

        public RepartidorItem(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return id + " - " + nombre;
        }
    }
}