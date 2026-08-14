package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import dao.ProdottoDAO;
import dao.ProdottoDAOImp;
import model.Commissione;
import model.Prodotto;
import model.Stampa;

@WebServlet("/catalogo")
public class catalogoControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProdottoDAO prodottoDao;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel contesto dell'applicazione.");
        }
        prodottoDao = new ProdottoDAOImp(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
        String ordinamento = request.getParameter("ordinamento");
        String tipo = request.getParameter("tipo");
        
        if (tipo == null || tipo.trim().isEmpty()) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/sceltaCatalogoView.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        String sqlOrdering = "p.id_prodotto ASC"; 
        if (ordinamento != null && !ordinamento.trim().isEmpty()) {
            switch (ordinamento) {
                case "prezzo_crescente":
                    sqlOrdering = "p.prezzo ASC";
                    break;
                case "prezzo_decrescente":
                    sqlOrdering = "p.prezzo DESC";
                    break;
                case "nome":
                    sqlOrdering = "p.nome ASC";
                    break;
                default:
                    sqlOrdering = "p.id_prodotto ASC";
                    break;
            }
        }

        try {

            List<Prodotto> tuttiProdotti = prodottoDao.doRetrieveAll(sqlOrdering);
            List<Prodotto> prodottiMostrati = new ArrayList<>();
            if ("stampa".equalsIgnoreCase(tipo)) {
                for (int i = 0; i <tuttiProdotti.size(); i++) {
                    Prodotto p = tuttiProdotti.get(i);
                    if (p instanceof Stampa) {
                        prodottiMostrati.add(p);
                    }
                }
            } else if ("commissione".equalsIgnoreCase(tipo)) {
                for (int i = 0; i < tuttiProdotti.size(); i++) {
                    Prodotto p = tuttiProdotti.get(i);
                    if (p instanceof Commissione) {
                        prodottiMostrati.add(p);
                    }
                }
            } else if ("tutti".equalsIgnoreCase(tipo)) {
                prodottiMostrati = tuttiProdotti;
            }

            request.setAttribute("prodotti", prodottiMostrati);
            request.setAttribute("selectedtipo", tipo);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/catalogoView.jsp");
            dispatcher.forward(request, response);

        }catch (SQLException e) {
            System.err.println("Errore SQL in CatalogoControl: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Impossibile caricare il catalogo dei prodotti.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}