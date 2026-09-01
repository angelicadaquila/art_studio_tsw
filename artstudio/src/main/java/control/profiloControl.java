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

import dao.IndirizzoDAO;
import dao.IndirizzoDAOImp;
import model.Indirizzo;
import model.Utente;

@WebServlet("/utente/profilo")
public class profiloControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IndirizzoDAO indirizzoDao;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile");
        }
        indirizzoDao = new IndirizzoDAOImp(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Utente utente = null;

        if (session != null) {
            utente = (Utente) session.getAttribute("utente");
        }
        if (utente != null) {
            try {
                List<Indirizzo> listaIndirizzi = indirizzoDao.doRetrieveByUtente(utente.getIdUtente());
                request.setAttribute("listaIndirizzi", listaIndirizzi);
            } catch (SQLException e) {
                System.err.println("Errore nel recupero indirizzi: " + e.getMessage());
                e.printStackTrace();
            }
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/utente/profiloView.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}