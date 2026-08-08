package artstudio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import artstudio.model.Commissione;
import artstudio.model.Prodotto;
import artstudio.model.Stampa;

public class ProdottoDAOImp implements ProdottoDAO {

    private static final String TABLE_NAME = "prodotto";
    private DataSource ds = null;

    public ProdottoDAOImp(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(Prodotto prod) throws SQLException {
        String insertProdottoSQL = "INSERT INTO " + TABLE_NAME 
                + " (id_prodotto, is_fisico, nome, descrizione, prezzo, disponibile, immagine) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ds.getConnection();
            PreparedStatement psProdotto = connection.prepareStatement(insertProdottoSQL)) {
            psProdotto.setInt(1, prod.getIdProdotto());
            psProdotto.setBoolean(2, prod.isFisico());
            psProdotto.setString(3, prod.getNome());
            psProdotto.setString(4, prod.getDescrizione());
            psProdotto.setDouble(5, prod.getPrezzo());
            psProdotto.setBoolean(6, prod.isDisponibile());
            psProdotto.setString(7, prod.getImmagine());
            psProdotto.executeUpdate();
        }

        if (prod instanceof Stampa) {
            Stampa stampa = (Stampa) prod;
            String insertStampaSQL = "INSERT INTO stampa (id_prodotto, dimensione, quantita) VALUES (?, ?, ?)";
            
            try (Connection connection = ds.getConnection();
                PreparedStatement psStampa = connection.prepareStatement(insertStampaSQL)) { 
                psStampa.setInt(1, stampa.getIdProdotto());
                psStampa.setString(2, stampa.getDimensione());
                psStampa.setInt(3, stampa.getQuantita());
                psStampa.executeUpdate();
            }
        } else if (prod instanceof Commissione) {
            Commissione commissione = (Commissione) prod;
            String insertCommissioneSQL = "INSERT INTO commissione (id_prodotto, tempo) VALUES (?, ?)";
            
            try (Connection connection = ds.getConnection();
                PreparedStatement psComm = connection.prepareStatement(insertCommissioneSQL)) { 
                psComm.setInt(1, commissione.getIdProdotto());
                psComm.setString(2, commissione.getTempo()); 
                psComm.executeUpdate();
            }
        }
    }

    @Override
    public synchronized boolean doUpdateImage(Prodotto prod) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET immagine = ? WHERE id_prodotto = ?";
        try (Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prod.getImmagine());
            ps.setInt(2, prod.getIdProdotto());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated != 0;
        }
    }

    @Override
    public synchronized Prodotto doRetrieveByKey(int idProdotto) throws SQLException {
        Prodotto bean = new Prodotto();
        String selectSQL = "SELECT p.*, s.dimensione, c.tempo " +
                "FROM " + TABLE_NAME + " p " +
                "LEFT JOIN stampa s ON p.id_prodotto = s.id_prodotto " +
                "LEFT JOIN commissione c ON p.id_prodotto = c.id_prodotto " +
                "WHERE p.id_prodotto = ?";
        
        try (Connection connection = ds.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, idProdotto);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    boolean isFisico = rs.getBoolean("is_fisico");

                    if (isFisico) {
                        bean = new Stampa();
                        ((Stampa) bean).setDimensione(rs.getString("dimensione"));
                        ((Stampa) bean).setQuantita(rs.getInt("quantita"));
                    } else {
                    	bean= new Commissione();
                    	((Commissione) bean).setTempo(rs.getString("tempo"));
                    }
                    bean.setIdProdotto(rs.getInt("id_prodotto"));
                    bean.setFisico(rs.getBoolean("is_fisico"));
                    bean.setNome(rs.getString("nome"));
                    bean.setDescrizione(rs.getString("descrizione"));
                    bean.setPrezzo(rs.getDouble("prezzo"));
                    bean.setDisponibile(rs.getBoolean("disponibile"));
                    bean.setImmagine(rs.getString("immagine"));
                }
            }
        }
        return bean;
    }

    @Override
    public synchronized boolean doDelete(int idProdotto) throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id_prodotto = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, idProdotto);
            int result = preparedStatement.executeUpdate();
            return result != 0;
        }
    }

    @Override
    public synchronized List<Prodotto> doRetrieveAll(String ordine) throws SQLException {
        List<Prodotto> products = new LinkedList<>();
        
        String selectSQL = "SELECT p.*, s.dimensione, c.tempo "
                + "FROM " + TABLE_NAME + " p "
                + "LEFT JOIN stampa s ON p.id_prodotto = s.id_prodotto "
                + "LEFT JOIN commissione c ON p.id_prodotto = c.id_prodotto";

        if (ordine != null && !ordine.isEmpty()) {
            selectSQL += " ORDER BY " + ordine;
        }
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                Prodotto bean;
                boolean isFisico = rs.getBoolean("is_fisico");
                if (isFisico) {
                    bean = new Stampa();
                    ((Stampa) bean).setDimensione(rs.getString("dimensione"));
                    ((Stampa) bean).setQuantita(rs.getInt("quantita"));
                } else {
                    bean = new Commissione();
                    ((Commissione) bean).setTempo(rs.getString("tempo"));
                }
                bean.setIdProdotto(rs.getInt("id_prodotto"));
                bean.setFisico(isFisico);
                bean.setNome(rs.getString("nome"));
                bean.setDescrizione(rs.getString("descrizione"));
                bean.setPrezzo(rs.getDouble("prezzo"));
                bean.setDisponibile(rs.getBoolean("disponibile"));
                bean.setImmagine(rs.getString("immagine"));
                products.add(bean);
            }
        }
        return products;
    }
}