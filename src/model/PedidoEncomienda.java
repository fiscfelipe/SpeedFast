package model;

/**
 * Representa un pedido de encomienda dentro del sistema SpeedFast.
 */
public class PedidoEncomienda extends Pedido {

    public PedidoEncomienda(int idPedido, String direccionEntrega, double distanciaKm) {
        super(idPedido, direccionEntrega, distanciaKm, PrioridadPedido.BAJA);
    }

    /**
     * 20 minutos base más 1,5 minutos por kilómetro.
     */
    @Override
    public int calcularTiempoEntrega() {
        return (int) Math.round(20 + (1.5 * getDistanciaKm()));
    }

    @Override
    public void asignarRepartidor() {
        this.repartidorAsignado = "Daniela Tapia";
    }

    public void asignarRepartidor(String nombre) {
        this.repartidorAsignado = nombre;
    }

    @Override
    public void mostrarResumen() {
        System.out.println("[Pedido Encomienda]");
        super.mostrarResumen();
    }
}
