package model;

/**
 * Representa a un repartidor de SpeedFast que trabaja
 * en un hilo independiente.
 *
 * Cada repartidor retira pedidos desde una zona de carga compartida y registra las entregas realizadas.
 */
public class Repartidor implements Runnable {

    private final String nombre;
    private final ZonaDeCarga zonaDeCarga;
    private final ControladorDeEnvios controlador;

    /**
     * Constructor que inicializa los datos del repartidor.
     *
     * @param nombre nombre del repartidor
     * @param zonaDeCarga zona de carga compartida desde donde retira pedidos
     * @param controlador controlador utilizado para registrar las entregas
     */
    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga, ControladorDeEnvios controlador) {

        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
        this.controlador = controlador;
    }

    // Getters

    public String getNombre() {
        return nombre;
    }

    /**
     * Ejecuta el trabajo del repartidor.
     *
     * Retira pedidos de la zona de carga hasta que no queden pedidos disponibles. 
     * Cada pedido pasa al estado EN_REPARTO y luego a ENTREGADO una vez finalizada la simulación.
     */
    @Override
    public void run() {

        Pedido pedido;

        while ((pedido = zonaDeCarga.retirarPedido()) != null) {

            pedido.setRepartidorAsignado(nombre);
            pedido.setEstado(EstadoPedido.EN_REPARTO);

            System.out.println("[Repartidor - " + nombre + "] Retirando pedido #" + pedido.getIdPedido() + " (" + pedido.getPrioridad() + ")...");

            System.out.println("[Repartidor - " + nombre + "] Estado: " + pedido.getEstado());

            try {
                // Convierte el tiempo estimado de entrega en una escala menor para simular la duración del reparto sin esperar minutos reales.    
                long tiempoSimulado = pedido.calcularTiempoEntrega() * 100L;
                Thread.sleep(tiempoSimulado);

                pedido.setEstado(EstadoPedido.ENTREGADO);

                System.out.println("[Repartidor - " + nombre + "] Pedido #" + pedido.getIdPedido() + " entregado.");

                System.out.println("[Repartidor - " + nombre + "] Estado: " + pedido.getEstado());

                controlador.registrarEntrega(pedido);

            } catch (InterruptedException ex) {

                Thread.currentThread().interrupt();

                System.out.println("[Repartidor - " + nombre + "] fue interrumpido durante la entrega.");

                break;
            }
        }

        System.out.println("[Repartidor - " + nombre + "] terminó su trabajo.");
    }
}