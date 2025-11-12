package Controlador;

import Modelo.Villano;
import bd.Conexion;

/**
 *
 * @author artor
 */
public class Encuentro {

    public Encuentro() {
    }

    public Villano generarVillano(ProgresionMapa piso) {
        int nivel = piso.getPisoActual();
        Villano villanoGenerado = null;

        String sql = "";

        if (nivel == 10 || nivel == 20 || nivel == 30) {
            sql = "SELECT * FROM villano WHERE esJefe = TRUE ORDER BY RAND() LIMIT 1";
        } else if (nivel <= 9) {
            sql = "SELECT * FROM villano WHERE nivelDificultad = 1 AND esJefe = FALSE ORDER BY RAND() LIMIT 1"; //<--------------------------------Recordar cambiar base de datos
        } else if (nivel <= 19) {
            sql = "SELECT * FROM villano WHERE nivelDificultad = 2 AND esJefe = FALSE ORDER BY RAND() LIMIT 1";
        } else if (nivel <= 30) {
            sql = "SELECT * FROM villano WHERE nivelDificultad = 3 AND esJefe = FALSE ORDER BY RAND() LIMIT 1";
        }

        try {Conexion con = new Conexion();
            Connection cnx = con.obtenerConexion();

            if (rs.next()) {
                villanoGenerado = new Villano(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("hp"),
                        rs.getInt("ataque"),
                        rs.getInt("defensa"),
                        rs.getString("habilidadEspecial"),
                        rs.getString("dificultad"),
                        rs.getBoolean("esJefe")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace(); // o maneja el error con logs, pero sin sout
        }

        return villanoGenerado;
    }
}
