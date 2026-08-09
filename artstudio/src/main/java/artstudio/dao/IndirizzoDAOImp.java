package artstudio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import artstudio.model.Indirizzo;

public class IndirizzoDAOImp implements IndirizzoDAO {

    private static final String TABLE_NAME = "indirizzo";
    private DataSource ds = null;

    public IndirizzoDAOImp(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(Indirizzo indirizzo) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (id_utente, via, civico, citta, regione) "+ "VALUES (?, ?, ?, ?, ?)";

        int idGenerato = -1;

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, indirizzo.getIdUtente());
            ps.setString(2, indirizzo.getVia());
            ps.setString(3, indirizzo.getCivico());
            ps.setString(4, indirizzo.getCitta());
            ps.setString(5, indirizzo.getRegione());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    idGenerato = rs.getInt(1);
                    indirizzo.setIdIndirizzo(idGenerato);
                }
            }
        }
    }

    @Override
    public synchronized Indirizzo doRetrieveByKey(int idIndirizzo) throws SQLException {
        Indirizzo bean = null;
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_indirizzo = ?";

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setInt(1, idIndirizzo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    bean = new Indirizzo();
                    bean.setIdIndirizzo(rs.getInt("id_indirizzo"));
                    bean.setIdUtente(rs.getInt("id_utente"));
                    bean.setVia(rs.getString("via"));
                    bean.setCivico(rs.getString("civico"));
                    bean.setCitta(rs.getString("citta"));
                    bean.setRegione(rs.getString("regione"));
                }
            }
        }
        return bean;
    }

    @Override
    public synchronized List<Indirizzo> doRetrieveByUtente(int idUtente) throws SQLException {
        List<Indirizzo> list = new ArrayList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_utente = ?";

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setInt(1, idUtente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Indirizzo bean = new Indirizzo();
                    bean.setIdIndirizzo(rs.getInt("id_indirizzo"));
                    bean.setIdUtente(rs.getInt("id_utente"));
                    bean.setVia(rs.getString("via"));
                    bean.setCivico(rs.getString("civico"));
                    bean.setCitta(rs.getString("citta"));
                    bean.setRegione(rs.getString("regione"));
                    list.add(bean);
                }
            }
        }
        return list;
    }

    @Override
    public synchronized boolean doUpdate(Indirizzo indirizzo) throws SQLException {
        String updateSQL = "UPDATE " + TABLE_NAME + " SET id_utente = ?, via = ?, civico = ?, citta = ?, regione = ? "+ "WHERE id_indirizzo = ?";
        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(updateSQL)) {
            ps.setInt(1, indirizzo.getIdUtente());
            ps.setString(2, indirizzo.getVia());
            ps.setString(3, indirizzo.getCivico());
            ps.setString(4, indirizzo.getCitta());
            ps.setString(5, indirizzo.getRegione());
            ps.setInt(6, indirizzo.getIdIndirizzo());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated != 0;
        }
    }

    @Override
    public synchronized boolean doDelete(int idIndirizzo) throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id_indirizzo = ?";
        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setInt(1, idIndirizzo);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated != 0;
        }
    }
}