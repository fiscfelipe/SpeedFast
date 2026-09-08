package model;

/**
 * Representa un pedido prioritario de compras rápidas o mensajería express.
 */
public class PedidoExpress extends Pedido {

    public PedidoExpress(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm, PrioridadPedido.ALTA);
    }

    /**
     * 10 minutos base y 5 minutos extra si la distancia es mayor a 5 km.
     */
    @Override
    public int calcularTiempoEntrega() {
        return getDistanciaKm() > 5 ? 15 : 10;
    }

    @Override
    public void asignarRepartidor() {
        this.repartidorAsignado = "Carlos Pérez";
    }

    public void asignarRepartidor(String nombre) {
        this.repartidorAsignado = nombre;
    }

    @Override
    public void mostrarResumen() {
        System.out.println("[Pedido Express]");
        super.mostrarResumen();
    }
}
