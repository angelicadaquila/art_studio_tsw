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
</head>
<body>
<div class="container">
<jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />
    <main class="container">
        <h1>Il tuo Carrello</h1>
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
                        boolean stampa = (prod instanceof Stampa);
                        boolean commissione = (prod instanceof Commissione);
                %>
                    <tr>
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
                                <form action="<%= request.getContextPath() %>/carrello" method="GET" class="form-quantita">
                                    <input type="hidden" name="azione" value="aggiorna">
                                    <input type="hidden" name="idProdotto" value="<%= prod.getIdProdotto() %>">
                                    <input type="number" name="quantita" value="<%= item.getQuantita() %>" min="1" required>
                                    <button type="submit">Aggiorna</button>
                                </form>
                            <% } else { %>
                                <span>1 (Commissione)</span>
                            <% } %>
                        </td>

                        <td><%= String.format("%.2f", item.getTotale()) %> €</td>

                        <td>
                            <a href="<%= request.getContextPath() %>/carrello?azione=elimina&idProdotto=<%= prod.getIdProdotto() %>" 
                               class="btn-elimina" 
                               onclick="return confirm('Vuoi davvero rimuovere questo articolo?');">
                               Rimuovi
                            </a>
                        </td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>

            <div class="riepilogo-carrello">
                <h2>Totale Complessivo: <%= String.format("%.2f", carrello.getTotale()) %> €</h2>
                
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
    </main>
</div>
</body>
</html>