<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Carrello" %>
<%@ page import="model.ElementoCarrello" %>
<%@ page import="model.Indirizzo" %>
<%@ page import="java.util.List" %>

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
        List<Indirizzo> listaIndirizzi = (List<Indirizzo>) request.getAttribute("listaIndirizzi");
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

            <% if (listaIndirizzi != null && !listaIndirizzi.isEmpty()) { %>
                <p>Seleziona un indirizzo salvato:</p>
                <% for (int i = 0; i < listaIndirizzi.size(); i++) { 
                    Indirizzo ind = listaIndirizzi.get(i);
                %>
                    <div style="margin-bottom: 10px;">
                        <input type="radio" id="ind_<%= ind.getIdIndirizzo() %>" name="idIndirizzo" value="<%= ind.getIdIndirizzo() %>" <%= (i == 0) ? "checked" : "" %>>
                        <label for="ind_<%= ind.getIdIndirizzo() %>">
                            <%= ind.getVia() %>, <%= ind.getCivico() %> - <%= ind.getCitta() %> (<%= ind.getRegione() %>)
                        </label>
                    </div>
                <% } %>
            <% } else { %>
                <p style="color: #dc3545;">Non hai ancora salvato alcun indirizzo di spedizione.</p>
            <% } %>

            <div style="margin-top: 15px;">
                <a href="${pageContext.request.contextPath}/utente/nuovoIndirizzo" class="btn-opzione" style="text-decoration: none; padding: 6px 12px; background-color: #28a745; color: white; border-radius: 4px;">
                    + Aggiungi Nuovo Indirizzo
                </a>
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
            <h3>Totale da Pagare: <%= String.format("%.2f", (carrello != null) ? carrello.getTotale() : 0.0) %> €</h3>
            
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