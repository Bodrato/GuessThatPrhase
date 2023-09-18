package com.example.guessthatprhase.presentation;

import com.example.guessthatprhase.HelloApplication;
import com.example.guessthatprhase.business.GestionUsuarios;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FrmMenu implements Initializable {
    @FXML
    Label lblMensaje;
    @FXML
    Button btnJugar;
    @FXML
    Button btnEstadisticas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblMensaje.setText("Welcome " + GestionUsuarios.getU().nombre());
    }

    public void pressBtnEstadisticas(){
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("FrmEstadisticas.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Menu");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void pressBtnJugar(){
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("FrmJuego.fxml"));
            Scene scene = new Scene(loader.load(), 700, 400);
            Stage stage = new Stage();
            stage.setTitle("Guess that Prhase");
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
