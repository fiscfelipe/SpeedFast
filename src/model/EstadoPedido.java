package model;

/**
 * Representa los posibles estados de un pedido dentro del sistema SpeedFast.
 *
 * PENDIENTE: el pedido se encuentra disponible en la zona de carga.
 * EN_REPARTO: el pedido fue retirado por un repartidor y está siendo entregado.
 * ENTREGADO: el pedido ya fue entregado correctamente.
 *
 */
public enum EstadoPedido {
    PENDIENTE,
    EN_REPARTO,
    ENTREGADO;
}