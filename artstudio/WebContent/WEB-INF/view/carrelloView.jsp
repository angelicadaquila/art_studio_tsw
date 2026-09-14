<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Carrello" %>
<%@ page import="model.ElementoCarrello" %>
<%@ page import="model.Prodotto" %>
<%@ page import="model.Stampa" %>
<%@ page import="model.Commissione" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ArtStudio - Carrello</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/carrello.css">
    <link href="<%=request.getContextPath()%>/styles/catalogo.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/carrelloAjax.js"></script>
</head>
<body>
<div class="container">
<jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />
    <main class="container">
        <h1>Il tuo Carrello</h1>

        <div id="contenitoreMessaggiErrore">
        <%
            String errore = request.getParameter("errore");
            if ("giacenza".equals(errore)) {
        %>
            <div class="messaggio-errore" style="background-color: #f8d7da; color: #721c24; padding: 12px; border: 1px solid #f5c6cb; border-radius: 5px; margin-bottom: 20px; text-align: center;">
                <strong>Attenzione!</strong> La quantità richiesta supera la disponibilità attuale in magazzino.
            </div>
        <%
            }
        %>
    </div>

        <div id="contenutoCarrello">
        <%
            Carrello carrello = (Carrello) session.getAttribute("carrello");
            List<ElementoCarrello> elementi = null;

            if (carrello != null) {
                elementi = carrello.getElementi();
            }

            if (elementi != null && !elementi.isEmpty()) {
        %>
            <table class="tabella-carrello">
                <thead>
                    <tr>
                        <th>Prodotto</th>
                        <th>Tipo</th>
                        <th>Dettagli / Note</th>
                        <th>Prezzo Unitario</th>
                        <th>Quantità</th>
                        <th>Totale</th>
                        <th>Azione</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    for (int i = 0; i < elementi.size(); i++) {
                        ElementoCarrello item = elementi.get(i);
                        Prodotto prod = item.getProdotto();
                        int idProd = prod.getIdProdotto();
                        boolean stampa = (prod instanceof Stampa);
                        boolean commissione = (prod instanceof Commissione);
                %>
                    <tr id="riga-prod-<%= idProd %>">
                        <td>
                            <strong><%= prod.getNome() %></strong>
                        </td>

                        <td>
                            <% 
                                if (stampa) { 
                                    out.print("Stampa");
                                } else { 
                                    out.print("Commissione Personalizzata");
                                }
                            %>
                        </td>

                        <td>
                            <% if (commissione) { %>
                                <% if (item.getDescrizioneComm() != null && !item.getDescrizioneComm().trim().isEmpty()) { %>
                                    <div class="note-commissione">
                                        <strong>Richiesta:</strong> <%= item.getDescrizioneComm() %>
                                    </div>
                                <% } %>
                                
                                <% if (item.getRefComm() != null && !item.getRefComm().trim().isEmpty()) { %>
                                    <div class="allegato-commissione">
                                        <strong>Immagine allegata:</strong><br>
                                        <a href="<%= request.getContextPath() %>/user_images/<%= item.getRefComm() %>" target="_blank">
                                            <img src="<%= request.getContextPath() %>/user_images/<%= item.getRefComm() %>" 
                                                 alt="Riferimento commissione" 
                                                 style="max-width: 80px; height: auto; border: 1px solid #ccc; margin-top: 5px;">
                                        </a>
                                    </div>
                                <% } %>
                            <% } else { %>
                                <span>-</span>
                            <% } %>
                        </td>

                        <td><%= String.format("%.2f", prod.getPrezzo()) %> €</td>

                        <td>
                            <% if (stampa) { %>
                                <div style="display: flex; align-items: center; justify-content: center; gap: 5px;">
                                    <button type="button" class="btn-opzione" onclick="aggiornaQuantita(<%= idProd %>, 'decrementa', '<%= request.getContextPath() %>')">-</button>
                                    <span id="qta-<%= idProd %>" style="font-weight: bold; padding: 0 5px;"><%= item.getQuantita() %></span>
                                    <button type="button" class="btn-opzione" onclick="aggiornaQuantita(<%= idProd %>, 'incrementa', '<%= request.getContextPath() %>')">+</button>
                                </div>
                            <% } else { %>
                                <span>1 (Commissione)</span>
                            <% } %>
                        </td>

                        <td id="subtotale-<%= idProd %>"><%= String.format("%.2f", item.getTotale()) %> €</td>

                        <td>
                            <button type="button" class="btn-elimina" onclick="aggiornaQuantita(<%= idProd %>, 'elimina', '<%= request.getContextPath() %>')">
                               Rimuovi
                            </button>
                        </td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>
<div class="riepilogo-carrello">
    <%
        Double speseSped = (Double) request.getAttribute("speseSpedizione");
        Double totComp = (Double) request.getAttribute("totaleComplessivo");
        if (speseSped == null){
        	speseSped = 0.0; 
        }
        if (totComp == null) {
        	totComp = carrello.getTotale(); 
        }
    %>
    <p>Spese di spedizione: <strong id="speseSpedizione"><%= String.format("%.2f", speseSped) %> €</strong></p>
    <h2>Totale Complessivo: <span id="totaleOrdine"><%= String.format("%.2f", totComp) %></span> €</h2>
    
    <div class="azioni-carrello">
        <a href="<%= request.getContextPath() %>/carrello?azione=svuota" 
           class="btn-svuota" 
           onclick="return confirm('Vuoi davvero svuotare il carrello?');">
           Svuota Carrello
        </a>

        <a href="<%= request.getContextPath() %>/utente/checkout" class="btn-checkout">
            Procedi all'Ordine
        </a>
    </div>
</div>

        <%
            } else {
        %>
            <div class="carrello-vuoto">
                <p>Il tuo carrello è attualmente vuoto.</p>
                <a href="<%= request.getContextPath() %>/catalogo?tipo=tutti" class="btn-catalogo">Torna al Catalogo</a>
            </div>
        <%
            }
        %>
        </div>
    </main>
</div>
</body>
</html>