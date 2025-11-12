package Controlador;

import Modelo.Heroe;
import Modelo.Villano;

/**
 * Controla la progresión del héroe a través de los pisos del mapa.
 * Cada 3 pisos, el héroe sube de nivel automáticamente.
 * El ciclo continúa hasta que el héroe muera o se alcance el piso máximo.
 * Usa la clase Batalla para los combates y Encuentro para generar enemigos.
 * @author artor
 */
public class ProgresionMapa {

    private int pisoActual;
    private final int PISO_MAX = 30;
    private Encuentro encuentro;
    private Batalla batalla;
    private Heroe heroe;

    public ProgresionMapa(Heroe heroe) {                         // Constructor principal
        this.heroe = heroe;
        this.pisoActual = 1;
        this.encuentro = new Encuentro();
        this.batalla = new Batalla();
    }

    public ProgresionMapa(int pisoActual, Heroe heroe) {          // Constructor con piso inicial
        this.pisoActual = pisoActual;
        this.heroe = heroe;
        this.encuentro = new Encuentro();
        this.batalla = new Batalla();
    }

    public int getPisoActual() { return pisoActual; }             // Getter piso actual
    public void setPisoActual(int pisoActual) { this.pisoActual = pisoActual; } // Setter piso actual

    // --- MÉTODO PRINCIPAL DE PROGRESIÓN ---
    public void progresion() {                                    // Controla el avance del héroe piso por piso

        while (heroe.getHp() > 0 && pisoActual <= PISO_MAX) {     // Bucle: sigue mientras el héroe esté vivo y no supere el máximo

            System.out.println("\n=== 🌄 Piso " + pisoActual + " ===");

            Villano villanoActual = encuentro.generarVillano();   // Generar enemigo aleatorio
            System.out.println("👹 Aparece un nuevo enemigo: " + villanoActual.getNombre());

            batalla.batalla(heroe, villanoActual);                // Iniciar batalla (usa clase Batalla, no se modifica)

            if (heroe.getHp() > 0) {                              // Si el héroe sobrevive
                System.out.println("✅ Has superado el piso " + pisoActual + "!");
                pisoActual++;                                     // Avanzar al siguiente piso
                comprobarSubidaPorPisos();                        // Subir nivel cada 3 pisos
            } else {
                System.out.println("💀 Has caído en el piso " + pisoActual + "..."); // Si muere, fin del ciclo
                break;
            }
        }

        if (heroe.getHp() > 0 && pisoActual > PISO_MAX) victoria(); // Si llega al final, gana
        else if (heroe.getHp() <= 0) derrota();                     // Si muere, pierde
    }

    private void comprobarSubidaPorPisos() {                       // Subir nivel cada 3 pisos
        if (pisoActual % 3 == 0) {
            heroe.subirNivel();
            System.out.println("⭐ ¡Has subido de nivel al alcanzar el piso " + pisoActual + "!");
        }
    }

    private void victoria() {                                      // Mensaje de victoria final
        System.out.println("🏆 ¡Felicidades! Has superado los " + PISO_MAX + " pisos del mapa!");
    }

    private void derrota() {                                       // Mensaje de derrota
        System.out.println("❌ Fin de la aventura. Intenta de nuevo.");
    }
}
