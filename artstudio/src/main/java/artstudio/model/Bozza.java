package artstudio.model;
import java.io.Serializable;

public class Bozza implements Serializable{
    private int idBozza;
    private int idOrdine;
    private int idProdotto;
    private String file;

    public Bozza() {
    }

    public Bozza(int idBozza, int idOrdine, int idProdotto, String file) {
        this.idBozza = idBozza;
        this.idOrdine = idOrdine;
        this.idProdotto = idProdotto;
        this.file = file;
    }

    public int getIdBozza() {
        return idBozza;
    }

    public void setIdBozza(int idBozza) {
        this.idBozza = idBozza;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}