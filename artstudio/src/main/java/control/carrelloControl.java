package control;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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

import dao.ProdottoDAO;
import dao.ProdottoDAOImp;
import model.Carrello;
import model.Commissione;
import model.ElementoCarrello;
import model.Prodotto;
import model.Stampa;

@WebServlet("/carrello")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 5, 
    maxFileSize = 1024 * 1024 * 50,
    maxRequestSize = 1024 * 1024 * 200
)
public class carrelloControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProdottoDAO prodottoDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile");
        }
        prodottoDao = new ProdottoDAOImp(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        if (carrello == null) {
            carrello = new Carrello();
            session.setAttribute("carrello", carrello);
        }

        String azione = request.getParameter("azione");

        if (azione != null && !azione.trim().isEmpty()) {
            try {
                if ("aggiungi".equalsIgnoreCase(azione)) {
                    String idStr = request.getParameter("idProdotto");
                    String qtaStr = request.getParameter("quantita");

                    if (idStr != null && !idStr.trim().isEmpty()) {
                        int idProdotto = Integer.parseInt(idStr);
                        int quantita = 1;
                        if (qtaStr != null && !qtaStr.trim().isEmpty()) {
                            quantita = Integer.parseInt(qtaStr);
                        }

                        Prodotto prod = prodottoDao.doRetrieveByKey(idProdotto);
                        if (prod != null) {
                            if (prod instanceof Commissione) {
                                String descrizioneComm = request.getParameter("descrizioneComm");
                                Part filePart = request.getPart("immagineRef");
                                String nomeFileReference = "";

                                if (filePart != null && filePart.getSize() > 0) {
                                    String originalName = filePart.getSubmittedFileName();
                                    String extension = "";
                                    if (originalName != null && originalName.contains(".")) {
                                        extension = originalName.substring(originalName.lastIndexOf("."));
                                    }
                                    nomeFileReference = UUID.randomUUID().toString() + extension;
                                    
                                    String uploadPath = getServletContext().getRealPath(File.separator + "user_images");
                                    File uploadDir = new File(uploadPath);

                                    if (!uploadDir.exists()) {
                                        uploadDir.mkdirs();
                                    }

                                    String fullPathOnDisk = uploadPath + File.separator + nomeFileReference;
                                    filePart.write(fullPathOnDisk);
                                }

                                carrello.aggiungiProd(prod, 1, descrizioneComm, nomeFileReference);
                            } else if (prod instanceof Stampa) {
                                Stampa stampa = (Stampa) prod;
                                int disponibilitaMagazzino = stampa.getQuantita();
                                int quantitaGiaInCarrello = 0;
                                if (carrello.getElementi() != null) {
                                    for (ElementoCarrello item : carrello.getElementi()) {
                                        if (item.getProdotto().getIdProdotto() == idProdotto) {
                                            quantitaGiaInCarrello = item.getQuantita();
                                            break;
                                        }
                                    }
                                }
                                if ((quantitaGiaInCarrello + quantita) <= disponibilitaMagazzino) {
                                    carrello.aggiungiProd(prod, quantita);
                                } else {
                                    response.sendRedirect(request.getContextPath() + "/carrello?errore=giacenza");
                                    return;
                                }
                            } else {
                                carrello.aggiungiProd(prod, quantita);
                            }
                        }
                    }
                } else if ("aggiorna".equalsIgnoreCase(azione)) {
                    String idStr = request.getParameter("idProdotto");
                    String qtaStr = request.getParameter("quantita");

                    if (idStr != null && !idStr.trim().isEmpty() && qtaStr != null && !qtaStr.trim().isEmpty()) {
                        int idProdotto = Integer.parseInt(idStr);
                        int nuovaQta = Integer.parseInt(qtaStr);

                        Prodotto prod = prodottoDao.doRetrieveByKey(idProdotto);
                        if (prod instanceof Stampa) {
                            Stampa stampa = (Stampa) prod;
                            if (nuovaQta <= stampa.getQuantita()) {
                                carrello.aggiornaQuantita(idProdotto, nuovaQta);
                            } else {
                                response.sendRedirect(request.getContextPath() + "/carrello?errore=giacenza");
                                return;
                            }
                        } else {
                            carrello.aggiornaQuantita(idProdotto, nuovaQta);
                        }
                    }
                } else if ("elimina".equalsIgnoreCase(azione)) {
                    String idStr = request.getParameter("idProdotto");
                    if (idStr != null && !idStr.trim().isEmpty()) {
                        int idProdotto = Integer.parseInt(idStr);
                        carrello.eliminaProd(idProdotto);
                    }
                } else if ("svuota".equalsIgnoreCase(azione)) {
                    carrello.svuota();
                }
            } catch (SQLException e) {
                System.err.println("Errore SQL in carrelloControl: " + e.getMessage());
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.err.println("Formato numero non valido nei parametri del carrello: " + e.getMessage());
            }
            
            response.sendRedirect(request.getContextPath() + "/carrello");
            return;
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/carrelloView.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}