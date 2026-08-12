package model;

public class Commissione extends Prodotto {
    private String tempo;

    public Commissione() {
    }

    public Commissione(int idProdotto, boolean isFisico, String nome, String descrizione, Double prezzo, boolean disponibile, String immagine, String tempo) {
        super(idProdotto, isFisico, nome, descrizione, prezzo, disponibile, immagine);
        this.tempo = tempo;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }
}
