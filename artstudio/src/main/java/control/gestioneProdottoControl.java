package control;

import java.io.IOException;
import java.io.File;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
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

import dao.ProdottoDAO;
import dao.ProdottoDAOImp;
import model.Commissione;
import model.Prodotto;
import model.Stampa;
import model.Utente;

@WebServlet("/admin/prodotti")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, 
    maxFileSize = 1024 * 1024 * 10,      
    maxRequestSize = 1024 * 1024 * 50    
)
public class gestioneProdottoControl extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDao;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile");
        }
        prodottoDao = new ProdottoDAOImp(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utente utente = null;
        if (session != null) {
            utente = (Utente) session.getAttribute("utente");
        }
        if (utente == null || utente.getRuolo() == null || !"admin".equalsIgnoreCase(utente.getRuolo())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Accesso riservato all'amministratore");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("elimina".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("idProdotto"));
                prodottoDao.doDelete(id);
                response.sendRedirect(request.getContextPath() + "/admin/prodotti");
                return;
            } 
            
            else if ("modifica".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("idProdotto"));
                Prodotto prod = prodottoDao.doRetrieveByKey(id);
                request.setAttribute("prodotto", prod);
                
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/admin/formProdotto.jsp");
                dispatcher.forward(request, response);
                return;
            } 
            
            else if ("addForm".equalsIgnoreCase(action)) {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/admin/formProdotto.jsp");
                dispatcher.forward(request, response);
                return;
            }

            List<Prodotto> prodotti = prodottoDao.doRetrieveAllAdmin(null);
            request.setAttribute("prodotti", prodotti);
            
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/admin/gestioneCatalogoView.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            System.err.println("Errore SQL in GestioneProdottoControl: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Impossibile completare l'operazione.");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato parametro numerico non valido.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessione = request.getSession(false);
        Utente utente = null;
        if (sessione != null) {
            utente = (Utente) sessione.getAttribute("utente");
        }
        if (utente == null || utente.getRuolo() == null || !"admin".equalsIgnoreCase(utente.getRuolo())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Accesso riservato all'amministratore.");
            return;
        }

        String action = request.getParameter("action");

        if ("salva".equalsIgnoreCase(action)) {
            try {
                String idStr = request.getParameter("idProdotto");
                String tipoProdotto = request.getParameter("tipoProdotto");
                String nome = request.getParameter("nome");
                String descrizione = request.getParameter("descrizione");
                String prezzoStr = request.getParameter("prezzo");
                double prezzo = 0.0;
                if (prezzoStr != null && !prezzoStr.trim().isEmpty()) {
                    prezzo = Double.parseDouble(prezzoStr);
                }
                
                boolean disponibile = false;
                if (request.getParameter("disponibile") != null) {
                    disponibile = true;
                }
                
                Part filePart = request.getPart("immagine");
                String nomeFileImmagine = "";

                if (filePart != null && filePart.getSize() > 0) {
                    String originalName = filePart.getSubmittedFileName();
                    String extension = "";
                    if (originalName != null && originalName.contains(".")) {
                        extension = originalName.substring(originalName.lastIndexOf("."));
                    }
                    nomeFileImmagine = java.util.UUID.randomUUID().toString() + extension;
                    String uploadPath = getServletContext().getRealPath(File.separator + "uploads");
                    java.io.File uploadDir = new java.io.File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs(); 
                    }
                    
                    String fullPathOnDisk = uploadPath + java.io.File.separator + nomeFileImmagine;
                    filePart.write(fullPathOnDisk);
                } else {
                    String immagineVecchia = request.getParameter("immagineVecchia");
                    if (immagineVecchia != null) {
                        nomeFileImmagine = immagineVecchia;
                    }
                }
                
                Prodotto prodotto;

                if ("stampa".equalsIgnoreCase(tipoProdotto)) {
                    Stampa stampa = new Stampa();
                    stampa.setDimensione(request.getParameter("dimensione"));
                    
                    String quantitaStr = request.getParameter("quantita");
                    int quantita = 0;
                    if (quantitaStr != null && !quantitaStr.trim().isEmpty()) {
                        quantita = Integer.parseInt(quantitaStr);
                    }
                    stampa.setQuantita(quantita);
                    stampa.setFisico(true);
                    
                    prodotto = stampa;
                } else {
                    Commissione commissione = new Commissione();
                    
                    String tempoStr = request.getParameter("tempo");
                    if (tempoStr == null) {
                        tempoStr = "";
                    }
                    commissione.setTempo(tempoStr); 
                    commissione.setFisico(false);
                    
                    prodotto = commissione;
                }

                prodotto.setNome(nome);
                prodotto.setDescrizione(descrizione);
                prodotto.setPrezzo(prezzo);
                prodotto.setDisponibile(disponibile);
                prodotto.setImmagine(nomeFileImmagine);

                if (idStr == null || idStr.trim().isEmpty()) {
                    prodottoDao.doSave(prodotto);
                } else {
                    prodotto.setIdProdotto(Integer.parseInt(idStr));
                    prodottoDao.doUpdate(prodotto);
                }

                response.sendRedirect(request.getContextPath() + "/admin/prodotti");

            } catch (SQLException e) {
                System.err.println("Errore SQL nel salvataggio prodotto: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel salvataggio del prodotto sul Database.");
            } catch (NumberFormatException e) {
                System.err.println("Errore nei dati numerici: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "I valori numerici inseriti non sono validi.");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/prodotti");
        }
    }
}