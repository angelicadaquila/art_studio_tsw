package control;

import java.io.IOException;
import java.sql.SQLException;

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
import model.Carrello;
import model.Prodotto;

@WebServlet("/dettaglioProdotto")
public class dettaglioProdottoControl extends HttpServlet {

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

        HttpSession session = request.getSession();
        Carrello cart = (Carrello) session.getAttribute("cart");
        if (cart == null) {
            cart = new Carrello();
            session.setAttribute("cart", cart);
        }

        String action = request.getParameter("action");
        String idStr = request.getParameter("id");

        try {
            if (idStr != null && !idStr.trim().isEmpty()) {
                int id = Integer.parseInt(idStr);

                if ("aggiungiC".equalsIgnoreCase(action)) {
                    int quantita = 1;
                    String qtaStr = request.getParameter("quantita");
                    if (qtaStr != null && !qtaStr.trim().isEmpty()) {
                        quantita = Integer.parseInt(qtaStr);
                    }

                    Prodotto p = prodottoDao.doRetrieveByKey(id);
                    if (p != null) {
                        cart.aggiungiProd(p, quantita); 
                        request.setAttribute("message", "Prodotto aggiunto al carrello con successo!");
                    }
                }

                Prodotto prodotto = prodottoDao.doRetrieveByKey(id);
                if (prodotto != null) {
                    request.setAttribute("prodotto", prodotto);
                } else {
                    request.setAttribute("errorMessage", "Prodotto non trovato.");
                }
            } else {
                request.setAttribute("errorMessage", "Identificativo prodotto non valido.");
            }

            session.setAttribute("cart", cart);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/dettaglioProdottoView.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            System.err.println("Errore SQL in dettaglioProdottoControl: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Impossibile recuperare i dettagli del prodotto.");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato parametro non valido.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}