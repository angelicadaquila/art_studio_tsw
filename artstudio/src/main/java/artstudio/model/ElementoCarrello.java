package artstudio.model;

import java.io.Serializable;

public class ElementoCarrello implements Serializable {

    private static final long serialVersionUID = 1L;

    private Prodotto prod;
    private int quantita;

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

    public double getTotale() {
        return prod.getPrezzo() * quantita;
    }
}