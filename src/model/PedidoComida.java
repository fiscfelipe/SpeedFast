package model;

/**
 * Representa un pedido de comida dentro del sistema SpeedFast.
 */
public class PedidoComida extends Pedido {

    /**
     * Constructor de un pedido de comida.
     *
     * @param idPedido identificador único del pedido
     * @param direccionEntrega dirección donde se realizará la entrega
     * @param distanciaKm distancia en kilómetros hasta el lugar de entrega
     */
    public PedidoComida(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm, PrioridadPedido.MEDIA);
    }

    /**
     * Calcula el tiempo estimado de entrega.
     * Considera 15 minutos base más 2 minutos por kilómetro.
     *
     * @return tiempo estimado de entrega en minutos
     */
    @Override
    public int calcularTiempoEntrega() {
        return 15 + (int) (2 * getDistanciaKm());
    }

    /**
     * Muestra el resumen del pedido indicando su tipo.
     */
    @Override
    public void mostrarResumen() {
        System.out.println("[Pedido Comida]");
        super.mostrarResumen();
    }
}