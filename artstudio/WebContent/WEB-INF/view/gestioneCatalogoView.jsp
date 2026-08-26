<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
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

<div class="container">

    <div>
        <h2>Gestione Catalogo Prodotti</h2>
        <a href="<%= request.getContextPath() %>/admin/prodotti?action=addForm" class="btn-add">+ Aggiungi Prodotto</a>
    </div>

    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
            out.print("<p class=\"message\">" + message + "</p>");
        }
    %>

    <table border="1" class="admin-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Immagine</th>
                <th>Nome</th>
                <th>Prezzo</th>
                <th>Tipologia</th>
                <th>Dettagli Specifici</th>
                <th>Disponibile</th>
                <th>Azioni</th>
            </tr>
        </thead>
        <tbody>
        <%
        	List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("prodotti");
        	if (prodotti != null && !prodotti.isEmpty()) {
            	for (int i = 0; i < prodotti.size(); i++) {
                	Prodotto p = prodotti.get(i);
                
        %>
            <tr>
                <td><%= p.getIdProdotto() %></td>
                <td>
                    <img src="<%= request.getContextPath() %>/images/<%= p.getImmagine() %>" alt="<%= p.getNome() %>" width="50">
                </td>
                <td><%= p.getNome() %></td>
                <td>&euro; <%= String.format("%.2f", p.getPrezzo()) %></td>
                <td>
                    <%
                        if (p instanceof Stampa) {
                            out.print("Stampa");
                        } else{
                            out.print("Commissione");
                        }
                    %>
                </td>
                <td>
                    <%
                        if (p instanceof Stampa) {
                            Stampa s = (Stampa) p;
                            out.print("Dimensione: " + s.getDimensione() + " | Quantita: " + s.getQuantita());
                        } else if (p instanceof Commissione) {
                            Commissione c = (Commissione) p;
                            out.print("Tempo: " + c.getTempo() + " giorni");
                        }
                    %>
                </td>
                <td>
                    <% if (p.isDisponibile()) { %>
                        <span style="color: green;">Sì</span>
                    <% } else { %>
                        <span style="color: red;">No</span>
                    <% } %>
                </td>
                <td>
                    <a href="<%= request.getContextPath() %>/admin/prodotti?action=edit&id=<%= p.getIdProdotto() %>" class="btn-edit">Modifica</a>
                    <a href="<%= request.getContextPath() %>/admin/prodotti?action=delete&id=<%= p.getIdProdotto() %>" class="btn-delete" onclick="return confirm('Sei sicuro di voler eliminare questo prodotto?');">Elimina</a>
                </td>
            </tr>
        <%
                }
            } else {
        %>
            <tr>
                <td colspan="8">Nessun prodotto presente nel catalogo.</td>
            </tr>
        <%
            }
        %>
        </tbody>
    </table>

</div>

</body>
</html>