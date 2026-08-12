package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import model.Bozza;

public class BozzaDAOImp implements BozzaDAO {

    private static final String TABLE_NAME = "bozza";
    private DataSource ds = null;

    public BozzaDAOImp(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(Bozza bozza) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME 
                + " (id_ordine, id_prodotto, file, commento_cliente) "
                + "VALUES (?, ?, ?, ?)";
        
        int idGenerato = -1;

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) { 
            ps.setInt(1, bozza.getIdOrdine());
            ps.setInt(2, bozza.getIdProdotto());
            ps.setString(3, bozza.getFile());
            ps.setString(4, bozza.getCommentoCliente());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    idGenerato = rs.getInt(1);
                    bozza.setIdBozza(idGenerato);
                }
            }
        }
    }

    @Override
    public synchronized Bozza doRetrieveByKey(int idBozza) throws SQLException {
        Bozza bean = null;
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_bozza = ?";

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setInt(1, idBozza);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    bean = new Bozza();
                    bean.setIdBozza(rs.getInt("id_bozza"));
                    bean.setIdOrdine(rs.getInt("id_ordine"));
                    bean.setIdProdotto(rs.getInt("id_prodotto"));
                    bean.setFile(rs.getString("file"));
                    bean.setStato(rs.getString("stato"));
                    bean.setCommentoCliente(rs.getString("commento_cliente"));
                }
            }
        }
        return bean;
    }

    @Override
    public synchronized List<Bozza> doRetrieveByRigaOrdine(int idOrdine, int idProdotto) throws SQLException {
        List<Bozza> list = new ArrayList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_ordine = ? AND id_prodotto = ?";

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setInt(1, idOrdine);
            ps.setInt(2, idProdotto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bozza bean = new Bozza();
                    bean.setIdBozza(rs.getInt("id_bozza"));
                    bean.setIdOrdine(rs.getInt("id_ordine"));
                    bean.setIdProdotto(rs.getInt("id_prodotto"));
                    bean.setFile(rs.getString("file"));
                    bean.setStato(rs.getString("stato"));
                    bean.setCommentoCliente(rs.getString("commento_cliente"));
                    list.add(bean);
                }
            }
        }
        return list;
    }

    @Override
    public synchronized Bozza doRetrieveUltimaBozza(int idOrdine, int idProdotto) throws SQLException {
        Bozza bean = null;
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_ordine = ? AND id_prodotto = ? ORDER BY id_bozza DESC LIMIT 1";

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setInt(1, idOrdine);
            ps.setInt(2, idProdotto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    bean = new Bozza();
                    bean.setIdBozza(rs.getInt("id_bozza"));
                    bean.setIdOrdine(rs.getInt("id_ordine"));
                    bean.setIdProdotto(rs.getInt("id_prodotto"));
                    bean.setFile(rs.getString("file"));
                    bean.setStato(rs.getString("stato"));
                    bean.setCommentoCliente(rs.getString("commento_cliente"));
                }
            }
        }
        return bean;
    }

    @Override
    public synchronized boolean doUpdateStatoECommento(int idBozza, String nuovoStato, String commentoCliente) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET stato = ?, commento_cliente = ? WHERE id_bozza = ?";
        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nuovoStato);
            ps.setString(2, commentoCliente);
            ps.setInt(3, idBozza);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated != 0;
        }
    }

    @Override
    public synchronized boolean doDelete(int idBozza) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id_bozza = ?";

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idBozza);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated != 0;
        }
    }
}