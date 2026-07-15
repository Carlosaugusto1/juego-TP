public class ControlEnemigos {

    private Enemigo[] enemigos;
    private Jugador jugador;
    private Tablero tablero;

    public ControlEnemigos(Enemigo[] enemigos, Jugador jugador, Tablero tablero) {

        this.enemigos = enemigos;
        this.jugador = jugador;
        this.tablero = tablero;
    }

    public void moverEnemigos() {
        for (Enemigo enemigo : enemigos) {
            if (enemigo != null && enemigo.isActivo()) {
                enemigo.mover(tablero, jugador);
            }
        }
    }

    public void verificarColisiones() {
    }

    public void eliminarEnemigosInactivos() {
    }

    public void generarMovimientos() {
    }
}