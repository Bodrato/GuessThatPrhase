package com.example.guessthatprhase.presentation;

import com.example.guessthatprhase.business.Frase;
import com.example.guessthatprhase.business.GestionUsuarios;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.Timer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;

public class FrmJuego implements Initializable {
    @FXML
    Label lblFraseSecreta;
    @FXML
    Label lblAutor;
    @FXML
    Label lblTurno;
    @FXML
    Button btnEnviarLetra;
    @FXML
    Button btnEnviarFrase;
    @FXML
    TextField txtLetra;
    @FXML
    TextField txtFrase;
    private List<Frase> frases;
    private Frase fraseSecreta;
    private StringBuilder secreta;
    private boolean victoria = false;
    private int ronda;
    private Timer timer;
    private float[] tiempoTardado;
    public static List<Frase> filtrar(List<Frase> frases) {
        List<Frase> frasesFiltradas = new ArrayList<>();
        for (Frase frase : frases) {
            if (frase.prhase().length() <= 50) {
                frasesFiltradas.add(frase);
            }
        }
        return frasesFiltradas;
    }
    public Frase obtenerFraseSecreta(){
        // Creo un constructor sin pasarle semilla para poder generar números aleatorios
        Random numAleatorio = new Random();

        return frases.get(numAleatorio.nextInt(frases.size() - 1));
    }

    public void Partida(){
        tiempoTardado = new float[]{0};
        timer = new Timer(1000, e -> tiempoTardado[0]++);
        timer.start();
        frases = filtrar(frases);
        ronda = 1;
        fraseSecreta = obtenerFraseSecreta();
        System.out.println(fraseSecreta.prhase());
        secreta = new StringBuilder();

        for (int i = 0; i < fraseSecreta.prhase().length(); i++){
            if (fraseSecreta.prhase().charAt(i) == ' ' || fraseSecreta.prhase().charAt(i) == '.'
                    || fraseSecreta.prhase().charAt(i) == ',' || fraseSecreta.prhase().charAt(i) == '\''
                || fraseSecreta.prhase().charAt(i) == '-'){
                secreta.append(fraseSecreta.prhase().charAt(i));
            }

            else{
                secreta.append('*');
            }
        }
        System.out.println(secreta);
        lblFraseSecreta.setText(String.valueOf(secreta));
    }

    public boolean comprobarVictoria(String cadena, String frase){
        return cadena.equals(frase);
    }

    public void pulsarBtnLetra(){
        char letra = ' ';
        try{
            letra = txtLetra.getText().charAt(0);
            // Buscamos la letra en la frase secreta
            int index = fraseSecreta.prhase().indexOf(letra);
            int cont = 0;
            while (index != -1) {
                // Si la letra está en la frase, la reemplazamos en la cadena secreta
                secreta = new StringBuilder(secreta.substring(0, index) + letra + secreta.substring(index + 1));

                // Buscamos la siguiente ocurrencia de la letra
                index = fraseSecreta.prhase().indexOf(letra, index + 1);
                cont++;
            }
            cont = cont * 5;
            GestionUsuarios.getE().setPuntuacion(GestionUsuarios.getE().getPuntuacion() + cont);
            // Imprimimos la frase secreta actualizada
            lblFraseSecreta.setText(String.valueOf(secreta));
            if (comprobarVictoria(secreta.toString(),fraseSecreta.prhase())){
                victoria = true;
                terminarPartida();
            }
            else {
                siguienteRonda();
            }
        }catch (Exception e){
            siguienteRonda();
        }
    }

    public void pulsarBtnFrase(){
        String cadena = txtFrase.getText();
        if (comprobarVictoria(cadena,fraseSecreta.prhase())){
            victoria = true;
            secreta = new StringBuilder(cadena);
            GestionUsuarios.getE().setPuntuacion(GestionUsuarios.getE().getPuntuacion() + 50);
        }
        lblFraseSecreta.setText(String.valueOf(secreta));
        if (comprobarVictoria(secreta.toString(),fraseSecreta.prhase())){
            victoria = true;
            terminarPartida();
        }
        else {
            siguienteRonda();
        }
    }

    public void terminarPartida(){
        timer.stop();
        if (victoria){
            GestionUsuarios.getE().setTiempo_promedio((GestionUsuarios.getE().getTiempo_promedio() + tiempoTardado[0]) / 2);
            GestionUsuarios.getE().setVictorias(GestionUsuarios.getE().getVictorias() + 1);
            GestionUsuarios.getE().setPuntuacion(GestionUsuarios.getE().getPuntuacion() + 100);
        }
        else{
            GestionUsuarios.getE().setDerrotas(GestionUsuarios.getE().getDerrotas() + 1);
            GestionUsuarios.getE().setPuntuacion(GestionUsuarios.getE().getPuntuacion() - 100);
        }
        GestionUsuarios.actualizarEstadisticas();
        if (victoria){
            lblAutor.setText("You won! the author was " + fraseSecreta.author());
        }
        else{
            lblFraseSecreta.setText(fraseSecreta.prhase());
            lblAutor.setText("You lose, the author was " + fraseSecreta.author());
        }

        Stage stage = (Stage) this.btnEnviarFrase.getScene().getWindow();

        PauseTransition delay = new PauseTransition(Duration.seconds(10));
        delay.setOnFinished( event -> stage.close() );
        delay.play();
    }

    public void siguienteRonda(){
        this.ronda++;
        if (ronda == 21){
            terminarPartida();
        }
        else{
            lblTurno.setText(String.valueOf(ronda));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Lee el archivo JSON
            BufferedReader br = new BufferedReader(new FileReader("data/en.json"));
            // Convierte el contenido del archivo a una lista de objetos Frase
            Type type = new TypeToken<ArrayList<Frase>>(){}.getType();
            frases = new Gson().fromJson(br, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Partida();
        lblTurno.setText(String.valueOf(ronda));
    }
}
