<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="<%=request.getContextPath()%>/styles/base.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/componenti.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/form.css" rel="stylesheet" type="text/css">
	<title>Registrazione Utente</title>
</head>
<body>

	<a href="<%=request.getContextPath()%>/catalogo?tipo=tutti" class="btn-indietro"> Torna al Catalogo</a>

	<%
		String errore = (String) request.getAttribute("errore");
		if (errore != null) {
	%>
		<p style="color: red; font-weight: bold;"><%= errore %></p>
	<%
		}
	%>

	<p id="errore" style="display: none; color: red; font-weight: bold;"></p>

	<h2>Registrazione Utente</h2>

	<form id="formRegistrazione" action="<%=request.getContextPath()%>/registrazione" method="post">
		
		<h3>Dati Personali</h3>
		
		<div class="form-gruppo">
			<label for="nome">Nome:</label>
			<input type="text" id="nome" name="nome" required>
		</div>

		<div class="form-gruppo">
			<label for="cognome">Cognome:</label>
			<input type="text" id="cognome" name="cognome" required>
		</div>

		<div class="form-gruppo">
			<label for="email">Email:</label>
			<input type="email" id="email" name="email" required>
		</div>

		<div class="form-gruppo">
			<label for="password">Password:</label>
			<input type="password" id="password" name="password" required>
		</div>

		<div class="form-gruppo">
			<label for="confermaPassword">Conferma Password:</label>
			<input type="password" id="confermaPassword" name="confermaPassword" required>
		</div>

		<h3>Indirizzo di Spedizione</h3>

		<div class="form-gruppo">
			<label for="via">Via / Piazza:</label>
			<input type="text" id="via" name="via" required>
		</div>

		<div class="form-gruppo">
			<label for="civico">Numero Civico:</label>
			<input type="text" id="civico" name="civico" required>
		</div>

		<div class="form-gruppo">
			<label for="citta">Citt&agrave;:</label>
			<input type="text" id="citta" name="citta" required>
		</div>

		<div class="form-gruppo">
			<label for="regione">Regione:</label>
			<input type="text" id="regione" name="regione" required>
		</div>

		<div class="form-azioni">
			<input type="submit" class="btn-invio" value="Registrati">
		</div>
	</form>

	<script src="<%=request.getContextPath()%>/scripts/validazioneRegistrazione.js"></script>
</body>
</html>