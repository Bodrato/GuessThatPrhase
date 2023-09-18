package com.example.guessthatprhase.data;

import com.example.guessthatprhase.business.Estadisticas;
import com.example.guessthatprhase.business.GestionUsuarios;
import com.example.guessthatprhase.business.Usuario;

import java.sql.*;

public class BDD {
    private PreparedStatement pst;
    private Connection con;

    public void conectarMySQL() {
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(driver);
            con = DriverManager.getConnection("jdbc:mysql://localhost/javajuego", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            GestionUsuarios.setError("Error charging the JDBC controller");
            e.printStackTrace();
        }
    }

    public int guardarUsuario(Usuario u) {
        ResultSet rs;
        int id = -1;
        try {
            conectarMySQL();
            pst = con.prepareStatement("INSERT INTO usuarios(nombre, email, password) VALUES (?, ?, ?)");
            pst.setString(1, u.nombre());
            pst.setString(2, u.correo());
            pst.setString(3, u.password());
            pst.executeUpdate();

            String consulta = "SELECT * FROM usuarios WHERE email = ?";
            pst = con.prepareStatement(consulta);
            pst.setString(1, u.correo());

            rs = pst.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }

            pst = con.prepareStatement("INSERT INTO `estadisticas`(`usuario_id`, `puntuacion`, `tiempo_promedio`," +
                    " `victorias`, `derrotas`) VALUES ('"+id+"','0','0','0','0')");
            pst.executeUpdate();
            con.close();
        } catch (SQLException e) {
            GestionUsuarios.setError(e.getMessage());
            return -1;
        }
        return 1;
    }

    public Usuario cargarUsuario(String correo) {
        Usuario usuario = null;
        ResultSet rs = null;

        try {
            // Obtenemos una conexión a la base de datos
            conectarMySQL();

            // Preparamos la consulta SQL
            String consulta = "SELECT * FROM usuarios WHERE email = ?";
            pst = con.prepareStatement(consulta);
            pst.setString(1, correo);

            // Ejecutamos la consulta
            rs = pst.executeQuery();

            // Si se encontró un usuario, lo creamos
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String password = rs.getString("password");

                usuario = new Usuario(nombre, password, correo);
            }
        } catch (SQLException e) {
            GestionUsuarios.setError(e.getMessage());
        } finally {
            // Cerramos los recursos
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                GestionUsuarios.setError(e.getMessage());
            }
        }

        return usuario;
    }

    public Estadisticas obtenerEstadisticas(String correo) {
        Estadisticas estadisticas = null;
        ResultSet rs = null;

        try {
            // Obtenemos una conexión a la base de datos
            conectarMySQL();

            // Preparamos la consulta SQL
            String consulta = "select * from `estadisticas` where id in " +
                    "(select estadisticas.id from estadisticas, usuarios where estadisticas.usuario_id = " +
                    "usuarios.id AND usuarios.id = (Select usuarios.id from usuarios where usuarios.email = " +
                    "?) )";
            pst = con.prepareStatement(consulta);
            pst.setString(1, correo);

            // Ejecutamos la consulta
            rs = pst.executeQuery();

            // Si se encontró las estadísticas del usuario las guardamos
            if (rs.next()) {
                int puntuacion = rs.getInt("puntuacion");
                float tiempo_promedio = rs.getFloat("tiempo_promedio");
                int victorias = rs.getInt("victorias");
                int derrotas = rs.getInt("derrotas");

                estadisticas = new Estadisticas(puntuacion, tiempo_promedio, victorias, derrotas);
            }
        } catch (SQLException e) {
            GestionUsuarios.setError(e.getMessage());
        } finally {
            // Cerramos los recursos
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                GestionUsuarios.setError(e.getMessage());
            }
        }

        return estadisticas;
    }

    public void actualizarEstadisticas(Estadisticas estadisticas, String correo) {
        ResultSet rs;
        int id = -1;
        try {
            //Nos conectamos a la base
            conectarMySQL();
            //Con un select pillamos la id del usuario en la base
            String consultaId = "SELECT id FROM `usuarios` WHERE usuarios.email = '"+correo+"'";
            pst = con.prepareStatement(consultaId);
            rs = pst.executeQuery(consultaId);
            if (rs.next()){
                id = rs.getInt("id");
            }
            //Con la id del usuario ya actualizamos las estadísticas en la base
            String consulta = "UPDATE `estadisticas` SET puntuacion = ?, tiempo_promedio = ?, victorias = ?," +
                    "derrotas = ? WHERE usuario_id = " + id;
            pst = con.prepareStatement(consulta);
            pst.setInt(1, estadisticas.getPuntuacion());
            pst.setFloat(2, estadisticas.getTiempo_promedio());
            pst.setInt(3, estadisticas.getVictorias());
            pst.setInt(4, estadisticas.getDerrotas());

            pst.executeUpdate();
            con.close();

        } catch (SQLException e) {
            GestionUsuarios.setError(e.getMessage());
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                GestionUsuarios.setError(e.getMessage());
            }
        }
    }
}
