package dao;

import java.sql.SQLException;
import java.util.List;

import model.Bozza;

public interface BozzaDAO {

    void doSave(Bozza bozza) throws SQLException;

    Bozza doRetrieveByKey(int idBozza) throws SQLException;

    List<Bozza> doRetrieveByRigaOrdine(int idOrdine, int idProdotto) throws SQLException;

    Bozza doRetrieveUltimaBozza(int idOrdine, int idProdotto) throws SQLException;

    boolean doUpdateStatoECommento(int idBozza, String nuovoStato, String commentoCliente) throws SQLException;

    boolean doDelete(int idBozza) throws SQLException;
}