public class ControlEnemigos {

    // Atributos obligatorios
    private Enemigo[] enemigos;
    private Jugador jugador;

    // Constructor
    public ControlEnemigos(Enemigo[] enemigos, Jugador jugador) {
        this.enemigos = enemigos;
        this.jugador = jugador;
    }

    // Mueve todos los enemigos activos
    public void moverEnemigos() {
        for (Enemigo enemigo : enemigos) {
            if (enemigo != null && enemigo.isActivo()) {
                enemigo.mover();
            }
        }
    }

    // Verifica las colisiones entre enemigos y jugador
    public void verificarColisiones() {
        for (Enemigo enemigo : enemigos) {
            if (enemigo != null && enemigo.isActivo()) {
                enemigo.verificarColision(jugador);
            }
        }
    }

    // Elimina enemigos inactivos (pendiente de implementación)
    public void eliminarEnemigosInactivos() {

        // Funcionalidad pendiente
    }

    // Genera el movimiento general de los enemigos
    public void generarMovimientos() {
        moverEnemigos();
        verificarColisiones();
    }
}