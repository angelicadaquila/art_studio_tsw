<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Carrello" %>
<%@ page import="model.ElementoCarrello" %>
<%@ page import="model.Indirizzo" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ArtStudio - Checkout</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/base.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/componenti.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/styles/form.css">
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/checkout.js"></script>
</head>
<body>

<jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />

<div class="container">

    <h2>Riepilogo e Completa Ordine</h2>

    <%
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        Indirizzo ind = (Indirizzo) request.getAttribute("indirizzo");
        String errore = (String) request.getAttribute("errore");
        if (errore != null) {
    %>
        <p id="errore" class="msg-errore"><%= errore %></p>
    <% } %>

    <form action="<%= request.getContextPath() %>/utente/checkout" method="POST" class="form-layout">
        
        <fieldset class="campi">
            <legend><strong>1. Indirizzo di Spedizione</strong></legend>
            
            <% if (ind != null) { %>
                <div class="form-gruppo">
                    <input type="radio" id="tipo_profilo" name="idIndirizzo" value="<%= ind.getIdIndirizzo() %>" checked onclick="toggleIndirizzo(false)">
                    <label for="tipo_profilo">
                        <strong>Usa il mio indirizzo predefinito:</strong> <%= ind.getVia() %>, <%= ind.getCivico() %> - <%= ind.getCitta() %> (<%= ind.getRegione() %>)
                    </label>
                </div>
            <% } %>

            <div class="form-gruppo">
                <input type="radio" id="tipo_nuovo" name="idIndirizzo" value="0" <% if (ind == null) { %>checked<% } %> onclick="toggleIndirizzo(true)">
                <label for="tipo_nuovo"><strong>Spedisci a un altro indirizzo (solo per questo ordine)</strong></label>
            </div>

            <div id="boxNuovoIndirizzo" style="display: <% if (ind == null) { %>block<% } else { %>none<% } %>;">
                <div class="form-gruppo">
                    <label for="via">Via/Piazza:</label>
                    <input type="text" id="via" name="via" placeholder="Es. Via Roma">
                </div>
                <div class="form-gruppo">
                    <label for="civico">Civico:</label>
                    <input type="text" id="civico" name="civico" placeholder="Es. 10">
                </div>
                <div class="form-gruppo">
                    <label for="citta">Città:</label>
                    <input type="text" id="citta" name="citta" placeholder="Es. Salerno">
                </div>
                <div class="form-gruppo">
                    <label for="regione">Regione:</label>
                    <input type="text" id="regione" name="regione" placeholder="Es. Campania">
                </div>
            </div>
        </fieldset>

        <fieldset class="campi">
            <legend><strong>2. Dati di Pagamento (Carta di Credito / Debito)</strong></legend>
    
            <div class="form-gruppo">
                <label for="intestatario">Intestatario Carta:</label>
                <input type="text" id="intestatario" name="intestatario" placeholder="Es. Mario Rossi">
            </div>

            <div class="form-gruppo">
                <label for="numeroCarta">Numero Carta:</label>
                <input type="text" id="numeroCarta" name="numeroCarta" placeholder="1234 5678 9012 3456" maxlength="19">
            </div>

            <div class="form-azioni">
                <div class="form-gruppo">
                    <label for="scadenza">Data di Scadenza (MM/AA):</label>
                    <input type="text" id="scadenza" name="scadenza" placeholder="MM/AA" maxlength="5">
                </div>
                <div class="form-gruppo">
                    <label for="cvv">CVV:</label>
                    <input type="password" id="cvv" name="cvv" placeholder="123" maxlength="4">
                </div>
            </div>
        </fieldset>

        <div class="form-azioni">
            <a href="<%= request.getContextPath() %>/carrello" class="btn-indietro">
                Torna al Carrello
            </a>

            <button type="submit" class="btn-invio">
                Conferma e Paga
            </button>
        </div>
    </form>
</div>

<script type="text/javascript" src="<%= request.getContextPath() %>/scripts/validazioneCheckout.js"></script>
</body>
</html>