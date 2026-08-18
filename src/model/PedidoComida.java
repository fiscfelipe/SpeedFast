package model;

/**
 * Representa un pedido de comida de SpeedFast.
 */
public class PedidoComida extends Pedido {

    // Constructor

    /**
     * Constructor de la clase PedidoComida.
     *
     * @param idPedido identificador del pedido.
     * @param direccionEntrega dirección donde se realizará la entrega.
     */
    public PedidoComida(int idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega, "Comida");
    }

    // Métodos públicos

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