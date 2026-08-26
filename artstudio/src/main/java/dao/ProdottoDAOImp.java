package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

import javax.sql.DataSource;

import model.Commissione;
import model.Prodotto;
import model.Stampa;

public class ProdottoDAOImp implements ProdottoDAO {

    private static final String TABLE_NAME = "prodotto";
    private DataSource ds = null;

    public ProdottoDAOImp(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(Prodotto prod) throws SQLException {
        String insertProdottoSQL = "INSERT INTO " + TABLE_NAME 
                + " (is_fisico, nome, descrizione, prezzo, disponibile, immagine) VALUES (?, ?, ?, ?, ?, ?)";

        int idGenerato = -1;
        
        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(insertProdottoSQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, prod.isFisico());
            ps.setString(2, prod.getNome());
            ps.setString(3, prod.getDescrizione());
            ps.setDouble(4, prod.getPrezzo());
            ps.setBoolean(5, prod.isDisponibile());
            ps.setString(6, prod.getImmagine());
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    idGenerato = rs.getInt(1);
                    prod.setIdProdotto(idGenerato);
                }
            }
        }
        
        if (prod instanceof Stampa) {
            Stampa stampa = (Stampa) prod;
            String insertStampaSQL = "INSERT INTO stampa (id_prodotto, dimensione, quantita) VALUES (?, ?, ?)";
            try (Connection connection = ds.getConnection();
                PreparedStatement psStampa = connection.prepareStatement(insertStampaSQL)) { 
                psStampa.setInt(1, idGenerato);
                psStampa.setString(2, stampa.getDimensione());
                psStampa.setInt(3, stampa.getQuantita());
                psStampa.executeUpdate();
            }
        } else if (prod instanceof Commissione) {
            Commissione commissione = (Commissione) prod;
            String insertCommissioneSQL = "INSERT INTO commissione (id_prodotto, tempo) VALUES (?, ?)";
            try (Connection connection = ds.getConnection();
                PreparedStatement psComm = connection.prepareStatement(insertCommissioneSQL)) { 
                psComm.setInt(1, idGenerato);
                psComm.setString(2, commissione.getTempo()); 
                psComm.executeUpdate();
            }
        }
    }

    @Override
    public synchronized boolean doUpdateImage(Prodotto prod) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET immagine = ? WHERE id_prodotto = ?";
        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, prod.getImmagine());
            ps.setInt(2, prod.getIdProdotto());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated != 0;
        }
    }
    
    @Override
    public synchronized boolean doUpdate(Prodotto prod) throws SQLException {
        if (prod == null) return false;
        String updateProdottoSQL = "UPDATE " + TABLE_NAME + " SET is_fisico = ?, nome = ?, descrizione = ?, prezzo = ?, disponibile = ?, immagine = ? " +"WHERE id_prodotto = ?";

        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(updateProdottoSQL)) {
            ps.setBoolean(1, prod.isFisico());
            ps.setString(2, prod.getNome());
            ps.setString(3, prod.getDescrizione());
            ps.setDouble(4, prod.getPrezzo());
            ps.setBoolean(5, prod.isDisponibile());
            ps.setString(6, prod.getImmagine());
            ps.setInt(7, prod.getIdProdotto());
            ps.executeUpdate();
        }

        if (prod instanceof Stampa) {
            Stampa stampa = (Stampa) prod;
            String updateStampaSQL = "INSERT INTO stampa (id_prodotto, dimensione, quantita) VALUES (?, ?, ?) " +"ON DUPLICATE KEY UPDATE dimensione = VALUES(dimensione), quantita = VALUES(quantita)";
            
            try (Connection connection = ds.getConnection();
                PreparedStatement ps = connection.prepareStatement(updateStampaSQL)) {
                ps.setInt(1, stampa.getIdProdotto());
                ps.setString(2, stampa.getDimensione());
                ps.setInt(3, stampa.getQuantita());
                ps.executeUpdate();
            }

        } else if (prod instanceof Commissione) {
            Commissione commissione = (Commissione) prod;
            String updateCommSQL = "INSERT INTO commissione (id_prodotto, tempo) VALUES (?, ?) " +"ON DUPLICATE KEY UPDATE tempo = VALUES(tempo)";
            
            try (Connection connection = ds.getConnection();
                PreparedStatement ps = connection.prepareStatement(updateCommSQL)) {
                ps.setInt(1, commissione.getIdProdotto());
                ps.setString(2, commissione.getTempo());
                ps.executeUpdate();
            }
        }

        return true;
    }
    
    @Override
    public synchronized boolean doUpdateQuantita(int idProdotto, int nuovaQuantita) throws SQLException {
        Prodotto prod = doRetrieveByKey(idProdotto); 
        if (prod != null && prod instanceof Stampa) {
            String sql = "UPDATE stampa SET quantita = ? WHERE id_prodotto = ?";
            try (Connection connection = ds.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, nuovaQuantita);
                ps.setInt(2, idProdotto);
                int rowsUpdated = ps.executeUpdate();
                return rowsUpdated != 0;
            }
        }
        
        return false;
    }

    @Override
    public synchronized Prodotto doRetrieveByKey(int idProdotto) throws SQLException {
        Prodotto bean = new Prodotto();
        String selectSQL = "SELECT p.*, s.dimensione, s.quantita, c.tempo " +
                "FROM " + TABLE_NAME + " p " +
                "LEFT JOIN stampa s ON p.id_prodotto = s.id_prodotto " +
                "LEFT JOIN commissione c ON p.id_prodotto = c.id_prodotto " +
                "WHERE p.id_prodotto = ?";
        
        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(selectSQL)) {
            ps.setInt(1, idProdotto);
            try (ResultSet rs = ps.executeQuery()) {
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
        String deleteSQL = "UPDATE " + TABLE_NAME + " SET disponibile = false WHERE id_prodotto = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setInt(1, idProdotto);
            int result = ps.executeUpdate();
            return result != 0;
        }
    }

    @Override
    public synchronized List<Prodotto> doRetrieveAll(String ordine) throws SQLException {
    	List<Prodotto> products = new ArrayList<>();
        String selectSQL = "SELECT p.id_prodotto, p.is_fisico, p.nome, p.descrizione, p.prezzo, p.disponibile, p.immagine, "
                + "s.dimensione, s.quantita, c.tempo "
                + "FROM " + TABLE_NAME + " p "
                + "LEFT JOIN stampa s ON p.id_prodotto = s.id_prodotto "
                + "LEFT JOIN commissione c ON p.id_prodotto = c.id_prodotto "
                + "WHERE p.disponibile = true"; 

        if (ordine != null && !ordine.trim().isEmpty()) {
            selectSQL = selectSQL + " ORDER BY " + ordine;
        } else {
            selectSQL = selectSQL + " ORDER BY p.id_prodotto ASC";
        }
        
        try (Connection connection = ds.getConnection();
            PreparedStatement ps = connection.prepareStatement(selectSQL);
            ResultSet rs = ps.executeQuery()) {
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
    
    @Override
    public synchronized List<Prodotto> doRetrieveAllAdmin(String ordine) throws SQLException {
        List<Prodotto> products = new ArrayList<>();
        String selectSQL = "SELECT p.id_prodotto, p.is_fisico, p.nome, p.descrizione, p.prezzo, p.disponibile, p.immagine, "
                + "s.dimensione, s.quantita, c.tempo "
                + "FROM " + TABLE_NAME + " p "
                + "LEFT JOIN stampa s ON p.id_prodotto = s.id_prodotto "
                + "LEFT JOIN commissione c ON p.id_prodotto = c.id_prodotto";

        if (ordine != null && !ordine.trim().isEmpty()) {
            selectSQL = selectSQL + " ORDER BY " + ordine;
        } else {
            selectSQL = selectSQL + " ORDER BY p.id_prodotto ASC";
        }

        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(selectSQL);
             ResultSet rs = ps.executeQuery()) {
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