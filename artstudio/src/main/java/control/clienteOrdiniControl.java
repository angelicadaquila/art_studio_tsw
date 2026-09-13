package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.json.JSONObject;
import org.json.JSONArray;

import dao.OrdineDAO;
import dao.OrdineDAOImp;
import model.Ordine;
import model.Utente;
import model.RigaOrdine;
import dao.RigaOrdineDAO;
import dao.RigaOrdineDAOImp;
import dao.ProdottoDAO;
import dao.ProdottoDAOImp;
import model.Prodotto;


@WebServlet("/utente/mieiOrdini")
public class clienteOrdiniControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAO ordineDao;
	private RigaOrdineDAO rigaOrdineDao;
	private ProdottoDAO prodottoDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel ServletContext");
        }
        ordineDao = new OrdineDAOImp(ds);
	    rigaOrdineDao = new RigaOrdineDAOImp(ds);
	    prodottoDao = new ProdottoDAOImp(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Utente utente = null;
        if (session != null) {
            utente = (Utente) session.getAttribute("utente");
        }

        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");

        if ("dettaglioAjax".equalsIgnoreCase(action)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            int idOrdine = Integer.parseInt(request.getParameter("idOrdine"));
            
            try {
                List<RigaOrdine> righe = rigaOrdineDao.doRetrieveByOrdine(idOrdine);
                JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < righe.size(); i++) {
                    RigaOrdine r = righe.get(i);
                    JSONObject jsonItem = new JSONObject();
                    
                    Prodotto p = prodottoDao.doRetrieveByKey(r.getIdProdotto());
                    
                    String nomeProdotto;
                    nomeProdotto = p.getNome();
                    jsonItem.put("idProdotto", r.getIdProdotto());
                    jsonItem.put("nomeProdotto", nomeProdotto);
                    jsonItem.put("quantita", r.getQuantita());
                    jsonItem.put("prezzo", r.getPrezzoOg());
                    
                    if (r.getDescrizioneComm() != null) {
                        jsonItem.put("note", r.getDescrizioneComm());
                    } else {
                        jsonItem.put("note", "");
                    }
                    
                    if (r.getRefComm() != null) {
                        jsonItem.put("ref", r.getRefComm());
                    } else {
                        jsonItem.put("ref", "");
                    }

                    jsonArray.put(jsonItem);
                }

                response.getWriter().write(jsonArray.toString());
                return;
                
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        }

        try {
            List<Ordine> listaOrdini = ordineDao.doRetrieveByUtente(utente.getIdUtente());
            request.setAttribute("listaOrdini", listaOrdini);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/utente/clienteOrdiniView.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            System.err.println("Errore recupero ordini utente: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/catalogo?errore=db");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}