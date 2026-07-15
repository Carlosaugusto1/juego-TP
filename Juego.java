import java.util.Scanner;

public class Juego {
   
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

        while (filasIngresadas < 8 || columnasIngresadas < 8) {
            System.out.print("Introduce la cantidad de filas (mínimo 8): ");
            filasIngresadas = teclado.nextInt();
            System.out.print("Introduce la cantidad de columnas (mínimo 8): ");
            columnasIngresadas = teclado.nextInt();

            if (filasIngresadas < 8 || columnasIngresadas < 8) {
                System.out.println("⚠️ El tablero es demasiado pequeño. Por favor, introduce dimensiones de al menos 8x8.\n");
            }
        }

        teclado.nextLine(); 

       
        tablero = new Tablero(filasIngresadas, columnasIngresadas);
        tablero.generarTablero();
        tablero.agregarMuros();
        tablero.agregarPuntos();
        tablero.agregarPoderes();

        jugador = new Jugador("Pac-Man", 1, 2);

        generarEnemigos(3);

        controladorEnemigos = new ControlEnemigos(enemigos, jugador, tablero);

        juegoTerminado = false;
        System.out.println("\n🎉 ¡Tablero de " + filasIngresadas + "x" + columnasIngresadas + " creado con éxito! Iniciando juego...\n");
    }

    public void generarEnemigos(int cantidad) {
        enemigos = new Enemigo[cantidad];

        char[][] matrizMapa = tablero.getMatriz();
        int fMax = matrizMapa.length;
        int cMax = matrizMapa[0].length;

  
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

      
        for (Enemigo e : enemigos) {
            if (e != null && e.isActivo()) {
                matrizVisual[e.getFila()][e.getColumna()] = 'G';
            }
        }

        matrizVisual[jugador.getFila()][jugador.getColumna()] = 'P';

        System.out.println("\n=================================");
        tablero.mostrarTablero(matrizVisual);
        System.out.println("=================================");
    }

    public void mostrarEstado() {
        jugador.mostrarEstado();
    }

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

        if (dirFila == 0 && dirColumna == 0) {
            System.out.println("⚠️ Dirección no válida. Usa W, A, S o D.");
            return;
        }

        boolean seMovioAlMenosUnPaso = false;

        for (int paso = 1; paso <= pasos; paso++) {
            int proximaFila = jugador.getFila() + dirFila;
            int proximaColumna = jugador.getColumna() + dirColumna;

            if (tablero.esMovimientoValido(proximaFila, proximaColumna)) {

                jugador.moverPaso(dirFila, dirColumna);
                seMovioAlMenosUnPaso = true;

                char[][] matriz = tablero.getMatriz();
                int fActual = jugador.getFila();
                int cActual = jugador.getColumna();

                if (matriz[fActual][cActual] == '.') {
                    for (Punto p : tablero.getPuntos()) {
                        if (p != null && p.getFila() == fActual && p.getColumna() == cActual) {
                            jugador.recogerPunto(p);
                            matriz[fActual][cActual] = ' ';
                            break;
                        }
                    }
                }
                else if (matriz[fActual][cActual] == 'O') {
                    for (Poder pod : tablero.getPoderes()) {
                        if (pod != null && pod.getFila() == fActual && pod.getColumna() == cActual) {
                            jugador.usarPoder(pod);
                            matriz[fActual][cActual] = ' ';
                            break;
                        }
                    }
                }

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

                if (jugador.getFila() == 1 && jugador.getColumna() == 2) {
                    break;
                }

            } else {
                if (paso == 1) {
                    System.out.println("¡PARED! No puedes atravesar los muros.");
                } else {
                    System.out.println("¡PARED! Te detuviste en el segundo paso de velocidad.");
                }
                break;
            }
        }

        if (seMovioAlMenosUnPaso) {


            if (controladorEnemigos != null) {
                if (jugador.isPoderActivo() && jugador.getTipoPoderActivo().equalsIgnoreCase("Congelar")) {
                    System.out.println("❄️ ¡Los enemigos están congelados y no pueden moverse en este turno!");
                } else {
                    controladorEnemigos.moverEnemigos();
                }
            }

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

            jugador.actualizarTurnosPoder();
        }
    }

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
