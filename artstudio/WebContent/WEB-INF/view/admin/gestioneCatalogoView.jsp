<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Prodotto" %>
<%@ page import="model.Stampa" %>
<%@ page import="model.Commissione" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Catalogo Prodotti</title>
    <link href="<%=request.getContextPath()%>/styles/base.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/styles/componenti.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/styles/catalogo.css" rel="stylesheet" type="text/css">
</head>
<body>
<jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />

<div class="container">

    <div class="catalogo-header">
        <h2>Gestione Catalogo Prodotti</h2>
        <a href="<%= request.getContextPath() %>/admin/prodotti?action=aggiungi" class="btn-aggiungi">+ Aggiungi Prodotto</a>
    </div>

    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
            out.print("<p class=\"msg-successo\">" + message + "</p>");
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
                         class="img-prodotto-catalogo"
                         onerror="this.src='<%= request.getContextPath() %>/images/placeholder.png';">
                <% } else { %>
                    <img src="<%= request.getContextPath() %>/images/placeholder.png" 
                         alt="Nessuna immagine disponibile" 
                         class="img-prodotto-catalogo">
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

                <p class="dettaglio-prodotto-testo">
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

                <div class="disponibilita-box">
                    <span class="disponibilita-titolo">Disponibile:</span> 
                    <% if (p.isDisponibile()) { %>
                        <span class="testo-disponibile-si">Sì</span>
                    <% } else { %>
                        <span class="testo-disponibile-no">No</span>
                    <% } %>
                </div>

                <div class="azioni-prodotto-card">
                    <a href="<%= request.getContextPath() %>/admin/prodotti?action=modifica&idProdotto=<%= p.getIdProdotto() %>" class="btn-opzione btn-piccolo">Modifica</a>
                    <a href="<%= request.getContextPath() %>/admin/prodotti?action=elimina&idProdotto=<%= p.getIdProdotto() %>" class="btn-elimina-piccolo" onclick="return confirm('Sei sicuro di voler eliminare questo prodotto? Non verrà eliminato dal db ma verrà reso non disponibile');">Elimina</a>
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