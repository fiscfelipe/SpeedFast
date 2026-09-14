package model;

/**
 * Representa los niveles de prioridad de un pedido en SpeedFast.
 *
 * El nivel numérico se utiliza para ordenar los pedidos, dando preferencia a los de mayor prioridad.
 */
public enum PrioridadPedido {

    ALTA(1),
    MEDIA(2),
    BAJA(3);

    private final int nivel;

    /**
     * Constructor que asigna un nivel numérico a cada prioridad.
     *
     * @param nivel valor utilizado para ordenar los pedidos
     */
    PrioridadPedido(int nivel) {
        this.nivel = nivel;
    }

    // Getters
    public int getNivel() {
        return nivel;
    }
}