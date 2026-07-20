public class Muro {

    private int fila;
    private int columna;

    public Muro(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int[] obtenerPosicion() {
        return new int[]{this.fila, this.columna};
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}