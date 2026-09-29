package UI;

import java.awt.*;
import javax.swing.*;
import model.ControladorDeEnvios;
import model.ZonaDeCarga;

/**
 * Ventana principal del sistema SpeedFast.
 *
 * Contiene la navegación general de la aplicación y permite cambiar
 * entre las distintas secciones sin abrir nuevas ventanas.
 */
public class VentanaPrincipal extends JFrame {

    private final ControladorDeEnvios controlador;
    private final ZonaDeCarga zonaDeCarga;

    private CardLayout cardLayout;
    private JPanel panelContenido;
    private PanelListaPedidos panelLista;

    private JButton btnInicio;
    private JButton btnRegistrarPedido;
    private JButton btnListarPedidos;
    private JButton btnIniciarEntrega;

    /**
     * Constructor que inicializa la ventana principal y los recursos
     * compartidos utilizados por el sistema.
     */
    public VentanaPrincipal() {
        this.controlador = new ControladorDeEnvios();
        this.zonaDeCarga = new ZonaDeCarga();

        configurarVentana();
        crearComponentes();
    }

    /**
     * Configura las características generales de la ventana.
     */
    private void configurarVentana() {
        setTitle("SpeedFast - Gestión de Entregas");
        setSize(900, 600);
        setMinimumSize(new Dimension(700, 450));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    /**
     * Crea y organiza los componentes principales de la interfaz.
     */
    private void crearComponentes() {
        setLayout(new BorderLayout());

        JPanel panelSuperior = crearPanelSuperior();
        JPanel panelNavegacion = crearPanelNavegacion();

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        PanelInicio panelInicio = new PanelInicio();
        PanelRegistroPedido panelRegistro = new PanelRegistroPedido(controlador, zonaDeCarga);
        panelLista = new PanelListaPedidos(controlador);
        PanelEntrega panelEntrega = new PanelEntrega(controlador, zonaDeCarga);

        panelContenido.add(panelInicio, "INICIO");
        panelContenido.add(panelRegistro, "REGISTRO");
        panelContenido.add(panelLista, "LISTA");
        panelContenido.add(panelEntrega, "ENTREGA");

        add(panelSuperior, BorderLayout.NORTH);
        add(panelNavegacion, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);

        configurarEventos();

        cardLayout.show(panelContenido, "INICIO");
    }

    /**
     * Crea el encabezado superior de la aplicación.
     *
     * @return panel superior
     */
    private JPanel crearPanelSuperior() {
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("SPEEDFAST - Gestión de Entregas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));

        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        return panelSuperior;
    }

    /**
     * Crea el menú lateral utilizado para navegar entre las
     * distintas secciones del sistema.
     *
     * @return panel de navegación
     */
    private JPanel crearPanelNavegacion() {
        JPanel panelNavegacion = new JPanel(new GridLayout(4, 1, 5, 5));
        panelNavegacion.setPreferredSize(new Dimension(210, 0));
        panelNavegacion.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        btnInicio = new JButton("Inicio");
        btnRegistrarPedido = new JButton("Registrar pedido");
        btnListarPedidos = new JButton("Listar pedidos");
        btnIniciarEntrega = new JButton("Iniciar entregas");

        panelNavegacion.add(btnInicio);
        panelNavegacion.add(btnRegistrarPedido);
        panelNavegacion.add(btnListarPedidos);
        panelNavegacion.add(btnIniciarEntrega);

        return panelNavegacion;
    }

    /**
     * Configura las acciones de los botones de navegación.
     */
    private void configurarEventos() {
        btnInicio.addActionListener(e -> cardLayout.show(panelContenido, "INICIO"));

        btnRegistrarPedido.addActionListener(e -> cardLayout.show(panelContenido, "REGISTRO"));

        btnListarPedidos.addActionListener(e -> {
            panelLista.refrescarTabla();
            cardLayout.show(panelContenido, "LISTA");
        });

        btnIniciarEntrega.addActionListener(e -> cardLayout.show(panelContenido, "ENTREGA"));
    }
}