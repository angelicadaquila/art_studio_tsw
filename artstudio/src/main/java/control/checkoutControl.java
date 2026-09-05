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
import dao.OrdineDAO;
import dao.OrdineDAOImp;
import model.Carrello;
import model.Indirizzo;
import model.Ordine;
import model.Utente;

@WebServlet("/utente/checkout")
public class checkoutControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IndirizzoDAO indirizzoDao;
    private OrdineDAO ordineDao;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile");
        }
        indirizzoDao = new IndirizzoDAOImp(ds);
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

        Carrello carrello = (Carrello) session.getAttribute("carrello");
        if (carrello == null || carrello.getElementi().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/carrello");
            return;
        }

        try {
            List<Indirizzo> listaIndirizzi = indirizzoDao.doRetrieveByUtente(utente.getIdUtente());
            request.setAttribute("listaIndirizzi", listaIndirizzi);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/checkoutView.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            System.err.println("Errore recupero indirizzi per checkout: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/carrello");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Utente utente = null;
        if (session != null) {
            utente = (Utente) session.getAttribute("utente");
        }

        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Carrello carrello = null;
        if (session != null) {
            carrello = (Carrello) session.getAttribute("carrello");
        }

        if (carrello == null || carrello.getElementi().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/carrello");
            return;
        }

        String idIndirizzoStr = request.getParameter("idIndirizzo");
        String metodoPagamento = request.getParameter("metodoPagamento");

        if (idIndirizzoStr == null || idIndirizzoStr.trim().isEmpty()) {
            try {
                List<Indirizzo> listaIndirizzi = indirizzoDao.doRetrieveByUtente(utente.getIdUtente());
                request.setAttribute("listaIndirizzi", listaIndirizzi);
                request.setAttribute("errore", "Seleziona un indirizzo di spedizione prima di proseguire.");
                
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/checkoutView.jsp");
                dispatcher.forward(request, response);
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/carrello");
                return;
            }
        }

        int idIndirizzo = Integer.parseInt(idIndirizzoStr);

        if (metodoPagamento == null || metodoPagamento.trim().isEmpty()) {
            metodoPagamento = "Carta di Credito";
        }

        Ordine nuovoOrdine = new Ordine();
        nuovoOrdine.setIdUtente(utente.getIdUtente());
        nuovoOrdine.setTotaleProdotti(carrello.getTotale());
        nuovoOrdine.setSpeseSpedizione(0.00);
        nuovoOrdine.setTotaleOrdine(carrello.getTotale());

        try {
            ordineDao.doSaveConCarrello(nuovoOrdine, carrello, idIndirizzo, metodoPagamento);

            carrello.svuota(); 

            response.sendRedirect(request.getContextPath() + "/utente/mieiOrdini?esito=ok");

        } catch (SQLException e) {
            System.err.println("Errore durante il salvataggio dell'ordine: " + e.getMessage());
            e.printStackTrace();

            try {
                List<Indirizzo> listaIndirizzi = indirizzoDao.doRetrieveByUtente(utente.getIdUtente());
                request.setAttribute("listaIndirizzi", listaIndirizzi);
                request.setAttribute("errore", "Si è verificato un errore durante l'elaborazione dell'ordine. Riprova.");
                
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/checkoutView.jsp");
                dispatcher.forward(request, response);
            } catch (SQLException ex) {
                ex.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/carrello");
            }
        }
    }
}