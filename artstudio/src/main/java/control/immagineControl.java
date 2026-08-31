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
    maxFileSize = 200 * 1024 * 1024,      
    maxRequestSize = 50 * 1024 * 1024, 
    fileSizeThreshold = 5 * 1024 * 1024  
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
            throw new ServletException("DataSource non disponibile nel contesto applicativo.");
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
            int productCode = Integer.parseInt(request.getParameter("id"));
            try {
                Prodotto prod = prodottoDao.doRetrieveByKey(productCode);
                if (prod != null && prod.getImmagine() != null && !prod.getImmagine().isEmpty()) {
                    String path = prod.getImmagine();
                    
                    File file = new File(path);
                    if (!file.isAbsolute() || !file.exists()) {
                        String realPath = getServletContext().getRealPath(File.separator + UPLOAD_DIR + File.separator + file.getName());
                        file = new File(realPath);
                    } else {
                        file = new File(path);
                    }

                    if (file.exists()) {
                        String mimeType = getServletContext().getMimeType(file.getName());
                        if (mimeType == null) {
                            mimeType = "image/jpeg";
                        }
                        response.setContentType(mimeType);

                        try (InputStream is = new FileInputStream(file)) {
                            OutputStream os = response.getOutputStream();
                            is.transferTo(os);
                        } catch (IOException ioe) {
                            System.err.println("Error:" + ioe.getMessage());
                        }
                    } else {
                        System.err.println("File immagine non trovato: " + file.getAbsolutePath());
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error:" + e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("upload".equalsIgnoreCase(action)) {
            int productCode = Integer.parseInt(request.getParameter("idProdotto"));
            Part part = request.getPart("immagine");

            if (part != null) {
                String originalFileName = part.getSubmittedFileName();
                if (originalFileName != null && !originalFileName.isEmpty() && part.getSize() > 0) {

                    String uniqueFileName = buildUniqueFileName(part);
                    String uploadPath = getServletContext().getRealPath(File.separator + UPLOAD_DIR + File.separator + uniqueFileName);

                    try {
                        part.write(uploadPath);

                        Prodotto prod = prodottoDao.doRetrieveByKey(productCode);
                        if (prod != null) {
                            prod.setImmagine(uploadPath);
                            prodottoDao.doUpdateImage(prod);
                            System.out.println(uploadPath);
                        }
                    } catch (SQLException e) {
                        System.err.println("Error:" + e.getMessage());
                    }
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