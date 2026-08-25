package app;

import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;


public class Main {

    public static void main(String[] args) {

        // Crear pedidos
        PedidoComida pedidoComida = new PedidoComida(1, "Av. Providencia 123", 5);

        PedidoEncomienda pedidoEncomienda = new PedidoEncomienda(2, "Av. Las Condes 456", 10);

        PedidoExpress pedidoExpress = new PedidoExpress(3, "Av. Apoquindo 789", 15);

        // Asignar repartidores y tiempos de entrega.
        pedidoComida.mostrarResumen();
        System.out.println();
        pedidoComida.asignarRepartidor("Juan Pérez");
        System.out.println();
        pedidoComida.mostrarTiempoEntrega();
        System.out.println( "-----------------------------------------------------------------");
        
        pedidoEncomienda.mostrarResumen();
        System.out.println();
        pedidoEncomienda.asignarRepartidor("Camila Soto");
        System.out.println();
        pedidoEncomienda.mostrarTiempoEntrega();
        System.out.println( "-----------------------------------------------------------------");
        
        
        pedidoExpress.mostrarResumen();
        System.out.println();
        pedidoExpress.asignarRepartidor("Luis Díaz");
        System.out.println();
        pedidoExpress.mostrarTiempoEntrega();
        System.out.println( "-----------------------------------------------------------------");
        
        
    }
}