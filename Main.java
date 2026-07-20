public class Main {
    public static void main(String[] args) {
        Juego partida = new Juego();

        partida.iniciarJuego();

        while (!partida.isJuegoTerminado()) {
            partida.actualizarTablero();
            partida.mostrarEstado();
            partida.ejecutarTurno();
            partida.verificarFinJuego();
        }

        System.out.println("\nGracias por jugar Pac-Man OOP.");
    }
}20