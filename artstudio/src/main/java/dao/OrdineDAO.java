package dao;

import java.sql.SQLException;
import model.Carrello;
import java.sql.Timestamp;
import java.util.List;

import model.Ordine;

public interface OrdineDAO {
    
    public void doSaveConCarrello(Ordine ord, Carrello carrello, int idIndirizzo) throws SQLException;

    public void doSaveConCarrello(Ordine ord, Carrello carrello, String via, String civico, String citta, String regione) throws SQLException;
    
    public Ordine doRetrieveByKey(int idOrdine) throws SQLException;

    public List<Ordine> doRetrieveAll() throws SQLException;

    public List<Ordine> doRetrieveByUtente(int idUtente) throws SQLException;
    
    public List<Ordine> doRetrieveByIntervalloData(Timestamp dataInizio, Timestamp dataFine) throws SQLException;
    
    public List<Ordine> doRetrieveByUtenteAndIntervalloData(int idUtente, Timestamp dataInizio, Timestamp dataFine) throws SQLException;
    
    public boolean doUpdateStato(int idOrdine, String nuovoStato) throws SQLException;

    public boolean doDelete(int idOrdine) throws SQLException;
    
    public boolean doUpdateImmagineConsegna(int idOrdine, String immagineConsegna) throws SQLException;
}