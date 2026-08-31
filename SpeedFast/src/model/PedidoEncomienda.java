package model;

/**
* Representa un pedido de encomienda dentro del sistema SpeedFast.
 */
public class PedidoEncomienda extends Pedido {

    /**
     * Constructor para inicializar los datos de un pedido de encomienda.
     * 
     * @param idPedido Identificador único del pedido.
     * @param direccion dirección donde se realizará la entrega.
     * @param distanciaKm distancia en kilómetros hasta el lugar de entrega.
     */
    public PedidoEncomienda(int idPedido, String direccion, double distanciaKm) {
        super(idPedido, direccion, distanciaKm);
    }

    // Métodos públicos

    /**
     * Calcula el tiempo estimado de entrega para un pedido de encomienda.
     *
     * El tiempo corresponde a 20 minutos base más 1,5 minutos por cada kilómetro de distancia.
     * @return tiempo estimado de entrega en minutos.
     */
    @Override
    public int calcularTiempoEntrega() {
        return (int) Math.round(20 + (1.5 * getDistanciaKm()));
    }

    /**
     * Asigna de manera automatica un repartidor para la entrega.
     */
    @Override
    public void asignarRepartidor() {
        this.repartidorAsignado = "Daniela Tapia";
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
     * Muestra la información correspondiente a un pedido Encomienda.
     */
    @Override
    public void mostrarResumen() {
        System.out.println("[Pedido Encomienda]");
        super.mostrarResumen();
    }
}
