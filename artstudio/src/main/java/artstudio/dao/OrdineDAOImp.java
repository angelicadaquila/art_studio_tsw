package artstudio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import artstudio.model.Commissione;
import artstudio.model.Ordine;
import artstudio.model.Stampa;

public class OrdineDAOImp implements OrdineDAO{
	
	 private static final String TABLE_NAME = "ordine";
	    private DataSource ds = null;

	    public OrdineDAOImp(DataSource ds) {
	        this.ds = ds;
	    }

	    @Override
	    public synchronized void doSave(Ordine ord) throws SQLException {
	        String insertOrdineSQL = "INSERT INTO " + TABLE_NAME 
	                + " (id_utente, dataOrdine, stato, totaleProdotti, spedeSpedizione, totaleOrdine) VALUES (?, ?, ?, ?, ?, ?)";

	        int idGenerato = -1;
	        
	        try (Connection connection = ds.getConnection();
	            PreparedStatement ps = connection.prepareStatement(insertOrdineSQL, Statement.RETURN_GENERATED_KEYS)) {
	            ps.setInt(1, ord.getIdUtente());
	            ps.setTimestamp(2, ord.getDataOrdine());
	            ps.setString(3, ord.getStato());
	            ps.setDouble(4, ord.getTotaleProdotti());
	            ps.setDouble(5, ord.getSpeseSpedizione());
	            ps.setDouble(6, ord.getTotaleOrdine());
	            ps.executeUpdate();
	            
	            try (ResultSet rs = ps.getGeneratedKeys()) {
	                if (rs.next()) {
	                    idGenerato = rs.getInt(1);
	                    ord.setIdOrdine(idGenerato);
	                }
	            }
	        }
	        
	    }
	    
	    @Override
	    public synchronized Ordine doRetrieveByKey(int idOrdine) throws SQLException {
	        Ordine bean = null;
	        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_ordine = ?";
	        try (Connection connection = ds.getConnection();
	            PreparedStatement ps = connection.prepareStatement(selectSQL)) { 
	            ps.setInt(1, idOrdine);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    bean = new Ordine();
	                    bean.setIdOrdine(rs.getInt("id_ordine"));
	                    bean.setIdUtente(rs.getInt("id_utente"));
	                    bean.setDataOrdine(rs.getTimestamp("data_ordine"));
	                    bean.setStato(rs.getString("stato"));
	                    bean.setTotaleProdotti(rs.getDouble("totale_prodotti"));
	                    bean.setSpeseSpedizione(rs.getDouble("spese_spedizione"));
	                    bean.setTotaleOrdine(rs.getDouble("totale_ordine"));
	                }
	            }
	        }
	        return bean;
	    }
	    
	    @Override
	    public synchronized List<Ordine> doRetrieveAll() throws SQLException {
	        List<Ordine> list = new ArrayList<>();
	        String selectSQL = "SELECT * FROM " + TABLE_NAME;
	        try (Connection connection = ds.getConnection();
	             PreparedStatement ps = connection.prepareStatement(selectSQL)) {
	             try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    Ordine bean = new Ordine();
	                    bean.setIdOrdine(rs.getInt("id_ordine"));
	                    bean.setIdUtente(rs.getInt("id_utente"));
	                    bean.setDataOrdine(rs.getTimestamp("data_ordine"));
	                    bean.setStato(rs.getString("stato"));
	                    bean.setTotaleProdotti(rs.getDouble("totale_prodotti"));
	                    bean.setSpeseSpedizione(rs.getDouble("spese_spedizione"));
	                    bean.setTotaleOrdine(rs.getDouble("totale_ordine"));
	                    list.add(bean);
	                }
	            }
	        }
	        return list;
	    }
	    
	    @Override
	    public synchronized List<Ordine> doRetrieveByUtente(int idUtente) throws SQLException {
	        List<Ordine> list = new ArrayList<>();
	        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_utente = ?";
	        try (Connection connection = ds.getConnection();
	            PreparedStatement ps = connection.prepareStatement(selectSQL)) { 
	            ps.setInt(1, idUtente);
	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    Ordine bean = new Ordine();
	                    bean.setIdOrdine(rs.getInt("id_ordine"));
	                    bean.setIdUtente(rs.getInt("id_utente"));
	                    bean.setDataOrdine(rs.getTimestamp("data_ordine"));
	                    bean.setStato(rs.getString("stato"));
	                    bean.setTotaleProdotti(rs.getDouble("totale_prodotti"));
	                    bean.setSpeseSpedizione(rs.getDouble("spese_spedizione"));
	                    bean.setTotaleOrdine(rs.getDouble("totale_ordine"));
	                    list.add(bean);
	                }
	            }
	        }
	        return list;
	    }
	    
	    @Override
	    public synchronized List<Ordine> doRetrieveByIntervalDate(Timestamp dataInizio, Timestamp dataFine) throws SQLException {
	        List<Ordine> list = new ArrayList<>();
	        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE data_ordine >= ? AND data_ordine <= ?";
	        try (Connection connection = ds.getConnection();
	            PreparedStatement ps = connection.prepareStatement(selectSQL)) {  
	            ps.setTimestamp(1, dataInizio);
	            ps.setTimestamp(2, dataFine);
	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    Ordine bean = new Ordine();
	                    bean.setIdOrdine(rs.getInt("id_ordine"));
	                    bean.setIdUtente(rs.getInt("id_utente"));
	                    bean.setDataOrdine(rs.getTimestamp("data_ordine"));
	                    bean.setStato(rs.getString("stato"));
	                    bean.setTotaleProdotti(rs.getDouble("totale_prodotti"));
	                    bean.setSpeseSpedizione(rs.getDouble("spese_spedizione"));
	                    bean.setTotaleOrdine(rs.getDouble("totale_ordine"));
	                    list.add(bean);
	                }
	            }
	        }
	        return list;
	    }
	    
	    @Override
	    public synchronized List<Ordine> doRetrieveByUtenteAndIntervalDate(int idUtente, Timestamp dataInizio, Timestamp dataFine) throws SQLException {
	        List<Ordine> list = new ArrayList<>();
	        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id_utente = ? AND data_ordine >= ? AND data_ordine <= ?";
	        try (Connection connection = ds.getConnection();
	            PreparedStatement ps = connection.prepareStatement(selectSQL)) {  
	            ps.setInt(1, idUtente);
	            ps.setTimestamp(2, dataInizio);
	            ps.setTimestamp(3, dataFine);
	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {
	                    Ordine bean = new Ordine();
	                    bean.setIdOrdine(rs.getInt("id_ordine"));
	                    bean.setIdUtente(rs.getInt("id_utente"));
	                    bean.setDataOrdine(rs.getTimestamp("data_ordine"));
	                    bean.setStato(rs.getString("stato"));
	                    bean.setTotaleProdotti(rs.getDouble("totale_prodotti"));
	                    bean.setSpeseSpedizione(rs.getDouble("spese_spedizione"));
	                    bean.setTotaleOrdine(rs.getDouble("totale_ordine"));
	                    list.add(bean);
	                }
	            }
	        }
	        return list;
	    }
	    
	    @Override
	    public synchronized boolean doUpdateStato(int idOrdine, String nuovoStato) throws SQLException {
	        String updateSQL = "UPDATE " + TABLE_NAME + " SET stato = ? WHERE id_ordine = ?";
	        try (Connection connection = ds.getConnection();
	            PreparedStatement ps = connection.prepareStatement(updateSQL)) {  
	            ps.setString(1, nuovoStato);
	            ps.setInt(2, idOrdine);
	            int result = ps.executeUpdate();
	            return result !=0;
	        }
	       
	    }
	    
	    @Override
	    public synchronized boolean doDelete(int idOrdine) throws SQLException {
	        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id_ordine = ?"; 
	        try (Connection connection = ds.getConnection();
	            PreparedStatement ps = connection.prepareStatement(deleteSQL)) { 
	            ps.setInt(1, idOrdine);
	            int result = ps.executeUpdate();
	            return result !=0;
	        }
	    }
	 

}
