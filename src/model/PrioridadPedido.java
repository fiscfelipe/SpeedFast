package model;

/**
 * Define el nivel de prioridad de un pedido.
 * Un valor numérico menor representa una prioridad mayor.
 */
public enum PrioridadPedido {
    ALTA(1),
    MEDIA(2),
    BAJA(3);

    private final int nivel;

    PrioridadPedido(int nivel) {
        this.nivel = nivel;
    }

    public int getNivel() {
        return nivel;
    }
}
