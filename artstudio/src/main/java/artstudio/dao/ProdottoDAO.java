package artstudio.dao;

import java.sql.SQLException;
import java.util.List;

import artstudio.model.Prodotto;

public interface ProdottoDAO {
	
	public void doSave(Prodotto prod) throws SQLException;
	
	public boolean doUpdateImage(Prodotto prod) throws SQLException;
	
	public boolean doUpdate(Prodotto prod) throws SQLException;
	
	public boolean doUpdateQuantita(int idProdotto, int nuovaQuantita) throws SQLException;

	public boolean doDelete(int idProdotto) throws SQLException;

	public Prodotto doRetrieveByKey(int idProdotto) throws SQLException;
	
	public List<Prodotto> doRetrieveAll(String ordine) throws SQLException;

}



