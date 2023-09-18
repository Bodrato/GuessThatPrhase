package com.example.guessthatprhase.business;

import com.example.guessthatprhase.data.BDD;

public final class GestionUsuarios {
    private static Usuario u;
    private static Estadisticas e;
    private final static BDD base = new BDD();
    private static String error;

    public static boolean registrarUsuario(Usuario u){
        return base.guardarUsuario(u) == 1;
    }

    public static boolean verificarDisponibilidad(String correo, String password){
        Usuario u2;
        try{
            u2 = base.cargarUsuario(correo);
            if (!u2.password().equals(password)){
                error = "The email or password is incorrect";
                return false;
            }
        }catch (Exception e) {
            error = "That user does not exist in the database";
            return false;
        }
        u = u2;
        e = base.obtenerEstadisticas(u.correo());
        return true;
    }

    public static void actualizarEstadisticas(){
        base.actualizarEstadisticas(getE(), getU().correo());
    }

    public static Usuario getU() {
        return u;
    }

    public static Estadisticas getE() {
        return e;
    }

    public static String getError() {
        return error;
    }

    public static void setError(String error) {
        GestionUsuarios.error = error;
    }
}
