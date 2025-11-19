package Modelo;

/**
 *
 * @author artor
 */
public class Heroe extends Personaje {

    private int nivel;
    private Modelo.Usuario usuarioAsociado;

    public Heroe() {
    }

    public Heroe(String Nombre, String textoATQespecial, String imagenPath, int ataque, int defensa, int hp, int hpMax, int especial, int inteligencia, int costoEspecial, boolean guardiaActiva, int nivel, Usuario usuarioAsociado) {
        super(Nombre, textoATQespecial, imagenPath, ataque, defensa, hp, hpMax, especial, inteligencia, costoEspecial, guardiaActiva);
        this.nivel = nivel;
        this.usuarioAsociado = usuarioAsociado;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public Usuario getUsuarioAsociado() {
        return this.usuarioAsociado;
    }

    public void setUsuarioAsociado(Usuario usuarioAsociado) {
        this.usuarioAsociado = usuarioAsociado;
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
        this.especial = this.especial - this.costoEspecial;
        return true;
    }

    public void subirNivel() {
        this.nivel++;
        this.setHpMax(getHpMax() + 10);
        this.setAtaque(getAtaque() + 3);
        this.setDefensa(getDefensa() + 1);
        this.setHp(getHpMax());
        this.setEspecial(getEspecial() + 2);
        this.setInteligencia(getInteligencia() + 3);

    }
}
