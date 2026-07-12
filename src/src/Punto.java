public class Punto {
    // Atributos obligatorios
    private int fila;
    private int columna;
    private int valor;
    private boolean recolectado;

    // Constructor
    public Punto(int fila, int columna, int valor) {
        this.fila = fila;
        this.columna = columna;
        this.valor = valor;
        this.recolectado = false; // Al crearse, ningún punto ha sido tomado
    }

    // Devuelve el puntaje que otorga este punto
    public int obtenerValor() {
        return valor;
    }

    // Indica si ya fue consumido por el jugador
    public boolean fueRecolectado() {
        return recolectado;
    }

    // Marca el punto como recogido
    public void recolectar() {
        this.recolectado = true;
    }

    // Getters
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
}