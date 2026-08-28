<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="<%=request.getContextPath()%>/styles/base.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/componenti.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/form.css" rel="stylesheet" type="text/css">
	<title>Login</title>
</head>
<body>

	<a href="<%=request.getContextPath()%>/catalogo?tipo=tutti" class="btn-indietro"> Torna al Catalogo</a>

	<h2>Accedi al tuo Account</h2>
	<%
		String errore = (String) request.getAttribute("errore");
		if (errore != null) {
	%>
		<p id="errore" style="color: red; font-weight: bold;"><%= errore %></p>
	<%
		} else {
	%>
		<p id="errore" style="display: none; color: red; font-weight: bold;"></p>
	<%
		}
	%>

	<form id="formLogin" action="<%=request.getContextPath()%>/login" method="post">
		
		<div class="form-gruppo">
			<label for="email">Email:</label>
			<input type="email" id="email" name="email" required>
		</div>

		<div class="form-gruppo">
			<label for="password">Password:</label>
			<input type="password" id="password" name="password" required>
		</div>

		<div class="form-azioni">
			<input type="submit" class="btn-invio" value="Accedi">
		</div>
	</form>

	<p style="margin-top: 15px;">
		Non hai ancora un account? 
		<a href="<%=request.getContextPath()%>/registrazione">Registrati</a>
	</p>

	<script src="<%=request.getContextPath()%>/scripts/validazioneLogin.js"></script>
</body>
</html>