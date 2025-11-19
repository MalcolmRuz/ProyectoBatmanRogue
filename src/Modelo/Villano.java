package Modelo;

/**
 *
 * @author artor
 */
public class Villano extends Personaje {

    private int indiceDificultad;
    private boolean EsJefe;

    public Villano() {
    }

    public Villano(int indiceDificultad, boolean EsJefe, String Nombre, String textoATQespecial, String imagenPath, int ataque, int defensa, int hp, int hpMax, int especial, int inteligencia, int costoEspecial, boolean guardiaActiva) {
        super(Nombre, textoATQespecial, imagenPath, ataque, defensa, hp, hpMax, especial, inteligencia, costoEspecial, guardiaActiva);
        this.indiceDificultad = indiceDificultad;
        this.EsJefe = EsJefe;
    }

    public int getIndiceDificultad() {
        return indiceDificultad;
    }

    public void setIndiceDificultad(int indiceDificultad) {
        this.indiceDificultad = indiceDificultad;
    }

    public boolean isEsJefe() {
        return EsJefe;
    }

    public void setEsJefe(boolean EsJefe) {
        this.EsJefe = EsJefe;
    }

    @Override
    public void atacarFisico(Personaje objetivo) {
        int dannio = calculateDamage(this.ataque, objetivo.getDefensa());
        if (objetivo.isGuardiaActiva()) {
            dannio = dannio / 2;
        }
        objetivo.setHp(Math.max(0, objetivo.getHp() - dannio));

        if (objetivo.isGuardiaActiva()) {
            objetivo.setGuardiaActiva(false);
        }
    }

    @Override
    public void enGuardia() {
        if (!this.isGuardiaActiva()) {
            this.setGuardiaActiva(true);
        }
    }

    @Override
    public boolean ataqueEspecial(Personaje objetivo) {
        if (this.getEspecial() < this.costoEspecial) {
            return false;
        }
        int dannio = calcularDañoEspecial(this.inteligencia, objetivo.getInteligencia());
        if (objetivo.isGuardiaActiva()) {
            dannio = dannio / 2;
        }
        objetivo.setHp(Math.max(0, objetivo.getHp() - dannio));

        if (objetivo.isGuardiaActiva()) {
            objetivo.setGuardiaActiva(false);

        }
        return true;

    }
}
