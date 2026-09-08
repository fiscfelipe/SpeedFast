package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Representa a un repartidor que procesa pedidos en un hilo independiente.
 */
public class Repartidor implements Runnable {

    private final String nombre;
    private final List<Pedido> pedidosAsignados;
    private final ControladorDeEnvios controlador;

    public Repartidor(String nombre, List<Pedido> pedidosAsignados,
                      ControladorDeEnvios controlador) {
        this.nombre = nombre;
        this.pedidosAsignados = new ArrayList<>(pedidosAsignados);
        this.controlador = controlador;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Pedido> getPedidosAsignados() {
        return new ArrayList<>(pedidosAsignados);
    }

    /**
     * Ejecuta secuencialmente los pedidos del repartidor.
     *
     * Cada repartidor trabaja en paralelo con los demás. Dentro de su propia
     * tarea, los pedidos se procesan uno a uno y se priorizan mediante una
     * PriorityBlockingQueue.
     */
    @Override
    public void run() {
        PriorityBlockingQueue<Pedido> colaPedidos = new PriorityBlockingQueue<>();
        colaPedidos.addAll(pedidosAsignados);

        try {
            controlador.esperarInicio();
            System.out.println("[" + nombre + "] inicia su ruta con "
                    + colaPedidos.size() + " pedidos.");

            Pedido pedido;
            while ((pedido = colaPedidos.poll()) != null) {
                pedido.setRepartidorAsignado(nombre);
                pedido.setEstado("En reparto");

                System.out.println("[" + nombre + "] preparando pedido #"
                        + pedido.getIdPedido() + " (" + pedido.getPrioridad()
                        + ") hacia " + pedido.getDireccionEntrega() + ".");

                // Simulación del tiempo de viaje con una pausa aleatoria.
                int pausaMs = ThreadLocalRandom.current().nextInt(700, 1801);
                Thread.sleep(pausaMs);

                controlador.despachar(pedido, nombre);

                System.out.println("[" + nombre + "] pedido #"
                        + pedido.getIdPedido() + " ENTREGADO."
                        + " Tiempo estimado real del modelo: "
                        + pedido.calcularTiempoEntrega() + " min.");
            }

            System.out.println("[" + nombre + "] terminó todas sus entregas.");

        } catch (InterruptedException ex) {
            // Se restablece el estado de interrupción para no ocultar la señal.
            Thread.currentThread().interrupt();
            System.out.println("[" + nombre
                    + "] fue interrumpido antes de completar su ruta.");
        } catch (RuntimeException ex) {
            // Evita que un error inesperado derribe toda la simulación.
            System.out.println("[" + nombre + "] error durante la entrega: "
                    + ex.getMessage());
        }
    }
}
