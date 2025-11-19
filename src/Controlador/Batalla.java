package Controlador;

import Modelo.Heroe;
import Modelo.Villano;

/**
 *
 * @author artor
 */
public class Batalla {

    private Heroe heroe;
    private Villano villano;
    private boolean turnoHeroe;
    private boolean batallaTerminada;

    public Batalla() {
    }

    public Batalla(Heroe heroe, Villano villano, boolean turnoHeroe, boolean batallaTerminada) {
        this.heroe = heroe;
        this.villano = villano;
        this.turnoHeroe = turnoHeroe;
        this.batallaTerminada = batallaTerminada;
    }

    public void iniciarBatalla(Heroe heroe, Villano villano) {
        this.heroe = heroe;
        this.villano = villano;
        this.turnoHeroe = true;
        this.batallaTerminada = false;
        heroe.setGuardiaActiva(false);
        villano.setGuardiaActiva(false);

    }

    public String turnoHeroe(int eleccionboton) {
        if (batallaTerminada || !turnoHeroe) {
            return "";
        }
        heroe.setGuardiaActiva(false);
        String resultado = "";
        switch (eleccionboton) { //<---generarlo desde la interfaz grafica
            case 1 -> {
                heroe.atacarFisico(villano);
                resultado = heroe.getNombre() + " atacó físicamente a " + villano.getNombre() + "!";
            }
            case 2 -> {
                if (heroe.ataqueEspecial(villano)) {
                    resultado = heroe.getNombre() + " " + heroe.getTextoATQespecial() + "!";
                } else {
                    heroe.atacarFisico(villano);
                    resultado = " No hay suficiente energía especial," + heroe.getNombre() + " golpea resignado.";
                }
            }
            case 3 -> {
                heroe.enGuardia();
                resultado = heroe.getNombre() + " se puso en guardia️";
            }
            default -> {
                resultado = "Accion invalida";
            }
        }

        if (villano.getHp() <= 0) {

            batallaTerminada = true;
            resultado += "\n " + villano.getNombre() + " ha sido derrotado!";
        } else {
            turnoHeroe = false;
        }

        return resultado;
    }

    public String turnoVillano() {
        if (batallaTerminada || turnoHeroe) {
            return "";
        }
        String resultado = "";

        double porcentajeVida = (double) villano.getHp() / villano.getHpMax();

        if (porcentajeVida > 0.6) {
            if (Math.random() > 0.5) {
                if (villano.ataqueEspecial(heroe)) {
                    resultado = villano.getNombre() + " " + villano.getTextoATQespecial() + "!";
                } else {

                    villano.atacarFisico(heroe);
                    resultado = villano.getNombre() + " atacó fisicamente a " + heroe.getNombre();
                }
            } else {
                villano.atacarFisico(heroe);
                resultado = villano.getNombre() + " atacó fisicamente a " + heroe.getNombre();
            }

        } else {
            if (Math.random() < 0.5) {
                villano.enGuardia();
                resultado = villano.getNombre() + " se puso en guardia. ️";
            } else if (Math.random() < 0.8) {
                villano.atacarFisico(heroe);
                resultado = villano.getNombre() + " atacó fisicamente a " + heroe.getNombre();
            } else {
                if (villano.ataqueEspecial(heroe)) {
                    resultado = villano.getNombre() + " " + villano.getTextoATQespecial() + "!";
                } else {

                    villano.atacarFisico(heroe);
                    resultado = villano.getNombre() + " atacó fisicamente a " + heroe.getNombre();
                }
            }

        }
        if (heroe.getHp() <= 0) {
            heroe.setHp(0);
            batallaTerminada = true;
            resultado += "\n " + heroe.getNombre() + " ha caído en combate!";
        } else {
            turnoHeroe = true;
        }
        return resultado;
    }

//    public String getEstadoBatalla() {
//        return heroe.getNombre() + ": " + heroe.getHp() + "/" + heroe.getHpMax()
//                + "   |   "
//                + villano.getNombre() + ": " + villano.getHp() + "/" + villano.getHpMax();
//    }

    public boolean comprobadadorHeroe() {
        return villano.getHp() == 0;
    }

    public boolean comprobadadorVillano() {
        return villano.getHp() == 0;
    }

    public Heroe getHeroe() {
        return heroe;
    }

    public void setHeroe(Heroe heroe) {
        this.heroe = heroe;
    }

    public Villano getVillano() {
        return villano;
    }

    public void setVillano(Villano villano) {
        this.villano = villano;
    }

    public boolean isTurnoHeroe() {
        return turnoHeroe;
    }

    public void setTurnoHeroe(boolean turnoHeroe) {
        this.turnoHeroe = turnoHeroe;
    }

    public boolean isBatallaTerminada() {
        return batallaTerminada;
    }

    public void setBatallaTerminada(boolean batallaTerminada) {
        this.batallaTerminada = batallaTerminada;
    }

}
