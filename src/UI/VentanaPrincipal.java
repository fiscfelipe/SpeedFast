package UI;

import java.awt.*;
import javax.swing.*;
import model.ControladorDeEnvios;
import model.ZonaDeCarga;

/**
 * Ventana principal de la aplicación SpeedFast.
 *
 * Administra la navegación entre los diferentes paneles utilizando CardLayout.
 */
public class VentanaPrincipal extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel panelContenido;

    private final ControladorDeEnvios controlador;
    private final ZonaDeCarga zonaDeCarga;

    private final PanelListaPedidos panelListaPedidos;
    private final PanelGestionRepartidores panelGestionRepartidores;
    private final PanelGestionEntregas panelGestionEntregas;

    /**
     * Constructor de la ventana principal.
     */
    public VentanaPrincipal() {

        controlador = new ControladorDeEnvios();
        zonaDeCarga = new ZonaDeCarga();

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        configurarVentana();

        PanelInicio panelInicio = new PanelInicio();
        PanelRegistroPedido panelRegistro = new PanelRegistroPedido(controlador, zonaDeCarga);
        panelListaPedidos = new PanelListaPedidos();
        PanelEntrega panelEntrega = new PanelEntrega(controlador, zonaDeCarga);
        panelGestionRepartidores = new PanelGestionRepartidores();
        panelGestionEntregas = new PanelGestionEntregas();

        panelContenido.add(panelInicio, "INICIO");
        panelContenido.add(panelRegistro, "REGISTRO");
        panelContenido.add(panelListaPedidos, "PEDIDOS");
        panelContenido.add(panelEntrega, "SIMULACION");
        panelContenido.add(panelGestionRepartidores, "REPARTIDORES");
        panelContenido.add(panelGestionEntregas, "ENTREGAS");

        add(crearPanelNavegacion(), BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);

        cardLayout.show(panelContenido, "INICIO");
    }

    /**
     * Configura las propiedades generales de la ventana.
     */
    private void configurarVentana() {

        setTitle("SpeedFast");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1000, 650);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        setResizable(true);

        setLayout(new BorderLayout());
    }
    
    /**
    * Configura el tamaño y la alineación de un botón utilizado en el menú lateral de navegación.
    *
    * @param boton botón que será configurado
    */
    private void configurarBotonNavegacion(JButton boton) {
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        boton.setPreferredSize(new Dimension(200, 40));
    }

    /**
     * Crea el panel lateral de navegación.
     *
     * @return panel con los botones principales
     */
    private JPanel crearPanelNavegacion() {

        JPanel panelNavegacion = new JPanel();
        panelNavegacion.setPreferredSize(new Dimension(230, 0));
        panelNavegacion.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        javax.swing.BoxLayout layout = new javax.swing.BoxLayout(panelNavegacion, javax.swing.BoxLayout.Y_AXIS);

        panelNavegacion.setLayout(layout);

        JButton btnInicio = new JButton("Inicio");
        JButton btnRegistrarPedido = new JButton("Registrar pedido");
        JButton btnGestionPedidos = new JButton("Gestionar pedidos");
        JButton btnGestionRepartidores = new JButton("Gestionar repartidores");
        JButton btnGestionEntregas = new JButton("Gestionar entregas");
        JButton btnSimularEntregas = new JButton("Iniciar entregas");

        configurarBotonNavegacion(btnInicio);
        configurarBotonNavegacion(btnRegistrarPedido);
        configurarBotonNavegacion(btnGestionPedidos);
        configurarBotonNavegacion(btnGestionRepartidores);
        configurarBotonNavegacion(btnGestionEntregas);
        configurarBotonNavegacion(btnSimularEntregas);

        btnInicio.addActionListener(e -> cardLayout.show(panelContenido, "INICIO"));

        btnRegistrarPedido.addActionListener(e -> cardLayout.show(panelContenido, "REGISTRO"));

        btnGestionPedidos.addActionListener(e -> {
            panelListaPedidos.refrescarTabla();
            cardLayout.show(panelContenido, "PEDIDOS");
        });

        btnGestionRepartidores.addActionListener(e -> {
            panelGestionRepartidores.refrescarTabla();
            cardLayout.show(panelContenido, "REPARTIDORES");
        });

        btnGestionEntregas.addActionListener(e -> {
            panelGestionEntregas.refrescarDatos();
            cardLayout.show(panelContenido, "ENTREGAS");
        });

        btnSimularEntregas.addActionListener(e -> cardLayout.show(panelContenido, "SIMULACION"));

        panelNavegacion.add(btnInicio);
        panelNavegacion.add(Box.createVerticalStrut(10));
        panelNavegacion.add(btnRegistrarPedido);
        panelNavegacion.add(Box.createVerticalStrut(10));
        panelNavegacion.add(btnGestionPedidos);
        panelNavegacion.add(Box.createVerticalStrut(10));
        panelNavegacion.add(btnGestionRepartidores);
        panelNavegacion.add(Box.createVerticalStrut(10));
        panelNavegacion.add(btnGestionEntregas);
        panelNavegacion.add(Box.createVerticalStrut(10));
        panelNavegacion.add(btnSimularEntregas);
        panelNavegacion.add(Box.createVerticalGlue());

        return panelNavegacion;
    }
}