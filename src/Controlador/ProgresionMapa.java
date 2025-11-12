package Controlador;

import Modelo.Heroe;
import Modelo.Villano;

/**
 * Controla la progresión del héroe a través de los pisos del mapa. Cada 3
 * pisos, el héroe sube de nivel automáticamente. El ciclo continúa hasta que el
 * héroe muera o se alcance el piso máximo. Usa la clase Batalla para los
 * combates y Encuentro para generar enemigos.
 *
 * @author artor
 */
public class ProgresionMapa {

    private int pisoActual;
    private final int PISO_MAX = 30;
    private Encuentro encuentro;
    private Batalla batalla;
    private Heroe heroe;

    public ProgresionMapa(int pisoActual, Encuentro encuentro, Batalla batalla, Heroe heroe) {
        this.pisoActual = pisoActual;
        this.encuentro = encuentro;
        this.batalla = batalla;
        this.heroe = heroe;
    }

    public ProgresionMapa() {
    }
    
    public int getPisoActual() {
        return pisoActual;
    }             

    public void setPisoActual(int pisoActual) {
        this.pisoActual = pisoActual;
    } 

    // --- MÉTODO PRINCIPAL DE PROGRESIÓN ---
    public void progresion() {                                    // Controla el avance del héroe piso por piso

        while (heroe.getHp() > 0 && pisoActual <= PISO_MAX) {     // Bucle: sigue mientras el héroe esté vivo y no supere el máximo

            Villano villanoActual = encuentro.generarVillano(this.pisoActual);   

            batalla.batalla(heroe, villanoActual);                 

            if (heroe.getHp() > 0) {                              
                pisoActual++;                                     
                comprobarSubidaPorPisos();                       
            } else {
                //System.out.println("💀 Has caído en el piso " + pisoActual + "..."); // Si muere, fin del ciclo <-------no usar sout. se puede guardar ese mensage en un string para que lo mande la consola en la interfaz
                derrota();
                break;
            }
        }

        if (heroe.getHp() > 0 && pisoActual > PISO_MAX) {
            victoria(); // Si llega al final, gana
        } else if (heroe.getHp() <= 0) {
            derrota();                     
        }
    }

    private void comprobarSubidaPorPisos() {                       
        if (pisoActual % 3 == 0) {
            heroe.subirNivel(); 
           
        }
    }

    private void victoria() {                                      // <---------------- POR IMPLEMENTAR mensaje de victoria final
    }

    private void derrota() {                                       // <-----------------POR IMPLEMENTAR mensaje de derrota
    }
}
