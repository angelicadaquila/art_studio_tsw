package control;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

import dao.UtenteDAO;
import dao.UtenteDAOImp;
import model.Utente;

@WebServlet("/login")
public class loginControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UtenteDAO utenteDao;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile");
        }
        utenteDao = new UtenteDAOImp(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Utente utente = (Utente) session.getAttribute("utente");
            if (utente != null) {
                if ("admin".equalsIgnoreCase(utente.getRuolo())) {
                    response.sendRedirect(request.getContextPath() + "/admin/prodotti");
                } else {
                    response.sendRedirect(request.getContextPath() + "/catalogo?tipo=tutti");
                }
                return;
            }
        }

        String erroreParam = request.getParameter("errore");
        if ("auth_required".equals(erroreParam)) {
            request.setAttribute("errore", "Devi effettuare il login prima di continuare.");
        }
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/loginView.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errore", "Tutti i campi sono obbligatori.");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/loginView.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            Utente utente = utenteDao.doRetrieveByEmail(email.trim().toLowerCase());
            String passwordHash = hashPasswordSHA256(password);

            if (utente != null && utente.getPassword() != null && utente.getPassword().equals(passwordHash)) {
                HttpSession session = request.getSession(true);
                session.setAttribute("utente", utente);
                if ("admin".equalsIgnoreCase(utente.getRuolo())) {
                    response.sendRedirect(request.getContextPath() + "/admin/prodotti");
                } else {
                    response.sendRedirect(request.getContextPath() + "/catalogo?tipo=tutti");
                }
                return;

            } else {
                request.setAttribute("errore", "Email o password errate.");
            }

        } catch (SQLException e) {
            System.err.println("Errore SQL in loginControl: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errore", "Si è verificato un errore");
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/loginView.jsp");
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