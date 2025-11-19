package Controlador;

import Modelo.Heroe;
import Modelo.Usuario;
import Modelo.Villano;
import Vista.Form_Pelea;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Controla la progresión del héroe a través de los pisos del mapa. Cada 3
 * pisos, el héroe sube de nivel automáticamente. Usa la clase Batalla para los
 * combates y Encuentro para generar enemigos.
 *
 * @author artor
 */
public class ProgresionMapa {

    private int pisoActual;
    private final int PISO_MAX = 30;
    private int puntosPorPiso = 20;
    private Encuentro encuentro;
    private Batalla batalla;
    private Heroe heroe;
    private Villano villanoActual;
    private JButton botonContinuar; // se asocia a la GUI
    private ActionLog log;
    private Form_Pelea formPelea;

    public ProgresionMapa() {
    }

    public ProgresionMapa(int pisoActual, Encuentro encuentro, Batalla batalla, Heroe heroe, Villano villanoActual, JButton botonContinuar, ActionLog log) {
        this.pisoActual = pisoActual;
        this.encuentro = encuentro;
        this.batalla = batalla;
        this.heroe = heroe;
        this.villanoActual = villanoActual;
        this.botonContinuar = botonContinuar;
        this.log = log;
    }

    public int getPisoActual() {
        return pisoActual;
    }

    public void setPisoActual(int pisoActual) {
        this.pisoActual = pisoActual;
    }

    public Encuentro getEncuentro() {
        return encuentro;
    }

    public void setEncuentro(Encuentro encuentro) {
        this.encuentro = encuentro;
    }

    public Batalla getBatalla() {
        return batalla;
    }

    public void setBatalla(Batalla batalla) {
        this.batalla = batalla;
    }

    public Heroe getHeroe() {
        return heroe;
    }

    public void setHeroe(Heroe heroe) {
        this.heroe = heroe;
    }

    public Villano getVillanoActual() {
        return villanoActual;
    }

    public void setVillanoActual(Villano villanoActual) {
        this.villanoActual = villanoActual;
    }

    public JButton getBotonContinuar() {
        return botonContinuar;
    }

    public void setBotonContinuar(JButton botonContinuar) {
        this.botonContinuar = botonContinuar;
    }

    public ActionLog getLog() {
        return log;
    }

    public void setLog(ActionLog log) {
        this.log = log;
    }

    @Override
    public String toString() {
        return "ProgresionMapa{" + "pisoActual=" + pisoActual + ", PISO_MAX=" + PISO_MAX + ", puntosPorPiso=" + puntosPorPiso + ", encuentro=" + encuentro + ", batalla=" + batalla + ", heroe=" + heroe + ", villanoActual=" + villanoActual + ", botonContinuar=" + botonContinuar + ", log=" + log + ", formPelea=" + formPelea + '}';
    }

    public void setFormPelea(Form_Pelea formPelea) {
        this.formPelea = formPelea;
    }

    // METODO PRINCIPAL DE PROGRESION 
    public void iniciarEncuentro(Heroe heroe) {

        //verificar si el heroe está derrotado o si ha ganado el juego
        if (heroe.getHp() <= 0) {
            derrota();
            return;
        }
        if (pisoActual > PISO_MAX) {
            victoria();
            return;
        }

        //GENERAR el villano para el piso actual
        Villano nuevoVillano = encuentro.generarVillano(pisoActual);

        // verificar si la generación fallo (si la query devolvio 0 resultados)
        if (nuevoVillano == null) {
            // este mensaje solo se imprime si la DB realmente no tiene datos para el piso
            System.out.println("No se pudo generar villano. La DB no contiene un enemigo válido para el piso " + pisoActual + ".");
            return;
        }

        // si el villano se genero correctamente, iniciar la batalla
        this.villanoActual = nuevoVillano;
        batalla.iniciarBatalla(heroe, villanoActual);

        log.limpiar(); //se limpia el log al inicio de cada nuevo encuentro
        log.agregarLinea("¡Encuentro en el Piso " + this.pisoActual + "! - ¡Ha aparecido " + villanoActual.getNombre() + "!");

        // desactivar boton “Continuar” mientras hay batalla
        if (botonContinuar != null) {
            botonContinuar.setEnabled(false);
        }

        if (formPelea != null) {
            formPelea.actualizarVillano();
        }
    }

    public void turnoHeroe(int eleccionBoton) {
        if (batalla == null || villanoActual == null) {
            return;
        }
        jugadorAccion(eleccionBoton); // se llama a jugadoAccion para centralizar la logica del turno
    }

    //se llama desde el botón "Atacar", "Especial" o "Defender"
    public void jugadorAccion(int eleccionBoton) {
        if (batalla == null || villanoActual == null || !batalla.isTurnoHeroe()) {
            return;
        }
        if (formPelea != null) {
            formPelea.deshabilitarBotonesAccion();
        }

// TURNO DEL HÉROE
        String resultadoHeroe = batalla.turnoHeroe(eleccionBoton);

        if (!resultadoHeroe.isEmpty()) {
            log.agregarLinea(resultadoHeroe);
        }

        // actualiza la HP del Villano inmediatamente después del golpe del Héroe
        if (formPelea != null) {
            formPelea.actualizarHPVillanoLabel();
        }

        if (batalla.isBatallaTerminada()) {
            verificarResultadoBatalla();
            return;
        }

        // TURNO DEL VILLANO (Con retraso para que el usuario pueda leer)
        Timer timer = new Timer(1000, e -> { // 1000 ms = 1 segundo de espera

            // Si la batalla no terminó con el héroe, sigue el villano
            if (!batalla.isBatallaTerminada()) {

                String respuestaVillano = batalla.turnoVillano();

                // 3. Log del Villano
                // El log ya usa agregarLinea, lo cual está bien.
                if (!respuestaVillano.isEmpty()) {
                    log.agregarLinea(respuestaVillano);
                }
            }

            // Actualiza la HP del Héroe después del golpe del Villano
            if (formPelea != null) {
                formPelea.actualizarHPLabel();
            }

            if (batalla.isBatallaTerminada()) {
                verificarResultadoBatalla();
            }
            if (!batalla.isBatallaTerminada() && formPelea != null) {

                SwingUtilities.invokeLater(() -> {
                    formPelea.habilitarBotonesAccion();
                });
            }

            // Detener el timer después de una ejecución
            ((Timer) e.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }
// Controla el fin de una batalla 

    private void verificarResultadoBatalla() {
        // los métodos comprobadadorHeroe y comprobadadorVillano deberían
        // verificar el HP del personaje OPUESTO (Villano y Héroe respectivamente).
        // Asumiendo que internamente ya hacen la verificación correcta:

        // if (batalla.comprobadadorHeroe()) // Debería ser algo como heroe.getHp() <= 0
        if (heroe.getHp() <= 0) {
            derrota();
        } // else if (batalla.comprobadadorVillano()) // Debería ser algo como villanoActual.getHp() <= 0
        else if (villanoActual.getHp() <= 0) {

            // Activar botón “Continuar” para avanzar de piso manualmente
            if (botonContinuar != null) {
                SwingUtilities.invokeLater(() -> {
                    botonContinuar.setEnabled(true);
                    botonContinuar.setVisible(true); // Asegurar que sea visible si lo ocultaste antes
                });
            }
        }
    }

    // Llamado desde el botón “Continuar” 
    public void avanzarDePiso() {

        //DEBUG comprobar el usuario asociado
        if (heroe == null || heroe.getUsuarioAsociado() == null) {
            log.agregarLinea("No se pudo actualizar el puntaje porque no hay un usuario asociado al heroe");
            pisoActual++; //avanza de piso para continuar el juego
        } else {
            Usuario usuario = heroe.getUsuarioAsociado();

            // ---CÁLCULO DE PUNTOS GANADOS ---
            // 40 puntos si es Jefe, sino el puntaje base por piso (20 por defecto)
            int puntosGanados = (this.villanoActual != null && this.villanoActual.isEsJefe()) ? 40 : puntosPorPiso;

            if (encuentro != null) {

                // --- OBTENCIÓN DEL PUNTAJE BASE FRESCO (Sincronización) ---
                // Leemos el puntaje actual directamente de la BD (es el valor más confiable).
                int puntajeActualBD = encuentro.obtenerPuntaje(usuario.getNombreUsuario());

                // Si la BD devuelve un valor de error (-1 o similar), usamos el puntaje que está en memoria.
                if (puntajeActualBD < 0) {
                    puntajeActualBD = usuario.getPuntaje();
                }

                // Cálculo final: (Puntaje confiable) + (Puntos obtenidos en la pelea actual)
                int nuevoPuntaje = puntajeActualBD + puntosGanados;

                // --- ACTUALIZACIÓN Y PERSISTENCIA ---
                // Guardamos el puntaje total en la Base de Datos
                encuentro.actualizarPuntaje(usuario.getNombreUsuario(), nuevoPuntaje);

                // Recargamos la instancia de Usuario para la Memoria (Memoria <-- BD)
                // Esto garantiza que la próxima vez que se llame a heroe.getUsuarioAsociado().getPuntaje(), 
                // devuelva el valor correcto (40, 60, etc.) en lugar de 0.
                Usuario usuarioRecargado = encuentro.buscarPorNombreUsuario(usuario.getNombreUsuario());

                if (usuarioRecargado != null) {
                    heroe.setUsuarioAsociado(usuarioRecargado);
                }

                pisoActual++;
            }
        }

        comprobarSubidaPorPisos();

        if (pisoActual <= PISO_MAX) {
            // Ocultar botón continuar y empezar el siguiente encuentro
            if (botonContinuar != null) {
                botonContinuar.setEnabled(false);
                botonContinuar.setVisible(false);
            }
            iniciarEncuentro(this.heroe);
            if (formPelea != null) {
                formPelea.habilitarBotonesAccion();
            }
        } else {
            victoria();
        }
    }

    private void comprobarSubidaPorPisos() {
        if (pisoActual % 3 == 0) {
            heroe.subirNivel();
            log.agregarLinea(heroe.getNombre() + " ha subido al nivel " + heroe.getNivel() + "!");
        }
    }

    private void victoria() {
        log.agregarLinea("\n¡🎉 FELICIDADES! Has derrotado a todos los villanos y ganado el juego.");
    }

    private void derrota() {
        log.agregarLinea("\n☠️ GAME OVER. " + heroe.getNombre() + " ha sido derrotado.");
    }

}
