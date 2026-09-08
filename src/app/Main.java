package app;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import model.ControladorDeEnvios;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import model.Repartidor;

/**
 * Clase principal de la simulación concurrente de SpeedFast.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("     SPEEDFAST - SIMULACIÓN DE ENTREGAS EN PARALELO");
        System.out.println("==================================================");

        ControladorDeEnvios controlador = new ControladorDeEnvios();

        // Cada repartidor recibe al menos dos pedidos.
        Repartidor repartidor1 = new Repartidor(
                "Luis Díaz",
                Arrays.asList(
                        new PedidoComida(101, "Av. Italia 456", 4.0),
                        new PedidoExpress(102, "Av. Providencia 1200", 6.5)
                ),
                controlador
        );

        Repartidor repartidor2 = new Repartidor(
                "Daniela Tapia",
                Arrays.asList(
                        new PedidoEncomienda(103, "Av. Santa Rosa 567", 7.0),
                        new PedidoComida(104, "Ñuble 980", 3.2)
                ),
                controlador
        );

        Repartidor repartidor3 = new Repartidor(
                "Carlos Pérez",
                Arrays.asList(
                        new PedidoEncomienda(105, "Irarrázaval 2450", 5.0),
                        new PedidoExpress(106, "Av. Apoquindo 1500", 7.0)
                ),
                controlador
        );

        // ExecutorService administra un pool de tres hilos en paralelo.
        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            executor.execute(repartidor1);
            executor.execute(repartidor2);
            executor.execute(repartidor3);

            System.out.println("Tres repartidores preparados. Iniciando rutas...\n");
            controlador.autorizarInicio();

        } finally {
            // No se aceptan nuevas tareas después de enviar las tres rutas.
            executor.shutdown();
        }

        try {
            // El programa espera hasta que todos los repartidores finalicen.
            boolean terminaron = executor.awaitTermination(1, TimeUnit.MINUTES);

            if (!terminaron) {
                System.out.println("Tiempo máximo de espera alcanzado."
                        + " Se solicitará detener las tareas pendientes.");
                executor.shutdownNow();
            }

        } catch (InterruptedException ex) {
            System.out.println("El hilo principal fue interrumpido.");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        controlador.verHistorial();
        System.out.println("Simulación finalizada.");
    }
}
