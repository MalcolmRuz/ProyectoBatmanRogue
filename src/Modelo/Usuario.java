package Modelo;
/**
 *
 * @author nicoa
 */
public class Usuario {
    
    private String nombreUsuario,genero;
    private int edad;

    public Usuario() {
    }

    public Usuario(String nombreUsuario, String genero, int edad) {
        this.nombreUsuario = nombreUsuario;
        this.genero = genero;
        this.edad = edad;
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

    @Override
    public String toString() {
        return "Usuario{" + "nombreUsuario=" + nombreUsuario + ", genero=" + genero + ", edad=" + edad + '}';
    }
    
    
    
    
    
}

