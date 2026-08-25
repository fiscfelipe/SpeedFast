package model;

/**
 * Representa un pedido de comida dentro del sistema SpeedFast.
 *
 * Hereda los atributos y métodos generales de la clase Pedido.
 */
public class PedidoComida extends Pedido {

    // Constructor

    /**
     * Constructor de la clase PedidoComida.
     *
     * @param idPedido identificador del pedido.
     * @param direccionEntrega dirección donde se realizará la entrega.
     * @param distanciaKm distancia en kilómetros hasta el lugar de entrega.
     */
    public PedidoComida(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm);
    }

    // Métodos públicos

    /**
     * Calcula el tiempo estimado de entrega para un pedido de comida.
     *
     * El tiempo corresponde a 15 minutos base más 2 minutos por cada kilómetro de distancia.
     * @return tiempo estimado de entrega en minutos.
     */
    @Override
    public int calcularTiempoEntrega() {
        return 15 + (int) (2 * getDistanciaKm());
    }
    
    
    /**
     * Asigna un repartidor al pedido de comida.
     *
     * @param nombreRepartidor nombre del repartidor asignado.
     */
    public void asignarRepartidor(String nombreRepartidor) {
        System.out.println("[Pedido Comida]");
        super.asignarRepartidor();
        System.out.println("→ Verificando mochila térmica... OK");
        System.out.println("→ Pedido asignado a " + nombreRepartidor);
    }
}