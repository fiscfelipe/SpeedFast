package UI;

import dao.PedidoDAO;
import java.awt.*;
import javax.swing.*;
import model.ControladorDeEnvios;
import model.Pedido;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import model.ZonaDeCarga;

/**
 * Panel utilizado para registrar nuevos pedidos en el sistema SpeedFast.
 *
 * Permite ingresar la dirección, la distancia y el tipo de pedido, validando los datos antes de incorporarlos al sistema y almacenarlos en la base de datos.
 */
public class PanelRegistroPedido extends JPanel {

    private final ControladorDeEnvios controlador;
    private final ZonaDeCarga zonaDeCarga;
    private final PedidoDAO pedidoDAO;

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
        this.pedidoDAO = new PedidoDAO();

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
        JLabel lblDireccion = new JLabel("Dirección:");
        JLabel lblDistancia = new JLabel("Distancia (km):");
        JLabel lblTipo = new JLabel("Tipo:");

        txtDireccion = new JTextField(20);
        txtDistancia = new JTextField(15);

        cmbTipo = new JComboBox<>(new String[]{"COMIDA", "ENCOMIENDA", "EXPRESS"});

        btnGuardar = new JButton("Guardar");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblDireccion, gbc);

        gbc.gridx = 1;
        add(txtDireccion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblDistancia, gbc);

        gbc.gridx = 1;
        add(txtDistancia, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblTipo, gbc);

        gbc.gridx = 1;
        add(cmbTipo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
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
                pedido = new PedidoComida(0, direccion, distanciaKm);
                break;

            case "ENCOMIENDA":
                pedido = new PedidoEncomienda(0, direccion, distanciaKm);
                break;

            case "EXPRESS":
                pedido = new PedidoExpress(0, direccion, distanciaKm);
                break;

            default:
                JOptionPane.showMessageDialog(this, "Tipo de pedido no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }

        if (!pedidoDAO.create(pedido)) {
            JOptionPane.showMessageDialog(this, "No fue posible guardar el pedido en la base de datos.", "Error de base de datos", JOptionPane.ERROR_MESSAGE);
            return;
        }

        controlador.registrarPedido(pedido);
        zonaDeCarga.agregarPedido(pedido);

        JOptionPane.showMessageDialog(this, "Pedido #" + pedido.getIdPedido() + " registrado correctamente.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

        limpiarFormulario();
    }

    /**
     * Limpia los campos del formulario después de registrar un pedido.
     */
    private void limpiarFormulario() {
        txtDireccion.setText("");
        txtDistancia.setText("");
        cmbTipo.setSelectedIndex(0);
        txtDireccion.requestFocus();
    }
}