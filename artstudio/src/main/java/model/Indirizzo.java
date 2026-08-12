package model;
import java.io.Serializable;

public class Indirizzo implements Serializable{
    private int idIndirizzo;
    private int idUtente;
    private String via;
    private String civico;
    private String citta;
    private String regione;

    public Indirizzo() {
    }

    public Indirizzo(int idIndirizzo, int idUtente, String via, String civico, String citta, String regione) {
        this.idIndirizzo = idIndirizzo;
        this.idUtente = idUtente;
        this.via = via;
        this.civico = civico;
        this.citta = citta;
        this.regione = regione;
    }

    public int getIdIndirizzo() {
        return idIndirizzo;
    }

    public void setIdIndirizzo(int idIndirizzo) {
        this.idIndirizzo = idIndirizzo;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getCivico() {
        return civico;
    }

    public void setCivico(String civico) {
        this.civico = civico;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }
}