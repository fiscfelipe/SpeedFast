package UI;

import dao.RepartidorDAO;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Repartidor;

/**
 * Panel para gestionar los repartidores registrados en SpeedFast.
 *
 * Permite crear, consultar, actualizar y eliminar repartidores utilizando la base de datos.
 */
public class PanelGestionRepartidores extends JPanel {

    private final RepartidorDAO repartidorDAO;

    private JTextField txtNombre;
    private JTable tablaRepartidores;
    private DefaultTableModel modeloTabla;
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    private int idSeleccionado = -1;

    /**
     * Constructor del panel de gestión de repartidores.
     */
    public PanelGestionRepartidores() {
        repartidorDAO = new RepartidorDAO();

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

        JLabel lblTitulo = new JLabel("Gestión de repartidores");

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(20);

        btnRegistrar = new JButton("Registrar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelFormulario.add(lblNombre);
        panelFormulario.add(txtNombre);
        panelFormulario.add(btnRegistrar);
        panelFormulario.add(btnActualizar);
        panelFormulario.add(btnEliminar);
        panelFormulario.add(btnLimpiar);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRepartidores = new JTable(modeloTabla);
        tablaRepartidores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabla = new JScrollPane(tablaRepartidores);

        add(scrollTabla, BorderLayout.CENTER);
    }

    /**
     * Configura los eventos de los componentes.
     */
    private void configurarEventos() {

        btnRegistrar.addActionListener(e -> registrarRepartidor());
        btnActualizar.addActionListener(e -> actualizarRepartidor());
        btnEliminar.addActionListener(e -> eliminarRepartidor());
        btnLimpiar.addActionListener(e -> limpiarSeleccion());

        tablaRepartidores.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                cargarRepartidorSeleccionado();
            }
        });
    }

    /**
     * Registra un nuevo repartidor en la base de datos.
     */
    private void registrarRepartidor() {

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes ingresar el nombre del repartidor.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Repartidor repartidor = new Repartidor(0, nombre);

        if (repartidorDAO.create(repartidor)) {
            JOptionPane.showMessageDialog(this, "Repartidor #" + repartidor.getId() + " registrado correctamente.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

            refrescarTabla();
            limpiarSeleccion();

        } else {
            JOptionPane.showMessageDialog(this, "No fue posible registrar el repartidor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza el nombre del repartidor seleccionado.
     */
    private void actualizarRepartidor() {

        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un repartidor de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del repartidor no puede estar vacío.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Repartidor repartidor = new Repartidor(idSeleccionado, nombre);

        if (repartidorDAO.update(repartidor)) {
            JOptionPane.showMessageDialog(this, "Repartidor actualizado correctamente.", "Actualización exitosa", JOptionPane.INFORMATION_MESSAGE);

            refrescarTabla();
            limpiarSeleccion();

        } else {
            JOptionPane.showMessageDialog(this, "No fue posible actualizar el repartidor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina el repartidor seleccionado.
     */
    private void eliminarRepartidor() {

        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un repartidor de la tabla.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar el repartidor seleccionado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        if (repartidorDAO.delete(idSeleccionado)) {
            JOptionPane.showMessageDialog(this, "Repartidor eliminado correctamente.", "Eliminación exitosa", JOptionPane.INFORMATION_MESSAGE);

            refrescarTabla();
            limpiarSeleccion();

        } else {
            JOptionPane.showMessageDialog(this, "No fue posible eliminar el repartidor. Puede tener entregas asociadas.", "Error al eliminar", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga en el formulario el repartidor seleccionado en la tabla.
     */
    private void cargarRepartidorSeleccionado() {

        int fila = tablaRepartidores.getSelectedRow();

        if (fila == -1) {
            return;
        }

        idSeleccionado = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
    }

    /**
     * Actualiza la tabla utilizando los registros almacenados en la base de datos.
     */
    public final void refrescarTabla() {

        modeloTabla.setRowCount(0);

        List<Repartidor> repartidores = repartidorDAO.readAll();

        for (Repartidor repartidor : repartidores) {
            modeloTabla.addRow(new Object[]{repartidor.getId(),repartidor.getNombre()});
        }
    }

    /**
     * Limpia el formulario y la selección actual.
     */
    private void limpiarSeleccion() {

        idSeleccionado = -1;
        txtNombre.setText("");
        tablaRepartidores.clearSelection();
        txtNombre.requestFocus();
    }
}