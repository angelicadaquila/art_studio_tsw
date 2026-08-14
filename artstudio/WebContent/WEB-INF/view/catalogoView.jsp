<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Prodotto" %>
<%@ page import="model.Stampa" %>
<%@ page import="model.Commissione" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Prodotti</title>
    <style>
        .container {
            max-width: 1000px;
            margin: 30px auto;
            font-family: Arial, sans-serif;
        }
        .header-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        .btn-back {
            padding: 8px 15px;
            background-color: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        .riga-prod {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
        }
        .singolo-prod {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 15px;
            text-align: center;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        .badge {
            display: inline-block;
            padding: 4px 8px;
            font-size: 0.8em;
            border-radius: 4px;
            color: white;
            margin-top: 5px;
        }
        .badge-stampa { background-color: #28a745; }
        .badge-commissione { background-color: #17a2b8; }
        .nessuno-trovato {
            text-align: center;
            font-size: 1.2em;
            color: #777;
            margin-top: 40px;
        }
    </style>
</head>
<body>

<div class="container">
    <% 
        List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("prodotti");
        String selectedTipo = (String) request.getAttribute("selectedtipo");
        if (selectedTipo == null) {
            selectedTipo = "tutti";
        }
        
        String ordinamento = request.getParameter("ordinamento");
        if (ordinamento == null) {
            ordinamento = "";
        }
    %>

    <div class="header-actions">
        <a href="${pageContext.request.contextPath}/catalogo" class="btn-back">&laquo; Indietro</a>
        
        <h1>
            Catalogo: 
            <% 
                if ("stampa".equalsIgnoreCase(selectedTipo)) {
                    out.print("Stampe");
                } else if ("commissione".equalsIgnoreCase(selectedTipo)) {
                    out.print("Commissioni");
                } else {
                    out.print("Tutti i Prodotti");
                }
            %>
        </h1>
        
        <form action="${pageContext.request.contextPath}/catalogo" method="get">
            <input type="hidden" name="tipo" value="<%= selectedTipo %>">
            <label for="ordinamento">Ordina per:</label>
            <select name="ordinamento" id="ordinamento" onchange="this.form.submit()">
    			<option value="" <% if ("".equals(ordinamento)) { out.print("selected"); } %>>Predefinito</option>
    			<option value="nome" <% if ("nome".equals(ordinamento)) { out.print("selected"); } %>>Nome</option>
    			<option value="prezzo_crescente" <% if ("prezzo_crescente".equals(ordinamento)) { out.print("selected"); } %>>Prezzo: crescente</option>
    			<option value="prezzo_decrescente" <% if ("prezzo_decrescente".equals(ordinamento)) { out.print("selected"); } %>>Prezzo: decrescente</option>
			</select>
        </form>
    </div>
    
    <% if (prodotti != null && !prodotti.isEmpty()) { %>
    
        <div class="riga-prod">
            <% 
                for (int i = 0; i < prodotti.size(); i++) { 
                    Prodotto p = prodotti.get(i);
            %>
                <div class="singolo-prod">
                    <h3><%= p.getNome() %></h3>
                    <p><strong>Prezzo:</strong> <%= String.format("%.2f", p.getPrezzo()) %> &euro;</p>
                
                    <% if (p instanceof Stampa) { %>
                        <span class="badge badge-stampa">Stampa</span>
                    <% } else if (p instanceof Commissione) { %>
                        <span class="badge badge-commissione">Commissione</span>
                    <% } %>

                    <br><br>
                    <a href="${pageContext.request.contextPath}/prodotto?id=<%= p.getIdProdotto() %>">Vedi dettagli</a>
                </div>
            <% } %>
        </div>
    <% } else { %>
        <div class="nessuno-trovato">
            <p>Nessun prodotto trovato per questa categoria.</p>
        </div>
    <% } %>

</div>

</body>
</html>