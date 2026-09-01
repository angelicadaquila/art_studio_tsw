<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="<%=request.getContextPath()%>/styles/base.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/componenti.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/form.css" rel="stylesheet" type="text/css">
	<title>Aggiungi Indirizzo</title>
</head>
<body>

	<a href="<%=request.getContextPath()%>/utente/profilo" class="btn-indietro">Annulla e Torna al Profilo</a>

	<h2>Aggiungi un Nuovo Indirizzo</h2>

	<%
		String errore = (String) request.getAttribute("errore");
		if (errore != null) {
	%>
		<p id="errore" style="color: red; font-weight: bold;"><%= errore %></p>
	<% } %>

	<form id="formIndirizzo" action="<%=request.getContextPath()%>/utente/nuovoIndirizzo" method="post">
		
		<div class="form-gruppo">
			<label for="via">Via/Piazza:</label>
			<input type="text" id="via" name="via" required>
		</div>

		<div class="form-gruppo">
			<label for="civico">Numero Civico:</label>
			<input type="text" id="civico" name="civico" required>
		</div>

		<div class="form-gruppo">
			<label for="citta">Città:</label>
			<input type="text" id="citta" name="citta" required>
		</div>

		<div class="form-gruppo">
			<label for="regione">Regione:</label>
			<input type="text" id="regione" name="regione" required>
		</div>

		<div class="form-azioni">
			<input type="submit" class="btn-invio" value="Salva Indirizzo">
		</div>
	</form>

</body>
</html>