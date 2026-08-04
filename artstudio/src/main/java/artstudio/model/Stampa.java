package artstudio.model;

public class Stampa extends Prodotto {
    private String dimensione;

    public Stampa() {
    }

    public Stampa(int idProdotto, boolean isFisico, String nome, String descrizione, Double prezzo, boolean disponibile, String immagine, String dimensione) {
        super(idProdotto, isFisico, nome, descrizione, prezzo, disponibile, immagine);
        this.dimensione = dimensione;
    }

    public String getDimensione() {
        return dimensione;
    }

    public void setDimensione(String dimensione) {
        this.dimensione = dimensione;
    }
}
