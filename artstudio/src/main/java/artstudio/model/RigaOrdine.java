package artstudio.model;
import java.io.Serializable;


public class RigaOrdine implements Serializable{
    private int idOrdine;
    private int idProdotto;
    private Double prezzoOg;
    private int quantita;
    private String descrizioneComm;
    private String refComm; 
    private String fileFinale;

    public RigaOrdine() {
    }

    public RigaOrdine(int idOrdine, int idProdotto, Double prezzoOg, int quantita, String descrizioneComm, String refComm, String fileFinale) {
        this.idOrdine = idOrdine;
        this.idProdotto = idProdotto;
        this.prezzoOg = prezzoOg;
        this.quantita = quantita;
        this.descrizioneComm = descrizioneComm;
        this.refComm = refComm;
        this.fileFinale = fileFinale;
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

    public Double getPrezzoOg() {
        return prezzoOg;
    }

    public void setPrezzoOg(Double prezzoOg) {
        this.prezzoOg = prezzoOg;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getDescrizioneComm() {
        return descrizioneComm;
    }

    public void setDescrizioneComm(String descrizioneComm) {
        this.descrizioneComm = descrizioneComm;
    }

    public String getRefComm() {
        return refComm;
    }

    public void setRefComm(String refComm) {
        this.refComm = refComm;
    }
    
    public String getFileFinale() {
        return fileFinale;
    }

    public void setFileFinale(String fileFinale) {
        this.fileFinale = fileFinale;
    }
}