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

import dao.ProdottoDAO;
import dao.ProdottoDAOImp;
import model.Commissione;
import model.Prodotto;
import model.Stampa;
import model.Utente;

@WebServlet("/admin/prodotti")
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utente utente = (session != null) ? (Utente) session.getAttribute("utente") : null;
        if (utente == null || utente.getRuolo() == null || !"admin".equalsIgnoreCase(utente.getRuolo())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Accesso riservato all'amministratore");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("idProdotto"));
                prodottoDao.doDelete(id);
                response.sendRedirect(request.getContextPath() + "/admin/prodotti");
                return;
            } 
            else if ("edit".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("idProdotto"));
                Prodotto prod = prodottoDao.doRetrieveByKey(id);
                request.setAttribute("prodotto", prod);
                
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/admin/formProdotto.jsp");
                dispatcher.forward(request, response);
                return;
            }

            List<Prodotto> prodotti = prodottoDao.doRetrieveAllAdmin(null);
            request.setAttribute("prodotti", prodotti);
            
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/admin/gestioneProdotti.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            System.err.println("Errore SQL in GestioneProdottoControl: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Impossibile completare l'operazione sui prodotti.");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato parametro numerico non valido.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utente utente = (session != null) ? (Utente) session.getAttribute("utente") : null;
        if (utente == null || utente.getRuolo() == null || !"admin".equalsIgnoreCase(utente.getRuolo())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Accesso riservato all'amministratore.");
            return;
        }

        String action = request.getParameter("action");

        if ("save".equalsIgnoreCase(action)) {
            try {
                String idStr = request.getParameter("idProdotto");
                boolean isFisico = Boolean.parseBoolean(request.getParameter("isFisico"));
                String nome = request.getParameter("nome");
                String descrizione = request.getParameter("descrizione");
                double prezzo = Double.parseDouble(request.getParameter("prezzo"));
                boolean disponibile = Boolean.parseBoolean(request.getParameter("disponibile"));
                String immagine = request.getParameter("immagine");

                Prodotto prod;
                if (isFisico) {
                    Stampa stampa = new Stampa();
                    stampa.setDimensione(request.getParameter("dimensione"));
                    stampa.setQuantita(Integer.parseInt(request.getParameter("quantita")));
                    prod = stampa;
                } else {
                    Commissione commissione = new Commissione();
                    commissione.setTempo(request.getParameter("tempo"));
                    prod = commissione;
                }

                prod.setFisico(isFisico);
                prod.setNome(nome);
                prod.setDescrizione(descrizione);
                prod.setPrezzo(prezzo);
                prod.setDisponibile(disponibile);
                prod.setImmagine(immagine);

                if (idStr == null || idStr.trim().isEmpty()) {
                    prodottoDao.doSave(prod);
                } else {
                    prod.setIdProdotto(Integer.parseInt(idStr));
                    prodottoDao.doUpdate(prod);
                }

                response.sendRedirect(request.getContextPath() + "/admin/prodotti");

            } catch (SQLException e) {
                System.err.println("Errore salvataggio/modifica prodotto: " + e.getMessage());
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel salvataggio del prodotto.");
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dati numerici inseriti non validi.");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/prodotti");
        }
    }
}