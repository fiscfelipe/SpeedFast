package model;

/**
 * Representa un pedido de compra express de SpeedFast.
 *
 * Hereda los atributos y métodos generales de la clase Pedido.
 */
public class PedidoExpress extends Pedido {

    // Constructor

    /**
     * Constructor de la clase PedidoExpress.
     *
     * @param idPedido identificador del pedido.
     * @param direccionEntrega dirección donde se realizará la entrega.
     * @param distanciaKm distancia en kilómetros hasta el lugar de entrega.
     */
    public PedidoExpress(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    // Métodos públicos
    
     /**
     * Calcula el tiempo estimado de entrega para un pedido de encomienda.
     *
     * El tiempo corresponde a 20 minutos base más 1,5 minutos por cada kilómetro de distancia.
     * @return tiempo estimado de entrega en minutos.
     */
    @Override
    public int calcularTiempoEntrega() {
        if (getDistanciaKm() > 5) {
            return 10 + 5;
        } 
        return 10;
    }

    /**
     * Asigna un repartidor al pedido de compra express.
     *
     * @param nombreRepartidor nombre del repartidor asignado.
     */
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("[Pedido Express]");
        super.asignarRepartidor();
        System.out.println("→ Repartidor más cercano con disponibilidad inmediata encontrado.");
        System.out.println("→ Pedido asignado a " + nombreRepartidor);
    }
}