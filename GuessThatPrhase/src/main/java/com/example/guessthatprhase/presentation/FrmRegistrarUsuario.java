package com.example.guessthatprhase.presentation;

import com.example.guessthatprhase.business.GestionUsuarios;
import com.example.guessthatprhase.business.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrmRegistrarUsuario {
    @FXML
    Button btnRegistrarUsuario;
    @FXML
    Label lblMensaje;
    @FXML
    Label lblBien;
    @FXML
    TextField txtName;
    @FXML
    TextField txtEmail;
    @FXML
    TextField txtPassword;

    public void registrarUsuario(){
        String name = txtName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        Pattern pattern = Pattern.compile("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@" +
                "[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$");

        if (!name.equals("") && !email.equals("") && !password.equals("")){
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()){
                lblMensaje.setText("Invalid email");
            }
            else{
                if (GestionUsuarios.registrarUsuario(new Usuario(name, password, email))){
                    lblMensaje.setText("");
                    lblBien.setText("User successfully registered");
                }
                else{
                    lblMensaje.setText(GestionUsuarios.getError());
                }
            }
        }
        else{
            lblMensaje.setText("You have to fill in the fields");
        }
    }
}
