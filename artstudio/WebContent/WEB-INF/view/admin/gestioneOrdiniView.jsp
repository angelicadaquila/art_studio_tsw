<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Ordine" %>
<%@ page import="model.Utente" %>
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
        List<Utente> listaUtenti = (List<Utente>) request.getAttribute("listaUtenti");
        List<Utente> tuttiIClienti = (List<Utente>) request.getAttribute("tuttiIClienti");
        
        String selectedIdUtente = (String) request.getAttribute("selectedIdUtente");
        String selectedDataInizio = (String) request.getAttribute("selectedDataInizio");
        String selectedDataFine = (String) request.getAttribute("selectedDataFine");

        if (selectedIdUtente == null) { selectedIdUtente = ""; }
        if (selectedDataInizio == null) { selectedDataInizio = ""; }
        if (selectedDataFine == null) { selectedDataFine = ""; }
    %>

    <form action="<%= request.getContextPath() %>/admin/ordini" method="get" style="margin-top: 20px; margin-bottom: 25px; padding: 15px; background-color: #f8f9fa; border: 1px solid #dee2e6; border-radius: 5px;">
        <div style="display: flex; gap: 15px; align-items: flex-end; flex-wrap: wrap;">
            <div>
                <label for="idUtente"><strong>Filtra per Cliente:</strong></label><br>
                <select name="idUtente" id="idUtente" style="padding: 6px; margin-top: 5px;">
                    <option value="" <% if ("".equals(selectedIdUtente)) { out.print("selected"); } %>>-- Tutti i Clienti --</option>
                    <% 
                        if (tuttiIClienti != null) {
                            for (int i = 0; i < tuttiIClienti.size(); i++) {
                                Utente cl = tuttiIClienti.get(i);
                                String valId = String.valueOf(cl.getIdUtente());
                                boolean isSelected = valId.equals(selectedIdUtente);
                    %>
                       <option value="<%= cl.getIdUtente() %>" <% if (isSelected) { %>selected<% } %>>
    						<%= cl.getNome() %> <%= cl.getCognome() %> (<%= cl.getEmail() %>)
					   </option>
                    <% 
                            }
                        } 
                    %>
                </select>
            </div>
            <div>
                <label for="dataInizio"><strong>Da Data:</strong></label><br>
                <input type="date" name="dataInizio" id="dataInizio" value="<%= selectedDataInizio %>" style="padding: 5px; margin-top: 5px;">
            </div>

            <div>
                <label for="dataFine"><strong>A Data:</strong></label><br>
                <input type="date" name="dataFine" id="dataFine" value="<%= selectedDataFine %>" style="padding: 5px; margin-top: 5px;">
            </div>

            <div>
                <button type="submit" class="btn-opzione" style="padding: 6px 15px;">Filtra</button>
                <a href="<%= request.getContextPath() %>/admin/ordini" style="margin-left: 10px; text-decoration: none; color: #555; font-size: 0.9em;">Mostra tutti</a>
            </div>
        </div>
    </form>
    
    <%
        if (listaOrdini != null && !listaOrdini.isEmpty()) {
    %>
        <table style="width: 100%; border-collapse: collapse; margin-top: 20px;">
            <thead>
                <tr style="background-color: #f8f9fa; border-bottom: 2px solid #dee2e6;">
                    <th style="padding: 10px; text-align: left;">ID</th>
                    <th style="padding: 10px; text-align: left;">Cliente</th>
                    <th style="padding: 10px; text-align: left;">Indirizzo Spedizione</th>
                    <th style="padding: 10px; text-align: left;">Data</th>
                    <th style="padding: 10px; text-align: left;">Totale</th>
                    <th style="padding: 10px; text-align: left;">Stato</th>
                    <th style="padding: 10px; text-align: center;">Azione</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    for (int i = 0; i < listaOrdini.size(); i++) { 
                        Ordine ord = listaOrdini.get(i);
                        
                        Utente u = null;
                        if (listaUtenti != null && i < listaUtenti.size()) {
                            u = listaUtenti.get(i);
                        }
                %>
                    <tr style="border-bottom: 1px solid #dee2e6;">
                        <td style="padding: 10px;">#<%= ord.getIdOrdine() %></td>
                        
                        <td style="padding: 10px;">
                            <% if (u != null) { %>
                                <strong><%= u.getNome() %> <%= u.getCognome() %></strong><br>
                                <small style="color: #555;"><%= u.getEmail() %></small>
                            <% } else { %>
                                <span style="color: gray;">Utente #<%= ord.getIdUtente() %></span>
                            <% } %>
                        </td>

                        <td style="padding: 10px;">
                            <% if (ord.getViaSpedizione() != null && !ord.getViaSpedizione().trim().isEmpty()) { %>
                                <%= ord.getViaSpedizione() %>, <%= ord.getCivicoSpedizione() %><br>
                                <small style="color: #555;"><%= ord.getCittaSpedizione() %> (<%= ord.getRegioneSpedizione() %>)</small>
                            <% } else { %>
                                <span style="color: gray;">Non disponibile</span>
                            <% } %>
                        </td>

                        <td style="padding: 10px;"><%= ord.getDataOrdine() %></td>
                        <td style="padding: 10px;">EUR <%= String.format("%.2f", ord.getTotaleOrdine()) %></td>
                        <td style="padding: 10px;"><%= ord.getStato() %></td>
                        <td style="padding: 10px; text-align: center;">
                            <button type="button" class="btn-opzione" onclick="caricaDettaglioOrdine(<%= ord.getIdOrdine() %>, '<%= request.getContextPath() %>')">
                                Dettagli
                            </button>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } else { %>
        <p style="margin-top: 20px; color: #777;">Nessun ordine disponibile.</p>
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

       
    </div>
</div>
</body>
</html>