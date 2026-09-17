<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Utente" %>
<%@ page import="model.Indirizzo" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="it">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="<%=request.getContextPath()%>/styles/base.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/componenti.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/form.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/ordini.css" rel="stylesheet" type="text/css">
	<title>Profilo Utente</title>
</head>
<body>

<jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />

<div class="container">

	<h2>Il Mio Profilo</h2>

	<%
		Utente utente = (Utente) session.getAttribute("utente");
		List<Indirizzo> listaIndirizzi = (List<Indirizzo>) request.getAttribute("listaIndirizzi");

		if (utente != null) {
	%>
		<h3>Dati Personali</h3>
		<div class="form-gruppo"><p><strong>Nome:</strong> <%= utente.getNome() %></p></div>
		<div class="form-gruppo"><p><strong>Cognome:</strong> <%= utente.getCognome() %></p></div>
		<div class="form-gruppo"><p><strong>Email:</strong> <%= utente.getEmail() %></p></div>

		<hr class="separatore-dettagli">

		<h3>Indirizzo Predefinito di Spedizione</h3>

		<%
    		Indirizzo ind = (Indirizzo) request.getAttribute("indirizzo");
		%>

		<% if (ind != null) { %>
    		<div class="campi">
        		<p><strong>Via/Piazza:</strong> <%= ind.getVia() %>, <%= ind.getCivico() %></p>
        		<p><strong>Città:</strong> <%= ind.getCitta() %></p>
        		<p><strong>Regione:</strong> <%= ind.getRegione() %></p>
        		<div class="form-azioni">
            		<a href="<%=request.getContextPath()%>/utente/modificaIndirizzo" class="btn-opzione">Modifica Indirizzo</a>
        		</div>
    		</div>
		<% } else { %>
    		<p class="testo-dettaglio-grigio">Nessun indirizzo salvato.</p>
		<% } %>

	<% } else { %>
		<p id="errore" class="msg-errore">Nessun utente trovato in sessione.</p>
	<% } %>

	<div class="form-azioni">
		<a href="<%=request.getContextPath()%>/logout" class="btn-cancella">Logout</a>
	</div>

</div>

</body>
</html>