package com.example.guessthatprhase.presentation;

import com.example.guessthatprhase.business.GestionUsuarios;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class FrmEstadisticas implements Initializable {
    @FXML
    Label lblPuntuacion;
    @FXML
    Label lblTiempo;
    @FXML
    Label lblVictorias;
    @FXML
    Label lblDerrotas;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblPuntuacion.setText(""+GestionUsuarios.getE().getPuntuacion());
        lblTiempo.setText(""+GestionUsuarios.getE().getTiempo_promedio());
        lblVictorias.setText(""+GestionUsuarios.getE().getVictorias());
        lblDerrotas.setText(""+GestionUsuarios.getE().getDerrotas());
    }
}
