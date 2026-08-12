package dao;

import java.sql.SQLException;
import java.util.List;

import model.RigaOrdine;

public interface RigaOrdineDAO {
    
    public void doSave(RigaOrdine rigaord) throws SQLException;
   
    public RigaOrdine doRetrieveByKey(int idOrdine, int idProdotto) throws SQLException;
    
    public List<RigaOrdine> doRetrieveByOrdine(int idOrdine) throws SQLException;
    
    boolean doUpdateFileFinale(int idOrdine, int idProdotto, String fileFinale) throws SQLException;
}


