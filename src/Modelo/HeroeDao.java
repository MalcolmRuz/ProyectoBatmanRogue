package Modelo;
import java.sql.*;
/**
 *
 * @author artor
 */



public class HeroeDao {
    
    private Connection conexion;

    public HeroeDao() {
    }

    public HeroeDao(Connection conexion) {
        this.conexion = conexion;
    }

    // Método para obtener un héroe por nombre
    public Heroe obtenerHeroePorNombre(String nombre) {
        Heroe heroe = null;
        String sql = "SELECT * FROM heroe WHERE nombre = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                heroe = new Heroe(
                    rs.getString("nombre"),
                    rs.getString("textoATQespecial"),
                    rs.getString("ImagenPath"),
                    rs.getInt("ataque"),
                    rs.getInt("defensa"),
                    rs.getInt("hp"),
                    rs.getInt("hpMax"),
                    rs.getInt("especial"),
                    rs.getInt("inteligencia"),
                    rs.getInt("costoEspecial"),
                    rs.getBoolean("guardiaActiva"),
                    rs.getInt("nivel")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener héroe: " + e.getMessage());
        }

        return heroe;
    }
    
    
}
