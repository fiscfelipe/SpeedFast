package model;

/**
 * Representa un pedido de encomienda dentro del sistema SpeedFast.
 *
 * Hereda los atributos y métodos generales de la clase Pedido.
 */
public class PedidoEncomienda extends Pedido {

    // Constructor

    /**
     * Constructor de la clase PedidoEncomienda.
     *
     * @param idPedido identificador del pedido.
     * @param direccionEntrega dirección donde se realizará la entrega.
     * @param distanciaKm distancia en kilómetros hasta el lugar de entrega.
     */
    public PedidoEncomienda(int idPedido, String direccionEntrega, double distanciaKm) {
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
        return (int) Math.round(20 + (1.5 * getDistanciaKm()));
    }

    /**
     * Asigna un repartidor al pedido de encomienda.
     *
     * @param nombreRepartidor nombre del repartidor asignado.
     */
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("[Pedido Encomienda]");
        super.asignarRepartidor();
        System.out.println("→ Validando peso y embalaje... OK");
        System.out.println("→ Pedido asignado a " + nombreRepartidor);
    }
}