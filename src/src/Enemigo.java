import java.util.Random;

public class Enemigo {
    // Atributos obligatorios
    private String tipo;
    private int fila;
    private int columna;
    private int daño;
    private boolean activo;

    // Constructor
    public Enemigo(String tipo, int filaInicial, int columnaInicial) {
        this.tipo = tipo;
        this.fila = filaInicial;
        this.columna = columnaInicial;
        this.daño = 1;          // Cada golpe quita 1 de vida por defecto
        this.activo = true;     // Inicia en el mapa
    }

    // Realiza el movimiento según su comportamiento
    public void mover() {
        Random rand = new Random();
        if (this.tipo.equals("Aleatorio")) {
            // Elige una dirección al azar: 0=Arriba, 1=Abajo, 2=Izquierda, 3=Derecha
            int direccion = rand.nextInt(4);
            switch (direccion) {
                case 0: fila--; break;
                case 1: fila++; break;
                case 2: columna--; break;
                case 3: columna++; break;
            }
        }
        // Nota: Los comportamientos "Perseguidor" y "Fantasma" se conectarán con el Tablero más adelante
    }

    // Detecta encuentros con el jugador
    public void verificarColision(Jugador j) {
        if (this.fila == j.getFila() && this.columna == j.getColumna()) {
            atacar(j);
        }
    }

    // Reduce la salud del jugador
    public void atacar(Jugador j) {
        System.out.println("¡El enemigo " + tipo + " ha atacado a " + j.getNombre() + "!");
        j.recibirDaño(this.daño);
    }

    public void mostrarEstado() {
        if (activo) {
            System.out.println("Enemigo [" + tipo + "] en posición: [" + fila + "," + columna + "]");
        }
    }

    // Getters y Setters
    public boolean isActivo() { return activo; }
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
}