package model;

import dao.EntregaDAO;
import dao.PedidoDAO;
import java.time.*;

/**
 * Representa a un repartidor de SpeedFast que trabaja en un hilo independiente.
 *
 * Cada repartidor puede estar asociado a un registro de la base de datos mediante su identificador.
 */
public class Repartidor implements Runnable {

    private int id;
    private final String nombre;
    private final ZonaDeCarga zonaDeCarga;
    private final ControladorDeEnvios controlador;
    private final PedidoDAO pedidoDAO;
    private final EntregaDAO entregaDAO;

    /**
     * Constructor utilizado para la ejecución concurrente.
     *
     * @param nombre nombre del repartidor
     * @param zonaDeCarga zona de carga compartida
     * @param controlador controlador de envíos
     */
    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga, ControladorDeEnvios controlador) {
        this.id = 0;
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
        this.controlador = controlador;
        this.pedidoDAO = new PedidoDAO();
        this.entregaDAO = new EntregaDAO();
    }

    /**
     * Constructor utilizado para representar un repartidor recuperado desde la base de datos.
     *
     * @param id identificador del repartidor
     * @param nombre nombre del repartidor
     */
    public Repartidor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.zonaDeCarga = null;
        this.controlador = null;
        this.pedidoDAO = new PedidoDAO();
        this.entregaDAO = new EntregaDAO();
    }

    /**
     * Constructor utilizado para ejecutar un repartidor previamente registrado en la base de datos.
     *
     * @param id identificador del repartidor
     * @param nombre nombre del repartidor
     * @param zonaDeCarga zona de carga compartida
     * @param controlador controlador de envíos
     */
    public Repartidor(int id, String nombre, ZonaDeCarga zonaDeCarga, ControladorDeEnvios controlador) {
        this.id = id;
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
        this.controlador = controlador;
        this.pedidoDAO = new PedidoDAO();
        this.entregaDAO = new EntregaDAO();
    }
    //Setters
    public void setId(int id) {
        this.id = id;
    }
    
    //Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Ejecuta el trabajo del repartidor.
     *
     * Retira pedidos de la zona de carga hasta que no queden pedidos disponibles. 
     * Cada pedido pasa al estado EN_REPARTO y luego a ENTREGADO una vez finalizada la simulación.
     *
     * Los cambios de estado y las entregas realizadas se registran también en la base de datos.
     *
     * Si el hilo es interrumpido durante una entrega, el pedido vuelve a estado PENDIENTE y se reincorpora a la zona de carga.
     */
    @Override
    public void run() {

        Pedido pedido;

        while ((pedido = zonaDeCarga.retirarPedido()) != null) {

            pedido.setRepartidorAsignado(nombre);
            pedido.setEstado(EstadoPedido.EN_REPARTO);

            if (!pedidoDAO.actualizarEstado(pedido)) {
                System.out.println("[Repartidor - " + nombre + "] No fue posible actualizar el pedido #" + pedido.getIdPedido() + " a EN_REPARTO.");
                pedido.setEstado(EstadoPedido.PENDIENTE);
                pedido.setRepartidorAsignado(null);
                zonaDeCarga.devolverPedido(pedido);
                continue;
            }

            System.out.println("[Repartidor - " + nombre + "] Retirando pedido #" + pedido.getIdPedido() + " (" + pedido.getPrioridad() + ")...");
            System.out.println("[Repartidor - " + nombre + "] Estado: " + pedido.getEstado());

            try {

                long tiempoSimulado = pedido.calcularTiempoEntrega() * 100L;

                Thread.sleep(tiempoSimulado);

                pedido.setEstado(EstadoPedido.ENTREGADO);

                Entrega entrega = new Entrega(pedido.getIdPedido(), id, LocalDate.now(), LocalTime.now()
                );

                boolean entregaRegistrada = entregaDAO.registrarEntregaCompleta(pedido, entrega);

                if (entregaRegistrada) {

                    System.out.println("[Repartidor - " + nombre + "] Pedido #" + pedido.getIdPedido() + " entregado.");
                    System.out.println("[Repartidor - " + nombre + "] Estado: " + pedido.getEstado());

                    controlador.registrarEntrega(pedido);

                } else {

                    pedido.setEstado(EstadoPedido.PENDIENTE);
                    pedido.setRepartidorAsignado(null);

                    pedidoDAO.actualizarEstado(pedido);
                    zonaDeCarga.devolverPedido(pedido);

                    System.out.println("[Repartidor - " + nombre + "] No fue posible registrar completamente la entrega del pedido #" + pedido.getIdPedido() + ".");
                    System.out.println("[Repartidor - " + nombre + "] El pedido volvió a estado PENDIENTE.");
                }

            } catch (InterruptedException ex) {

                pedido.setEstado(EstadoPedido.PENDIENTE);
                pedido.setRepartidorAsignado(null);

                pedidoDAO.actualizarEstado(pedido);
                zonaDeCarga.devolverPedido(pedido);

                Thread.currentThread().interrupt();

                System.out.println("[Repartidor - " + nombre + "] fue interrumpido durante la entrega del pedido #" + pedido.getIdPedido() + ". El pedido volvió a estado PENDIENTE.");

                break;
            }
        }

        System.out.println("[Repartidor - " + nombre + "] terminó su trabajo.");
    }
}