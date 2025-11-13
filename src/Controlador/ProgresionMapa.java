package Controlador;

import Modelo.Heroe;
import Modelo.Villano;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

/**
 * Controla la progresión del héroe a través de los pisos del mapa.
 * Cada 3 pisos, el héroe sube de nivel automáticamente.
 * Usa la clase Batalla para los combates y Encuentro para generar enemigos.
 *
 * @author artor
 */
public class ProgresionMapa {

    private int pisoActual;
    private final int PISO_MAX = 30;
    private Encuentro encuentro;
    private Batalla batalla;
    private Heroe heroe;
    private Villano villanoActual;
    private JButton botonContinuar; // 🔹 se asocia a la GUI

    public ProgresionMapa(int pisoActual, Encuentro encuentro, Batalla batalla, Heroe heroe) {
        this.pisoActual = pisoActual;
        this.encuentro = encuentro;
        this.batalla = batalla;
        this.heroe = heroe;
    }

    public ProgresionMapa() {
    }

    public void setBotonContinuar(JButton botonContinuar) {
        this.botonContinuar = botonContinuar;
    }

    public int getPisoActual() {
        return pisoActual;
    }

    public void setPisoActual(int pisoActual) {
        this.pisoActual = pisoActual;
    }

    // --- MÉTODO PRINCIPAL DE PROGRESIÓN ---
    public void iniciarEncuentro() {
        if (heroe.getHp() <= 0) {
            derrota();
            return;
        }
        if (pisoActual > PISO_MAX) {
            victoria();
            return;
        }

        villanoActual = encuentro.generarVillano(pisoActual);
        batalla.iniciarBatalla(heroe, villanoActual);
        

        // 🔹 Desactivar botón “Continuar” mientras hay batalla
        if (botonContinuar != null) {
            botonContinuar.setEnabled(false);
        }
    }

    // --- Se llama desde el botón "Atacar", "Especial" o "Defender" ---
    public void turnoHeroe(int eleccionBoton) {
        if (batalla == null || villanoActual == null) return;

        String resultado = batalla.turnoHeroe(eleccionBoton);
        System.out.println(resultado);

        if (batalla.isBatallaTerminada()) {
            verificarResultadoBatalla();
            return;
        }

        // Esperar un poco antes del contraataque enemigo (sin bloquear GUI)
        javax.swing.Timer timer = new javax.swing.Timer(1000, e -> {
            String respuestaVillano = batalla.turnoVillano();
            System.out.println(respuestaVillano);

            if (batalla.isBatallaTerminada()) {
                verificarResultadoBatalla();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // --- 🔹 Controla el fin de una batalla ---
    private void verificarResultadoBatalla() {
        if (batalla.comprobadadorHeroe()) {
            derrota();
        } else if (batalla.comprobadadorVillano()) {
            

            // 🔹 Activar botón “Continuar” para avanzar de piso manualmente
            if (botonContinuar != null) {
                SwingUtilities.invokeLater(() -> botonContinuar.setEnabled(true));
            }
        }
    }

    // --- 🔹 Llamado desde el botón “Continuar” ---
    public void avanzarDePiso() {
        pisoActual++;
        comprobarSubidaPorPisos();

        if (pisoActual <= PISO_MAX) {
            
            iniciarEncuentro();
        } else {
            victoria();
        }
    }

    private void comprobarSubidaPorPisos() {
        if (pisoActual % 3 == 0) {
            heroe.subirNivel();
            
        }
    }

    private void victoria() {
        
    }

    private void derrota() {
        
    }
}