package app;

import model.*;

/**
 * Clase principal que ejecuta la simulacion de las órdenes de pedidos.
 */
public class Main {
    public static void main(String[] args) {
        ControladorDeEnvios controlador = new ControladorDeEnvios();

        System.out.println("==================================================");
        System.out.println("          SISTEMA DE ENTREGAS SPEEDFAST           ");
        System.out.println("==================================================");
        System.out.println();

        //Ejemplo pedido 1: Pedido tipo Comida con asignación automático y despacho.
        PedidoComida p1 = new PedidoComida(101, "Av. Italia 456", 4.0);
        
        p1.asignarRepartidor(); 
        p1.mostrarResumen();
        System.out.println();
        System.out.println(); 
        controlador.setPedidoActual(p1);
        controlador.despachar(); 
        System.out.println("\n--------------------------------------------------\n");

        //Ejemplo pedido 2: Pedido tipo Encomienda con asignación manual y despacho.
        PedidoEncomienda p2 = new PedidoEncomienda(102, "Av. Santa Rosa 567", 7.0);
        
        p2.asignarRepartidor("Daniela Tapia"); 
        p2.mostrarResumen();
        System.out.println();
        controlador.setPedidoActual(p2);
        controlador.despachar();
        System.out.println("\n--------------------------------------------------\n");

        //Ejemplo pedido 3: Pedido tipo Express con asignación automático y cancelación.
        PedidoExpress p3 = new PedidoExpress(103, "Av. Apoquindo 1500", 7.0);
        
        p3.asignarRepartidor();
        p3.mostrarResumen();
        System.out.println();
        controlador.setPedidoActual(p3);
        controlador.cancelar();
        System.out.println("\n--------------------------------------------------\n");


        //Ejemplo del historial.
        controlador.verHistorial();
        System.out.println("==================================================");
    }
}
