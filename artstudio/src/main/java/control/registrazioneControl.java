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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dao.IndirizzoDAO;
import dao.IndirizzoDAOImp;
import dao.UtenteDAO;
import dao.UtenteDAOImp;
import model.Indirizzo;
import model.Utente;

@WebServlet("/registrazione")
public class registrazioneControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UtenteDAO utenteDao;
    private IndirizzoDAO indirizzoDao;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile");
        }
        utenteDao = new UtenteDAOImp(ds);
        indirizzoDao = new IndirizzoDAOImp(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/registrazioneView.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confermaPassword = request.getParameter("confermaPassword");

        String via = request.getParameter("via");
        String civico = request.getParameter("civico");
        String citta = request.getParameter("citta");
        String regione = request.getParameter("regione");

        if (nome == null || nome.trim().isEmpty() ||
            cognome == null || cognome.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            via == null || via.trim().isEmpty() ||
            civico == null || civico.trim().isEmpty() ||
            citta == null || citta.trim().isEmpty() ||
            regione == null || regione.trim().isEmpty()) {
            request.setAttribute("errore", "Tutti i campi sono obbligatori.");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/registrazioneView.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if (!password.equals(confermaPassword)) {
            request.setAttribute("errore", "Le password inserite non corrispondono.");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/registrazioneView.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            if (utenteDao.doRetrieveByEmail(email.trim().toLowerCase()) != null) {
                request.setAttribute("errore", "Email già in uso.");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/registrazioneView.jsp");
                dispatcher.forward(request, response);
                return;
            }
            
            Utente nuovoUtente = new Utente();
            nuovoUtente.setNome(nome.trim());
            nuovoUtente.setCognome(cognome.trim());
            nuovoUtente.setEmail(email.trim().toLowerCase());
            nuovoUtente.setRuolo("utente");

            String passwordHash = hashPasswordSHA256(password);
            nuovoUtente.setPassword(passwordHash);
            
            if (nuovoUtente.getIdUtente() > 0) {
                Indirizzo nuovoIndirizzo = new Indirizzo();
                nuovoIndirizzo.setIdUtente(nuovoUtente.getIdUtente());
                nuovoIndirizzo.setVia(via.trim());
                nuovoIndirizzo.setCivico(civico.trim());
                nuovoIndirizzo.setCitta(citta.trim());
                nuovoIndirizzo.setRegione(regione.trim());
                indirizzoDao.doSave(nuovoIndirizzo);

                HttpSession session = request.getSession(true);
                session.setAttribute("utente", nuovoUtente);
                response.sendRedirect(request.getContextPath() + "/catalogo?tipo=tutti");
                return;
            } else {
                request.setAttribute("errore", "Errore durante la registrazione dell'utente.");
            }

        } catch (SQLException e) {
            System.err.println("Errore SQL in RegistrazioneServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errore", "Si è verificato un errore");
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/registrazioneView.jsp");
        dispatcher.forward(request, response);
    }
    
    
    private String hashPasswordSHA256(String passwordInChiaro) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(passwordInChiaro.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                byte b = hash[i];
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo di hashing non trovato", e);
        }
    }
}