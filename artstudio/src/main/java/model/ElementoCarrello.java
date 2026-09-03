package model;

import java.io.Serializable;

public class ElementoCarrello implements Serializable {

    private static final long serialVersionUID = 1L;

    private Prodotto prod;
    private int quantita;
    private String descrizioneComm;
    private String refComm; 

    public ElementoCarrello() {}

    public ElementoCarrello(Prodotto prod, int quantita) {
        this.prod = prod;
        this.quantita = quantita;
    }

    public Prodotto getProdotto() {
        return prod;
    }

    public void setProdotto(Prodotto prod) {
        this.prod = prod;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
    
    public void setDescrizioneComm(String descrizioneComm) {
        this.descrizioneComm = descrizioneComm;
    }
    
    public String getDescrizioneComm() {
        return descrizioneComm;
    }
    
    public void setRefComm(String refComm) {
        this.refComm = refComm;
    }
    
    public String getRefComm() {
        return refComm;
    }

    public double getTotale() {
        return prod.getPrezzo() * quantita;
    }
}