package dao;

import java.sql.SQLException;
import java.util.List;

import model.Indirizzo;

public interface IndirizzoDAO {

    void doSave(Indirizzo indirizzo) throws SQLException;

    Indirizzo doRetrieveByKey(int idIndirizzo) throws SQLException;

    List<Indirizzo> doRetrieveByUtente(int idUtente) throws SQLException;

    boolean doUpdate(Indirizzo indirizzo) throws SQLException;

    boolean doDelete(int idIndirizzo) throws SQLException;
}