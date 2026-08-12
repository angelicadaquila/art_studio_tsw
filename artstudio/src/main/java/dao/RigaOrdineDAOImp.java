package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import model.RigaOrdine;

public class RigaOrdineDAOImp implements RigaOrdineDAO {

    private static final String TABLE_NAME = "riga_ordine";
    private DataSource ds = null;

    public RigaOrdineDAOImp(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(RigaOrdine riga) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME 
                + " (id_ordine, id_prodotto, prezzo_og, quantita, descrizione_comm, ref_comm, file_finale) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(insertSQL)) {
            ps.setInt(1, riga.getIdOrdine());
            ps.setInt(2, riga.getIdProdotto());
            ps.setDouble(3, riga.getPrezzoOg());
            ps.setInt(4, riga.getQuantita());
            ps.setString(5, riga.getDescrizioneComm());
            ps.setString(6, riga.getRefComm());
            ps.setString(7, riga.getFileFinale());
            ps.executeUpdate();
        }
    }

    @Override
    public synchronized RigaOrdine doRetrieveByKey(int idOrdine, int idProdotto) throws SQLException {
        RigaOrdine bean = null;
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_ordine = ? AND id_prodotto = ?";
        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(selectSQL)) { 
            ps.setInt(1, idOrdine);
            ps.setInt(2, idProdotto);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    bean = new RigaOrdine();
                    bean.setIdOrdine(rs.getInt("id_ordine"));
                    bean.setIdProdotto(rs.getInt("id_prodotto"));
                    bean.setPrezzoOg(rs.getDouble("prezzo_og"));
                    bean.setQuantita(rs.getInt("quantita"));
                    bean.setDescrizioneComm(rs.getString("descrizione_comm"));
                    bean.setRefComm(rs.getString("ref_comm"));
                    bean.setFileFinale(rs.getString("file_finale"));
                }
            }
        }
        return bean;
    }

    @Override
    public synchronized List<RigaOrdine> doRetrieveByOrdine(int idOrdine) throws SQLException {
        List<RigaOrdine> list = new LinkedList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_ordine = ?";
        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setInt(1, idOrdine);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RigaOrdine bean = new RigaOrdine();
                    bean.setIdOrdine(rs.getInt("id_ordine"));
                    bean.setIdProdotto(rs.getInt("id_prodotto"));
                    bean.setPrezzoOg(rs.getDouble("prezzo_og"));
                    bean.setQuantita(rs.getInt("quantita"));
                    bean.setDescrizioneComm(rs.getString("descrizione_comm"));
                    bean.setRefComm(rs.getString("ref_comm"));
                    bean.setFileFinale(rs.getString("file_finale"));
                    list.add(bean);
                }
            }
        }
        return list;
    }
    
    @Override
    public synchronized boolean doUpdateFileFinale(int idOrdine, int idProdotto, String fileFinale) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET file_finale = ? WHERE id_ordine = ? AND id_prodotto = ?";
        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, fileFinale);
            ps.setInt(2, idOrdine);
            ps.setInt(3, idProdotto);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated != 0;
        }
    }
}