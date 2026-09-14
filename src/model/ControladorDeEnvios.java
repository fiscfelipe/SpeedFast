package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Administra el registro de las entregas realizadas en SpeedFast.
 *
 * Mantiene un historial compartido de pedidos entregados y controla de forma segura el acceso a este recurso durante la ejecución concurrente de los repartidores.
 */
public class ControladorDeEnvios implements Rastreable {

    private final List<Pedido> historialEntregas;
    private final ReentrantLock lockHistorial;
    private final AtomicInteger totalEntregados;

    /**
     * Constructor que inicializa el historial de entregas y los mecanismos utilizados para controlar su acceso.
     */
    public ControladorDeEnvios() {
        this.historialEntregas = new ArrayList<>();
        this.lockHistorial = new ReentrantLock();
        this.totalEntregados = new AtomicInteger(0);
    }

    // Getters

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

    /**
     * Registra un pedido que ha sido entregado.
     *
     * El acceso al historial se protege con ReentrantLock para evitar problemas cuando varios repartidores registran entregas simultáneamente.
     *
     * @param pedido pedido que fue entregado
     */
    public void registrarEntrega(Pedido pedido) {
        lockHistorial.lock();

        try {
            historialEntregas.add(pedido);
            totalEntregados.incrementAndGet();
        } finally {
            lockHistorial.unlock();
        }
    }

    /**
     * Muestra por consola el historial final de pedidos entregados.
     */
    @Override
    public void verHistorial() {

        List<Pedido> copia = getHistorialEntregas();

        System.out.println("\n================ HISTORIAL FINAL ================");

        if (copia.isEmpty()) {
            System.out.println("No hay pedidos entregados.");
        } else {
            for (Pedido pedido : copia) {
                System.out.println("- " + pedido.getClass().getSimpleName() + " #" + pedido.getIdPedido() + " | " + pedido.getPrioridad() + " | entregado por " + pedido.getRepartidorAsignado());
            }
        }

        System.out.println("Total de pedidos entregados: " + getTotalEntregados());

        System.out.println("=================================================");
    }
}