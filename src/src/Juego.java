import java.util.Random;
import java.util.Scanner;

public class Juego {
    // Atributos obligatorios
    private Jugador jugador;
    private Tablero tablero;
    private Enemigo[] enemigos;
    private boolean juegoTerminado;

    // Auxiliares para controlar la partida
    private int puntosRestantes;
    private Scanner sc;

    public Juego() {
        this.sc = new Scanner(System.in);
        this.juegoTerminado = false;
    }

    // Inicializa el tablero, jugador, enemigos y puntos
    public void iniciarJuego() {
        System.out.println("=== BIENVENIDO A PAC-MAN ===");

        System.out.print("Ingresa el número de filas del tablero: ");
        int filas = sc.nextInt();
        System.out.print("Ingresa el número de columnas del tablero: ");
        int columnas = sc.nextInt();
        sc.nextLine();

        this.tablero = new Tablero(filas, columnas);
        tablero.generarTablero();

        System.out.print("Ingresa tu nombre: ");
        String nombre = sc.nextLine();
        this.jugador = new Jugador(nombre, 1, 1);

        // Como el tablero se llena con '.', restamos la casilla donde arranca el jugador
        this.puntosRestantes = (filas * columnas) - 1;

        System.out.print("¿Cuántos enemigos quieres enfrentar? ");
        int cantidadEnemigos = sc.nextInt();
        generarEnemigos(cantidadEnemigos);
        actualizarTablero();

        System.out.println("\n¡Juego iniciado! Usa W (arriba), S (abajo), A (izquierda), D (derecha) para moverte.");
    }

    // Crea enemigos en posiciones aleatorias
    public void generarEnemigos(int cantidad) {
        Random rand = new Random();
        String[] tipos = {"Perseguidor", "Aleatorio", "Fantasma"};
        enemigos = new Enemigo[cantidad];

        for (int i = 0; i < cantidad; i++) {

            int filaEnemigo;
            int columnaEnemigo;

            do{
                filaEnemigo = rand.nextInt(tablero.getFilas());
                columnaEnemigo = rand.nextInt(tablero.getColumnas());

            }while(!tablero.esMovimientoValido(filaEnemigo,columnaEnemigo)
                    || (filaEnemigo==1 && columnaEnemigo==1));

            String tipo = tipos[rand.nextInt(tipos.length)];

            enemigos[i] = new Enemigo(tipo,filaEnemigo,columnaEnemigo);
        }
    }

    // Procesa las acciones del jugador y enemigos
    public void ejecutarTurno() {
        System.out.print("\nMovimiento (W/A/S/D): ");
        String direccion = sc.next();

        int filaAnterior = jugador.getFila();
        int columnaAnterior = jugador.getColumna();

        jugador.mover(direccion);

        // Si se sale del tablero, se revierte el movimiento
        if (!tablero.esMovimientoValido(jugador.getFila(), jugador.getColumna())) {
            jugador.setFila(filaAnterior);
            jugador.setColumna(columnaAnterior);
            System.out.println("¡No puedes salir del tablero!");
        } else {
            jugador.incrementarPuntaje(1);
            puntosRestantes--;
        }

        for (Enemigo e : enemigos) {
            if (e.isActivo()) {
                e.mover();
                e.verificarColision(jugador);
            }
        }

        actualizarTablero();
        verificarFinJuego();
    }

    // Determina si el jugador ganó o perdió
    public void verificarFinJuego() {
        if (!jugador.estaVivo()) {
            System.out.println("\n¡GAME OVER! " + jugador.getNombre() + " se quedó sin salud.");
            juegoTerminado = true;
        } else if (puntosRestantes <= 0) {
            System.out.println("\n¡FELICIDADES! Has recolectado todos los puntos.");
            juegoTerminado = true;
        }
    }

    // Muestra el estado actual del juego
    public void mostrarEstado() {
        jugador.mostrarEstado();
        for (Enemigo e : enemigos) {
            e.mostrarEstado();
        }
    }

    // Refresca la representación del tablero
    public void actualizarTablero() {

        tablero.mostrarTablero(jugador);

        System.out.println();

        for (Enemigo e : enemigos) {
            e.mostrarEstado();
        }
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    // Menú principal / punto de entrada

}