package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Representa la zona de carga compartida de SpeedFast.
 *
 * Almacena los pedidos pendientes y controla su acceso para evitar que un mismo pedido sea retirado por más de un repartidor.
 *
 * Los pedidos se almacenan en una PriorityBlockingQueue, por lo que se retiran de acuerdo con su prioridad.
 */
public class ZonaDeCarga {

    private final BlockingQueue<Pedido> pedidosPendientes;

    /**
     * Constructor que inicializa la zona de carga.
     */
    public ZonaDeCarga() {
        this.pedidosPendientes = new PriorityBlockingQueue<>();
    }

    /**
     * Agrega un pedido a la zona de carga.
     *
     * @param pedido pedido que será agregado
     */
    public synchronized void agregarPedido(Pedido pedido) {
        pedidosPendientes.add(pedido);

        System.out.println("Pedido #" + pedido.getIdPedido() + " agregado. Destino: " + pedido.getDireccionEntrega());
    }

    /**
     * Retira de forma segura el siguiente pedido disponible.
     *
     * @return siguiente pedido disponible o null si no quedan pedidos
     */
    public synchronized Pedido retirarPedido() {
        return pedidosPendientes.poll();
    }

    /**
     *  Nos permite saber la cantidad de pedidos pendientes.
     * 
     * @return cantidad de pedidos pendientes.
     */
    public synchronized int getCantidadPedidosPendientes() {
        return pedidosPendientes.size();
    }
}