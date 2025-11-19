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
import javax.swing.JOptionPane;

/**
 *MANEJO DEL CRUD PARA USUARIO Y VILLANO
 * @author artor
 */
public class Encuentro {

    public Encuentro() {
    }

    //CREACION DE USUARIO
    public boolean crearUsuario(Usuario nuevoUsuario) {
        Conexion con = new Conexion();
        Connection cnx = con.obtenerConexion();

        try {
            //consulta sql para insertar un nuevo usuario a la tabla usuario
            String queryUsuario = "INSERT INTO usuario (nombreUsuario, genero, edad, puntaje)values(?,?,?,?)";

            PreparedStatement ps = cnx.prepareStatement(queryUsuario);
            ps.setString(1, nuevoUsuario.getNombreUsuario());
            ps.setString(2, nuevoUsuario.getGenero());
            ps.setInt(3, nuevoUsuario.getEdad());
            ps.setInt(4, nuevoUsuario.getPuntaje());

            ps.executeUpdate();
            ps.close();
            cnx.close();

            return true;

        } catch (SQLException e) {
            //mensaje de error en caso de error
            System.out.println("Error al Crear Usuario: " + e.getMessage());
            return false;
        }
    }

    //LISTAR A LOS USUARIOS ..
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        //consulta sql donde seran ordenados de forma descendente por puntaje
        String query = "SELECT nombreUsuario, edad, genero, puntaje FROM usuario ORDER BY puntaje DESC";

        try {
            Conexion con = new Conexion();
            Connection cnx = con.obtenerConexion();
            PreparedStatement ps = cnx.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            //agregar usuario a la lista usando un ciclo while
            while (rs.next()) {
                Usuario us = new Usuario();

                us.setNombreUsuario(rs.getString("nombreUsuario"));
                us.setEdad(rs.getInt("edad"));
                us.setGenero(rs.getString("genero"));
                us.setPuntaje(rs.getInt("puntaje"));

                lista.add(us);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuario: " + e.getMessage());
        }

        return lista;

    }

    //ELIMINAR A UN USUARIO MEDIANTE CONFIRMACION DE NOMBREUSUARIO
    public boolean eliminarUsuario(String nombreUsuario) {

        try {
            Conexion con = new Conexion();
            Connection cnx = con.obtenerConexion();
            
            //consulta sql
            String query = "DELETE FROM usuario WHERE nombreUsuario = ?";
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setString(1, nombreUsuario); //asignacion del parametro
            
            //cuadro de confirmacion donde No = 1 / Si = 0
            int resp = JOptionPane.showConfirmDialog(null, "¿Seguro desea eliminar Usuario?", "Eliminar Usuario", 1);
            if (resp == 0) { //verifica la confirmacion si es Si

                int filasAfectadas = ps.executeUpdate(); //ejecucion de la consulta

                ps.close();
                cnx.close();
                
                //verificacion de si se borro alguna fila en la bd
                if (filasAfectadas > 0) {
                    return true;
                } else {
                    return false; //si el cuadro confirmacion es No = 1 retorna false
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al elminiar Usuario: " + e.getMessage());
            return false;
        }
    }

    //ACTUALIZAR DATOS USUARIO con datos nuevos en el objeto usuarioActualizado
    // y nombreOriginal para identificar el registro a modificar
    public boolean actualizarUsuario(Usuario usuarioActualizado, String nombreOriginal) {
        
        //sentencia sql para actualizar solo cuando el nombreUsuario coincida con =?
        String query = "UPDATE usuario SET nombreUsuario=?, edad=?, genero=? WHERE nombreUsuario=?";

        try {
            Conexion con = new Conexion();
            Connection cnx = con.obtenerConexion();

            PreparedStatement ps = cnx.prepareStatement(query);

            ps.setString(1, usuarioActualizado.getNombreUsuario());
            ps.setInt(2, usuarioActualizado.getEdad());
            ps.setString(3, usuarioActualizado.getGenero());

            ps.setString(4, nombreOriginal); //aqui es la asignacion de WHERE

            ps.executeUpdate();
            ps.close();
            cnx.close();

            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar Usuario: " + e.getMessage());
            return false;
        }
    }

    //BUSCAR USUARIO POR NOMBRE
    public Usuario buscarPorNombreUsuario(String nombre) {
        
        //esta variable contendra el objeto si se encuentra en la bd, si no permanece como null
        Usuario usu = null;

        try {
            Conexion con = new Conexion();
            Connection cnx = con.obtenerConexion();
            
            //sentencia sql, se usa LOWER(?) para asegurar que la busqueda de nombre no distinga entre mayus/minusculas
            String query = "SELECT nombreUsuario, edad, genero FROM usuario WHERE LOWER(nombreUsuario) = LOWER(?)";
            PreparedStatement ps = cnx.prepareStatement(query);

            ps.setString(1, nombre);

            ResultSet rs = ps.executeQuery(); //ejecucion consulta

            if (rs.next()) {
                //si encuentra el usuario, se crea el objeto, si no devuelve null mas abajo
                usu = new Usuario();
                usu.setNombreUsuario(rs.getString("nombreUsuario"));
                usu.setEdad(rs.getInt("edad"));
                usu.setGenero(rs.getString("genero"));
            }

            rs.close();
            ps.close();
            cnx.close();

        } catch (SQLException e) {
            System.out.println("Error al buscar Usuario: " + e.getMessage());

        }
        return usu;  //si falla devuelve null
    }

    //obtencion de puntaje de usuario
    public int obtenerPuntaje(String nombreUsuario) {

        int puntaje = -1; //valor predeterminado para indicar error
        //consulta sql
        String query = "SELECT puntaje FROM usuario WHERE nombreUsuario = ?";
        try {
            Conexion con = new Conexion();
            Connection cnx = con.obtenerConexion();
            PreparedStatement ps = cnx.prepareStatement(query);

            ps.setString(1, nombreUsuario);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                puntaje = rs.getInt("puntaje"); //obtiene el puntaje si encuentra al usuario
            }

            rs.close();
            ps.close();
            cnx.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar el puntaje: " + e.getMessage(), "Error SQL", 0);
            e.printStackTrace(); //esto e. captura una excepcion, sirve para depurar(debug) te indica donde y como fallo el codigo
            puntaje = -1;

        }
        return puntaje;

    }

    
    //actualizacion de puntaje al jugar
    public boolean actualizarPuntaje(String nombreUsuario, int nuevoPuntaje) {
        
        //consulta sql, actualizar un nuevo valor de puntaje cuyo nombreUsuario de usuario coincida
        String query = "UPDATE usuario SET puntaje = ? WHERE nombreUsuario = ?";
        Connection cnx = null;

        try {
            Conexion con = new Conexion();
            cnx = con.obtenerConexion();

            //fuerza la conexión para que active el auto-commit (guardado automático)
            //si no lo está, el update se pierde al cerrar la conexión.
            //esto asegura que la modificación de la base de datos se haga efectiva inmediatamente
            cnx.setAutoCommit(true);

            //uso de try-with-resources para el PreparedStatement (limpieza automática)
            //garantiza que el objeto PreparedStatement (ps) se cerrará automáticamente al salir del bloque si hay error
            try (PreparedStatement ps = cnx.prepareStatement(query)) {
                
                ps.setInt(1, nuevoPuntaje); //nuevo valor del puntaje
                ps.setString(2, nombreUsuario);

                //ejecutar y verificar si se actualizó una fila
                return ps.executeUpdate() > 0; //true si se guardo bien
            }//aqui se cierra el preparedstatemnt

        } catch (SQLException e) {
            System.out.println("Error al actualizar el puntaje: " + e.getMessage());
            return false;
        } finally {
            //asegurar que la conexión se cierre, siempre y cuando no sea null
            try {
                if (cnx != null) {
                    cnx.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
    }

    //creacion del villano en la gui
    public boolean crearVillano(Villano villano) {
        Connection cnx = null;
        boolean exito = false; //flag/bandera para la confirmacion si fue exitosa o no 

        //consulta para tabla personaje donde se obtiene todos los atributos tabla padre
        String queryPersonaje = "INSERT INTO personaje (nombre, hp, hpMax, ataque, defensa, textoATQespecial, especial, inteligencia, costoEspecial, guardiaActiva, imagenPath) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        //consulta para la tabla villano, tabla hija
        String queryVillano = "INSERT INTO villano (nombre, nivelDificultad, esJefe) VALUES (?, ?, ?)";

        try {
            Conexion con = new Conexion();
            cnx = con.obtenerConexion();

            //deshabilitar el auto-commit para manejar la transaccion
            //la base de datos que no guarde permanentemente ningun cambio hasta que se le indique explicitamente con commit()
            //esto garantiza que ambas inserciones (personaje y villano) se completen o ninguna lo haga.
            cnx.setAutoCommit(false);

            //el try-with-resources asegura que psPersonaje se cierra automaticamente al salir del bloque, aunque haya errores.
            try (PreparedStatement psPersonaje = cnx.prepareStatement(queryPersonaje)) {

                //asignacion de parametros (ver el orden de la bd para no causar errores al insertar)
                psPersonaje.setString(1, villano.getNombre());
                psPersonaje.setInt(2, villano.getHp());
                psPersonaje.setInt(3, villano.getHpMax());
                psPersonaje.setInt(4, villano.getAtaque());
                psPersonaje.setInt(5, villano.getDefensa());
                psPersonaje.setString(6, villano.getTextoATQespecial());
                psPersonaje.setInt(7, villano.getEspecial());
                psPersonaje.setInt(8, villano.getInteligencia());
                psPersonaje.setInt(9, villano.getCostoEspecial());
                psPersonaje.setBoolean(10, villano.isGuardiaActiva());
                psPersonaje.setString(11, villano.getImagenPath());

                psPersonaje.executeUpdate();
            }

            //el try-with-resources asegura que psVillano se cierra automaticamente al salir del bloque, aunque haya errores
            try (PreparedStatement psVillano = cnx.prepareStatement(queryVillano)) {

                // asignacion de parametro
                psVillano.setString(1, villano.getNombre());
                psVillano.setInt(2, villano.getIndiceDificultad());
                psVillano.setBoolean(3, villano.isEsJefe());

                psVillano.executeUpdate();
            }

            //si ambos update fueron exitosas commit
            cnx.commit();
            exito = true; //flag o bandera

        } catch (SQLException e) {
            System.out.println("Error al crear villano: " + e.getMessage());
            try {
                if (cnx != null) {
                    cnx.rollback(); // deshacer si fallo algo
                }
            } catch (SQLException ex) {
                System.out.println("Error al revertir la transacción: " + ex.getMessage());
            }
            exito = false;

        } finally {
            //si algo fallo se llama a rollback() donde esto deshace completamente todas las operaciones pendientes
            try {
                if (cnx != null) {
                    cnx.setAutoCommit(true);
                    cnx.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }

        return exito;
    }

    //ELIMINAR A UN USUARIO MEDIANTE CONFIRMACION DE NOMBREUSUARIO
    public boolean eliminarVillano(String nombre) {

        try {
            Conexion con = new Conexion();
            Connection cnx = con.obtenerConexion();
            
            //consulta multi tabla sql
            //que elimine filas de ambas tablas (villano y personaje) donde los nombres coinciden y el nombre del villano es el valor proporcionado
            //para asegurar que se eliminen ne una sola consulta
            String query = "DELETE villano,personaje FROM villano JOIN personaje ON villano.nombre = personaje.nombre WHERE villano.nombre = ?";
            PreparedStatement ps = cnx.prepareStatement(query);
            ps.setString(1, nombre);
            
            //cuadro de confirmacion donde No = 1 / Si = 0
            int resp = JOptionPane.showConfirmDialog(null, "¿Seguro desea eliminar Usuario?", "Eliminar Usuario", 1);
            if (resp == 0) {

                int filasAfectadas = ps.executeUpdate();
                ps.close();
                cnx.close();

                if (filasAfectadas > 0) {
                    return true; //exito
                } else {
                    System.out.println("No se encontraron registros para eliminar");
                    return false; //fallo
                }
            }
            return false; //si usuario apreta no, se sale del try y devuelve false sin eliminar
        } catch (SQLException e) {
            System.out.println("Error al elminiar Usuario: " + e.getMessage());
            return false;
        }
    }

    
    //lista de villanos en la bd
    public List<Villano> listarVillanos() { //metodo que devuelve una lista de objetos villano
        List<Villano> lista = new ArrayList<>(); //lista vacia
        
        
        //sentencia sql
        //donde se utiliza JOIN para combinar los datos de la tabla villano y la tabla personaje
        //la combinacion se realiza cuando el nombre en ambas tablas es el mismo
        //el SELECT * trae todas las columnas de ambas tablas.
        String query = "SELECT * FROM villano JOIN personaje ON villano.nombre = personaje.nombre";

        
        try (Connection cnx = new Conexion().obtenerConexion(); PreparedStatement ps = cnx.prepareStatement(query); ResultSet rs = ps.executeQuery()) {

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
                v.setImagenPath(rs.getString("imagenPath"));
                v.setIndiceDificultad(rs.getInt("nivelDificultad"));
                v.setEsJefe(rs.getBoolean("esJefe"));

                lista.add(v);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar villanos: " + e.getMessage());
            // En caso de error, la lista devuelta estará vacía.
        }

        return lista;
    }

    public Villano generarVillano(int piso) {
        int nivel = piso;
        Villano villanoGenerado = null;

        String query = "";

        //consultas sql para cada nivel obtener un villano en base a dificultad o es Jefe
        if (nivel == 5 || nivel == 10) {
            query = "SELECT * FROM villano JOIN personaje ON villano.nombre = personaje.nombre WHERE villano.nivelDificultad = 1 AND villano.esJefe = TRUE ORDER BY RAND() LIMIT 1";
        } else if (nivel <= 9) {
            query = "SELECT * FROM villano JOIN personaje ON villano.nombre = personaje.nombre WHERE villano.nivelDificultad = 1 AND villano.esJefe = FALSE ORDER BY RAND() LIMIT 1"; //<---Recordar cambiar base de datos
        } else if (nivel == 15 || nivel == 20) {
            query = "SELECT * FROM villano JOIN personaje ON villano.nombre = personaje.nombre WHERE villano.nivelDificultad = 2 AND villano.esJefe = TRUE ORDER BY RAND() LIMIT 1";
        } else if (nivel >= 11 && nivel <= 19) {
            query = "SELECT * FROM villano JOIN personaje ON villano.nombre = personaje.nombre WHERE villano.nivelDificultad = 2 AND villano.esJefe = FALSE ORDER BY RAND() LIMIT 1";
        } else if (nivel == 25 || nivel == 30) {
            query = "SELECT * FROM villano JOIN personaje ON villano.nombre = personaje.nombre WHERE villano.nivelDificultad = 3 AND villano.esJefe = TRUE ORDER BY RAND() LIMIT 1";
        } else if (nivel >= 21 && nivel <= 29) {
            query = "SELECT * FROM villano JOIN personaje ON villano.nombre = personaje.nombre WHERE villano.nivelDificultad = 3 AND villano.esJefe = FALSE ORDER BY RAND() LIMIT 1";
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

                //imagen default villano que hay que meter en el package imagenes
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

// BUSCAR VILLANO POR NOMBRE 
    public Villano buscarPorNombreVillano(String nombre) {

        Villano villano = null; //se crea un objeto nulo por si se encuentra un registro que no coincide

        //consulta sql 
        //SELECT que combina las tablas villano y personaje a través de un JOIN utilizando el nombre
        //trae solo las columnas necesarias y LOWER() se aplica tanto al nombre de la tabla como al parámetro (?)
        //para asegurar que la busqueda no distinga entre mayus/minusculas
        String query = "SELECT personaje.nombre, textoATQespecial, esJefe, nivelDificultad FROM villano JOIN personaje ON villano.nombre = personaje.nombre WHERE LOWER(villano.nombre) = LOWER(?)";

        // Usamos try-with-resources para el cierre seguro
        try (Connection cnx = new Conexion().obtenerConexion(); PreparedStatement ps = cnx.prepareStatement(query)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    villano = new Villano();

                    villano.setNombre(rs.getString("nombre"));
                    villano.setTextoATQespecial(rs.getString("textoATQespecial"));
                    villano.setIndiceDificultad(rs.getInt("nivelDificultad"));
                    villano.setEsJefe(rs.getBoolean("esJefe"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar Villano: " + e.getMessage());
        }

        return villano;
    }

    //actualizar algunos datos del villano
    public boolean actualizarVillano(Villano villanoActualizado, String nombreOriginal) { //nuevo objeto villano y nombreOriginal para buscar registro
        
        //fuera del bloque try para que puedan ser accesibles y cerradas correctamente en el bloque finally
        Connection cnx = null;
        PreparedStatement psPersonaje = null;
        PreparedStatement psVillano = null;
        boolean exito = false; //flag de exito
        
        //consulta sql donde en personaje se actualiza nombre y textoAtaque
        String queryPersonaje = "UPDATE personaje SET nombre=?, textoATQespecial=? WHERE nombre=?";
        
        //consulta sql donde en villano se actualiza nivelDificultad y si es jefe
        String queryVillano = "UPDATE villano SET nivelDificultad=?, esJefe=? WHERE nombre=?";

        try {
            Conexion con = new Conexion();
            cnx = con.obtenerConexion();

            // Deshabilitar el auto-commit asi asegura que las dos actualizaciones (en personaje y villano) solo se guarden si ambas son exitosas.
            cnx.setAutoCommit(false);

            psPersonaje = cnx.prepareStatement(queryPersonaje);

            psPersonaje.setString(1, villanoActualizado.getNombre()); //nuevo nombre del villano
            psPersonaje.setString(2, villanoActualizado.getTextoATQespecial()); //nuevo ataque especial
            psPersonaje.setString(3, nombreOriginal);

            psPersonaje.executeUpdate();

            psVillano = cnx.prepareStatement(queryVillano);

            psVillano.setInt(1, villanoActualizado.getIndiceDificultad()); // nuevo nivel
            psVillano.setBoolean(2, villanoActualizado.isEsJefe()); // nuevo esJefe (boolean -> TINYINT)
            psVillano.setString(3, nombreOriginal);

            psVillano.executeUpdate();

            //Si ambos updates fueron exitosos: COMMIT
            cnx.commit();
            exito = true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar Villano: " + e.getMessage());
            try {
                if (cnx != null) {
                    cnx.rollback(); // Deshacer si algo falló
                }
            } catch (SQLException ex) {
                System.out.println("Error al revertir la transacción: " + ex.getMessage());
            }
            exito = false;

        } finally {
            // Cierre de recursos en el bloque finally
            try {
                if (psVillano != null) {
                    psVillano.close();
                }
                if (psPersonaje != null) {
                    psPersonaje.close();
                }
                if (cnx != null) {
                    cnx.setAutoCommit(true); // Restaurar auto-commit
                    cnx.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar recursos: " + ex.getMessage());
            }
        }

        return exito;
    }

}
