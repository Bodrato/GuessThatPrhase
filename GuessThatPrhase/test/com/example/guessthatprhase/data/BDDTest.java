package com.example.guessthatprhase.data;

import com.example.guessthatprhase.business.Estadisticas;
import com.example.guessthatprhase.business.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

class BDDTest {
    private Connection con;

    @BeforeEach
    public void setup() {
        // Establecer la conexión antes de cada test
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(driver);
            con = DriverManager.getConnection("jdbc:mysql://localhost/javajuego", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            Assertions.fail("No se pudo establecer la conexión: " + e.getMessage());
        }
    }

    @Test
    public void testGuardarUsuario() {
        Usuario usuario = new Usuario("John Doe","123456","pruab1234@gmail.com");
        BDD bdd = new BDD();

        int result = bdd.guardarUsuario(usuario);

        // Verificar que el resultado sea igual a 1 (éxito)
        Assertions.assertEquals(1, result, "El resultado debería ser 1");

        // Verificar que el usuario se haya guardado correctamente en la base de datos
        try {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM usuarios WHERE email = ?");
            pst.setString(1, usuario.correo());
            ResultSet rs = pst.executeQuery();

            Assertions.assertTrue(rs.next(), "El usuario debería existir en la base de datos");

            int userId = rs.getInt("id");
            Assertions.assertNotEquals(-1, userId, "El ID de usuario debería ser válido");

            // Verificar que se haya insertado el registro correspondiente en la tabla "estadisticas"
            PreparedStatement pstEstadisticas = con.prepareStatement("SELECT * FROM estadisticas WHERE usuario_id = ?");
            pstEstadisticas.setInt(1, userId);
            ResultSet rsEstadisticas = pstEstadisticas.executeQuery();

            Assertions.assertTrue(rsEstadisticas.next(), "El registro de estadísticas debería existir en la base de datos");

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Assertions.fail("Se produjo una excepción: " + e.getMessage());
        }
    }

    @Test
    public void testCargarUsuario() {
        String correo = "prueba@gmail.com";
        BDD bdd = new BDD();

        Usuario usuario = bdd.cargarUsuario(correo);

        Assertions.assertNotNull(usuario, "El usuario no debería ser nulo");
        Assertions.assertEquals("prueba", usuario.nombre(), "El nombre del usuario debería ser 'prueba'");
        Assertions.assertEquals("1234", usuario.password(), "La contraseña del usuario debería ser '1234'");
        Assertions.assertEquals(correo, usuario.correo(), "El correo del usuario debería ser '" + correo + "'");
    }

    @Test
    public void testObtenerEstadisticas() {
        String correo = "prueba@gmail.com";
        BDD bdd = new BDD();

        Estadisticas estadisticas = bdd.obtenerEstadisticas(correo);

        Assertions.assertNotNull(estadisticas, "Las estadísticas no deberían ser nulas");
    }

    @Test
    public void testActualizarEstadisticas() {
        String correo = "prueba@gmail.com";
        BDD bdd = new BDD();

        Estadisticas estadisticas = new Estadisticas(100, 10.5f, 5, 3);
        bdd.actualizarEstadisticas(estadisticas, correo);

        // Verificar que las estadísticas se hayan actualizado correctamente en la base de datos
        try {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM estadisticas WHERE usuario_id = (SELECT id FROM usuarios WHERE email = ?)");
            pst.setString(1, correo);
            ResultSet rs = pst.executeQuery();

            Assertions.assertTrue(rs.next(), "Las estadísticas deberían existir en la base de datos");

            int puntuacion = rs.getInt("puntuacion");
            float tiempoPromedio = rs.getFloat("tiempo_promedio");
            int victorias = rs.getInt("victorias");
            int derrotas = rs.getInt("derrotas");

            Assertions.assertEquals(estadisticas.getPuntuacion(), puntuacion, "La puntuación no coincide");
            Assertions.assertEquals(estadisticas.getTiempo_promedio(), tiempoPromedio, "El tiempo promedio no coincide");
            Assertions.assertEquals(estadisticas.getVictorias(), victorias, "El número de victorias no coincide");
            Assertions.assertEquals(estadisticas.getDerrotas(), derrotas, "El número de derrotas no coincide");

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Assertions.fail("Se produjo una excepción: " + e.getMessage());
        }
    }
}