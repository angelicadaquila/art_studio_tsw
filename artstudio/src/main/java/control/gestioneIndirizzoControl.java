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

import dao.IndirizzoDAO;
import dao.IndirizzoDAOImp;
import model.Indirizzo;
import model.Utente;

@WebServlet("/utente/nuovoIndirizzo")
public class gestioneIndirizzoControl extends HttpServlet {
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
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/utente/nuovoIndirizzoView.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Utente utente = null;

        if (session != null) {
            utente = (Utente) session.getAttribute("utente");
        }

        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String via = request.getParameter("via");
        String civico = request.getParameter("civico");
        String citta = request.getParameter("citta");
        String regione = request.getParameter("regione");

        if (via == null || via.trim().isEmpty() || civico == null || civico.trim().isEmpty() || citta == null || citta.trim().isEmpty() || regione == null || regione.trim().isEmpty()) {
            request.setAttribute("errore", "Tutti i campi dell'indirizzo sono obbligatori.");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/utente/nuovoIndirizzoView.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            Indirizzo nuovoIndirizzo = new Indirizzo();
            nuovoIndirizzo.setIdUtente(utente.getIdUtente());
            nuovoIndirizzo.setVia(via.trim());
            nuovoIndirizzo.setCivico(civico.trim());
            nuovoIndirizzo.setCitta(citta.trim());
            nuovoIndirizzo.setRegione(regione.trim());
            indirizzoDao.doSave(nuovoIndirizzo);
            response.sendRedirect(request.getContextPath() + "/utente/profilo");

        } catch (SQLException e) {
            System.err.println("Errore nel salvataggio dell'indirizzo: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errore", "Impossibile salvare il nuovo indirizzo.");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/utente/nuovoIndirizzoView.jsp");
            dispatcher.forward(request, response);
        }
    }
}