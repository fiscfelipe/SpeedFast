package model;

/**
 * Representa un pedido prioritario de compras rapidas de mensajeria Express.
 * 
 * @author SpeedFast
 * @version 1.0
 */
public class PedidoExpress extends Pedido {

    /**
     * Constructor para inicializar los datos de un pedido Express.
     * 
     * @param idPedido Identificador único del pedido.
     * @param direccion Dirección donde se realizará la entrega.
     * @param distanciaKm Distancia en kilómetros hasta el lugar de entrega.
     */
    public PedidoExpress(int idPedido, String direccion, double distanciaKm) {
        super(idPedido, direccion, distanciaKm);
    }

    // Métodos públicos
    
     /**
     * Calcula el tiempo estimado de entrega para un pedido de encomienda.
     *
     * El tiempo corresponde a 10 minutos bases y 5 minutos más si la distancia es mayor a 5 km.
     * @return tiempo estimado de entrega en minutos.
     */
    @Override
    public int calcularTiempoEntrega() {
        if (getDistanciaKm() > 5) {
            return 10 + 5;
        } 
        return 10;
    }

    /**
     * Asigna de manera automatica un repartidor para pedidos Express.
     */
    @Override
    public void asignarRepartidor() {
        this.repartidorAsignado = "Carlos Pérez";
    }
    
    /**
     * Metodo sobrecargado para realizar la asignacion manual de un repartidor por nombre.
     * 
     * @param nombre Nombre del repartidor a asignar
     */
    public void asignarRepartidor(String nombre) {
        this.repartidorAsignado = nombre;
    }

    /**
     * Muestra la información correspondiente a un pedido Express.
     */
    @Override
    public void mostrarResumen() {
        System.out.println("[Pedido Express]");
        super.mostrarResumen();
    }
}
