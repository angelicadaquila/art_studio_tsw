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
import model.Indirizzo;

public class OrdineDAOImp implements OrdineDAO {

    private static final String TABLE_NAME = "ordine";
    private DataSource ds = null;

    public OrdineDAOImp(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSaveConCarrello(Ordine ord, Carrello carrello, int idIndirizzo) throws SQLException {
        String insertOrdineSQL = "INSERT INTO " + TABLE_NAME + " (id_utente, totale_prodotti, spese_spedizione, totale_ordine, id_indirizzo, stato) VALUES (?, ?, ?, ?, ?, ?)";
        String insertRigaSQL = "INSERT INTO riga_ordine (id_ordine, id_prodotto, prezzo_og, quantita, descrizione_comm, ref_comm) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = ds.getConnection();
            PreparedStatement psOrdine = connection.prepareStatement(insertOrdineSQL, Statement.RETURN_GENERATED_KEYS)) {

            psOrdine.setInt(1, ord.getIdUtente());
            psOrdine.setDouble(2, ord.getTotaleProdotti());
            psOrdine.setDouble(3, ord.getSpeseSpedizione());
            psOrdine.setDouble(4, ord.getTotaleOrdine());
            psOrdine.setInt(5, idIndirizzo);
            psOrdine.setString(6, "In lavorazione");
            psOrdine.executeUpdate();

            int idOrdineGenerato = -1;
            try (ResultSet rs = psOrdine.getGeneratedKeys()) {
                if (rs.next()) {
                    idOrdineGenerato = rs.getInt(1);
                    ord.setIdOrdine(idOrdineGenerato);
                }
            }

            try (PreparedStatement psRiga = connection.prepareStatement(insertRigaSQL)) {
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
                    psRiga.executeUpdate();
                }
            }
        }
    }

    @Override
    public synchronized void doSaveConCarrello(Ordine ord, Carrello carrello, String via, String civico, String citta, String regione) throws SQLException {
        String insertOrdineSQL = "INSERT INTO " + TABLE_NAME + " (id_utente, totale_prodotti, spese_spedizione, totale_ordine, via_spedizione, civico_spedizione, citta_spedizione, regione_spedizione, stato) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertRigaSQL = "INSERT INTO riga_ordine (id_ordine, id_prodotto, prezzo_og, quantita, descrizione_comm, ref_comm) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = ds.getConnection();
             PreparedStatement psOrdine = connection.prepareStatement(insertOrdineSQL, Statement.RETURN_GENERATED_KEYS)) {

            psOrdine.setInt(1, ord.getIdUtente());
            psOrdine.setDouble(2, ord.getTotaleProdotti());
            psOrdine.setDouble(3, ord.getSpeseSpedizione());
            psOrdine.setDouble(4, ord.getTotaleOrdine());
            psOrdine.setString(5, via);
            psOrdine.setString(6, civico);
            psOrdine.setString(7, citta);
            psOrdine.setString(8, regione);
            psOrdine.setString(9, "In lavorazione");
            psOrdine.executeUpdate();

            int idOrdineGenerato = -1;
            try (ResultSet rs = psOrdine.getGeneratedKeys()) {
                if (rs.next()) {
                    idOrdineGenerato = rs.getInt(1);
                    ord.setIdOrdine(idOrdineGenerato);
                }
            }

            try (PreparedStatement psRiga = connection.prepareStatement(insertRigaSQL)) {
                List<ElementoCarrello> elementi = carrello.getElementi();
                for (ElementoCarrello item : elementi) {
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
                    psRiga.executeUpdate();
                }
            }
        }
    }

    @Override
    public synchronized Ordine doRetrieveByKey(int idOrdine) throws SQLException {
        Ordine bean = null;
        String selectSQL = "SELECT o.*, i.via, i.civico, i.citta, i.regione " + "FROM " + TABLE_NAME + " o " + "LEFT JOIN indirizzo i ON o.id_indirizzo = i.id_indirizzo " + "WHERE o.id_ordine = ?";

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
                    bean.setImmagineConsegna(rs.getString("immagine_consegna"));

                    Indirizzo ind = new Indirizzo();
                    String viaSped = rs.getString("via_spedizione");

                    if (viaSped != null && !viaSped.trim().isEmpty()) {
                        ind.setVia(viaSped);
                        ind.setCivico(rs.getString("civico_spedizione"));
                        ind.setCitta(rs.getString("citta_spedizione"));
                        ind.setRegione(rs.getString("regione_spedizione"));
                    } else if (rs.getString("via") != null) {
                        ind.setIdIndirizzo(rs.getInt("id_indirizzo"));
                        ind.setVia(rs.getString("via"));
                        ind.setCivico(rs.getString("civico"));
                        ind.setCitta(rs.getString("citta"));
                        ind.setRegione(rs.getString("regione"));
                    }

                    bean.setIndirizzo(ind);
                }
            }
        }
        return bean;
    }

    @Override
    public synchronized List<Ordine> doRetrieveAll() throws SQLException {
        List<Ordine> list = new ArrayList<>();
        String selectSQL = "SELECT o.*, i.via, i.civico, i.citta, i.regione " + "FROM " + TABLE_NAME + " o " + "LEFT JOIN indirizzo i ON o.id_indirizzo = i.id_indirizzo " + "ORDER BY o.data_ordine DESC";

        try (Connection connection = ds.getConnection();
        PreparedStatement ps = connection.prepareStatement(selectSQL);
        ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ordine bean = new Ordine();
                bean.setIdOrdine(rs.getInt("id_ordine"));
                bean.setIdUtente(rs.getInt("id_utente"));
                bean.setDataOrdine(rs.getTimestamp("data_ordine"));
                bean.setStato(rs.getString("stato"));
                bean.setTotaleProdotti(rs.getDouble("totale_prodotti"));
                bean.setSpeseSpedizione(rs.getDouble("spese_spedizione"));
                bean.setTotaleOrdine(rs.getDouble("totale_ordine"));
                bean.setImmagineConsegna(rs.getString("immagine_consegna"));

                Indirizzo ind = new Indirizzo();
                String viaSped = rs.getString("via_spedizione");

                if (viaSped != null && !viaSped.trim().isEmpty()) {
                    ind.setVia(viaSped);
                    ind.setCivico(rs.getString("civico_spedizione"));
                    ind.setCitta(rs.getString("citta_spedizione"));
                    ind.setRegione(rs.getString("regione_spedizione"));
                } else if (rs.getString("via") != null) {
                    ind.setIdIndirizzo(rs.getInt("id_indirizzo"));
                    ind.setVia(rs.getString("via"));
                    ind.setCivico(rs.getString("civico"));
                    ind.setCitta(rs.getString("citta"));
                    ind.setRegione(rs.getString("regione"));
                }

                bean.setIndirizzo(ind);
                list.add(bean);
            }
        }
        return list;
    }

    @Override
    public synchronized List<Ordine> doRetrieveByUtente(int idUtente) throws SQLException {
        List<Ordine> list = new ArrayList<>();
        String selectSQL = "SELECT o.*, i.via, i.civico, i.citta, i.regione " + "FROM " + TABLE_NAME + " o " + "LEFT JOIN indirizzo i ON o.id_indirizzo = i.id_indirizzo " + "WHERE o.id_utente = ? ORDER BY o.data_ordine DESC";

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
                    bean.setImmagineConsegna(rs.getString("immagine_consegna"));

                    Indirizzo ind = new Indirizzo();
                    String viaSped = rs.getString("via_spedizione");

                    if (viaSped != null && !viaSped.trim().isEmpty()) {
                        ind.setVia(viaSped);
                        ind.setCivico(rs.getString("civico_spedizione"));
                        ind.setCitta(rs.getString("citta_spedizione"));
                        ind.setRegione(rs.getString("regione_spedizione"));
                    } else if (rs.getString("via") != null) {
                        ind.setIdIndirizzo(rs.getInt("id_indirizzo"));
                        ind.setVia(rs.getString("via"));
                        ind.setCivico(rs.getString("civico"));
                        ind.setCitta(rs.getString("citta"));
                        ind.setRegione(rs.getString("regione"));
                    }

                    bean.setIndirizzo(ind);
                    list.add(bean);
                }
            }
        }
        return list;
    }

    @Override
    public synchronized List<Ordine> doRetrieveByIntervalloData(Timestamp dataInizio, Timestamp dataFine) throws SQLException {
        List<Ordine> list = new ArrayList<>();
        String selectSQL = "SELECT o.*, i.via, i.civico, i.citta, i.regione " + "FROM " + TABLE_NAME + " o " + "LEFT JOIN indirizzo i ON o.id_indirizzo = i.id_indirizzo " + "WHERE o.data_ordine >= ? AND o.data_ordine <= ? ORDER BY o.data_ordine DESC";

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
                    bean.setImmagineConsegna(rs.getString("immagine_consegna"));

                    Indirizzo ind = new Indirizzo();
                    String viaSped = rs.getString("via_spedizione");

                    if (viaSped != null && !viaSped.trim().isEmpty()) {
                        ind.setVia(viaSped);
                        ind.setCivico(rs.getString("civico_spedizione"));
                        ind.setCitta(rs.getString("citta_spedizione"));
                        ind.setRegione(rs.getString("regione_spedizione"));
                    } else if (rs.getString("via") != null) {
                        ind.setIdIndirizzo(rs.getInt("id_indirizzo"));
                        ind.setVia(rs.getString("via"));
                        ind.setCivico(rs.getString("civico"));
                        ind.setCitta(rs.getString("citta"));
                        ind.setRegione(rs.getString("regione"));
                    }

                    bean.setIndirizzo(ind);
                    list.add(bean);
                }
            }
        }
        return list;
    }

    @Override
    public synchronized List<Ordine> doRetrieveByUtenteAndIntervalloData(int idUtente, Timestamp dataInizio, Timestamp dataFine) throws SQLException {
        List<Ordine> list = new ArrayList<>();
        String selectSQL = "SELECT o.*, i.via, i.civico, i.citta, i.regione " + "FROM " + TABLE_NAME + " o " + "LEFT JOIN indirizzo i ON o.id_indirizzo = i.id_indirizzo " + "WHERE o.id_utente = ? AND o.data_ordine >= ? AND o.data_ordine <= ? ORDER BY o.data_ordine DESC";

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
                    bean.setImmagineConsegna(rs.getString("immagine_consegna"));

                    Indirizzo ind = new Indirizzo();
                    String viaSped = rs.getString("via_spedizione");

                    if (viaSped != null && !viaSped.trim().isEmpty()) {
                        ind.setVia(viaSped);
                        ind.setCivico(rs.getString("civico_spedizione"));
                        ind.setCitta(rs.getString("citta_spedizione"));
                        ind.setRegione(rs.getString("regione_spedizione"));
                    } else if (rs.getString("via") != null) {
                        ind.setIdIndirizzo(rs.getInt("id_indirizzo"));
                        ind.setVia(rs.getString("via"));
                        ind.setCivico(rs.getString("civico"));
                        ind.setCitta(rs.getString("citta"));
                        ind.setRegione(rs.getString("regione"));
                    }

                    bean.setIndirizzo(ind);
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
            return result != 0;
        }
    }

    @Override
    public synchronized boolean doDelete(int idOrdine) throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id_ordine = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteSQL)) {
            ps.setInt(1, idOrdine);
            int result = ps.executeUpdate();
            return result != 0;
        }
    }

    @Override
    public synchronized boolean doUpdateImmagineConsegna(int idOrdine, String immagineConsegna) throws SQLException {
        String updateSQL = "UPDATE " + TABLE_NAME + " SET immagine_consegna = ? WHERE id_ordine = ?";
        try (Connection connection = ds.getConnection();
             PreparedStatement ps = connection.prepareStatement(updateSQL)) {
            ps.setString(1, immagineConsegna);
            ps.setInt(2, idOrdine);
            int result = ps.executeUpdate();
            return result != 0;
        }
    }
}