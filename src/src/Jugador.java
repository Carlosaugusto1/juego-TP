public class Jugador {
    // Atributos obligatorios
    private String nombre;
    private int fila;
    private int columna;
    private int salud;
    private int puntaje;
    private int velocidad;
    private boolean poderActivo;

    // Constructor
    public Jugador(String nombre, int filaInicial, int columnaInicial) {
        this.nombre = nombre;
        this.fila = filaInicial;
        this.columna = columnaInicial;
        this.salud = 3;          // Iniciamos con 3 puntos de vida
        this.puntaje = 0;         // El puntaje inicia en cero
        this.velocidad = 1;       // Velocidad base
        this.poderActivo = false; // Inicia sin poderes
    }

    // Método para desplazarse por el tablero
    public void mover(String direccion) {
        switch (direccion.toUpperCase()) {
            case "W": fila--; break;    // Arriba
            case "S": fila++; break;    // Abajo
            case "A": columna--; break; // Izquierda
            case "D": columna++; break; // Derecha
            default: System.out.println("Dirección no válida (Usa W, A, S, D).");
        }
    }

    // Reduce la salud del jugador al ser atacado
    public void recibirDaño(int cantidad) {
        this.salud -= cantidad;
        if (this.salud < 0) this.salud = 0;
    }

    // Retorna true si la salud es mayor que cero
    public boolean estaVivo() {
        return this.salud > 0;
    }

    // Muestra la información en la consola
    public void mostrarEstado() {
        System.out.println("--- JUGADOR ---");
        System.out.println("Nombre: " + nombre + " | Salud: " + salud + " | Puntaje: " + puntaje);
        System.out.println("Posición: [" + fila + "," + columna + "]");
    }

    // Getters y Setters necesarios para el Tablero y Enemigos
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
    public String getNombre() { return nombre; }
    public void setFila(int fila) { this.fila = fila; }
    public void setColumna(int columna) { this.columna = columna; }
    public void incrementarPuntaje(int valor) { this.puntaje += valor; }
}