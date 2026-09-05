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
   		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/base.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/componenti.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/catalogo.css">
</head>
<body>

<div class="container">
<jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />
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

    <div>
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
        
        <form action="<%= request.getContextPath() %>/catalogo" method="get">
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
                	<% if (p.getImmagine() != null && !p.getImmagine().trim().isEmpty()) { %>
    					<img src="<%= request.getContextPath() %>/immagine?action=show&id=<%= p.getIdProdotto() %>" 
    					alt="<%= p.getNome() %>" 
						width="80" 
         				height="80" 
         				style="object-fit: cover;"
         				onerror="this.src='<%= request.getContextPath() %>/images/placeholder.png';">
				<% } else { %>
    					<img src="<%= request.getContextPath() %>/images/placeholder.png" 
         				alt="Nessuna immagine disponibile" 
        				width="80" 
         				height="80" 
         				style="object-fit: cover;">
				<% } %>
                    <h3><%= p.getNome() %></h3>
                    <p><strong>Prezzo:</strong> <%= String.format("%.2f", p.getPrezzo()) %> &euro;</p>
                
                    <% if (p instanceof Stampa) { %>
                        <span class="badge badge-stampa">Stampa</span>
                    <% } else if (p instanceof Commissione) { %>
                        <span class="badge badge-commissione">Commissione</span>
                    <% } %>

                    <br><br>
                    <a href="<%= request.getContextPath() %>/dettaglioProdotto?id=<%= p.getIdProdotto() %>">Vedi dettagli</a>
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