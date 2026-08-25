package model;

/**
 * Representa un pedido genérico de SpeedFast.
 */
public abstract class Pedido {

    // Atributos
    private int idPedido;
    private String direccionEntrega;
    private double distanciaKm;

    // Constructor

    /**
     * Constructor de la clase Pedido.
     *
     * @param idPedido identificador del pedido.
     * @param direccionEntrega dirección donde se realizará la entrega.
     * @param distanciaKm distancia en kilómetros hasta el lugar de entrega.
     */
    public Pedido(int idPedido, String direccionEntrega, double distanciaKm) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.distanciaKm = distanciaKm;
    }

    // Setters
    
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    // Getters

    public int getIdPedido() {
        return idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }


    public double getDistanciaKm() {
        return distanciaKm;
    }

    // Métodos públicos

    /**
     * Muestra un resumen con los datos básicos del pedido.
     */
    public void mostrarResumen() {
        System.out.println("Pedido: #" + idPedido);
        System.out.println("Dirección: " + direccionEntrega);
        System.out.println("Distancia: " + distanciaKm + " km");
    }

    /**
     * Calcula el tiempo estimado de entrega del pedido.
     *
     * La fórmula para calcular el tiempo estimado de entrega dependerá del tipo de pedido.
     *
     * @return tiempo estimado de entrega en minutos.
     */
    public abstract int calcularTiempoEntrega();
    
    /**
     * Muestra el tiempo estimado de entrega del pedido.
     */
    public void mostrarTiempoEntrega() {
        System.out.println("El tiempo estimado de entrega es: " + calcularTiempoEntrega() + " minutos.");
    }
    
    /**
     * Asigna un repartidor al pedido.
     */
    public void asignarRepartidor() {
        System.out.println("Asignando repartidor...");
    }
}