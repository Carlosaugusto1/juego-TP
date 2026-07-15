public class Poder {

    private String tipo;
    private int duracion;
    private int fila;
    private int columna;
    private boolean consumido;

    public Poder(int fila, int columna, String tipo, int duracion) {
        this.fila = fila;
        this.columna = columna;
        this.tipo = tipo;
        this.duracion = duracion;
        this.consumido = false;
    }

    public void activar(Jugador j) {
        this.consumido = true;
        System.out.println("¡Poder activado! Tipo: " + this.tipo);
    }

    public String descripcion() {
        return "Poder de tipo [" + tipo + "] con una duración de " + duracion + " turnos.";
    }

    public boolean fueConsumido() {
        return this.consumido;
    }

    public String getTipo() {
        return tipo;
    }

    public int getDuracion() {
        return duracion;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }
}