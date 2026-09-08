package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Administrador central de la simulación.
 *
 * La clase mantiene el historial compartido de entregas y coordina el inicio
 * de los repartidores. Los recursos compartidos se protegen para evitar
 * condiciones de carrera durante la ejecución concurrente.
 */
public class ControladorDeEnvios implements Despachable, Cancelable, Rastreable {

    // Se conserva para compatibilidad con la versión de la semana anterior.
    // La simulación concurrente usa despachar(Pedido, String) directamente.
    private Pedido pedidoActual;

    private final List<Pedido> historialEntregas;
    private final ReentrantLock lockHistorial;
    private final AtomicInteger totalEntregados;

    // Monitor usado con synchronized, wait() y notifyAll().
    private boolean inicioAutorizado;

    public ControladorDeEnvios() {
        this.historialEntregas = new ArrayList<>();
        this.lockHistorial = new ReentrantLock();
        this.totalEntregados = new AtomicInteger(0);
        this.inicioAutorizado = false;
    }

    public void setPedidoActual(Pedido pedido) {
        this.pedidoActual = pedido;
    }

    public Pedido getPedidoActual() {
        return pedidoActual;
    }

    /**
     * Los repartidores esperan aquí hasta que Main autoriza el comienzo.
     * El while evita continuar por despertares espurios.
     *
     * @throws InterruptedException si el hilo es interrumpido mientras espera
     */
    public synchronized void esperarInicio() throws InterruptedException {
        while (!inicioAutorizado) {
            wait();
        }
    }

    /**
     * Libera a todos los repartidores que estén esperando el inicio.
     */
    public synchronized void autorizarInicio() {
        inicioAutorizado = true;
        notifyAll();
    }

    /**
     * Registra de forma segura la entrega de un pedido realizada por un hilo.
     * Se usa ReentrantLock porque el historial es un recurso compartido.
     *
     * @param pedido pedido entregado
     * @param nombreRepartidor repartidor que realizó la entrega
     */
    public void despachar(Pedido pedido, String nombreRepartidor) {
        lockHistorial.lock();
        try {
            pedido.setRepartidorAsignado(nombreRepartidor);
            pedido.setEstado("Entregado");
            historialEntregas.add(pedido);
            totalEntregados.incrementAndGet();
        } finally {
            lockHistorial.unlock();
        }
    }

    /**
     * Método heredado de Despachable, conservado para compatibilidad.
     */
    @Override
    public void despachar() {
        if (pedidoActual != null) {
            String repartidor = pedidoActual.getRepartidorAsignado();
            despachar(pedidoActual, repartidor);
        }
    }

    /**
     * Cancela el pedido actual de la versión secuencial anterior.
     */
    @Override
    public void cancelar() {
        if (pedidoActual != null) {
            pedidoActual.setEstado("Cancelado");
            System.out.println("Pedido #" + pedidoActual.getIdPedido()
                    + " cancelado correctamente.");
        }
    }

    /**
     * Devuelve una copia del historial para no exponer la lista compartida.
     */
    public List<Pedido> getHistorialEntregas() {
        lockHistorial.lock();
        try {
            return new ArrayList<>(historialEntregas);
        } finally {
            lockHistorial.unlock();
        }
    }

    public int getTotalEntregados() {
        return totalEntregados.get();
    }

    @Override
    public void verHistorial() {
        List<Pedido> copia = getHistorialEntregas();

        System.out.println("\n================ HISTORIAL FINAL ================");
        if (copia.isEmpty()) {
            System.out.println("No hay pedidos entregados.");
        } else {
            for (Pedido pedido : copia) {
                System.out.println("- " + pedido.getClass().getSimpleName()
                        + " #" + pedido.getIdPedido()
                        + " | " + pedido.getPrioridad()
                        + " | entregado por " + pedido.getRepartidorAsignado());
            }
        }
        System.out.println("Total de pedidos entregados: " + getTotalEntregados());
        System.out.println("=================================================");
    }
}
