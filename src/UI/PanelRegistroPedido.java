package UI;

import java.awt.*;
import javax.swing.*;
import model.*;

/**
 * Panel utilizado para registrar nuevos pedidos en el sistema SpeedFast.
 *
 * Permite ingresar el ID, la dirección, la distancia y el tipo de pedido,
 * validando los datos antes de incorporarlos al sistema.
 */
public class PanelRegistroPedido extends JPanel {

    private final ControladorDeEnvios controlador;
    private final ZonaDeCarga zonaDeCarga;

    private JTextField txtId;
    private JTextField txtDireccion;
    private JTextField txtDistancia;
    private JComboBox<String> cmbTipo;
    private JButton btnGuardar;

    /**
     * Constructor que inicializa el panel de registro.
     *
     * @param controlador controlador general de pedidos
     * @param zonaDeCarga zona de carga compartida
     */
    public PanelRegistroPedido(ControladorDeEnvios controlador, ZonaDeCarga zonaDeCarga) {
        this.controlador = controlador;
        this.zonaDeCarga = zonaDeCarga;

        configurarPanel();
        crearComponentes();
        configurarEventos();
    }

    /**
     * Configura la distribución general del panel.
     */
    private void configurarPanel() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
    }

    /**
     * Crea y organiza los componentes visuales del formulario.
     */
    private void crearComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Registrar pedido");

        JLabel lblId = new JLabel("ID:");
        JLabel lblDireccion = new JLabel("Dirección:");
        JLabel lblDistancia = new JLabel("Distancia (km):");
        JLabel lblTipo = new JLabel("Tipo:");

        txtId = new JTextField(15);
        txtDireccion = new JTextField(20);
        txtDistancia = new JTextField(15);

        cmbTipo = new JComboBox<>(new String[]{
            "COMIDA",
            "ENCOMIENDA",
            "EXPRESS"
        });

        btnGuardar = new JButton("Guardar");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblId, gbc);

        gbc.gridx = 1;
        add(txtId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblDireccion, gbc);

        gbc.gridx = 1;
        add(txtDireccion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblDistancia, gbc);

        gbc.gridx = 1;
        add(txtDistancia, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(lblTipo, gbc);

        gbc.gridx = 1;
        add(cmbTipo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        add(btnGuardar, gbc);
    }

    /**
     * Configura la acción del botón Guardar.
     */
    private void configurarEventos() {
        btnGuardar.addActionListener(e -> guardarPedido());
    }

    /**
     * Valida los datos ingresados y registra un nuevo pedido.
     */
    private void guardarPedido() {
        String textoId = txtId.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String textoDistancia = txtDistancia.getText().trim().replace(",", ".");

        if (textoId.isEmpty() || direccion.isEmpty() || textoDistancia.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes completar todos los campos.", "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPedido;

        try {
            idPedido = Integer.parseInt(textoId);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero.", "ID inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (idPedido <= 0) {
            JOptionPane.showMessageDialog(this, "El ID debe ser mayor que 0.", "ID inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (controlador.existePedido(idPedido)) {
            JOptionPane.showMessageDialog(this, "Ya existe un pedido con ese ID.", "ID duplicado", JOptionPane.WARNING_MESSAGE);
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
                pedido = new PedidoComida(idPedido, direccion, distanciaKm);
                break;

            case "ENCOMIENDA":
                pedido = new PedidoEncomienda(idPedido, direccion, distanciaKm);
                break;

            case "EXPRESS":
                pedido = new PedidoExpress(idPedido, direccion, distanciaKm);
                break;

            default:
                JOptionPane.showMessageDialog(this, "Tipo de pedido no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        controlador.registrarPedido(pedido);
        zonaDeCarga.agregarPedido(pedido);

        JOptionPane.showMessageDialog(this, "Pedido registrado correctamente.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

        limpiarFormulario();
    }

    /**
     * Limpia los campos del formulario después de registrar un pedido.
     */
    private void limpiarFormulario() {
        txtId.setText("");
        txtDireccion.setText("");
        txtDistancia.setText("");
        cmbTipo.setSelectedIndex(0);
        txtId.requestFocus();
    }
}