package dao;

import java.sql.SQLException;
import model.Carrello;
import java.sql.Timestamp;
import java.util.List;

import model.Ordine;

public interface OrdineDAO {
    
    public void doSaveConCarrello(Ordine ord, Carrello carrello, int idIndirizzo, String metodoPagamento) throws SQLException;

    public Ordine doRetrieveByKey(int idOrdine) throws SQLException;

    public List<Ordine> doRetrieveAll() throws SQLException;

    public List<Ordine> doRetrieveByUtente(int idUtente) throws SQLException;
    
    public List<Ordine> doRetrieveByIntervalDate(Timestamp dataInizio, Timestamp dataFine) throws SQLException;
    
    public List<Ordine> doRetrieveByUtenteAndIntervalDate(int idUtente, Timestamp dataInizio, Timestamp dataFine) throws SQLException;
    
    public boolean doUpdateStato(int idOrdine, String nuovoStato) throws SQLException;

    public boolean doDelete(int idOrdine) throws SQLException;
}