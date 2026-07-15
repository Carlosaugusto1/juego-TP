public class Tablero {

    private int filas;
    private int columnas;
    private char[][] matriz;
    private Muro[] muros;
    private Punto[] puntos;
    private Poder[] poderes;

    private int contadorMuros = 0;
    private int contadorPuntos = 0;
    private int contadorPoderes = 0;

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.matriz = new char[filas][columnas];

        int totalCasilleros = filas * columnas;
        this.muros = new Muro[totalCasilleros];
        this.puntos = new Punto[totalCasilleros];
        this.poderes = new Poder[10];
    }

    public void generarTablero() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = ' ';
            }
        }
    }

    public void agregarMuros() {
        // Calculamos los centros matemáticos del mapa
        int centroFila = filas / 2;
        int centroColumna = columnas / 2;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {

                // 1. Bordes perimetrales exteriores
                boolean esBorde = (i == 0 || i == filas - 1 || j == 0 || j == columnas - 1);

                // 2. Línea horizontal de la cruz (en el centro, recortada a los lados)
                boolean esHorizontalCruz = (i == centroFila && j > 2 && j < columnas - 3);

                // 3. Eje vertical justo en el centro de la cruz (muro de tamaño 3)
                boolean esCentroVerticalCruz = (j == centroColumna && i >= centroFila - 1 && i <= centroFila + 1);

                // 4. Segmento vertical superior (deja un pasaje libre antes de tocar la cruz)
                boolean esVerticalSuperior = (j == centroColumna && i >= 1 && i < centroFila - 2);

                // 5. Segmento vertical inferior (deja un pasaje libre después de la cruz)
                boolean esVerticalInferior = (j == centroColumna && i > centroFila + 2 && i < filas - 1);

                // Si se cumple cualquiera de estas condiciones, colocamos un bloque sólido
                if (esBorde || esHorizontalCruz || esCentroVerticalCruz || esVerticalSuperior || esVerticalInferior) {
                    matriz[i][j] = '#';

                    // Guardamos el objeto Muro en el arreglo obligatorio para colisiones
                    muros[contadorMuros] = new Muro(i, j);
                    contadorMuros++;
                }
            }
        }
    }

    public void agregarPuntos() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (matriz[i][j] == ' ') {
                    matriz[i][j] = '.';
                    puntos[contadorPuntos] = new Punto(i, j, 10);
                    contadorPuntos++;
                }
            }
        }
    }

    public void agregarPoderes() {
        // Poder 1: Velocidad (Dura 5 turnos)
        if (matriz[1][1] == '.') {
            matriz[1][1] = 'O';
            poderes[contadorPoderes] = new Poder(1, 1, "Velocidad", 5);
            contadorPoderes++;
        }

        // Poder 2: Congelar (Dura 5 turnos)
        if (matriz[filas - 2][columnas - 2] == '.') {
            matriz[filas - 2][columnas - 2] = 'O';
            poderes[contadorPoderes] = new Poder(filas - 2, columnas - 2, "Congelar", 5);
            contadorPoderes++;
        }

        // Poder 3: Cazador (Dura 6 turnos para dar tiempo de perseguir fantasmas)
        if (matriz[1][columnas - 2] == '.') {
            matriz[1][columnas - 2] = 'O';
            poderes[contadorPoderes] = new Poder(1, columnas - 2, "Cazador", 6);
            contadorPoderes++;
        }

        // Poder 4: Escudo (Dura 4 turnos) - Esquina inferior izquierda, espejo del Poder 1
        if (matriz[filas - 2][1] == '.') {
            matriz[filas - 2][1] = 'O';
            poderes[contadorPoderes] = new Poder(filas - 2, 1, "Escudo", 4);
            contadorPoderes++;
        }

        // Poder 5: Invisibilidad (Dura 5 turnos) - Columna izquierda, altura del centro del mapa
        if (matriz[filas / 2][1] == '.') {
            matriz[filas / 2][1] = 'O';
            poderes[contadorPoderes] = new Poder(filas / 2, 1, "Invisibilidad", 5);
            contadorPoderes++;
        }
    }
    public Poder[] getPoderes() {
        return poderes;
    }

    public void mostrarTablero() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Igual que mostrarTablero(), pero imprime una matriz externa (por ejemplo, una copia
    // "pintada" con el jugador y los enemigos) sin depender del atributo interno 'matriz'.
    public void mostrarTablero(char[][] matrizAMostrar) {
        for (int i = 0; i < matrizAMostrar.length; i++) {
            for (int j = 0; j < matrizAMostrar[i].length; j++) {
                System.out.print(matrizAMostrar[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean esMovimientoValido(int fila, int columna) {

        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            return false;
        }

        if (matriz[fila][columna] == '#') {
            return false;
        }
        return true;
    }

    public char[][] getMatriz() {
        return matriz;
    }

    public Punto[] getPuntos() {
        return puntos;
    }

    public int getContadorPuntos() {
        return contadorPuntos;
    }
}