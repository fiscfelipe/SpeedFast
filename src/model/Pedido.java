package model;

/**
 * Clase abstracta que representa un pedido genérico de SpeedFast.
 *
 * Implementa Comparable para que los pedidos puedan ser ordenados de manera
 * automática dentro de una PriorityBlockingQueue según su prioridad.
 */
public abstract class Pedido implements Comparable<Pedido> {
    protected int idPedido;
    protected String direccionEntrega;
    protected double distanciaKm;
    protected String repartidorAsignado;
    protected String estado;
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
        this.estado = "Pendiente";
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    /**
     * Se mantiene este getter por compatibilidad con la versión anterior.
     */
    public String getDireccion() {
        return direccionEntrega;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public String getRepartidorAsignado() {
        return repartidorAsignado;
    }

    public void setRepartidorAsignado(String repartidorAsignado) {
        this.repartidorAsignado = repartidorAsignado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
     * Realiza una asignación automática de repartidor.
     */
    public abstract void asignarRepartidor();

    /**
     * Muestra los datos principales del pedido.
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
     * Orden natural usado por PriorityBlockingQueue: primero prioridad y luego ID.
     */
    @Override
    public int compareTo(Pedido otro) {
        int porPrioridad = Integer.compare(
                this.prioridad.getNivel(), otro.prioridad.getNivel());

        if (porPrioridad != 0) {
            return porPrioridad;
        }

        return Integer.compare(this.idPedido, otro.idPedido);
    }
}
