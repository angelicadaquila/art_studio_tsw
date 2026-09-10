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
                    <input type="radio" id="tipo_profilo" name="tipoIndirizzo" value="PROFILO" checked onclick="toggleIndirizzo(false)">
                    <label for="tipo_profilo">
                        <strong>Usa il mio indirizzo predefinito:</strong> <%= ind.getVia() %>, <%= ind.getCivico() %> - <%= ind.getCitta() %> (<%= ind.getRegione() %>)
                    </label>
                    <input type="hidden" name="idIndirizzoProfilo" value="<%= ind.getIdIndirizzo() %>">
                </div>
            <% } %>

            <div style="margin-bottom: 10px;">
                <input type="radio" id="tipo_nuovo" name="tipoIndirizzo" value="NUOVO" <% if (ind == null) { %>checked<% } %> onclick="toggleIndirizzo(true)">
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
            <legend><strong>2. Metodo di Pagamento</strong></legend>
            
            <div style="margin-bottom: 10px;">
                <input type="radio" id="carta" name="metodoPagamento" value="Carta di Credito" checked>
                <label for="carta">Carta di Credito / Debito</label>
            </div>

            <div style="margin-bottom: 10px;">
                <input type="radio" id="paypal" name="metodoPagamento" value="PayPal">
                <label for="paypal">PayPal</label>
            </div>

            <div>
                <input type="radio" id="bonifico" name="metodoPagamento" value="Bonifico">
                <label for="bonifico">Bonifico Bancario</label>
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

<script>
function toggleIndirizzo(mostra) {
    var box = document.getElementById("boxNuovoIndirizzo");
    if (mostra) {
        box.style.display = "block";
    } else {
        box.style.display = "none";
    }
}
</script>

</body>
</html>