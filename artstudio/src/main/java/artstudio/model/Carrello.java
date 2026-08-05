package artstudio.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrello implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ElementoCarrello> elementi;

    public Carrello() {
        elementi = new ArrayList<ElementoCarrello>();
    }

    public void aggiungiProd(Prodotto prod, int quantita) {
        if (prod instanceof Stampa) {
            boolean giaPresente = false;

            for (int i = 0; i < elementi.size(); i++) {
                ElementoCarrello item = elementi.get(i);
                if (item.getProdotto() instanceof Stampa && item.getProdotto().getIdProdotto() == prod.getIdProdotto()) {
                    item.setQuantita(item.getQuantita() + quantita);
                    giaPresente = true;
                    break;
                }
            }

            if (!giaPresente) {
                elementi.add(new ElementoCarrello(prod, quantita));
            }
        } else {
            elementi.add(new ElementoCarrello(prod, 1));
        }
    }

    public void aggiornaQuantita(int idProdotto, int nuovaQuantita) {
        if (nuovaQuantita <= 0) {
            eliminaProd(idProdotto);
            return;
        }

        for (int i = 0; i < elementi.size(); i++) {
            ElementoCarrello item = elementi.get(i);
            if (item.getProdotto().getIdProdotto() == idProdotto) {
                if (item.getProdotto() instanceof Stampa) {
                    item.setQuantita(nuovaQuantita);
                }
                break;
            }
        }
    }

    public void eliminaProd(int idProdotto) {
        for (int i = 0; i < elementi.size(); i++) {
            if (elementi.get(i).getProdotto().getIdProdotto() == idProdotto) {
                elementi.remove(i);
                break;
            }
        }
    }

    public void svuota() {
        elementi.clear();
    }

    public List<ElementoCarrello> getElementi() {
        return elementi;
    }

    public double getTotale() {
        double totale = 0.0;
        for (int i = 0; i < elementi.size(); i++) {
            totale += elementi.get(i).getTotale();
        }
        return totale;
    }
}