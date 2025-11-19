package Modelo;

import java.sql.*;

/**
 *
 * @author artor
 */
//dao = data access object
public class HeroeDao {

    private Connection conexion;

    public HeroeDao() {
    }

    public HeroeDao(Connection conexion) {
        this.conexion = conexion;
    }

    // Método para obtener un héroe por nombre
    public Heroe obtenerHeroePorNombre(String nombre, Usuario usuarioAsociado) {
        Heroe heroe = null;
        //consulta sql
        //recupera todos los atributos base del héroe (de personaje) y su nivel específico (de heroe), uniéndolos por el nombre para un héroe en particular
        String sql = "SELECT personaje.*, heroe.nivel FROM personaje JOIN heroe ON personaje.nombre = heroe.nombre WHERE personaje.nombre = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                heroe = new Heroe(
                        rs.getString("nombre"),
                        rs.getString("textoATQespecial"),
                        rs.getString("imagenPath"),
                        rs.getInt("ataque"), 
                        rs.getInt("defensa"),
                        rs.getInt("hp"),
                        rs.getInt("hpMax"),
                        rs.getInt("especial"),
                        rs.getInt("inteligencia"),
                        rs.getInt("costoEspecial"),
                        rs.getBoolean("guardiaActiva"),
                        rs.getInt("nivel"),
                        usuarioAsociado
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener héroe: " + e.getMessage());
        }

        return heroe;
    }

}
