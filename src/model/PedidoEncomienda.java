package model;

/**
 * Representa un pedido de encomienda dentro del sistema SpeedFast.
 */
public class PedidoEncomienda extends Pedido {

    /**
     * Constructor de un pedido de encomienda.
     *
     * @param idPedido identificador único del pedido
     * @param direccionEntrega dirección donde se realizará la entrega
     * @param distanciaKm distancia en kilómetros hasta el lugar de entrega
     */
    public PedidoEncomienda(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm, PrioridadPedido.BAJA);
    }

    /**
     * Calcula el tiempo estimado de entrega.
     * Considera 20 minutos base más 1,5 minutos por kilómetro.
     *
     * @return tiempo estimado de entrega en minutos
     */
    @Override
    public int calcularTiempoEntrega() {
        return (int) Math.round(20 + (1.5 * getDistanciaKm()));
    }

    /**
     * Muestra el resumen del pedido indicando su tipo.
     */
    @Override
    public void mostrarResumen() {
        System.out.println("[Pedido Encomienda]");
        super.mostrarResumen();
    }
}