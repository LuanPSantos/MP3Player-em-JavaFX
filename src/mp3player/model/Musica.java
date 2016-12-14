package mp3player.model;

import javafx.scene.image.Image;

public class Musica {
    private String titulo;
    private String artista;
    private String duracao;
    private String url;
    private Image imagem;
    
    public Musica(){ }

    public Musica(String titulo, String artista, String duracao, String url, Image imagem) {
        setTitulo(titulo);
        setArtista(artista);
        setDuracao(duracao);
        setUrl(url);
        setImagem(imagem);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Image getImagem() {
        return imagem;
    }

    public void setImagem(Image imagem) {
        this.imagem = imagem;
    }
}
