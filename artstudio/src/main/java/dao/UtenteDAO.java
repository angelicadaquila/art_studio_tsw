package dao;

import java.sql.SQLException;
import java.util.List;

import model.Utente;

public interface UtenteDAO {

    void doSave(Utente utente) throws SQLException;

    Utente doRetrieveByKey(int idUtente) throws SQLException;

    Utente doRetrieveByEmail(String email) throws SQLException;

    Utente doRetrieveByEmailAndPassword(String email, String password) throws SQLException;

    List<Utente> doRetrieveAll() throws SQLException;

    boolean doUpdate(Utente utente) throws SQLException;

    boolean doDelete(int idUtente) throws SQLException;
}