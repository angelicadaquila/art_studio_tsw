<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Carrello" %>
<%@ page import="model.ElementoCarrello" %>
<%@ page import="model.Indirizzo" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>ArtStudio - Checkout</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/componenti.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/form.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/catalogo.css">
    <script src="${pageContext.request.contextPath}/scripts/checkout.js"></script>
</head>
<body>

<div class="container">
    <jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />

    <h2>Riepilogo e Completa Ordine</h2>

    <%
        Carrello carrello = (Carrello) session.getAttribute("carrello");
        Indirizzo ind = (Indirizzo) request.getAttribute("indirizzo");
        String errore = (String) request.getAttribute("errore");
    %>

    <% if (errore != null) { %>
        <div style="color: #dc3545; background-color: #f8d7da; padding: 10px; border-radius: 4px; margin-bottom: 15px;">
            <%= errore %>
        </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/utente/checkout" method="POST">
        <fieldset style="border: 1px solid #ccc; padding: 15px; border-radius: 5px; margin-bottom: 20px;">
            <legend><strong>1. Indirizzo di Spedizione</strong></legend>
            
            <% if (ind != null) { %>
                <div style="margin-bottom: 15px;">
                    <input type="radio" id="tipo_profilo" name="idIndirizzo" value="<%= ind.getIdIndirizzo() %>" checked onclick="toggleIndirizzo(false)">
                    <label for="tipo_profilo">
                        <strong>Usa il mio indirizzo predefinito:</strong> <%= ind.getVia() %>, <%= ind.getCivico() %> - <%= ind.getCitta() %> (<%= ind.getRegione() %>)
                    </label>
                </div>
            <% } %>

            <div style="margin-bottom: 10px;">
                <input type="radio" id="tipo_nuovo" name="idIndirizzo" value="0" <% if (ind == null) { %>checked<% } %> onclick="toggleIndirizzo(true)">
                <label for="tipo_nuovo"><strong>Spedisci a un altro indirizzo (solo per questo ordine)</strong></label>
            </div>

            <div id="boxNuovoIndirizzo" style="margin-top: 15px; padding-left: 20px; display: <% if (ind == null) { %>block<% } else { %>none<% } %>;">
                <div class="form-gruppo" style="margin-bottom: 10px;">
                    <label for="via">Via/Piazza:</label>
                    <input type="text" id="via" name="via" placeholder="Es. Via Roma">
                </div>
                <div class="form-gruppo" style="margin-bottom: 10px;">
                    <label for="civico">Civico:</label>
                    <input type="text" id="civico" name="civico" placeholder="Es. 10">
                </div>
                <div class="form-gruppo" style="margin-bottom: 10px;">
                    <label for="citta">Città:</label>
                    <input type="text" id="citta" name="citta" placeholder="Es. Milano">
                </div>
                <div class="form-gruppo" style="margin-bottom: 10px;">
                    <label for="regione">Regione:</label>
                    <input type="text" id="regione" name="regione" placeholder="Es. Lombardia">
                </div>
            </div>
        </fieldset>

        <fieldset style="border: 1px solid #ccc; padding: 15px; border-radius: 5px; margin-bottom: 20px;">
    	<legend><strong>2. Dati di Pagamento (Carta di Credito / Debito)</strong></legend>
    
    		<div class="form-gruppo" style="margin-bottom: 12px;">
        		<label for="intestatario">Intestatario Carta:</label>
        		<input type="text" id="intestatario" name="intestatario" placeholder="Es. Mario Rossi" style="width: 100%; padding: 8px;" >
    		</div>

    		<div class="form-gruppo" style="margin-bottom: 12px;">
        		<label for="numeroCarta">Numero Carta:</label>
        		<input type="text" id="numeroCarta" name="numeroCarta" placeholder="1234 5678 9012 3456" maxlength="19" style="width: 100%; padding: 8px;">
    		</div>

    		<div style="display: flex; gap: 15px;">
        		<div class="form-gruppo" style="flex: 1;">
            		<label for="scadenza">Data di Scadenza (MM/AA):</label>
            		<input type="text" id="scadenza" name="scadenza" placeholder="MM/AA" maxlength="5" style="width: 100%; padding: 8px;" >
        		</div>
        		<div class="form-gruppo" style="flex: 1;">
            		<label for="cvv">CVV / CVC:</label>
            		<input type="password" id="cvv" name="cvv" placeholder="123" maxlength="4" style="width: 100%; padding: 8px;" >
        		</div>
    		</div>
		</fieldset>

        <div style="text-align: right; margin-top: 20px;">
           <h3>Totale da Pagare: 
			<% 
    			double totale = 0.0;
   				if (carrello != null) {
        			totale = carrello.getTotale();
    			}
			%>
			<%= String.format("%.2f", totale) %> €</h3>
            <a href="${pageContext.request.contextPath}/carrello" class="btn-indietro" style="text-decoration: none; padding: 10px 15px; display: inline-block; margin-right: 10px;">
                Torna al Carrello
            </a>

            <button type="submit" style="padding: 10px 20px; background-color: #0d6efd; color: white; border: none; border-radius: 4px; font-size: 1rem; font-weight: bold; cursor: pointer;">
                Conferma e Paga
            </button>
        </div>
    </form>
</div>
</body>
</html>