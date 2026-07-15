public class Enemigo {

    private String tipo;        // "Perseguidor", "Aleatorio" o "Fantasma"
    private int fila;           // Posición actual en el eje Y
    private int columna;        // Posición actual en el eje X
    private int daño;           // Cuánta salud le quita al jugador si lo toca
    private boolean activo;     // Indica si el enemigo está en pantalla

    public Enemigo(String tipo, int fila, int columna) {
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
        this.daño = 1;
        this.activo = true;
    }

    // Realiza el movimiento en el mapa
    public void mover(Tablero tablero, Jugador jugador) {
        // Las 4 direcciones posibles: {Fila, Columna}
        int[][] direcciones = {
                {-1, 0}, // Arriba
                { 1, 0}, // Abajo
                {0, -1}, // Izquierda
                { 0, 1}  // Derecha
        };

        // Si el jugador está invisible, los enemigos que dependen de la vista del jugador
        // (Perseguidor y Fantasma) se comportan como si fueran Aleatorios
        boolean jugadorInvisible = jugador.isPoderActivo()
                && jugador.getTipoPoderActivo().equalsIgnoreCase("Invisibilidad");

        if (tipo.equalsIgnoreCase("Aleatorio")) {
            moverAleatorio(tablero, direcciones);
        }
        else if (tipo.equalsIgnoreCase("Perseguidor")) {
            if (jugadorInvisible) {
                moverAleatorio(tablero, direcciones);
            } else {
                moverPerseguidor(tablero, jugador, direcciones);
            }
        }
        else if (tipo.equalsIgnoreCase("Fantasma")) {
            if (jugadorInvisible) {
                moverAleatorio(tablero, direcciones);
            } else {
                moverEspectro(tablero, jugador, direcciones);
            }
        }
    }

    private void moverAleatorio(Tablero tablero, int[][] direcciones) {
        // Creamos un array fijo de tamaño 4 (el máximo de opciones posibles)
        int[][] movimientosValidos = new int[4][2];
        int contadorValidos = 0;

        // Filtramos qué movimientos de los 4 son válidos (no son muros)
        for (int i = 0; i < direcciones.length; i++) {
            int nuevaFila = this.fila + direcciones[i][0];
            int nuevaColumna = this.columna + direcciones[i][1];

            if (tablero.esMovimientoValido(nuevaFila, nuevaColumna)) {
                movimientosValidos[contadorValidos][0] = nuevaFila;
                movimientosValidos[contadorValidos][1] = nuevaColumna;
                contadorValidos++;
            }
        }

        // Si encontramos al menos una opción para movernos
        if (contadorValidos > 0) {
            // Obtenemos un número pseudoaleatorio usando el tiempo del procesador en nanosegundos
            long tiempo = System.nanoTime();
            if (tiempo < 0) {
                tiempo = -tiempo; // Evitamos que sea negativo
            }
            int indiceAzar = (int) (tiempo % contadorValidos);

            // Asignamos la nueva posición elegida al azar
            this.fila = movimientosValidos[indiceAzar][0];
            this.columna = movimientosValidos[indiceAzar][1];
        }
    }

    private void moverPerseguidor(Tablero tablero, Jugador jugador, int[][] direcciones) {
        int targetFila = jugador.getFila();
        int targetColumna = jugador.getColumna();

        int mejorFila = this.fila;
        int mejorColumna = this.columna;
        int menorDistancia = 999999; // Usamos un número muy grande como valor inicial

        for (int i = 0; i < direcciones.length; i++) {
            int nuevaFila = this.fila + direcciones[i][0];
            int nuevaColumna = this.columna + direcciones[i][1];

            if (tablero.esMovimientoValido(nuevaFila, nuevaColumna)) {
                // Calculamos la distancia
                int diffFila = nuevaFila - targetFila;
                int diffColumna = nuevaColumna - targetColumna;

                // Si la diferencia es negativa, la multiplicamos por -1 para hacerla positiva (valor absoluto)
                int absFila = (diffFila < 0) ? -diffFila : diffFila;
                int absColumna = (diffColumna < 0) ? -diffColumna : diffColumna;

                int distancia = absFila + absColumna;

                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    mejorFila = nuevaFila;
                    mejorColumna = nuevaColumna;
                }
            }
        }

        this.fila = mejorFila;
        this.columna = mejorColumna;
    }

    private void moverEspectro(Tablero tablero, Jugador jugador, int[][] direcciones) {
        int targetFila = jugador.getFila();
        int targetColumna = jugador.getColumna();

        int mejorFila = this.fila;
        int mejorColumna = this.columna;
        int menorDistancia = 999999;

        // Obtenemos los límites de la matriz directamente
        char[][] matriz = tablero.getMatriz();
        int limiteFilas = matriz.length;
        int limiteColumnas = matriz[0].length;

        for (int i = 0; i < direcciones.length; i++) {
            int nuevaFila = this.fila + direcciones[i][0];
            int nuevaColumna = this.columna + direcciones[i][1];

            // El fantasma ignora los muros del tablero, pero validamos que no se salga de la pantalla
            if (nuevaFila >= 0 && nuevaFila < limiteFilas && nuevaColumna >= 0 && nuevaColumna < limiteColumnas) {
                // Distancia manual
                int diffFila = nuevaFila - targetFila;
                int diffColumna = nuevaColumna - targetColumna;

                int absFila = (diffFila < 0) ? -diffFila : diffFila;
                int absColumna = (diffColumna < 0) ? -diffColumna : diffColumna;

                int distancia = absFila + absColumna;

                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    mejorFila = nuevaFila;
                    mejorColumna = nuevaColumna;
                }
            }
        }

        this.fila = mejorFila;
        this.columna = mejorColumna;
    }

    // Reduce la salud del jugador
    public void atacar(Jugador j) {
        j.recibirDaño(this.daño);
    }

    // Detecta si las coordenadas del enemigo coinciden con las del jugador
    public boolean verificarColision(Jugador j) {

        return (this.fila == j.getFila() && this.columna == j.getColumna());
    }

    // Muestra en la consola la información del fantasma
    public void mostrarEstado() {
        System.out.println("--- ESTADO DEL ENEMIGO ---");
        System.out.println("Tipo: " + tipo);
        System.out.println("Posición: [" + fila + ", " + columna + "]");
        System.out.println("Daño: " + daño);
        System.out.println("Activo: " + (activo ? "SÍ" : "NO"));
    }

    public boolean isActivo() {
        return this.activo;
    }

    public int getFila() {
        return this.fila;
    }

    public int getColumna() {
        return this.columna;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public String getTipo() {
        return this.tipo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}