package model;

/**
 * Representa un pedido express dentro del sistema SpeedFast.
 */
public class PedidoExpress extends Pedido {

    /**
     * Constructor de un pedido express.
     *
     * @param idPedido identificador único del pedido
     * @param direccionEntrega dirección donde se realizará la entrega
     * @param distanciaKm distancia en kilómetros hasta el lugar de entrega
     */
    public PedidoExpress(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm, PrioridadPedido.ALTA);
    }

    /**
     * Calcula el tiempo estimado de entrega.
     * Considera 8 minutos base más 1 minuto por kilómetro.
     *
     * @return tiempo estimado de entrega en minutos
     */
    @Override
    public int calcularTiempoEntrega() {
        return 8 + (int) getDistanciaKm();
    }

    /**
     * Muestra el resumen del pedido indicando su tipo.
     */
    @Override
    public void mostrarResumen() {
        System.out.println("[Pedido Express]");
        super.mostrarResumen();
    }
}