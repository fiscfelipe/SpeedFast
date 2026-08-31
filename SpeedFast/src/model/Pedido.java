package model;

/**
 * Clase abstracta que representa un pedido genérico de SpeedFast.
 */
public abstract class Pedido {
    protected int idPedido;
    protected String direccion;
    protected double distanciaKm;
    protected String repartidorAsignado;
    protected String estado;

    /**
     * Constructor para inicializar los datos base de un pedido.
     * 
     * @param idPedido Identificador único del pedido.
     * @param direccion Direccion donde se realizará la entrega.
     * @param distanciaKM Distancia en kilómetros hasta el lugar de entrega.
     */
    public Pedido(int idPedido, String direccion, double distanciaKM) {
        this.idPedido = idPedido;
        this.direccion = direccion;
        this.distanciaKm = distanciaKM;
        this.repartidorAsignado = "No asignado";
        this.estado = "Pendiente";
    }

    // Setters
    
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setDistanciaKM(double distanciaKM) {
        this.distanciaKm = distanciaKM;
    }

    public void setRepartidorAsignado(String repartidorAsignado) {
        this.repartidorAsignado = repartidorAsignado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
    // Getters
    
    public int getIdPedido() {
        return idPedido;
    }

    public String getDireccion() {
        return direccion;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public String getRepartidorAsignado() {
        return repartidorAsignado;
    }

    public String getEstado() {
        return estado;
    }


    /**
     * Metodo abstracto para calcular el tiempo estimado de entrega de un pedido.
     * 
     * La fórmula para calcular el tiempo depende del tipo de pedido.
     * 
     * @return int Tiempo estimado de entrega en minutos.
     */
    public abstract int calcularTiempoEntrega();

    
    /**
     * Metodo abstracto para realizar la asignacion automatica de un repartidor.
     */
    public abstract void asignarRepartidor(); 

    
    /**
     * Muestra un resumen con los datos básicos del pedido.
     */
    public void mostrarResumen() {
        System.out.println(   "Pedido #" + idPedido 
                            + "\nDirección: " + direccion 
                            + "\nDistancia: " + distanciaKm + " km"
                            + "\nRepartidor asignado: " + repartidorAsignado
                            + "\nTiempo estimado: " + calcularTiempoEntrega() + " minutos");
    }
}

   