package artstudio.model;

public class Stampa extends Prodotto {
    private String dimensione;
    private int quantita;

    public Stampa() {
    }

    public Stampa(int idProdotto, boolean isFisico, String nome, String descrizione, Double prezzo, boolean disponibile, String immagine, String dimensione, int quantita) {
        super(idProdotto, isFisico, nome, descrizione, prezzo, disponibile, immagine);
        this.dimensione = dimensione;
        this.quantita=quantita;
    }

    public String getDimensione() {
        return dimensione;
    }

    public void setDimensione(String dimensione) {
        this.dimensione = dimensione;
    }
    
    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
}
