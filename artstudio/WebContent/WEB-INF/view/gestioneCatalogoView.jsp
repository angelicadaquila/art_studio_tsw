<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Prodotto" %>
<%@ page import="model.Stampa" %>
<%@ page import="model.Commissione" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Gestione Catalogo Prodotti</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/componenti.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/catalogo.css">
</head>
<body>

<div style="padding: 20px; max-width: 1200px; margin: 0 auto;">

    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
        <h2>Gestione Catalogo Prodotti</h2>
        <a href="<%= request.getContextPath() %>/admin/prodotti?action=addForm" class="btn-aggiungi">+ Aggiungi Prodotto</a>
    </div>

    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
            out.print("<p style=\"color: green; font-weight: bold;\">" + message + "</p>");
        }

        List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("prodotti");
        if (prodotti != null && !prodotti.isEmpty()) {
    %>
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
                <p><strong>ID:</strong> <%= p.getIdProdotto() %></p>
                <p><strong>Prezzo:</strong> &euro; <%= String.format("%.2f", p.getPrezzo()) %></p>

                <div>
                    <% if (p instanceof Stampa) { %>
                        <span class="badge badge-stampa">Stampa</span>
                    <% } else if (p instanceof Commissione) { %>
                        <span class="badge badge-commissione">Commissione</span>
                    <% } %>
                </div>

                <p style="font-size: 0.9em; margin-top: 10px;">
                    <%
                        if (p instanceof Stampa) {
                            Stampa s = (Stampa) p;
                            out.print("Dimensione: " + s.getDimensione() + "<br>Quantità: " + s.getQuantita());
                        } else if (p instanceof Commissione) {
                            Commissione c = (Commissione) p;
                            out.print("Tempo di realizzazione: " + c.getTempo() + " giorni");
                        }
                    %>
                </p>

                <p>
                    <h2>Disponibile:</h2> 
                    <% if (p.isDisponibile()) { %>
                        <span style="color: green; font-weight: bold;">Sì</span>
                    <% } else { %>
                        <span style="color: red; font-weight: bold;">No</span>
                    <% } %>
                </p>

                <div style="margin-top: 15px; justify-content: center; gap: 10px;">
                    <a href="<%= request.getContextPath() %>/admin/prodotti?action=edit&idProdotto=<%= p.getIdProdotto() %>" class="btn-opzione" style="padding: 5px 10px; font-size: 0.9em;">Modifica</a>
                    <a href="<%= request.getContextPath() %>/admin/prodotti?action=delete&idProdotto=<%= p.getIdProdotto() %>" class="btn-indietro" style="background-color: #dc3545; padding: 5px 10px; font-size: 0.9em;" onclick="return confirm('Sei sicuro di voler eliminare questo prodotto?');">Elimina</a>
                </div>
            </div>
        <%
            }
        %>
        </div>
    <%
        } else {
    %>
        <div class="nessuno-trovato">
            <p>Nessun prodotto presente nel catalogo.</p>
        </div>
    <%
        }
    %>

</div>

</body>
</html>