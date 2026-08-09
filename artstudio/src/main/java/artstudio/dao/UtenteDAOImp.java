package artstudio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import artstudio.model.Utente;

public class UtenteDAOImp implements UtenteDAO {

    private static final String TABLE_NAME = "utente";
    private DataSource ds = null;

    public UtenteDAOImp(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(Utente utente) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (email, password, nome, cognome, ruolo) "+ "VALUES (?, ?, ?, ?, ?)";

        int idGenerato = -1;
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, utente.getEmail());
            ps.setString(2, utente.getPassword());
            ps.setString(3, utente.getNome());
            ps.setString(4, utente.getCognome());
            ps.setString(5, utente.getRuolo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    idGenerato = rs.getInt(1);
                    utente.setIdUtente(idGenerato);
                }
            }
        }
    }

    @Override
    public synchronized Utente doRetrieveByKey(int idUtente) throws SQLException {
        Utente bean = null;
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_utente = ?";

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setInt(1, idUtente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    bean = extractUtenteFromResultSet(rs);
                }
            }
        }
        return bean;
    }

    @Override
    public synchronized Utente doRetrieveByEmail(String email) throws SQLException {
        Utente bean = null;
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE email = ?";

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    bean = extractUtenteFromResultSet(rs);
                }
            }
        }
        return bean;
    }

    @Override
    public synchronized Utente doRetrieveByEmailAndPassword(String email, String password) throws SQLException {
        Utente bean = null;
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE email = ? AND password = ?";

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    bean = extractUtenteFromResultSet(rs);
                }
            }
        }
        return bean;
    }

    @Override
    public synchronized List<Utente> doRetrieveAll() throws SQLException {
        List<Utente> list = new ArrayList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME;

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractUtenteFromResultSet(rs));
            }
        }
        return list;
    }

    @Override
    public synchronized boolean doUpdate(Utente utente) throws SQLException {
        String updateSQL = "UPDATE " + TABLE_NAME + " SET email = ?, password = ?, nome = ?, cognome = ?, ruolo = ? "+ "WHERE id_utente = ?";

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(updateSQL)) {
            ps.setString(1, utente.getEmail());
            ps.setString(2, utente.getPassword());
            ps.setString(3, utente.getNome());
            ps.setString(4, utente.getCognome());
            ps.setString(5, utente.getRuolo());
            ps.setInt(6, utente.getIdUtente());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated != 0;
        }
    }

    @Override
    public synchronized boolean doDelete(int idUtente) throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id_utente = ?";

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setInt(1, idUtente);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated != 0;
        }
    }

    private Utente extractUtenteFromResultSet(ResultSet rs) throws SQLException {
        Utente bean = new Utente();
        bean.setIdUtente(rs.getInt("id_utente"));
        bean.setEmail(rs.getString("email"));
        bean.setPassword(rs.getString("password"));
        bean.setNome(rs.getString("nome"));
        bean.setCognome(rs.getString("cognome"));
        bean.setRuolo(rs.getString("ruolo"));
        return bean;
    }
}