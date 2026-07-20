public class Jugador {

    private String nombre;
    private int fila;
    private int columna;
    private int salud;
    private int puntaje;
    private int velocidad;
    private boolean poderActivo;

    // NUEVOS ATRIBUTOS PARA GESTIONAR LOS PODERES
    private String tipoPoderActivo;
    private int turnosPoder;

    public Jugador(String nombre, int fila, int columna) {
        this.nombre = nombre;
        this.fila = fila;
        this.columna = columna;
        this.salud = 3;
        this.puntaje = 0;
        this.velocidad = 1;
        this.poderActivo = false;
        this.tipoPoderActivo = "Ninguno";
        this.turnosPoder = 0;
    }

    public int getFila() { return fila; }
    public int getColumna() { return columna; }
    public void setFila(int fila) { this.fila = fila; }
    public void setColumna(int columna) { this.columna = columna; }
    public int getPuntaje() { return puntaje; }
    public int getSalud() { return salud; }
    public int getVelocidad() { return velocidad; }

    public boolean isPoderActivo() { return poderActivo; }
    public String getTipoPoderActivo() { return tipoPoderActivo; }
    public int getTurnosPoder() { return turnosPoder; }

    // Método para mover un paso individual (esencial para la velocidad x2)
    public void moverPaso(int dirFila, int dirColumna) {
        this.fila += dirFila;
        this.columna += dirColumna;
    }

    // Mantiene compatibilidad con el código antiguo por si se requiere
    public void mover(String direccion) {
        switch (direccion.toUpperCase()) {
            case "W": this.fila--; break;
            case "S": this.fila++; break;
            case "A": this.columna--; break;
            case "D": this.columna++; break;
        }
    }

    public void recogerPunto(Punto p) {
        if (p != null && !p.fueRecolectado()) {
            this.puntaje += p.obtenerValor();
            p.consumir();
        }
    }

    public void recibirDaño(int cantidad) {
        this.salud -= cantidad;
        if (this.salud < 0) {
            this.salud = 0;
        }
        System.out.println("\n ¡Ouch! Un fantasma te ha alcanzado. Perdiste una vida.");
    }

    // IMPLEMENTACIÓN: El jugador asimila el poder consumido
    public void usarPoder(Poder p) {
        if (p != null) {
            this.tipoPoderActivo = p.getTipo();
            this.turnosPoder = p.getDuracion();
            this.poderActivo = true;

            // Si es de velocidad, alteramos la estadística del jugador
            if (this.tipoPoderActivo.equalsIgnoreCase("Velocidad")) {
                this.velocidad = 2;
            } else {
                this.velocidad = 1;
            }
            System.out.println("\n ¡SÚPER PODER ADQUIRIDO! -> [" + tipoPoderActivo + "] por " + turnosPoder + " turnos.");
        }
    }

    // Disminuye la duración del poder en cada turno
    public void actualizarTurnosPoder() {
        if (poderActivo) {
            turnosPoder--;
            if (turnosPoder <= 0) {
                System.out.println("\nEl poder [" + tipoPoderActivo + "] ha terminado. Volviendo al estado normal.");
                this.poderActivo = false;
                this.tipoPoderActivo = "Ninguno";
                this.velocidad = 1;
            }
        }
    }

    public boolean estaVivo() {
        return this.salud > 0;
    }

    public void mostrarEstado() {
        System.out.println("--- ESTADO DEL JUGADOR ---");
        System.out.println("Nombre: " + nombre);
        System.out.println("Salud: " + salud);
        System.out.println("Puntaje: " + puntaje);
        if (poderActivo) {
            System.out.println("Poder Activo: SÍ (" + tipoPoderActivo + " - " + turnosPoder + " turnos restantes)");
        } else {
            System.out.println("Poder Activo: NO");
        }
    }
}