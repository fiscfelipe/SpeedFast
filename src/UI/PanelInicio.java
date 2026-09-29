package UI;

import java.awt.*;
import javax.swing.*;

/**
 * Panel de inicio del sistema SpeedFast.
 *
 * Muestra una bienvenida al usuario y una breve indicación
 * sobre las funciones disponibles en la aplicación.
 */
public class PanelInicio extends JPanel {

    /**
     * Constructor que inicializa el panel de inicio.
     */
    public PanelInicio() {
        configurarPanel();
        crearComponentes();
    }

    /**
     * Configura la distribución general del panel.
     */
    private void configurarPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
    }

    /**
     * Crea y organiza los componentes visuales del panel.
     */
    private void crearComponentes() {
        JLabel lblTitulo = new JLabel("Bienvenido a SpeedFast", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel lblDescripcion = new JLabel(
                "<html><div style='text-align: center;'>"
                + "Utiliza el menú lateral para registrar pedidos, consultar el listado "
                + "y gestionar las entregas."
                + "</div></html>",
                SwingConstants.CENTER
        );

        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 16));

        add(lblTitulo, BorderLayout.CENTER);
        add(lblDescripcion, BorderLayout.SOUTH);
    }
}