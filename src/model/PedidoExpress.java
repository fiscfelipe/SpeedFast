package model;

/**
 * Representa un pedido de compra express de SpeedFast.
 */
public class PedidoExpress extends Pedido {

    // Constructor

    /**
     * Constructor de la clase PedidoExpress.
     *
     * @param idPedido identificador del pedido.
     * @param direccionEntrega dirección donde se realizará la entrega.
     */
    public PedidoExpress(int idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega, "Compra Express");
    }

    // Métodos públicos

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