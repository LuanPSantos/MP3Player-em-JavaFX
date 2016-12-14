package mp3player.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class FXMLPlayerController implements Initializable {

    @FXML private ImageView imageViewImagemMusica;
    @FXML private Label labelNomeMusica;
    @FXML private Button buttonPlayPause;
    @FXML private Slider sliderVolumeMusica;
    @FXML private Button buttonAleatorio;
    @FXML private Button buttonRepetir;
    @FXML private TextField textFildCaminho;
    
    // Variaveis
    private MediaPlayer mediaPlayer;
    private Media musica;
    private String caminho = "C:/Users/santo/Music";
    private List<String> minhasMusicas = new ArrayList<>();;
    private String musicaAtual;
    private int indexMusicaAtual;
    private int indexmusicaAnterior;
    private boolean musicaCarregada = false;
    private boolean aleatorio = false;
    private boolean repetir = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listarArquivos(caminho);
        iniciarUmaMusica();        
    }
    
    public void iniciarUmaMusica(){        
        try {
            Random random = new Random();
            indexMusicaAtual = random.nextInt(minhasMusicas.size());
            musicaAtual = URLEncoder.encode(minhasMusicas.get(indexMusicaAtual),"UTF-8").replace("+","%20");
            
            prepararMusica(musicaAtual);
            
        } catch (UnsupportedEncodingException ex) {
            System.out.println("Não foi possivel carregar a musica. ERRO: " + ex.getLocalizedMessage());
        }
    }

    private void listarArquivos(String caminho){
        File folder = new File(caminho);
        File[] listOfFiles = folder.listFiles();
        List<String> musicas;

        for (File file : listOfFiles) {    
            if(file.isDirectory()){
                listarArquivos(caminho +"/"+ file.getName());
            }if(file.isFile()){
                int extensao = file.getName().indexOf("mp3");
                if(extensao != -1){
                    minhasMusicas.add(file.getAbsolutePath());
                }
            }
        }
    }
    
    public void playPauseMusica(){                
        if(buttonPlayPause.getText().equals("Play")){
            buttonPlayPause.setText("Pause");            
            mediaPlayer.play();
        }else{
            buttonPlayPause.setText("Play");
            mediaPlayer.pause();
        }
    }
    
    private void prepararMusica(String novaMusca){
        
        musica = new Media("file:///" + novaMusca);
        mediaPlayer = new MediaPlayer(musica);  
        mediaPlayer.setOnReady(() -> {                
            String autor = mediaPlayer.getMedia().getMetadata().get("artist").toString();
            String titulo = mediaPlayer.getMedia().getMetadata().get("title").toString();
            Image imagem = (Image) mediaPlayer.getMedia().getMetadata().get("image");
            if(imagem != null)
                imageViewImagemMusica.setImage(imagem);

            labelNomeMusica.setText(autor + " - " + titulo);
        });
        if(repetir){
            mediaPlayer.setOnEndOfMedia(new Runnable() {            
                @Override
                public void run() {
                    prepararMusica(musicaAtual);
                    mediaPlayer.play();
                }
            });
        }else{
            mediaPlayer.setOnEndOfMedia(new Runnable() {            
                @Override
                public void run() {
                    proximaMusica();
                }
            });
        }
    }
    
    public void proximaMusica(){
        
        try {
            indexmusicaAnterior = indexMusicaAtual;
            if(aleatorio){
                Random random = new Random();
                indexMusicaAtual = random.nextInt(minhasMusicas.size());
            }else{
                indexMusicaAtual++;
            }
            musicaAtual = URLEncoder.encode(minhasMusicas.get(indexMusicaAtual),"UTF-8").replace("+","%20");
            mediaPlayer.dispose();
            mediaPlayer.stop();
            prepararMusica(musicaAtual);
            mediaPlayer.play();
        } catch (UnsupportedEncodingException ex) {
            System.out.println("Não foi possivel carregar a musica. ERRO: " + ex.getLocalizedMessage());
        }
    }
    public void anteriorMusica(){
        try {
            indexMusicaAtual = indexmusicaAnterior;
            musicaAtual = URLEncoder.encode(minhasMusicas.get(indexMusicaAtual),"UTF-8").replace("+","%20");
            mediaPlayer.stop();
            prepararMusica(musicaAtual);
            mediaPlayer.play();
        } catch (UnsupportedEncodingException ex) {
            System.out.println("Não foi possível voltar uma música.");
        }        
    }
    
    public void alterarVolume(){
        mediaPlayer.setVolume(sliderVolumeMusica.getValue());
    }
    
    public void switchAleatorio(){
        aleatorio = !aleatorio;
        if(aleatorio){
            buttonAleatorio.setStyle("-fx-background-color: #DCFFE3; -fx-background-radius: 50%");
        }else{
            buttonAleatorio.setStyle("-fx-background-color: #FFDCDC; -fx-background-radius: 50%");
        }
    }
    
    public void switchRepetir(){
        repetir = !repetir;
        if(repetir){
            mediaPlayer.setOnEndOfMedia(new Runnable() {            
                @Override
                public void run() {
                    prepararMusica(musicaAtual);
                    mediaPlayer.play();
                }
            });
            buttonRepetir.setStyle("-fx-background-color: #DCFFE3; -fx-background-radius: 50%");
        }else{
            mediaPlayer.setOnEndOfMedia(new Runnable() {            
                @Override
                public void run() {
                    proximaMusica();
                }
            });
            buttonRepetir.setStyle("-fx-background-color: #FFDCDC; -fx-background-radius: 50%");
        }
    }
    
    public void buscarMusicas(){
        mediaPlayer.stop();
        buttonPlayPause.setText("Play");
        
        caminho = textFildCaminho.getText();
        minhasMusicas = new ArrayList<>();
        listarArquivos(caminho);        
        iniciarUmaMusica();
    }
}
