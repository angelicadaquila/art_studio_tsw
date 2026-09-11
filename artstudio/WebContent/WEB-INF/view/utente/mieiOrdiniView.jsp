<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Ordine" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath()%>/styles/base.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/styles/componenti.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/styles/catalogo.css" rel="stylesheet" type="text/css">
    <title>I Miei Ordini</title>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />

    <h2>I Miei Ordini e Commissioni</h2>

    <% if ("ok".equals(request.getParameter("esito"))) { %>
        <p style="color: green; font-weight: bold; background-color: #d4edda; padding: 10px; border-radius: 4px;">
            Ordine completato con successo! Puoi seguirne lo stato da questa pagina.
        </p>
    <% } %>

    <%
        List<Ordine> listaOrdini = (List<Ordine>) request.getAttribute("listaOrdini");
        if (listaOrdini != null && !listaOrdini.isEmpty()) {
    %>
        <table style="width: 100%; border-collapse: collapse; margin-top: 20px;">
            <thead>
                <tr style="background-color: #f8f9fa; border-bottom: 2px solid #dee2e6;">
                    <th style="padding: 10px; text-align: left;">ID Ordine</th>
                    <th style="padding: 10px; text-align: left;">Indirizzo Spedizione</th>
                    <th style="padding: 10px; text-align: left;">Totale</th>
                    <th style="padding: 10px; text-align: left;">Stato</th>
                    <th style="padding: 10px; text-align: left;">Foto Consegna</th>
                </tr>
            </thead>
            <tbody>
                <% for (int i = 0; i < listaOrdini.size(); i++) { 
                    Ordine ord = listaOrdini.get(i);
                %>
                    <tr style="border-bottom: 1px solid #dee2e6;">
                        <td style="padding: 10px;">#<%= ord.getIdOrdine() %></td>
                        <td style="padding: 10px;">
                            <% if (ord.getIndirizzo() != null && ord.getIndirizzo().getVia() != null && !ord.getIndirizzo().getVia().trim().isEmpty()) { %>
                                <%= ord.getIndirizzo().getVia() %>, <%= ord.getIndirizzo().getCivico() %> - 
                                <%= ord.getIndirizzo().getCitta() %> (<%= ord.getIndirizzo().getRegione() %>)
                            <% } else { %>
                                <span style="color: gray;">Non disponibile</span>
                            <% } %>
                        </td>

                        <td style="padding: 10px;">€ <%= String.format("%.2f", ord.getTotaleOrdine()) %></td>
                        <td style="padding: 10px;">
                            <span style="font-weight: bold;"><%= ord.getStato() %></span>
                        </td>
                        
                        <td style="padding: 10px;">
                            <% if (ord.getImmagineConsegna() != null && !ord.getImmagineConsegna().trim().isEmpty()) { %>
                                <a href="<%=request.getContextPath()%>/admin_images/<%= ord.getImmagineConsegna() %>" target="_blank" style="color: #0d6efd; font-weight: bold;">Visualizza Foto</a>
                            <% } else { %>
                                <span style="color: gray;">Non ancora disponibile</span>
                            <% } %>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } else { %>
        <p style="margin-top: 20px; color: gray;">Non hai ancora effettuato alcun ordine.</p>
    <% } %>

</div>
</body>
</html>