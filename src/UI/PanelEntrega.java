package UI;

import dao.PedidoDAO;
import dao.RepartidorDAO;
import java.awt.*;
import javax.swing.*;
import java.util.concurrent.*;
import java.util.List;
import model.ControladorDeEnvios;
import model.EstadoPedido;
import model.Pedido;
import model.Repartidor;
import model.ZonaDeCarga;

/**
 * Panel encargado de ejecutar la simulación concurrente de entregas de SpeedFast.
 */
public class PanelEntrega extends JPanel {

    private final ControladorDeEnvios controlador;
    private final ZonaDeCarga zonaDeCarga;
    private final RepartidorDAO repartidorDAO;
    private final PedidoDAO pedidoDAO;

    private JComboBox<Integer> cmbCantidadRepartidores;
    private JButton btnIniciar;
    private JTextArea txtResultados;

    /**
     * Constructor del panel de entregas.
     *
     * @param controlador controlador general de envíos
     * @param zonaDeCarga zona de carga compartida
     */
    public PanelEntrega(ControladorDeEnvios controlador, ZonaDeCarga zonaDeCarga) {
        this.controlador = controlador;
        this.zonaDeCarga = zonaDeCarga;
        this.repartidorDAO = new RepartidorDAO();
        this.pedidoDAO = new PedidoDAO();

        configurarPanel();
        crearComponentes();
        configurarEventos();
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

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel lblTitulo = new JLabel("Simulación de entregas");
        JLabel lblCantidad = new JLabel("Cantidad de repartidores:");

        cmbCantidadRepartidores = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});

        cmbCantidadRepartidores.setSelectedItem(3);

        btnIniciar = new JButton("Iniciar entregas");

        panelSuperior.add(lblTitulo);
        panelSuperior.add(lblCantidad);
        panelSuperior.add(cmbCantidadRepartidores);
        panelSuperior.add(btnIniciar);

        add(panelSuperior, BorderLayout.NORTH);

        txtResultados = new JTextArea();
        txtResultados.setEditable(false);
        txtResultados.setLineWrap(true);
        txtResultados.setWrapStyleWord(true);

        JScrollPane scrollResultados = new JScrollPane(txtResultados);

        add(scrollResultados, BorderLayout.CENTER);
    }

    /**
     * Configura los eventos del panel.
     */
    private void configurarEventos() {
        btnIniciar.addActionListener(e -> iniciarEntregas());
    }

    /**
     * Inicia la simulación de entregas.
     */
    private void iniciarEntregas() {

        cargarPedidosPendientesDesdeBD();

        if (zonaDeCarga.getCantidadPedidosPendientes() == 0) {
            JOptionPane.showMessageDialog(this, "No hay pedidos pendientes para entregar.", "Zona de carga vacía", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int cantidadRepartidores = (Integer) cmbCantidadRepartidores.getSelectedItem();

        List<Repartidor> repartidores = repartidorDAO.readAll();

        if (repartidores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay repartidores registrados en la base de datos.", "Sin repartidores", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cantidadRepartidores > repartidores.size()) {
            JOptionPane.showMessageDialog(this, "Solo existen " + repartidores.size() + " repartidores registrados.", "Cantidad inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnIniciar.setEnabled(false);
        txtResultados.setText("Iniciando simulación...\n");

        Thread hiloSimulacion = new Thread(() -> ejecutarSimulacion(repartidores, cantidadRepartidores));

        hiloSimulacion.start();
    }

    /**
     * Recupera desde MySQL los pedidos pendientes que todavía no se encuentran cargados en memoria.
     */
    private void cargarPedidosPendientesDesdeBD() {

        List<Pedido> pedidos = pedidoDAO.readAll();

        for (Pedido pedido : pedidos) {

            if (pedido.getEstado() == EstadoPedido.PENDIENTE && !controlador.existePedido(pedido.getIdPedido())) {

                controlador.registrarPedido(pedido);
                zonaDeCarga.agregarPedido(pedido);
            }
        }
    }

    /**
     * Ejecuta los repartidores utilizando un pool de hilos.
     *
     * @param repartidores repartidores disponibles
     * @param cantidad cantidad de repartidores seleccionados
     */
    private void ejecutarSimulacion(List<Repartidor> repartidores, int cantidad) {

        ExecutorService executor = Executors.newFixedThreadPool(cantidad);

        for (int i = 0; i < cantidad; i++) {

            Repartidor repartidorBD = repartidores.get(i);

            Repartidor repartidor = new Repartidor(repartidorBD.getId(), repartidorBD.getNombre(), zonaDeCarga, controlador);

            executor.submit(repartidor);
        }

        executor.shutdown();

        try {

            while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                // Espera hasta que todos los repartidores terminen.
            }

        } catch (InterruptedException ex) {

            executor.shutdownNow();
            Thread.currentThread().interrupt();

            SwingUtilities.invokeLater(() -> {
                txtResultados.append("\nLa simulación fue interrumpida.\n");
                btnIniciar.setEnabled(true);
            });

            return;
        }

        SwingUtilities.invokeLater(() -> {
            mostrarResultados();
            btnIniciar.setEnabled(true);
        });
    }

    /**
     * Muestra el resultado final de los pedidos procesados.
     */
    private void mostrarResultados() {

        List<Pedido> pedidos = controlador.getPedidosRegistrados();

        int entregados = 0;
        int pendientes = 0;

        StringBuilder resultado = new StringBuilder();

        resultado.append("RESULTADO DE LA SIMULACIÓN\n\n");

        for (Pedido pedido : pedidos) {

            resultado.append("Pedido #").append(pedido.getIdPedido()).append(" - ").append(pedido.getEstado());

            if (pedido.getRepartidorAsignado() != null) {
                resultado.append(" - Repartidor: ").append(pedido.getRepartidorAsignado());
            }

            resultado.append("\n");

            if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
                entregados++;
            } else if (pedido.getEstado() == EstadoPedido.PENDIENTE) {
                pendientes++;
            }
        }

        resultado.append("\nPedidos entregados: ").append(entregados);

        resultado.append("\nPedidos pendientes: ").append(pendientes);

        txtResultados.setText(resultado.toString());
    }
}