package model;

/**
 * Representa un pedido de encomienda de SpeedFast.
 */
public class PedidoEncomienda extends Pedido {

    // Constructor

    /**
     * Constructor de la clase PedidoEncomienda.
     *
     * @param idPedido identificador del pedido.
     * @param direccionEntrega dirección donde se realizará la entrega.
     */
    public PedidoEncomienda(int idPedido, String direccionEntrega) {
        super(idPedido, direccionEntrega, "Encomienda");
    }

    // Métodos públicos

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