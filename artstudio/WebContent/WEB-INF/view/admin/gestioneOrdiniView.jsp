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
    <title>Admin - Gestione Ordini</title>

    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/ajax.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/ordiniDettagli.js"></script>
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />

    <h2>Pannello Admin - Gestione Ordini</h2>

    <%
        List<Ordine> listaOrdini = (List<Ordine>) request.getAttribute("listaOrdini");
        if (listaOrdini != null && !listaOrdini.isEmpty()) {
    %>
        <table style="width: 100%; border-collapse: collapse; margin-top: 20px;">
            <thead>
                <tr style="background-color: #f8f9fa; border-bottom: 2px solid #dee2e6;">
                    <th style="padding: 10px;">ID</th>
                    <th style="padding: 10px;">Data</th>
                    <th style="padding: 10px;">Totale</th>
                    <th style="padding: 10px;">Stato</th>
                    <th style="padding: 10px;">Azione</th>
                </tr>
            </thead>
            <tbody>
                <% for (Ordine ord : listaOrdini) { %>
                    <tr style="border-bottom: 1px solid #dee2e6;">
                        <td style="padding: 10px;">#<%= ord.getIdOrdine() %></td>
                        <td style="padding: 10px;"><%= ord.getDataOrdine() %></td>
                        <td style="padding: 10px;">EUR <%= String.format("%.2f", ord.getTotaleOrdine()) %></td>
                        <td style="padding: 10px;"><%= ord.getStato() %></td>
                        <td style="padding: 10px;">
                            <button type="button" class="btn-opzione" onclick="caricaDettaglioOrdine(<%= ord.getIdOrdine() %>, '<%= request.getContextPath() %>')">
                                Dettagli
                            </button>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } %>
</div>

<div id="finestraDettaglio" class="finestra-overlay">
    <div class="finestra-content">
        <button type="button" class="close-finestra-btn" onclick="chiudiFinestra()">&times;</button>
        
        <h3>Dettaglio Ordine <span id="finestraIdOrdine"></span></h3>
        <hr style="margin-bottom: 15px; border: 0; border-top: 1px solid #eee;">

        <h4>Articoli Acquistati</h4>
        <ul id="listaArticoliFinestra" style="padding-left: 20px; margin-bottom: 20px;">
        </ul>

        <hr style="margin: 15px 0; border: 0; border-top: 1px solid #eee;">

        <form action="<%=request.getContextPath()%>/admin/ordini" method="post" style="margin-bottom: 15px;">
            <input type="hidden" name="action" value="cambiaStato">
            <input type="hidden" name="idOrdine" id="formStatoIdOrdine">
            <label><strong>Cambia Stato:</strong></label><br>
            <select name="nuovoStato" style="padding: 6px; margin-top: 5px;">
                <option value="In lavorazione">In lavorazione</option>
                <option value="Completato">Completato</option>
                <option value="Annullato">Annullato</option>
            </select>
            <button type="submit" class="btn-opzione" style="padding: 6px 12px; margin-left: 5px;">Aggiorna</button>
        </form>

        <form action="<%=request.getContextPath()%>/admin/ordini" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="uploadFoto">
            <input type="hidden" name="idOrdine" id="formFotoIdOrdine">
            <label><strong>Foto Consegna:</strong></label><br>
            <input type="file" name="immagineConsegna" accept="image/*" required style="margin-top: 5px;"><br>
            <button type="submit" class="btn-opzione" style="padding: 6px 12px; margin-top: 5px;">Carica Foto</button>
        </form>
    </div>
</div>
</body>
</html>