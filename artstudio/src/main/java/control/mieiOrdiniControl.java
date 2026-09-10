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

import dao.OrdineDAO;
import dao.OrdineDAOImp;
import model.Ordine;
import model.Utente;

@WebServlet("/utente/mieiOrdini")
public class mieiOrdiniControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrdineDAO ordineDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile nel ServletContext");
        }
        ordineDao = new OrdineDAOImp(ds);
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

        try {
            List<Ordine> listaOrdini = ordineDao.doRetrieveByUtente(utente.getIdUtente());
            request.setAttribute("listaOrdini", listaOrdini);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/utente/mieiOrdiniView.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            System.err.println("Errore nel recupero degli ordini per l'utente ID " + utente.getIdUtente() + ": " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/utente/profilo");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}