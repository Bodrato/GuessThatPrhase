package com.example.guessthatprhase.presentation;

import com.example.guessthatprhase.HelloApplication;
import com.example.guessthatprhase.business.GestionUsuarios;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrmIniciarSesion{
    @FXML
    Button btnIniciarSesion;
    @FXML
    Button btnRegistrar;
    @FXML
    Label lblMensaje;
    @FXML
    TextField txtEmail;
    @FXML
    TextField txtPassword;

    public boolean iniciarSesion(){
        // Guardamos el contenido de los txt en variables y también hacemos un patrón para comprobar el email
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        Pattern pattern = Pattern.compile("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@" +
                "[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$");

        if (!email.equals("") || !password.equals("")){
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()){
                lblMensaje.setText("Invalid email");
            }
            else{
                try{
                    //Si todo es correcto revisamos si existe el usuario y lanzamos su menu
                    if (GestionUsuarios.verificarDisponibilidad(email,password)){
                        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("FrmMenu.fxml"));
                        Scene scene = new Scene(loader.load(), 600, 400);
                        Stage stage = new Stage();
                        stage.setTitle("Menu");
                        stage.setScene(scene);
                        stage.show();
                        lblMensaje.setText("");
                        return true;
                    }
                    else{
                        lblMensaje.setText(GestionUsuarios.getError());
                        GestionUsuarios.setError("");
                    }
                }catch (Exception e){
                    lblMensaje.setText(e.getMessage());

                }
            }
        }
        else{
            lblMensaje.setText("You have to fill in the fields");
        }

        return false;
    }

    public void registrarUsuario(){
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("FrmRegistrarUsuario.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Sign in");
            stage.setScene(scene);
            stage.show();
            lblMensaje.setText("");
        }catch (Exception e){
            lblMensaje.setText(e.getMessage());
        }
    }
}
