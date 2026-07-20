import java.util.Scanner;

public class Juego {
    // ATRIBUTOS PRIVADOS
    private Jugador jugador;
    private Tablero tablero;
    private Enemigo[] enemigos;
    private ControlEnemigos controladorEnemigos; 
    private boolean juegoTerminado;
    private Scanner teclado = new Scanner(System.in);


    public void iniciarJuego() {
        System.out.println("=== CONFIGURACIÓN DE LA PARTIDA ===");

        int filasIngresadas = 0;
        int columnasIngresadas = 0;

       
        int limiteMinimo = 5;

        while (filasIngresadas < limiteMinimo || columnasIngresadas < limiteMinimo) {
            System.out.print("Introduce la cantidad de filas (mínimo " + limiteMinimo + "): ");
            filasIngresadas = teclado.nextInt();
            System.out.print("Introduce la cantidad de columnas (mínimo " + limiteMinimo + "): ");
            columnasIngresadas = teclado.nextInt();

            if (filasIngresadas < limiteMinimo || columnasIngresadas < limiteMinimo) {
                System.out.println("⚠️ El tablero es demasiado pequeño. Por favor, introduce dimensiones de al menos " + limiteMinimo + "x" + limiteMinimo + ".\n");
            }
        }

        teclado.nextLine();

        // Instanciar el tablero
        tablero = new Tablero(filasIngresadas, columnasIngresadas);
        tablero.generarTablero();
        tablero.agregarMuros();
        tablero.agregarPuntos();
        tablero.agregarPoderes();

        // Instanciar al Jugador
        jugador = new Jugador("Pac-Man", 1, 1);

        // Generar enemigos
        generarEnemigos(3);

        // INICIALIZAR EL CONTROLADOR DE ENEMIGOS
        controladorEnemigos = new ControlEnemigos(enemigos, jugador, tablero);

        juegoTerminado = false;
        System.out.println("\n🎉 ¡Tablero de " + filasIngresadas + "x" + columnasIngresadas + " creado con éxito! Iniciando juego...\n");
    }
    public void generarEnemigos(int cantidad) {
        enemigos = new Enemigo[cantidad];

        char[][] matrizMapa = tablero.getMatriz();
        int fMax = matrizMapa.length;
        int cMax = matrizMapa[0].length;

        // Enemigos distribuidos de forma segura
        enemigos[0] = new Enemigo("Perseguidor", fMax / 2, 2);
        enemigos[1] = new Enemigo("Aleatorio", fMax - 2, cMax - 2);
        enemigos[2] = new Enemigo("Fantasma", fMax - 2, 1);
    }


    public void actualizarTablero() {
        char[][] matrizReal = tablero.getMatriz();
        int filasTotal = matrizReal.length;
        int columnasTotal = matrizReal[0].length;

        char[][] matrizVisual = new char[filasTotal][columnasTotal];
        for (int i = 0; i < filasTotal; i++) {
            matrizVisual[i] = matrizReal[i].clone();
        }

        // 1. Pintamos primero a los enemigos activos usando la letra 'F'
        for (Enemigo e : enemigos) {
            if (e != null && e.isActivo()) {
                matrizVisual[e.getFila()][e.getColumna()] = 'F'; 
            }
        }

        // 2. Pintamos al jugador AL FINAL ('P')
        matrizVisual[jugador.getFila()][jugador.getColumna()] = 'P';

        // 3. Imprimimos el mapa actualizado
        System.out.println("\n=================================");
        tablero.mostrarTablero(matrizVisual);
        System.out.println("=================================");
    }

    // Muestra las estadísticas en tiempo real
    public void mostrarEstado() {
        jugador.mostrarEstado();
    }

    // Procesa las acciones de cada turno del juego
    public void ejecutarTurno() {
        System.out.print("\nMuévete (W: Arriba, S: Abajo, A: Izquierda, D: Derecha): ");
        String direccion = teclado.nextLine();

        int pasos = jugador.getVelocidad(); 
        int dirFila = 0;
        int dirColumna = 0;

        switch (direccion.toUpperCase()) {
            case "W": dirFila = -1; break;
            case "S": dirFila = 1; break;
            case "A": dirColumna = -1; break;
            case "D": dirColumna = 1; break;
        }

        // Si ingresa una tecla inválida
        if (dirFila == 0 && dirColumna == 0) {
            System.out.println("⚠️ Dirección no válida. Usa W, A, S o D.");
            return;
        }

        boolean seMovioAlMenosUnPaso = false;

        // BUCLE DE MOVIMIENTO PASO A PASO
        for (int paso = 1; paso <= pasos; paso++) {
            int proximaFila = jugador.getFila() + dirFila;
            int proximaColumna = jugador.getColumna() + dirColumna;

            // Validamos que el paso que vamos a dar sea legal (no haya muros)
            if (tablero.esMovimientoValido(proximaFila, proximaColumna)) {

                // Movemos físicamente al jugador un casillero
                jugador.moverPaso(dirFila, dirColumna);
                seMovioAlMenosUnPaso = true;

                char[][] matriz = tablero.getMatriz();
                int fActual = jugador.getFila();
                int cActual = jugador.getColumna();

                // 1. ¿Había un punto '.'?
                if (matriz[fActual][cActual] == '.') {
                    for (Punto p : tablero.getPuntos()) {
                        if (p != null && p.getFila() == fActual && p.getColumna() == cActual) {
                            jugador.recogerPunto(p);
                            matriz[fActual][cActual] = ' ';
                            break;
                        }
                    }
                }
              
                else if (matriz[fActual][cActual] == 'V' || matriz[fActual][cActual] == 'C' ||
                        matriz[fActual][cActual] == 'X' || matriz[fActual][cActual] == 'E' ||
                        matriz[fActual][cActual] == 'I') {

                    for (Poder pod : tablero.getPoderes()) {
                        if (pod != null && pod.getFila() == fActual && pod.getColumna() == cActual) {
                            jugador.usarPoder(pod);
                            matriz[fActual][cActual] = ' ';
                            break;
                        }
                    }
                }

                // Pac-Man los ataca
                for (Enemigo e : enemigos) {
                    if (e != null && e.isActivo() && e.verificarColision(jugador)) {

                        // Si tenemos el poder de comer fantasmas
                        if (jugador.isPoderActivo() && jugador.getTipoPoderActivo().equalsIgnoreCase("Cazador")) {
                            System.out.println("\n🔥 ¡CRUNCH! Te has devorado al enemigo [" + e.getTipo() + "]! 🔥");
                            e.setActivo(false); // Lo eliminamos del mapa

                            // Recompensamos al jugador con 200 puntos por la hazaña
                            jugador.recogerPunto(new Punto(e.getFila(), e.getColumna(), 200));
                        } else if (jugador.isPoderActivo() && jugador.getTipoPoderActivo().equalsIgnoreCase("Escudo")) {
                          
                            System.out.println("\n🛡️ ¡El escudo absorbió el golpe del enemigo [" + e.getTipo() + "]!");
                        } else {
                            // Si no tenemos poder, nos atacan de forma normal
                            e.atacar(jugador);
                            jugador.setFila(1);
                            jugador.setColumna(2);
                            break; 
                        }
                    }
                }

                // Si por culpa de una colisión fuimos devueltos a la salida, cancelamos el segundo paso de velocidad
                if (jugador.getFila() == 1 && jugador.getColumna() == 2) {
                    break;
                }

            } else {
                if (paso == 1) {
                    System.out.println("¡PARED! No puedes atravesar los muros.");
                } else {
                    System.out.println("¡PARED! Te detuviste en el segundo paso de velocidad.");
                }
                break; // Si choca con una pared en el camino, se frena inmediatamente
            }
        }

        // Si el jugador realizó con éxito su acción de movimiento en este turno
        if (seMovioAlMenosUnPaso) {
            // ENEMIGOS
           
            if (controladorEnemigos != null) {
                // Si el poder de congelar está activo, los enemigos pierden su turno
                if (jugador.isPoderActivo() && jugador.getTipoPoderActivo().equalsIgnoreCase("Congelar")) {
                    System.out.println("❄️ ¡Los enemigos están congelados y no pueden moverse en este turno!");
                } else {
                    controladorEnemigos.moverEnemigos();
                }
            }

            // Volvemos a comprobar colisiones (Por si un enemigo se movió a nuestra misma casilla en su turno)
            for (Enemigo e : enemigos) {
                if (e != null && e.isActivo() && e.verificarColision(jugador)) {
                    if (jugador.isPoderActivo() && jugador.getTipoPoderActivo().equalsIgnoreCase("Cazador")) {
                        System.out.println("\n🔥 ¡CRUNCH! Te has devorado al enemigo [" + e.getTipo() + "]! 🔥");
                        e.setActivo(false);
                        jugador.recogerPunto(new Punto(e.getFila(), e.getColumna(), 200));
                    } else if (jugador.isPoderActivo() && jugador.getTipoPoderActivo().equalsIgnoreCase("Escudo")) {
                        System.out.println("\n🛡️ ¡El escudo absorbió el golpe del enemigo [" + e.getTipo() + "]!");
                    } else {
                        e.atacar(jugador);
                        jugador.setFila(1);
                        jugador.setColumna(2);
                        break;
                    }
                }
            }

            // Descontamos un turno de la duración de nuestro poder activo al finalizar la acción
            jugador.actualizarTurnosPoder();
        }
    }

    // Determina si ganaste o perdiste
    public void verificarFinJuego() {
        if (!jugador.estaVivo()) {
            juegoTerminado = true;
            System.out.println("\n💀 ¡GAME OVER! Los fantasmas te atraparon. 💀");
            return;
        }

        boolean quedanPuntos = false;
        char[][] matriz = tablero.getMatriz();
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] == '.') {
                    quedanPuntos = true;
                    break;
                }
            }
        }

        if (!quedanPuntos) {
            juegoTerminado = true;
            System.out.println("\n🏆 ¡FELICIDADES! Has recolectado todos los puntos del tablero. 🏆");
        }
    }
    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }
}
