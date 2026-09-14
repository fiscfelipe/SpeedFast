package app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import model.ControladorDeEnvios;
import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;
import model.Repartidor;
import model.ZonaDeCarga;

/**
 * Clase principal del sistema SpeedFast.
 *
 * Inicializa la zona de carga, agrega los pedidos y ejecuta varios repartidores de manera concurrente.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("==================================================");
        System.out.println("     SPEEDFAST - SISTEMA DE ENTREGAS");
        System.out.println("==================================================");

        // Se crean los recursos compartidos.
        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();
        ControladorDeEnvios controlador = new ControladorDeEnvios();

        System.out.println("\n[Zona de carga inicializada]");

        // Se agregan los pedidos a la zona de carga.
        zonaDeCarga.agregarPedido(
                new PedidoComida(101, "Av. Italia 456", 4.0));

        zonaDeCarga.agregarPedido(
                new PedidoExpress(102, "Av. Providencia 1200", 6.5));

        zonaDeCarga.agregarPedido(
                new PedidoEncomienda(103, "Av. Santa Rosa 567", 7.0));

        zonaDeCarga.agregarPedido(
                new PedidoComida(104, "Ñuble 980", 3.2));

        zonaDeCarga.agregarPedido(
                new PedidoEncomienda(105, "Irarrázaval 2450", 5.0));

        zonaDeCarga.agregarPedido(
                new PedidoExpress(106, "Av. Apoquindo 1500", 7.0));

        int totalPedidos = 6;

        // Se crean los repartidores.
        Repartidor repartidor1 = new Repartidor("Luis Díaz", zonaDeCarga, controlador);

        Repartidor repartidor2 = new Repartidor("Daniela Tapia", zonaDeCarga, controlador);

        Repartidor repartidor3 = new Repartidor("Carlos Pérez", zonaDeCarga, controlador);

        // Se crea un grupo de tres hilos.
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Se inicia la ejecución concurrente de los repartidores.
        executor.execute(repartidor1);
        executor.execute(repartidor2);
        executor.execute(repartidor3);

        // No se aceptarán nuevas tareas.
        executor.shutdown();

        try {

            boolean finalizaron = executor.awaitTermination(1, TimeUnit.MINUTES);

            if (!finalizaron) {
                System.out.println("Se alcanzó el tiempo máximo de espera.");

                executor.shutdownNow();
            }

        } catch (InterruptedException ex) {

            executor.shutdownNow();
            Thread.currentThread().interrupt();

            System.out.println("El proceso principal fue interrumpido.");
        }
        
        // Se verifica que la zona de carga esté vacía.
        if (zonaDeCarga.getCantidadPedidosPendientes() == 0) {
            System.out.println("\n[Zona de carga vacía]");
        }

        // Se verifica que todos los pedidos hayan sido entregados.
        if (controlador.getTotalEntregados() == totalPedidos) {
            System.out.println("Todos los pedidos han sido entregados correctamente");
        } else {
            System.out.println("No todos los pedidos pudieron ser entregados.");
        }

        // Se muestra el historial de las entregas realizadas.
        controlador.verHistorial();
    }    

}