package model;

import java.time.*;

/**
 * Representa una entrega realizada por un repartidor para un pedido determinado.
 */
public class Entrega {

    private int id;
    private final int idPedido;
    private final int idRepartidor;
    private final LocalDate fecha;
    private final LocalTime hora;

    /**
     * Constructor utilizado al crear una nueva entrega.
     * El identificador será generado por MySQL.
     *
     * @param idPedido identificador del pedido
     * @param idRepartidor identificador del repartidor
     * @param fecha fecha de la entrega
     * @param hora hora de la entrega
     */
    public Entrega(int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this.id = 0;
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    /**
     * Constructor utilizado al recuperar una entrega existente desde la base de datos.
     *
     * @param id identificador de la entrega
     * @param idPedido identificador del pedido
     * @param idRepartidor identificador del repartidor
     * @param fecha fecha de la entrega
     * @param hora hora de la entrega
     */
    public Entrega(int id, int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this.id = id;
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }
}