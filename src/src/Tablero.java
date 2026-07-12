public class Tablero {
    private int filas;
    private int columnas;
    private char[][] matriz;
    private Muro[] muros;
    private Poder[] poderes;
    private int contadorMuros;
    private int contadorPoderes;

    // Constructor para definir el tamaño dinámico del mapa
    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.matriz = new char[filas][columnas];
        this.muros = new Muro[filas * columnas];
        this.poderes = new Poder[10];
        this.contadorMuros = 0;
        this.contadorPoderes = 0;
    }

    // Inicializa el tablero llenándolo con los puntos que Pac-Man debe recolectar
    public void generarTablero() {

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = '.';
            }
        }

        agregarMuros();
        agregarPoderes();
    }

    // Dibuja el mapa actual y la posición en tiempo real del jugador en la consola
    public void mostrarTablero(Jugador j) {
        char[][] vistaActual = new char[filas][columnas];
        for (int f = 0; f < filas; f++) {
            System.arraycopy(matriz[f], 0, vistaActual[f], 0, columnas);
        }

        // Si la posición del jugador es correcta, lo pintamos con una 'P'
        if (esMovimientoValido(j.getFila(), j.getColumna())) {
            vistaActual[j.getFila()][j.getColumna()] = 'P';
        }

        // Limpieza visual para evitar que el mapa se duplique infinitamente hacia abajo
        for (int i = 0; i < 25; i++) {
            System.out.println();
        }

        System.out.println("=== MAPA DE PAC-MAN ===");
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                System.out.print(vistaActual[f][c] + " ");
            }
            System.out.println();
        }
        System.out.println("=======================");
    }

    // Valida que los movimientos no excedan los límites de la matriz
    public boolean esMovimientoValido(int fila, int columna) {

        if (fila < 0 || fila >= filas ||
                columna < 0 || columna >= columnas) {

            return false;
        }

        return matriz[fila][columna] != '#';
    }
    // Genera los muros del mapa
    public void agregarMuros() {

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {

                if (i == 0 || i == filas - 1 ||
                        j == 0 || j == columnas - 1) {

                    matriz[i][j] = '#';

                    muros[contadorMuros] = new Muro(i, j);
                    contadorMuros++;
                }
            }
        }
    }

    // Coloca algunos poderes en el mapa
    public void agregarPoderes() {

        if (filas > 2 && columnas > 2) {

            matriz[1][1] = 'O';
            poderes[contadorPoderes++] =
                    new Poder("Pendiente",1,1,0);

            matriz[filas-2][columnas-2] = 'O';
            poderes[contadorPoderes++] =
                    new Poder("Pendiente",filas-2,columnas-2,0);
        }
    }

    // Métodos de acceso para obtener las dimensiones del mapa
    public int getFilas() {
        return this.filas;
    }

    public int getColumnas() {
        return this.columnas;
    }
}