package control;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
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

@WebServlet("/admin/ordini")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
public class gestioneOrdiniControl extends HttpServlet {
	private OrdineDAO ordineDao;
	private RigaOrdineDAO rigaOrdineDao;
	private ProdottoDAO prodottoDao;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
	    super.init(servletConfig);
	    DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
	    if (ds == null) {
	        throw new ServletException("DataSource non disponibile");
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
	    
	    if (utente == null || !"admin".equalsIgnoreCase(utente.getRuolo())) {
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
	        List<Ordine> listaOrdini = ordineDao.doRetrieveAll();
	        request.setAttribute("listaOrdini", listaOrdini);

	        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/admin/gestioneOrdiniView.jsp");
	        dispatcher.forward(request, response);

	    } catch (SQLException e) {
	        System.err.println("Errore recupero ordini admin: " + e.getMessage());
	        e.printStackTrace();
	        response.sendRedirect(request.getContextPath() + "/catalogo?errore=db");
	    }
	}
        
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Utente utente = null;
        if (session != null) {
            utente = (Utente) session.getAttribute("utente");
        }
        
        if (utente == null || !"admin".equalsIgnoreCase(utente.getRuolo())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("uploadFoto".equalsIgnoreCase(action)) {
                int idOrdine = Integer.parseInt(request.getParameter("idOrdine"));
                Part filePart = request.getPart("immagineConsegna");
                if (filePart != null && filePart.getSize() > 0) {
                    String originalName = filePart.getSubmittedFileName();
                    String extension = "";
                    if (originalName != null && originalName.contains(".")) {
                        extension = originalName.substring(originalName.lastIndexOf("."));
                    }
                    String uniqueFileName = UUID.randomUUID().toString() + extension;

                    String uploadPath = getServletContext().getRealPath(File.separator + "admin_images");
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    filePart.write(uploadPath + File.separator + uniqueFileName);
                    ordineDao.doUpdateImmagineConsegna(idOrdine, uniqueFileName);
                }
            } else if ("cambiaStato".equalsIgnoreCase(action)) {
                int idOrdine = Integer.parseInt(request.getParameter("idOrdine"));
                String nuovoStato = request.getParameter("nuovoStato");
                
                if (nuovoStato != null && !nuovoStato.trim().isEmpty()) {
                    ordineDao.doUpdateStato(idOrdine, nuovoStato);
                }
            }

            response.sendRedirect(request.getContextPath() + "/admin/ordini?esito=ok");

        } catch (SQLException e) {
            System.err.println("Errore gestione ordine admin: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/ordini?errore=1");
        }
    }
}