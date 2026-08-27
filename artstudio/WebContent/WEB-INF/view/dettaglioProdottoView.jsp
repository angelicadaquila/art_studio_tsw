<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Prodotto, model.Stampa, model.Commissione"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="<%=request.getContextPath()%>/styles/base.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/componenti.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/form.css" rel="stylesheet" type="text/css">
	<title>Dettaglio Prodotto</title>
</head>
<body>

	<a href="catalogo?tipo=tutti" class="btn-indietro"> Torna al Catalogo</a>
	<%
		String message = (String) request.getAttribute("message");
		if (message != null) {
	%>
		<p style="color: green; font-weight: bold;"><%= message %></p>
	<%
		}

		String errorMessage = (String) request.getAttribute("errorMessage");
		if (errorMessage != null) {
	%>
		<p style="color: red;"><%= errorMessage %></p>
	<%
		}
	%>

	<h2>Dettaglio Prodotto</h2>
	<%
		Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
		if (prodotto != null) {
	%>
	<table border="1">
		<tr>
			<th>ID</th>
			<th>Nome</th>
			<th>Descrizione</th>
			<th>Prezzo</th>
			<th>Tipo</th>
			<th>Dettagli Specifici</th>
			<th>Immagine</th>
		</tr>
		<tr>
			<td><%= prodotto.getIdProdotto() %></td>
			<td><%= prodotto.getNome() %></td>
			<td><%= prodotto.getDescrizione() %></td>
			<td><%= String.format("%.2f", prodotto.getPrezzo()) %> &euro;</td>
			<td>
				<% if (prodotto instanceof Stampa) { %>
					<span class="badge badge-stampa">Stampa</span>
				<% } else if (prodotto instanceof Commissione) { %>
					<span class="badge badge-commissione">Commissione</span>
				<% } %>
			</td>
			<td>
				<% if (prodotto instanceof Stampa) { 
					   Stampa s = (Stampa) prodotto;
				%>
					Dimensione: <%= s.getDimensione() %>
				<% } else if (prodotto instanceof Commissione) { 
					   Commissione c = (Commissione) prodotto;
				%>
					Tempo: <%= c.getTempo() %> giorni
				<% } %>
			</td>
			<td>
    			<img src="<%= request.getContextPath() %>/image?action=show&code=<%= prodotto.getIdProdotto() %>" 
     				alt="<%= prodotto.getNome() %>" 
    				width="80" 
     				height="80" 
     				style="object-fit: cover;"
     				onerror="this.src='<%= request.getContextPath() %>/images/placeholder.png';">
			</td>
		</tr>
	</table>

	<h2>Aggiungi al Carrello</h2>
	<form action="dettaglioProdotto" method="post">
		<input type="hidden" name="action" value="aggiungiC">
		<input type="hidden" name="id" value="<%= prodotto.getIdProdotto() %>">
		
		<div class="form-gruppo">
			<label for="quantita">Quantit&agrave;:</label>
			<% if (prodotto instanceof Stampa) { %>
				<input type="number" id="quantita" name="quantita" value="1" min="1" required>
			<% } else { %>
				<input type="number" id="quantita" name="quantita" value="1" min="1" max="1" readonly>
			<% } %>
		</div>
		
		<div class="form-azioni">
			<input type="submit" class="btn-invio" value="Aggiungi al Carrello">
		</div>
	</form>
	<%
		} else {
	%>
		<p class="nessuno-trovato">Nessun prodotto selezionato o non trovato.</p>
	<%
		}
	%>

</body>
</html>