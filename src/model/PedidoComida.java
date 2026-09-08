package model;

/**
 * Representa un pedido de comida dentro del sistema SpeedFast.
 */
public class PedidoComida extends Pedido {

    public PedidoComida(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm, PrioridadPedido.MEDIA);
    }

    /**
     * 15 minutos base más 2 minutos por kilómetro.
     */
    @Override
    public int calcularTiempoEntrega() {
        return 15 + (int) (2 * getDistanciaKm());
    }

    @Override
    public void asignarRepartidor() {
        this.repartidorAsignado = "Luis Díaz";
    }

    public void asignarRepartidor(String nombre) {
        this.repartidorAsignado = nombre;
    }

    @Override
    public void mostrarResumen() {
        System.out.println("[Pedido Comida]");
        super.mostrarResumen();
    }
}
