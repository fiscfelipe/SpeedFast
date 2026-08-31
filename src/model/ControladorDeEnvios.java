package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Administrador central del sistema encargado de procesar los despachos, cancelaciones y el almacenamiento del historial de entregas realizadas.
 */
public class ControladorDeEnvios implements Despachable, Cancelable, Rastreable {
    private Pedido pedidoActual;
    private List<Pedido> historialEntregas;

     
    /**
     * Constructor que inicializa la lista interna de historial.
     */
    public ControladorDeEnvios() {
        this.historialEntregas = new ArrayList<>();
    } 
    
    // Métodos públicos
    
    /**
     * Establece el pedido que se procesara en las operaciones.
     * 
     * @param pedido pedido a gestionar
     */
    public void setPedidoActual(Pedido pedido) {
        this.pedidoActual = pedido;
    }
    
    /**
     * Establece o sobrescribe la lista del historial de entregas.
     * 
     * @param historialEntregas Nueva lista de historial
     */
    public void setHistorialEntregas(List<Pedido> historialEntregas) {
        this.historialEntregas = historialEntregas;
    }
    
    /**
     * Obtiene el pedido que se encuentra en procesamiento.
     * 
     * @return Pedido en procesamiento.
     */
    public Pedido getPedidoActual() {
        return pedidoActual;
    }

    /**
     * Obtiene la lista completa de pedidos registrados en el historial de entrega.
     * 
     * @return Lista con los pedidos completados
     */
    public List<Pedido> getHistorialEntregas() {
        return historialEntregas;
    }

    
    /**
     * Cambia el estado del pedido actual a Entregado y lo agrega a la lista de historial.
     */
    @Override
    public void despachar() {
        if (pedidoActual != null) {
            pedidoActual.setEstado("Entregado");
            System.out.println("Pedido despachado correctamente.");
            historialEntregas.add(pedidoActual); 
        }
    }

    /**
     * Modifica el estado del pedido actual a Cancelado emitiendo su respectiva alerta.
     */
    @Override
    public void cancelar() {
        if (pedidoActual != null) {
            pedidoActual.setEstado("Cancelado");
            System.out.println("Cancelando Pedido Express #" + pedidoActual.getIdPedido() + "...");
            System.out.println("→ Pedido cancelado exitosamente.");
        }
    }

    /**
     * Imprime en pantalla de forma ordenada todos los pedidos que fueron despachados.
     */
    @Override
    public void verHistorial() {
        System.out.println("Historial:");
        if (!historialEntregas.isEmpty()) {
            for (Pedido p : historialEntregas) {
                System.out.println("- " + p.getClass().getSimpleName() + " #" + p.getIdPedido() + " - entregado por " + p.getRepartidorAsignado());
            }
        }
    }

}
