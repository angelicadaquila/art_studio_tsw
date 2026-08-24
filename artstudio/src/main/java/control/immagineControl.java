package control;
import java.io.*;
import java.sql.SQLException;
import java.util.UUID;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import dao.ProdottoDAO;
import dao.ProdottoDAOImp;
import model.Prodotto;

@WebServlet("/immagine")
@MultipartConfig(
    maxFileSize = 5 * 1024 * 1024,      
    maxRequestSize = 10 * 1024 * 1024,  
    fileSizeThreshold = 2 * 1024 * 1024 
)
public class immagineControl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String UPLOAD_DIR = "uploads";

    private ProdottoDAO prodottoDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) {
            throw new ServletException("DataSource non disponibile");
        }
        prodottoDao = new ProdottoDAOImp(ds);

        String uploadPath = getServletContext().getRealPath(File.separator + UPLOAD_DIR);
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action != null && action.equalsIgnoreCase("show")) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    Prodotto prod = prodottoDao.doRetrieveByKey(id);
                    if (prod != null && prod.getImmagine() != null && !prod.getImmagine().isEmpty()) {
                        String path = prod.getImmagine();
                        String mimeType = getServletContext().getMimeType(path);
                        if (mimeType == null) {
                            mimeType = "image/jpeg";
                        }
                        response.setContentType(mimeType);

                        try (InputStream is = new FileInputStream(path)) {
                            OutputStream os = response.getOutputStream();
                            is.transferTo(os);
                        } catch (IOException ioe) {
                            System.err.println("Error:" + ioe.getMessage());
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("Error:" + e.getMessage());
                } catch (NumberFormatException e) {
                    System.err.println("Error ID non valido: " + e.getMessage());
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action != null && "upload".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("idProdotto");
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    Part part = request.getPart("immagine");

                    if (part != null) {
                        String originalFileName = part.getSubmittedFileName();
                        if (originalFileName != null && !originalFileName.isEmpty() && part.getSize() > 0) {
                            String uniqueFileName = buildUniqueFileName(part);
                            String uploadPath = getServletContext().getRealPath(File.separator + UPLOAD_DIR + File.separator + uniqueFileName);

                            Prodotto prod = prodottoDao.doRetrieveByKey(id);
                            if (prod != null) {
                                prod.setImmagine(uploadPath); 
                                try {
                                    part.write(uploadPath);           
                                    prodottoDao.doUpdateImage(prod);  
                                    System.out.println(uploadPath);
                                } catch (SQLException e) {
                                    System.err.println("Error:" + e.getMessage());
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("Error:" + e.getMessage());
                } catch (NumberFormatException e) {
                    System.err.println("Error ID non valido: " + e.getMessage());
                }
            }
        }
        response.sendRedirect(request.getContextPath() + "/catalogo?tipo=tutti");
    }

    private String buildUniqueFileName(Part part) {
        String originalName = part.getSubmittedFileName();
        String extension;
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        } else {
            extension = "";
        }
        return UUID.randomUUID() + extension;
    }
}