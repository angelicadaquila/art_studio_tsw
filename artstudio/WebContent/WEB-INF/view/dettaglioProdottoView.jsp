<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Prodotto, model.Stampa, model.Commissione"%>
<!DOCTYPE html>
<html lang="it">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Dettaglio Prodotto</title>
	<link href="<%=request.getContextPath()%>/styles/base.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/componenti.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/form.css" rel="stylesheet" type="text/css">
	<link href="<%=request.getContextPath()%>/styles/catalogo.css" rel="stylesheet" type="text/css">
</head>
<body>

<jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />

<div class="container">

	<%
		String message = (String) request.getAttribute("message");
		if (message != null) {
	%>
		<p class="msg-successo"><%= message %></p>
	<%
		}

		String errorMessage = (String) request.getAttribute("errorMessage");
		if (errorMessage != null) {
	%>
		<p class="msg-errore"><%= errorMessage %></p>
	<%
		}
	%>

	<%
		Prodotto p = (Prodotto) request.getAttribute("prodotto");
		if (p != null) {
	%>
		<div class="dettaglio-prodotto-box">
			<div class="dettaglio-colonna-img">
				<% if (p.getImmagine() != null && !p.getImmagine().trim().isEmpty()) { %>
					<img src="<%= request.getContextPath() %>/immagine?action=show&id=<%= p.getIdProdotto() %>" 
						 alt="<%= p.getNome() %>" 
						 class="img-prodotto-dettaglio"
						 onerror="this.src='<%= request.getContextPath() %>/images/placeholder.png';">
				<% } else { %>
					<img src="<%= request.getContextPath() %>/images/placeholder.png" 
						 alt="Nessuna immagine disponibile" 
						 class="img-prodotto-dettaglio">
				<% } %>
			</div>

			<div class="dettaglio-colonna-info">	
				<h2><%= p.getNome() %></h2>
				<div class="form-gruppo">
					<% if (p instanceof Stampa) { %>
						<span class="badge badge-stampa">Stampa</span>
					<% } else if (p instanceof Commissione) { %>
						<span class="badge badge-commissione">Commissione</span>
					<% } %>
				</div>

				<p class="prezzo-dettaglio"><%= String.format("%.2f", p.getPrezzo()) %> &euro;</p>

				<p class="descrizione-dettaglio"><%= p.getDescrizione() %></p>

				<div class="dettagli-specifici">
					<% if (p instanceof Stampa) { 
						   Stampa s = (Stampa) p;
					%>
						<p><strong>Dimensione:</strong> <%= s.getDimensione() %></p>
					<% } else if (p instanceof Commissione) { 
						   Commissione c = (Commissione) p;
					%>
						<p><strong>Tempo di realizzazione:</strong> <%= c.getTempo() %> giorni</p>
					<% } %>
				</div>

				<hr class="separatore-dettagli">

				<form action="<%= request.getContextPath() %>/carrello" method="post" enctype="multipart/form-data" class="form-layout">
					<input type="hidden" name="azione" value="aggiungi">
					<input type="hidden" name="idProdotto" value="<%= p.getIdProdotto() %>">
					
					<% if (p instanceof Stampa) { %>
						<div class="form-gruppo">
							<label for="quantita">Quantità:</label>
							<input type="number" id="quantita" name="quantita" value="1" min="1" required>
						</div>
					<% } else if (p instanceof Commissione) { %>
						<input type="hidden" name="quantita" value="1">

						<div class="form-gruppo">
							<label for="descrizioneComm">Descrizione della Commissione:</label>
							<textarea id="descrizioneComm" name="descrizioneComm" rows="4" required placeholder="Descrivi qui come vuoi la tua opera personalizzata..."></textarea>
						</div>

						<div class="form-gruppo">
							<label for="immagineRef">Immagine di riferimento (opzionale):</label>
							<input type="file" id="immagineRef" name="immagineRef" accept="image/*">
						</div>
					<% } %>
					
					<div class="form-azioni">
						<input type="submit" class="btn-invio" value="Aggiungi al Carrello">
					</div>
				</form>

			</div>

		</div>

	<%
		} else {
	%>
		<p class="nessuno-trovato">Nessun prodotto selezionato o non trovato.</p>
	<%
		}
	%>

</div>

</body>
</html>