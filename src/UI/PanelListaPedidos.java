package UI;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.ControladorDeEnvios;
import model.Pedido;

/**
 * Panel utilizado para mostrar los pedidos registrados en SpeedFast.
 *
 * Presenta los pedidos en una tabla y permite actualizar la información
 * para reflejar los cambios realizados en el sistema.
 */
public class PanelListaPedidos extends JPanel {

    private final ControladorDeEnvios controlador;

    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;

    /**
     * Constructor que inicializa el panel de listado de pedidos.
     *
     * @param controlador controlador general de pedidos
     */
    public PanelListaPedidos(ControladorDeEnvios controlador) {
        this.controlador = controlador;

        configurarPanel();
        crearComponentes();
        refrescarTabla();
    }

    /**
     * Configura la distribución general del panel.
     */
    private void configurarPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    }

    /**
     * Crea y organiza los componentes visuales del panel.
     */
    private void crearComponentes() {
        JLabel lblTitulo = new JLabel("Listado de pedidos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));

        String[] columnas = {
            "ID",
            "Dirección",
            "Distancia (km)",
            "Tipo",
            "Estado",
            "Repartidor"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setFillsViewportHeight(true);

        JScrollPane scrollTabla = new JScrollPane(tablaPedidos);

        add(lblTitulo, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
    }

    /**
     * Actualiza el contenido de la tabla utilizando los pedidos
     * actualmente registrados en el controlador.
     */
    public void refrescarTabla() {
        modeloTabla.setRowCount(0);

        List<Pedido> pedidos = controlador.getPedidosRegistrados();

        for (Pedido pedido : pedidos) {

            String repartidor = pedido.getRepartidorAsignado();

            if (repartidor == null || repartidor.isEmpty()) {
                repartidor = "-";
            }

            Object[] fila = {
                pedido.getIdPedido(),
                pedido.getDireccionEntrega(),
                pedido.getDistanciaKm(),
                pedido.getTipoPedido(),
                pedido.getEstado(),
                repartidor
            };

            modeloTabla.addRow(fila);
        }
    }
}