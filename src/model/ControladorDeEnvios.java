package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Administra los pedidos y el registro de las entregas realizadas en SpeedFast.
 *
 * Mantiene una lista de todos los pedidos registrados y un historial de pedidos entregados.
 * El acceso a estas colecciones se controla para permitir su uso seguro durante la ejecución concurrente.
 */
public class ControladorDeEnvios implements Rastreable {

    private final List<Pedido> pedidosRegistrados;
    private final List<Pedido> historialEntregas;
    private final ReentrantLock lockPedidos;
    private final ReentrantLock lockHistorial;
    private final AtomicInteger totalEntregados;

    /**
     * Constructor que inicializa las listas y los mecanismos utilizados para controlar su acceso.
     */
    public ControladorDeEnvios() {
        this.pedidosRegistrados = new ArrayList<>();
        this.historialEntregas = new ArrayList<>();
        this.lockPedidos = new ReentrantLock();
        this.lockHistorial = new ReentrantLock();
        this.totalEntregados = new AtomicInteger(0);
    }

    // Getters

    public List<Pedido> getPedidosRegistrados() {
        lockPedidos.lock();

        try {
            return new ArrayList<>(pedidosRegistrados);
        } finally {
            lockPedidos.unlock();
        }
    }

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
     * Registra un nuevo pedido en el sistema.
     *
     * @param pedido pedido que será registrado
     */
    public void registrarPedido(Pedido pedido) {
        lockPedidos.lock();

        try {
            pedidosRegistrados.add(pedido);
        } finally {
            lockPedidos.unlock();
        }
    }

    /**
     * Comprueba si ya existe un pedido con el ID indicado.
     *
     * @param idPedido ID que se desea buscar
     * @return true si el ID ya se encuentra registrado
     */
    public boolean existePedido(int idPedido) {
        lockPedidos.lock();

        try {
            for (Pedido pedido : pedidosRegistrados) {
                if (pedido.getIdPedido() == idPedido) {
                    return true;
                }
            }

            return false;
        } finally {
            lockPedidos.unlock();
        }
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