package app;

import UI.VentanaPrincipal;
import javax.swing.SwingUtilities;

/**
 * Clase principal del sistema SpeedFast.
 *
 * Inicia la interfaz gráfica de la aplicación.
 */
public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}