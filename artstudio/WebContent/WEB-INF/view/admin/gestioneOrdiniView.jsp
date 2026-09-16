<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Ordine" %>
<%@ page import="model.Utente" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Admin - Gestione Ordini</title>
    
    <link href="<%=request.getContextPath()%>/styles/base.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/styles/componenti.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/styles/ordini.css" rel="stylesheet" type="text/css">

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

    <form action="<%= request.getContextPath() %>/admin/ordini" method="get" class="box-filtri-ordini">
        <div class="riga-filtri-ordini">
            <div>
                <label for="idUtente"><strong>Filtra per Cliente:</strong></label><br>
                <select name="idUtente" id="idUtente" class="select-filtro-ordini">
                    <option value="" <% if ("".equals(selectedIdUtente)) { out.print("selected"); } %>>Tutti i Clienti</option>
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
                <input type="date" name="dataInizio" id="dataInizio" value="<%= selectedDataInizio %>" class="input-data-ordini">
            </div>

            <div>
                <label for="dataFine"><strong>A Data:</strong></label><br>
                <input type="date" name="dataFine" id="dataFine" value="<%= selectedDataFine %>" class="input-data-ordini">
            </div>

            <div class="azioni-filtro-ordini">
                <button type="submit" class="btn-opzione btn-filtra-ordini">Filtra</button>
                <a href="<%= request.getContextPath() %>/admin/ordini" class="link-reset-filtri">Mostra tutti</a>
            </div>
        </div>
    </form>
    
    <%
        if (listaOrdini != null && !listaOrdini.isEmpty()) {
    %>
        <table class="tabella-ordini-admin">
            <thead>
                <tr>
                    <th class="testo-sinistra">ID</th>
                    <th class="testo-sinistra">Cliente</th>
                    <th class="testo-sinistra">Indirizzo Spedizione</th>
                    <th class="testo-sinistra">Data</th>
                    <th class="testo-sinistra">Totale</th>
                    <th class="testo-sinistra">Stato</th>
                    <th class="testo-centro">Azione</th>
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
                    <tr>
                        <td>#<%= ord.getIdOrdine() %></td>
                        
                        <td>
                            <% if (u != null) { %>
                                <strong><%= u.getNome() %> <%= u.getCognome() %></strong><br>
                                <small class="testo-dettaglio-grigio"><%= u.getEmail() %></small>
                            <% } else { %>
                                <span class="testo-dettaglio-grigio">Utente #<%= ord.getIdUtente() %></span>
                            <% } %>
                        </td>

                        <td>
                            <% if (ord.getViaSpedizione() != null && !ord.getViaSpedizione().trim().isEmpty()) { %>
                                <%= ord.getViaSpedizione() %>, <%= ord.getCivicoSpedizione() %><br>
                                <small class="testo-dettaglio-grigio"><%= ord.getCittaSpedizione() %> (<%= ord.getRegioneSpedizione() %>)</small>
                            <% } else { %>
                                <span class="testo-dettaglio-grigio">Non disponibile</span>
                            <% } %>
                        </td>

                        <td><%= ord.getDataOrdine() %></td>
                        <td>EUR <%= String.format("%.2f", ord.getTotaleOrdine()) %></td>
                        <td><%= ord.getStato() %></td>
                        <td class="testo-centro">
                            <button type="button" class="btn-opzione" onclick="caricaDettaglioOrdine(<%= ord.getIdOrdine() %>, '<%= request.getContextPath() %>')">
                                Dettagli
                            </button>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    <% } else { %>
        <p class="testo-nessun-ordine">Nessun ordine disponibile.</p>
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

        <hr class="separatore-dettagli">

        <form action="<%=request.getContextPath()%>/admin/ordini" method="post" class="form-cambia-stato-dettagli">
            <input type="hidden" name="action" value="cambiaStato">
            <input type="hidden" name="idOrdine" id="formStatoIdOrdine">
            <label><strong>Cambia Stato:</strong></label><br>
            <select name="nuovoStato" class="select-stato-ordini">
                <option value="In lavorazione">In lavorazione</option>
                <option value="Completato">Completato</option>
            </select>
            <button type="submit" class="btn-opzione btn-aggiorna-stato">Aggiorna</button>
        </form>
    </div>
</div>
</body>
</html>