package Controlador;

import Modelo.Usuario;
import Modelo.Villano;
import bd.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author artor
 */
public class Encuentro {

    public Encuentro() {
    }

    //CREACION DE USUARIO
    public void crearUsuario(Usuario nuevoUsuario) {
        Conexion con = new Conexion();
        Connection cnx = con.obtenerConexion();

        try {

            String queryUsuario = "INSERT INTO usuario (nombreUsuario, genero, edad)values(?,?,?)";

            PreparedStatement ps = cnx.prepareStatement(queryUsuario);
            ps.setString(1, nuevoUsuario.getNombreUsuario());
            ps.setString(2, nuevoUsuario.getGenero());
            ps.setInt(3, nuevoUsuario.getEdad());
            ps.executeUpdate();

            ps.close();
            cnx.close();

        } catch (SQLException e) {
            System.out.println("Error al Crear Usuario: " + e.getMessage());
        }

    }

    //LISTAR A LOS USUARIOS ..
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String query = "SELECT * FROM usuario";

        try {
            Conexion con = new Conexion();
            Connection cnx = con.obtenerConexion();
            PreparedStatement ps = cnx.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario us = new Usuario();

                us.setNombreUsuario(rs.getString("nombreUsuario"));
                us.setEdad(rs.getInt("edad"));
                us.setGenero(rs.getString("genero"));

                lista.add(us);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuario: " + e.getMessage());
        }

        return lista;

    }

    //ELIMINAR A UN USUARIO MEDIANTE CONFIRMACION DE NOMBREUSUARIO
    public boolean eliminar(String nombreUsuario) {
        try {
            Conexion con = new Conexion();
            Connection cnx = con.obtenerConexion();

            String query = "DELETE FROM usuario WHERE nombreUsuario = ?";
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setString(1, nombreUsuario);

            ps.executeUpdate();
            ps.close();
            cnx.close();

            return true;

        } catch (SQLException e) {
            System.out.println("Error al elminiar Usuario: " + e.getMessage());
            return false;
        }
    }

    //ACTUALIZAR DATOS USUARIO
    public boolean actualizar(Usuario usuario) {
        try {
            Conexion con = new Conexion();
            Connection cnx = con.obtenerConexion();

            String query = "UPDATE usuario SET genero=?,edad =? WHERE nombreUsuario =?";
            PreparedStatement ps = cnx.prepareStatement(query);

            ps.setString(1, usuario.getNombreUsuario());
            ps.setInt(2, usuario.getEdad());
            ps.setString(3, usuario.getGenero());

            ps.executeUpdate();
            ps.close();
            cnx.close();

            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar Usuario: " + e.getMessage());
            return false;
        }
    }

    public void crearVillano(Villano nuevoVillano) {
        Conexion con = new Conexion();
        Connection cnx = con.obtenerConexion();

        try {

            //imagen default villano
            if (nuevoVillano.getImagenPath() == null || nuevoVillano.getImagenPath().isEmpty()) {
                nuevoVillano.setImagenPath("/Imagenes/villano_default.png");
            }

            //primero insertar el personaje
            String queryPersonaje = "INSERT INTO personaje (nombre, textoATQespecial, imagenPath, ataque, defensa, hp, hpMax, especial, inteligencia, costoEspecial, guardiaActiva)values(?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = cnx.prepareStatement(queryPersonaje);
            ps.setString(1, nuevoVillano.getNombre());
            ps.setString(2, nuevoVillano.getTextoATQespecial());
            ps.setString(3, nuevoVillano.getImagenPath());
            ps.setInt(4, nuevoVillano.getAtaque());
            ps.setInt(5, nuevoVillano.getDefensa());
            ps.setInt(6, nuevoVillano.getHp());
            ps.setInt(7, nuevoVillano.getHpMax());
            ps.setInt(8, nuevoVillano.getEspecial());
            ps.setInt(9, nuevoVillano.getInteligencia());
            ps.setInt(10, nuevoVillano.getCostoEspecial());
            ps.setBoolean(11, nuevoVillano.isGuardiaActiva());
            ps.executeUpdate();

            //ahora insertar el personaje en villano
            String queryVillano = "INSERT INTO villano (nombre, nivelDificultad, esJefe)values(?,?,?)";
            PreparedStatement ps1 = cnx.prepareStatement(queryVillano);
            ps1.setString(1, nuevoVillano.getNombre());
            ps1.setInt(2, nuevoVillano.getIndiceDificultad());
            ps1.setBoolean(3, nuevoVillano.isEsJefe());
            ps1.executeUpdate();

            ps.close();
            ps1.close();
            cnx.close();

        } catch (SQLException e) {
            System.out.println("Error al agregar Villano: " + e.getMessage());
        }
    }

    public List<Villano> listarVillanos() {
        List<Villano> lista = new ArrayList<>();
        String query = "SELECT * FROM villano JOIN personaje ON villano.nombre = personaje.nombre";

        try {
            Conexion con = new Conexion();
            Connection cnx = con.obtenerConexion();
            PreparedStatement ps = cnx.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Villano v = new Villano();
                v.setNombre(rs.getString("nombre"));
                v.setHp(rs.getInt("hp"));
                v.setHpMax(rs.getInt("hpMax"));
                v.setAtaque(rs.getInt("ataque"));
                v.setDefensa(rs.getInt("defensa"));
                v.setTextoATQespecial(rs.getString("textoATQespecial"));
                v.setEspecial(rs.getInt("especial"));
                v.setCostoEspecial(rs.getInt("costoEspecial"));
                v.setGuardiaActiva(rs.getBoolean("guardiaActiva"));
                v.setInteligencia(rs.getInt("inteligencia"));
                v.setIndiceDificultad(rs.getInt("nivelDificultad"));
                v.setEsJefe(rs.getBoolean("esJefe"));
                v.setImagenPath(rs.getString("imagenPath"));

                lista.add(v);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar villanos: " + e.getMessage());
        }

        return lista;

    }

    public Villano generarVillano(int piso) {
        int nivel = piso;
        Villano villanoGenerado = null;

        String query = "";

        //NICO WEON CAMBIA LA BASE DATOS NIVEL DIFICULTAD QUE NO SE TE OLVIDEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
        if (nivel == 10 || nivel == 20 || nivel == 30) {
            query = "SELECT * FROM villano JOIN personaje ON villano.nombre = personaje.nombre WHERE villano.esJefe = TRUE ORDER BY RAND() LIMIT 1";
        } else if (nivel <= 9) {
            query = "SELECT * FROM villano JOIN personaje ON villano.nombre = personaje.nombre WHERE villano.nivelDificultad = 1 AND esJefe = FALSE ORDER BY RAND() LIMIT 1"; //<---Recordar cambiar base de datos
        } else if (nivel <= 19) {
            query = "SELECT * FROM villano JOIN personaje ON villano.nombre = personaje.nombre WHERE villano.nivelDificultad = 2 AND esJefe = FALSE ORDER BY RAND() LIMIT 1";
        } else if (nivel <= 30) {
            query = "SELECT * FROM villano JOIN personaje ON villano.nombre = personaje.nombre WHERE villano.nivelDificultad = 3 AND esJefe = FALSE ORDER BY RAND() LIMIT 1";
        }

        try {
            Conexion con = new Conexion();
            Connection cnx = con.obtenerConexion();

            PreparedStatement ps = cnx.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                villanoGenerado = new Villano();
                villanoGenerado.setNombre(rs.getString("nombre"));
                villanoGenerado.setTextoATQespecial(rs.getString("textoATQespecial"));
                villanoGenerado.setImagenPath(rs.getString("imagenPath"));
                villanoGenerado.setAtaque(rs.getInt("ataque"));
                villanoGenerado.setDefensa(rs.getInt("defensa"));
                villanoGenerado.setHp(rs.getInt("hp"));
                villanoGenerado.setHpMax(rs.getInt("hpMax"));
                villanoGenerado.setEspecial(rs.getInt("especial"));
                villanoGenerado.setInteligencia(rs.getInt("inteligencia"));
                villanoGenerado.setCostoEspecial(rs.getInt("costoEspecial"));
                villanoGenerado.setGuardiaActiva(rs.getBoolean("guardiaActiva"));
                villanoGenerado.setIndiceDificultad(rs.getInt("nivelDificultad"));
                villanoGenerado.setEsJefe(rs.getBoolean("esJefe"));

                //imagen default villano
                if (villanoGenerado.getImagenPath() == null || villanoGenerado.getImagenPath().isEmpty()) {
                    villanoGenerado.setImagenPath("/Imagenes/villano_default.png");
                }
            }

            rs.close();
            ps.close();
            cnx.close();

        } catch (SQLException e) {
            System.out.println("Error al generar Villano: " + e.getMessage());
            //e.printStackTrace(); // o maneja el error con logs, pero sin sout
        }

        return villanoGenerado;
    }

    //idUsuario, nombre, edad, genero
}