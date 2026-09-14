package control;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import javax.sql.DataSource;
import org.json.JSONArray;
import org.json.JSONObject;

import dao.OrdineDAO;
import dao.OrdineDAOImp;
import dao.ProdottoDAO;
import dao.ProdottoDAOImp;
import dao.RigaOrdineDAO;
import dao.RigaOrdineDAOImp;
import dao.UtenteDAO;
import dao.UtenteDAOImp;

import model.Ordine;
import model.Prodotto;
import model.RigaOrdine;
import model.Utente;

@WebServlet("/admin/ordini")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
public class gestioneOrdiniControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrdineDAO ordineDao;
    private RigaOrdineDAO rigaOrdineDao;
    private ProdottoDAO prodottoDao;
    private UtenteDAO utenteDao;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile");
        }
        ordineDao = new OrdineDAOImp(ds);
        rigaOrdineDao = new RigaOrdineDAOImp(ds);
        prodottoDao = new ProdottoDAOImp(ds);
        utenteDao = new UtenteDAOImp(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Utente utente = null;
        if (session != null) {
            utente = (Utente) session.getAttribute("utente");
        }
        
        if (utente == null || !"admin".equalsIgnoreCase(utente.getRuolo())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = request.getParameter("azione");
        }

        String isAjaxParam = request.getParameter("ajax");
        boolean isAjax = "true".equalsIgnoreCase(isAjaxParam) || "dettaglioAjax".equalsIgnoreCase(action);

        if ("dettaglioAjax".equalsIgnoreCase(action) || isAjax) {
            response.setContentType("application/json"); 
            response.setCharacterEncoding("UTF-8");
            
            String idOrdineStr = request.getParameter("idOrdine");
            if (idOrdineStr != null && !idOrdineStr.trim().isEmpty()) {
                try {
                    int idOrdine = Integer.parseInt(idOrdineStr);
                    List<RigaOrdine> righe = rigaOrdineDao.doRetrieveByOrdine(idOrdine);
                    JSONArray jsonArray = new JSONArray();

                    for (int i = 0; i < righe.size(); i++) {
                        RigaOrdine r = righe.get(i);
                        JSONObject jsonItem = new JSONObject();
                        
                        Prodotto p = prodottoDao.doRetrieveByKey(r.getIdProdotto());
                        
                        String nomeProdotto = "";
                        if (p != null) {
                            nomeProdotto = p.getNome();
                        }

                        jsonItem.put("idRiga", r.getIdRiga());
                        jsonItem.put("idOrdine", r.getIdOrdine());
                        jsonItem.put("idProdotto", r.getIdProdotto());
                        jsonItem.put("nomeProdotto", nomeProdotto);
                        jsonItem.put("quantita", r.getQuantita());
                        jsonItem.put("prezzo", r.getPrezzoOg());

                        String note = "";
                        if (r.getDescrizioneComm() != null) {
                            note = r.getDescrizioneComm();
                        }
                        jsonItem.put("note", note);

                        String ref = "";
                        if (r.getRefComm() != null) {
                            ref = r.getRefComm();
                        }
                        jsonItem.put("ref", ref);

                        String fileFinale = "";
                        if (r.getFileFinale() != null) {
                            fileFinale = r.getFileFinale();
                        }
                        jsonItem.put("fileFinale", fileFinale);
                        jsonArray.put(jsonItem);
                    }

                    response.getWriter().write(jsonArray.toString());
                    return;
                    
                } catch (SQLException e) {
                    System.err.println("Errore SQL in dettaglioAjax admin: " + e.getMessage());
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                } catch (NumberFormatException e) {
                    System.err.println("ID Ordine non valido: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }
        }

        String idUtenteParam = request.getParameter("idUtente");
        String dataInizioParam = request.getParameter("dataInizio");
        String dataFineParam = request.getParameter("dataFine");

        boolean filtroCliente = (idUtenteParam != null && !idUtenteParam.trim().isEmpty());
        boolean filtroDate = (dataInizioParam != null && !dataInizioParam.trim().isEmpty() && dataFineParam != null && !dataFineParam.trim().isEmpty());

        String modoFiltro = "TUTTI";
        if (filtroCliente && filtroDate) {
            modoFiltro = "CLIENTE_E_DATE";
        } else if (filtroCliente) {
            modoFiltro = "CLIENTE";
        } else if (filtroDate) {
            modoFiltro = "DATE";
        }

        try {
            List<Ordine> listaOrdini;

            switch (modoFiltro) {
                case "CLIENTE_E_DATE":
                    int idUtente = Integer.parseInt(idUtenteParam);
                    Timestamp start = Timestamp.valueOf(dataInizioParam + " 00:00:00");
                    Timestamp end = Timestamp.valueOf(dataFineParam + " 23:59:59");
                    listaOrdini = ordineDao.doRetrieveByUtenteAndIntervalloData(idUtente, start, end);
                    break;

                case "CLIENTE":
                    listaOrdini = ordineDao.doRetrieveByUtente(Integer.parseInt(idUtenteParam));
                    break;

                case "DATE":
                    Timestamp dataInizio = Timestamp.valueOf(dataInizioParam + " 00:00:00");
                    Timestamp dataFine = Timestamp.valueOf(dataFineParam + " 23:59:59");
                    listaOrdini = ordineDao.doRetrieveByIntervalloData(dataInizio, dataFine);
                    break;

                default:
                    listaOrdini = ordineDao.doRetrieveAll();
                    break;
            }

            List<Utente> tuttiIClienti = utenteDao.doRetrieveAll();

            List<Utente> listaUtenti = new ArrayList<>();
            if (listaOrdini != null) {
                for (int i = 0; i < listaOrdini.size(); i++) {
                    Ordine ord = listaOrdini.get(i);
                    if (ord.getIdUtente() > 0) {
                        Utente u = utenteDao.doRetrieveByKey(ord.getIdUtente());
                        listaUtenti.add(u);
                    } else {
                        listaUtenti.add(null);
                    }
                }
            }

            request.setAttribute("listaOrdini", listaOrdini);
            request.setAttribute("listaUtenti", listaUtenti);
            request.setAttribute("tuttiIClienti", tuttiIClienti);

            request.setAttribute("selectedIdUtente", idUtenteParam);
            request.setAttribute("selectedDataInizio", dataInizioParam);
            request.setAttribute("selectedDataFine", dataFineParam);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/admin/gestioneOrdiniView.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException e) {
            System.err.println("Errore recupero ordini admin: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/catalogo?errore=db");
        }
    }
        
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Utente utente = null;
        if (session != null) {
            utente = (Utente) session.getAttribute("utente");
        }
        
        if (utente == null || !"admin".equalsIgnoreCase(utente.getRuolo())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = request.getParameter("azione");
        }

        try {
            if ("cambiaStato".equalsIgnoreCase(action)) {
                String idOrdineStr = request.getParameter("idOrdine");
                String nuovoStato = request.getParameter("nuovoStato");
                
                if (idOrdineStr != null && nuovoStato != null && !nuovoStato.trim().isEmpty()) {
                    int idOrdine = Integer.parseInt(idOrdineStr);
                    ordineDao.doUpdateStato(idOrdine, nuovoStato);
                }
                response.sendRedirect(request.getContextPath() + "/admin/ordini?esito=ok");
                return;
            } else if ("uploadFotoSingola".equalsIgnoreCase(action)) {
                String idRigaStr = request.getParameter("idRiga");
                Part filePart = request.getPart("fileFinale");

                if (idRigaStr != null && filePart != null && filePart.getSize() > 0) {
                    int idRiga = Integer.parseInt(idRigaStr);

                    String originalName = filePart.getSubmittedFileName();
                    String extension = "";
                    if (originalName != null && originalName.contains(".")) {
                        extension = originalName.substring(originalName.lastIndexOf("."));
                    }
                    String uniqueFileName = UUID.randomUUID().toString() + extension;

                    String uploadPath = getServletContext().getRealPath(File.separator + "admin_images");
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }

                    filePart.write(uploadPath + File.separator + uniqueFileName);
                    
                    rigaOrdineDao.doUpdateFileFinale(idRiga, uniqueFileName);
                }
                response.sendRedirect(request.getContextPath() + "/admin/ordini?esito=ok");
                return;
            }
        } catch (SQLException e) {
            System.err.println("Errore SQL in doPost admin: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/ordini?errore=1");
            return;
        }

        doGet(request, response);
    }
}