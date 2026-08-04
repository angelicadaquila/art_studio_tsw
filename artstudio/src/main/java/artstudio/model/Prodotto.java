package artstudio.model;
import java.io.Serializable;

public class Prodotto implements Serializable{
    private int idProdotto;
    private boolean isFisico;
    private String nome;
    private String descrizione;
    private Double prezzo;
    private boolean disponibile;
    private String immagine;

    public Prodotto() {
    }

    public Prodotto(int idProdotto, boolean isFisico, String nome, String descrizione, Double prezzo, boolean disponibile, String immagine) {
        this.idProdotto = idProdotto;
        this.isFisico = isFisico;
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.disponibile = disponibile;
        this.immagine = immagine;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    public boolean isFisico() {
        return isFisico;
    }

    public void setFisico(boolean isFisico) {
        this.isFisico = isFisico;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }
}