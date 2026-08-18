package model;

/**
 * Representa un pedido genérico de SpeedFast.
 */
public class Pedido {

    // Atributos
    private int idPedido;
    private String direccionEntrega;
    private String tipoPedido;

    // Constructor

    /**
     * Constructor de la clase Pedido.
     *
     * @param idPedido identificador del pedido.
     * @param direccionEntrega dirección donde se realizará la entrega.
     * @param tipoPedido tipo de pedido.
     */
    public Pedido(int idPedido, String direccionEntrega, String tipoPedido) {
        this.idPedido = idPedido;
        this.direccionEntrega = direccionEntrega;
        this.tipoPedido = tipoPedido;
    }

    // 
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public void setTipoPedido(String tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    // Getters
    public int getIdPedido() {
        return idPedido;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public String getTipoPedido() {
        return tipoPedido;
    }

    // Métodos públicos

    @Override
    public String toString() {
        return "PEDIDO:"
                + "\n idPedido = " + idPedido 
                + "\n direccionEntrega = " + direccionEntrega 
                + "\n tipoPedido = " + tipoPedido;
    }
    
    /**
     * Asigna un repartidor al pedido.
     */
    public void asignarRepartidor() {
        System.out.println("Asignando repartidor...");
    }

}