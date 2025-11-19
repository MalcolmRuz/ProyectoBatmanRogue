package Modelo;
/**
 *
 * @author nicoa
 */
public class Usuario {
    
    private String nombreUsuario,genero;
    private int edad,puntaje;

    public Usuario() {
    }

    public Usuario(String nombreUsuario, String genero, int edad, int puntaje) {
        this.nombreUsuario = nombreUsuario;
        this.genero = genero;
        this.edad = edad;
        this.puntaje = puntaje;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    

    @Override
    public String toString() {
        return nombreUsuario;
    }

    
    
    
}