<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Carrello" %>
<%@ page import="model.ElementoCarrello" %>
<%@ page import="model.Prodotto" %>
<%@ page import="model.Stampa" %>
<%@ page import="model.Commissione" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ArtStudio - Carrello</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/base.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/componenti.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/form.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/carrello.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/ordini.css">
</head>
<body>

<jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />

<div class="container">

    <h1>Il tuo Carrello</h1>

    <div id="contenitoreMessaggiErrore">
    <%
        String errore = request.getParameter("errore");
        if ("giacenza".equals(errore)) {
    %>
        <p class="msg-errore">
            <strong>Attenzione!</strong> La quantità richiesta supera la disponibilità attuale in magazzino.
        </p>
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
    <div class="tabella-responsive">
        <table class="tabella-carrello">
            <thead>
                <tr>
                    <th class="testo-sinistra">Prodotto</th>
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
                <tr id="riga-prod-<%= i %>">
                    <td class="testo-sinistra">
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
                                             class="img-prodotto-catalogo">
                                    </a>
                                </div>
                            <% } %>
                        <% } else { %>
                            <span class="testo-dettaglio-grigio">-</span>
                        <% } %>
                    </td>

                    <td><%= String.format("%.2f", prod.getPrezzo()) %> &euro;</td>

                    <td>
                        <% if (stampa) { %>
                            <div class="form-azioni">
                                <button type="button" class="btn-opzione" onclick="aggiornaQuantita(<%= idProd %>, 'decrementa', '<%= request.getContextPath() %>')">-</button>
                                <span id="qta-<%= idProd %>"><strong><%= item.getQuantita() %></strong></span>
                                <button type="button" class="btn-opzione" onclick="aggiornaQuantita(<%= idProd %>, 'incrementa', '<%= request.getContextPath() %>')">+</button>
                            </div>
                        <% } else { %>
                            <span>1 (Commissione)</span>
                        <% } %>
                    </td>

                    <td id="subtotale-<%= idProd %>"><%= String.format("%.2f", item.getTotale()) %> &euro;</td>

                    <td>
                       <button type="button" class="btn-cancella" onclick="rimuoviDinamico(this, '<%= request.getContextPath() %>')">
      						 Rimuovi
   					</button>
                    </td>
                </tr>
            <%
                }
            %>
            </tbody>
        </table>
</div>

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
            <p>Spese di spedizione: <strong id="speseSpedizione"><%= String.format("%.2f", speseSped) %> &euro;</strong></p>
            <h2>Totale Complessivo: <span id="totaleOrdine"><%= String.format("%.2f", totComp) %></span> &euro;</h2>
            
            <div class="form-azioni">
                <a href="<%= request.getContextPath() %>/carrello?azione=svuota" 
                   class="btn-cancella" 
                   onclick="return confirm('Vuoi davvero svuotare il carrello?');">
                   Svuota Carrello
                </a>

                <a href="<%= request.getContextPath() %>/utente/checkout" class="btn-invio">
                    Procedi all'Ordine
                </a>
            </div>
        </div>

    <%
        } else {
    %>
        <div class="nessuno-trovato">
            <p>Il tuo carrello è attualmente vuoto.</p>
            <a href="<%= request.getContextPath() %>/catalogo?tipo=tutti" class="btn-opzione">Torna al Catalogo</a>
        </div>
    <%
        }
    %>
    </div>

</div>

<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/ajax/carrelloAjax.js"></script>
</body>
</html>