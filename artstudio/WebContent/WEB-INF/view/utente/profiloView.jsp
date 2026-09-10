<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Utente" %>
<%@ page import="model.Indirizzo" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="<%=request.getContextPath()%>/styles/base.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/componenti.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/form.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/catalogo.css" rel="stylesheet" type="text/css">
	<title>Profilo Utente</title>
</head>
<body>
<div class="container">
<jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />
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

		<hr style="margin: 20px 0;">

		<h3>Indirizzo Predefinito di Spedizione</h3>

		<%
    		Indirizzo ind = (Indirizzo) request.getAttribute("indirizzo");
		%>

		<% if (ind != null) { %>
    		<div class="form-gruppo" style="border: 1px solid #ccc; padding: 15px; margin-bottom: 15px; border-radius: 5px;">
        		<p><strong>Via/Piazza:</strong> <%= ind.getVia() %>, <%= ind.getCivico() %></p>
        		<p><strong>Città:</strong> <%= ind.getCitta() %></p>
        		<p><strong>Regione:</strong> <%= ind.getRegione() %></p>
        		<div style="margin-top: 10px;">
            		<a href="<%=request.getContextPath()%>/utente/modificaIndirizzo" class="btn-opzione" style="text-decoration: none; padding: 6px 12px; background-color: #0d6efd; color: white; border-radius: 4px;">Modifica Indirizzo</a>
        		</div>
    		</div>
		<% } else { %>
    		<p style="color: gray;">Nessun indirizzo salvato.</p>
		<% } %>

	<% } else { %>
		<p id="errore" style="color: red; font-weight: bold;">Nessun utente trovato in sessione.</p>
	<% } %>

	<div class="form-azioni" style="margin-top: 20px;">
		<a href="<%=request.getContextPath()%>/logout" class="btn-indietro" style="background-color: #dc3545; text-decoration: none; display: inline-block;">Logout</a>
	</div>
</div>
</body>
</html>