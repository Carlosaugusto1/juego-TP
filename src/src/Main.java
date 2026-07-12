public class Main {

    public static void main(String[] args) {

        Juego juego = new Juego();
        juego.iniciarJuego();

        while (!juego.isJuegoTerminado()) {
            juego.mostrarEstado();
            juego.ejecutarTurno();
        }

        System.out.println("\nGracias por jugar Pac-Man.");
    }
}