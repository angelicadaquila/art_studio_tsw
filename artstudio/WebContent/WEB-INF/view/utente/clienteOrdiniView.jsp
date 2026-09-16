<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Ordine" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>I Miei Ordini</title>
    
    <link href="<%=request.getContextPath()%>/styles/base.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/styles/componenti.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/styles/ordini.css" rel="stylesheet" type="text/css">
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/ajax/ajax.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/ajax/ordiniDettagli.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/view/barraSuperioreView.jsp"/>
<div class="container">

    <% if ("ok".equals(request.getParameter("esito"))) { %>
        <p class="msg-successo">
            Ordine completato con successo!
        </p>
    <% } %>

    <%
        List<Ordine> listaOrdini = (List<Ordine>) request.getAttribute("listaOrdini");
        if (listaOrdini != null && !listaOrdini.isEmpty()) {
    %>
        <table class="tabella-ordini-admin">
            <thead>
                <tr>
                    <th class="testo-sinistra">ID Ordine</th>
                    <th class="testo-sinistra">Indirizzo Spedizione</th>
                    <th class="testo-sinistra">Totale</th>
                    <th class="testo-sinistra">Stato</th>
                    <th class="testo-centro">Azione</th>
                </tr>
            </thead>
            <tbody>
                <% for (int i = 0; i < listaOrdini.size(); i++) { 
                    Ordine ord = listaOrdini.get(i);
                %>
                    <tr>
                        <td>#<%= ord.getIdOrdine() %></td>
                        <td>
                            <% if (ord.getViaSpedizione() != null && !ord.getViaSpedizione().trim().isEmpty()) { %>
                                <%= ord.getViaSpedizione() %>, <%= ord.getCivicoSpedizione() %><br>
                                <small class="testo-dettaglio-grigio"><%= ord.getCittaSpedizione() %> (<%= ord.getRegioneSpedizione() %>)</small>
                            <% } else { %>
                                <span class="testo-dettaglio-grigio">Non disponibile</span>
                            <% } %>
                        </td>

                        <td>&euro; <%= String.format("%.2f", ord.getTotaleOrdine()) %></td>
                        <td>
                            <strong><%= ord.getStato() %></strong>
                        </td>
                        
                        <td class="testo-centro">
                            <button type="button" class="btn-opzione" onclick="caricaDettaglioOrdine(<%= ord.getIdOrdine() %>, '<%= request.getContextPath() %>', '/utente/mieiOrdini')">
                                Dettagli
                            </button>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } else { %>
        <p class="testo-nessun-ordine">Non hai ancora effettuato alcun ordine.</p>
    <% } %>

</div>

<div id="finestraDettaglio" class="finestra-overlay">
    <div class="finestra-content">
        <button type="button" class="close-finestra-btn" onclick="chiudiFinestra()">&times;</button>
        
        <h3>Dettaglio Ordine <span id="finestraIdOrdine"></span></h3>
        <hr class="separatore-dettagli">

        <h4>Articoli Acquistati</h4>
        <ul id="listaArticoliFinestra" class="lista-articoli-dettagli">
        </ul>
    </div>
</div>
</body>
</html>