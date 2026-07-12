public class Poder {

    // Atributos obligatorios
    private String tipo;
    private int fila;
    private int columna;
    private int duracion;
    private boolean consumido;

    // Constructor
    public Poder(String tipo, int fila, int columna, int duracion) {
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
        this.duracion = duracion;
        this.consumido = false;
    }

    // Activa el poder (pendiente de implementación)
    public void activar(Jugador j) {
        System.out.println("Poder activado: " + tipo);
        this.consumido = true;

        // Aquí se implementará el efecto del poder
    }

    // Devuelve una descripción del poder
    public String descripcion() {
        return "Poder: " + tipo + " | Duración: " + duracion + " turnos.";
    }

    // Getters
    public String getTipo() {
        return tipo;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getDuracion() {
        return duracion;
    }

    public boolean fueConsumido() {
        return consumido;
    }
}