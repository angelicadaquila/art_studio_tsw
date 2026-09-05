package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import model.Commissione;
import model.Ordine;
import model.Carrello;
import model.ElementoCarrello;
import model.Prodotto;

public class OrdineDAOImp implements OrdineDAO{
	
	 private static final String TABLE_NAME = "ordine";
	    private DataSource ds = null;

	    public OrdineDAOImp(DataSource ds) {
	        this.ds = ds;
	    }
	   
	    public synchronized void doSaveConCarrello(Ordine ord, Carrello carrello, int idIndirizzo, String metodoPagamento) throws SQLException {
	        String insertOrdineSQL = "INSERT INTO " + TABLE_NAME  + " (id_utente, totale_prodotti, spese_spedizione, totale_ordine, id_indirizzo, metodo_pagamento, stato) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        String insertRigaSQL = "INSERT INTO riga_ordine (id_ordine, id_prodotto, prezzo_acquisto, quantita, descrizione_comm, ref_comm) VALUES (?, ?, ?, ?, ?, ?)";

	        Connection connection = null;
	        PreparedStatement psOrdine = null;
	        PreparedStatement psRiga = null;

	        try {
	            connection = ds.getConnection();
	            connection.setAutoCommit(false);
	            psOrdine = connection.prepareStatement(insertOrdineSQL, Statement.RETURN_GENERATED_KEYS);
	            psOrdine.setInt(1, ord.getIdUtente());
	            psOrdine.setDouble(2, ord.getTotaleProdotti());
	            psOrdine.setDouble(3, ord.getSpeseSpedizione());
	            psOrdine.setDouble(4, ord.getTotaleOrdine());
	            psOrdine.setInt(5, idIndirizzo);
	            psOrdine.setString(6, metodoPagamento);
	            psOrdine.setString(7, "In lavorazione");
	            psOrdine.executeUpdate();

	            int idOrdineGenerato = -1;
	            try (ResultSet rs = psOrdine.getGeneratedKeys()) {
	                if (rs.next()) {
	                    idOrdineGenerato = rs.getInt(1);
	                    ord.setIdOrdine(idOrdineGenerato);
	                }
	            }
	            psRiga = connection.prepareStatement(insertRigaSQL);
	            List<ElementoCarrello> elementi = carrello.getElementi();

	            for (int i = 0; i < elementi.size(); i++) {
	                ElementoCarrello item = elementi.get(i);
	                Prodotto prod = item.getProdotto();

	                psRiga.setInt(1, idOrdineGenerato);
	                psRiga.setInt(2, prod.getIdProdotto());
	                psRiga.setDouble(3, prod.getPrezzo());
	                psRiga.setInt(4, item.getQuantita());
	                
	                if (prod instanceof Commissione) {
	                    psRiga.setString(5, item.getDescrizioneComm());
	                    psRiga.setString(6, item.getRefComm());
	                } else {
	                    psRiga.setString(5, null);
	                    psRiga.setString(6, null);
	                }

	                psRiga.addBatch();
	            }

	            psRiga.executeBatch(); 
	            connection.commit();

	        } catch (SQLException e) {
	            if (connection != null) {
	                connection.rollback();
	            }
	            throw e;
	        } finally {
	            if (psRiga != null) psRiga.close();
	            if (psOrdine != null) psOrdine.close();
	            if (connection != null) {
	                connection.setAutoCommit(true);
	                connection.close();
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
