package app;

import model.PedidoComida;
import model.PedidoEncomienda;
import model.PedidoExpress;


public class Main {

    public static void main(String[] args) {

        // Crear pedidos
        PedidoComida pedidoComida = new PedidoComida(1, "Av. Providencia 123");

        PedidoEncomienda pedidoEncomienda = new PedidoEncomienda(2, "Av. Las Condes 456");

        PedidoExpress pedidoExpress = new PedidoExpress(3, "Av. Apoquindo 789");

        // Asignar repartidores
        pedidoComida.asignarRepartidor("Juan Pérez");
        System.out.println();

        pedidoEncomienda.asignarRepartidor("Camila Soto");
        System.out.println();

        pedidoExpress.asignarRepartidor("Luis Díaz");
    }
}