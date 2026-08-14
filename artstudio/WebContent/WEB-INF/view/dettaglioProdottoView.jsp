<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Prodotto, model.Stampa, model.Commissione, model.Carrello, model.ElementoCarrello, java.util.*"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="<%=request.getContextPath()%>/styles/ProductStyle.css" rel="stylesheet" type="text/css">
	<title>Dettaglio Prodotto</title>
</head>
<body>

	<a href="catalogo?tipo=tutti">&lt; Torna al Catalogo</a>

	<%
		String message = (String) request.getAttribute("message");
		if (message != null) {
	%>
		<p style="color: green;"><%= message %></p>
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
					Stampa
				<% } else if (prodotto instanceof Commissione) { %>
					Commissione
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
				<img alt="img" width="80" height="80" src="image?action=show&id=<%= prodotto.getIdProdotto() %>" onerror="this.src='images/placeholder.png';">
			</td>
		</tr>
	</table>

	<h2>Aggiungi al Carrello</h2>
	<form action="dettaglioProdotto" method="post">
		<input type="hidden" name="action" value="addC">
		<input type="hidden" name="id" value="<%= prodotto.getIdProdotto() %>">
		
	<label for="quantita">Quantit&agrave;:</label><br>
	<% if (prodotto instanceof Stampa) { %>
    	<input type="number" id="quantita" name="quantita" value="1" min="1" required><br><br>
	<% } else { %>
    	<input type="number" id="quantita" name="quantita" value="1" min="1" max="1" readonly><br>
	<% } %>
		<input type="submit" value="Aggiungi al Carrello">
	</form>
	<%
		} else {
	%>
		<p>Nessun prodotto selezionato o non trovato.</p>
	<%
		}
	%>

	<% 
		Carrello c = (Carrello) session.getAttribute("c");
		if (c == null) {
			c = new Carrello();
		}
	%>
	<h2>Carrello Attuale</h2>
	<table border="1">
		<tr>
			<th>Nome Prodotto</th>
			<th>Quantit&agrave;</th>
			<th>Prezzo Totale Parziale</th>
			<th>Azione</th>
		</tr>
		<% 
			List<ElementoCarrello> elementi = c.getElementi(); 	
			if (elementi != null && !elementi.isEmpty()) {
				for (ElementoCarrello item : elementi) {
		%>
		<tr>
			<td><%= item.getProdotto().getNome() %></td>
			<td><%= item.getQuantita() %></td>
			<td><%= String.format("%.2f", item.getTotale()) %> &euro;</td>
			<td>
				<a href="carrello?action=delete&id=<%= item.getProdotto().getIdProdotto() %>">Rimuovi</a>
			</td>
		</tr>
		<% 
				}
			} else {
		%>
		<tr>
			<td colspan="4">Il carrello &egrave; vuoto</td>
		</tr>
		<% 
			} 
		%>
	</table>

	<% if (elementi != null && !elementi.isEmpty()) { %>
		<p><strong>Totale Complessivo: <%= String.format("%.2f", c.getTotale()) %> &euro;</strong></p>
		<a href="carrello">Vai alla gestione completa del carrello</a>
	<% } %>

</body>
</html>