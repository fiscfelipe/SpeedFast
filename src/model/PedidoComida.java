package model;

/**
 * Representa un pedido de comida dentro del sistema SpeedFast.
 */
public class PedidoComida extends Pedido {

    /**
     * Constructor para inicializar los datos de un pedido de comida.
     * 
     * @param idPedido Identificador único del pedido.
     * @param direccion Dirección donde se realizará la entrega.
     * @param distanciaKM Distancia en kilómetros hasta el lugar de entrega.
     */
    public PedidoComida(int idPedido, String direccion, double distanciaKM) {
        super(idPedido, direccion, distanciaKM);
    }

    // Métodos públicos

    /**
     * Calcula el tiempo estimado de entrega para un pedido de comida.
     *
     * El tiempo corresponde a 15 minutos base más 2 minutos por cada kilómetro de distancia.
     * @return tiempo estimado de entrega en minutos.
     */
    @Override
    public int calcularTiempoEntrega() {
        return 15 + (int) (2 * getDistanciaKm());
    }
    

    /**
     * Asigna de manera automatica un repartidor para la entrega.
     */
    @Override
    public void asignarRepartidor() {
        this.repartidorAsignado = "Luis Díaz";
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
     * Muestra la información correspondiente a un pedido de comida.
     */
    @Override
    public void mostrarResumen() {
        System.out.println("[Pedido Comida]");
        super.mostrarResumen();
    }
}
