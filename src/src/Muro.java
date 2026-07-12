public class Muro {
    // Atributos obligatorios
    private int fila;
    private int columna;

    // Constructor
    public Muro(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    // Devuelve la ubicación del muro
    public int[] obtenerPosicion() {
        return new int[]{fila, columna};
    }

    // Getters
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
}