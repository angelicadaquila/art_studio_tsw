package artstudio.model;
import java.io.Serializable;

import java.sql.Timestamp;

public class Ordine implements Serializable{
    private int idOrdine;
    private int idUtente;
    private Timestamp dataOrdine;
    private String stato;
    private Double totaleProdotti;
    private Double speseSpedizione;
    private Double totaleOrdine;

    public Ordine() {
    }

    public Ordine(int idOrdine, int idUtente, Timestamp dataOrdine, String stato, Double totaleProdotti, Double speseSpedizione, Double totaleOrdine) {
        this.idOrdine = idOrdine;
        this.idUtente = idUtente;
        this.dataOrdine = dataOrdine;
        this.stato = stato;
        this.totaleProdotti = totaleProdotti;
        this.speseSpedizione = speseSpedizione;
        this.totaleOrdine = totaleOrdine;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public Timestamp getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(Timestamp dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public Double getTotaleProdotti() {
        return totaleProdotti;
    }

    public void setTotaleProdotti(Double totaleProdotti) {
        this.totaleProdotti = totaleProdotti;
    }

    public Double getSpeseSpedizione() {
        return speseSpedizione;
    }

    public void setSpeseSpedizione(Double speseSpedizione) {
        this.speseSpedizione = speseSpedizione;
    }

    public Double getTotaleOrdine() {
        return totaleOrdine;
    }

    public void setTotaleOrdine(Double totaleOrdine) {
        this.totaleOrdine = totaleOrdine;
    }
}
