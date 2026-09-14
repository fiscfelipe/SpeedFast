package model;

/**
 * Clase abstracta que representa un pedido genérico de SpeedFast.
 *
 * Implementa Comparable para que los pedidos puedan ser ordenados automáticamente según su prioridad.
 */
public abstract class Pedido implements Comparable<Pedido> {

    protected int idPedido;
    protected String direccionEntrega;
    protected double distanciaKm;
    protected String repartidorAsignado;
    protected EstadoPedido estado;
    protected PrioridadPedido prioridad;

    /**
     * Constructor para inicializar los datos base de un pedido.
     *
     * @param idPedido identificador único del pedido
     * @param direccionEntrega dirección donde se realizará la entrega
     * @param distanciaKm distancia en kilómetros hasta el lugar de entrega
     * @param prioridad prioridad del pedido
     */
    public Pedido(int idPedido, String direccionEntrega, double distanciaKm,
                  PrioridadPedido prioridad) {

        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.distanciaKm = distanciaKm;
        this.prioridad = prioridad;
        this.repartidorAsignado = "No asignado";
        this.estado = EstadoPedido.PENDIENTE;
    }

    //Setters

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public void setRepartidorAsignado(String repartidorAsignado) {
        this.repartidorAsignado = repartidorAsignado;
    }

    public void setEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }

    //Getters

    public int getIdPedido() {
        return idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public String getRepartidorAsignado() {
        return repartidorAsignado;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public PrioridadPedido getPrioridad() {
        return prioridad;
    }

    /**
     * Calcula el tiempo estimado de entrega de acuerdo con el tipo de pedido.
     *
     * @return tiempo estimado en minutos
     */
    public abstract int calcularTiempoEntrega();

    /**
    * Muestra por consola un resumen con la información principal del pedido.
    */
    public void mostrarResumen() {
        System.out.println("Pedido #" + idPedido
                + "\nDirección: " + direccionEntrega
                + "\nDistancia: " + distanciaKm + " km"
                + "\nPrioridad: " + prioridad
                + "\nRepartidor asignado: " + repartidorAsignado
                + "\nEstado: " + estado
                + "\nTiempo estimado: " + calcularTiempoEntrega() + " minutos");
    }

    /**
     * Devuelve una representación en texto del pedido.
     *
     * @return información principal del pedido
     */
    @Override
    public String toString() {
        return "Pedido #" + idPedido + " | Dirección: " + direccionEntrega + " | Estado: " + estado + " | Prioridad: " + prioridad;
    }

    /**
     * Ordena los pedidos primero por prioridad y luego por ID.
     *
     * @param otro pedido con el que se realizará la comparación
     * @return valor negativo, cero o positivo según el orden
     */
    @Override
    public int compareTo(Pedido otro) {

        int porPrioridad = Integer.compare(this.prioridad.getNivel(), otro.prioridad.getNivel());

        if (porPrioridad != 0) {
            return porPrioridad;
        }

        return Integer.compare(this.idPedido, otro.idPedido);
    }
}