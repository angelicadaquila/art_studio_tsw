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

import org.json.JSONObject;

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
        if (azione == null || azione.trim().isEmpty()) {
            azione = request.getParameter("action");
        }

        String isAjaxParam = request.getParameter("ajax");
        boolean isAjax = "true".equalsIgnoreCase(isAjaxParam);

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
                                    for (int i = 0; i < carrello.getElementi().size(); i++) {
                                        ElementoCarrello item = carrello.getElementi().get(i);
                                        if (item.getProdotto().getIdProdotto() == idProdotto) {
                                            quantitaGiaInCarrello = item.getQuantita();
                                            break;
                                        }
                                    }
                                }
                                if ((quantitaGiaInCarrello + quantita) <= disponibilitaMagazzino) {
                                    carrello.aggiungiProd(prod, quantita);
                                } else {
                                    if (isAjax) {
                                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                        return;
                                    }
                                    response.sendRedirect(request.getContextPath() + "/carrello?errore=giacenza");
                                    return;
                                }
                            } else {
                                carrello.aggiungiProd(prod, quantita);
                            }
                        }
                    }
                } else if ("aggiorna".equalsIgnoreCase(azione) || "incrementa".equalsIgnoreCase(azione) || "decrementa".equalsIgnoreCase(azione)) {
                    String idStr = request.getParameter("idProdotto");
                    String qtaStr = request.getParameter("quantita");

                    if (idStr != null && !idStr.trim().isEmpty()) {
                        int idProdotto = Integer.parseInt(idStr);
                        
                        ElementoCarrello elemEsistente = null;
                        if (carrello.getElementi() != null) {
                            for (int i = 0; i < carrello.getElementi().size(); i++) {
                                ElementoCarrello item = carrello.getElementi().get(i);
                                if (item.getProdotto().getIdProdotto() == idProdotto) {
                                    elemEsistente = item;
                                    break;
                                }
                            }
                        }

                        int nuovaQta = 1;
                        if ("incrementa".equalsIgnoreCase(azione) && elemEsistente != null) {
                            nuovaQta = elemEsistente.getQuantita() + 1;
                        } else if ("decrementa".equalsIgnoreCase(azione) && elemEsistente != null) {
                            nuovaQta = elemEsistente.getQuantita() - 1;
                        } else if (qtaStr != null && !qtaStr.trim().isEmpty()) {
                            nuovaQta = Integer.parseInt(qtaStr);
                        }

                        if (nuovaQta <= 0) {
                            carrello.eliminaProd(idProdotto);
                        } else {
                            Prodotto prod = prodottoDao.doRetrieveByKey(idProdotto);
                            if (prod instanceof Stampa) {
                                Stampa stampa = (Stampa) prod;
                                if (nuovaQta <= stampa.getQuantita()) {
                                    carrello.aggiornaQuantita(idProdotto, nuovaQta);
                                } else {
                                    if (isAjax) {
                                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                        return;
                                    }
                                    response.sendRedirect(request.getContextPath() + "/carrello?errore=giacenza");
                                    return;
                                }
                            } else {
                                carrello.aggiornaQuantita(idProdotto, nuovaQta);
                            }
                        }
                    }
                } else if ("elimina".equalsIgnoreCase(azione) || "rimuovi".equalsIgnoreCase(azione)) {
                    String idStr = request.getParameter("idProdotto");
                    if (idStr != null && !idStr.trim().isEmpty()) {
                        int idProdotto = Integer.parseInt(idStr);
                        carrello.eliminaProd(idProdotto);
                    }
                } else if ("svuota".equalsIgnoreCase(azione)) {
                    carrello.svuota();
                }

                if (isAjax) {
                    double speseSpedizione = calcolaSpedizione(carrello);
                    double totaleOrdine = carrello.getTotale() + speseSpedizione;

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    
                    JSONObject json = new JSONObject();

                    if (carrello.getElementi() == null || carrello.getElementi().isEmpty()) {
                        json.put("carrelloVuoto", true);
                    } else {
                        json.put("carrelloVuoto", false);

                        String idStr = request.getParameter("idProdotto");
                        if (idStr != null && !idStr.trim().isEmpty()) {
                            int idProdotto = Integer.parseInt(idStr);
                            ElementoCarrello elem = null;

                            for (int i = 0; i < carrello.getElementi().size(); i++) {
                                ElementoCarrello item = carrello.getElementi().get(i);
                                if (item.getProdotto().getIdProdotto() == idProdotto) {
                                    elem = item;
                                    break;
                                }
                            }

                            if (elem == null) {
                                json.put("rimosso", true);
                            } else {
                                json.put("rimosso", false);
                                json.put("nuovaQuantita", elem.getQuantita());
                                json.put("nuovoSubtotale", elem.getTotale());
                            }
                        }

                        json.put("totaleProdotti", carrello.getTotale());
                        json.put("speseSpedizione", speseSpedizione);
                        json.put("totaleOrdine", totaleOrdine);
                    }

                    response.getWriter().write(json.toString());
                    return;
                }

            } catch (SQLException e) {
                System.err.println("Errore SQL in carrelloControl: " + e.getMessage());
                e.printStackTrace();
                if (isAjax) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                System.err.println("Formato numero non valido nei parametri del carrello: " + e.getMessage());
                if (isAjax) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }

            response.sendRedirect(request.getContextPath() + "/carrello");
            return;
        }

        double speseSpedizione = calcolaSpedizione(carrello);
        double totaleComplessivo = carrello.getTotale() + speseSpedizione;

        request.setAttribute("speseSpedizione", speseSpedizione);
        request.setAttribute("totaleComplessivo", totaleComplessivo);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/carrelloView.jsp");
        dispatcher.forward(request, response);
    }

    private double calcolaSpedizione(Carrello carrello) {
        if (carrello == null || carrello.getElementi() == null) {
            return 0.0;
        }
        for (int i = 0; i < carrello.getElementi().size(); i++) {
            ElementoCarrello item = carrello.getElementi().get(i);
            Prodotto prod = item.getProdotto();
            if (prod != null && !(prod instanceof Commissione)) {
                return 3.00;
            }
        }
        return 0.0;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}