<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= request.getAttribute("titoloPagina") %></title>
    <link href="<%=request.getContextPath()%>/styles/base.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/styles/componenti.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/styles/form.css" rel="stylesheet" type="text/css">
</head>
<body>

<jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />

<div class="form-container-admin">

    <h2><%= request.getAttribute("titoloPagina") %></h2>

    <form id="formProdotto" action="<%= request.getContextPath() %>/admin/prodotti?action=salva" method="post" enctype="multipart/form-data" class="form-layout">

        <input type="hidden" name="idProdotto" value="<%= request.getAttribute("idProdotto") %>">

        <div class="form-gruppo">
            <label for="nome">Nome Prodotto:</label>
            <input type="text" id="nome" name="nome" value="<%= request.getAttribute("nome") %>" required>
        </div>

        <div class="form-gruppo">
            <label for="descrizione">Descrizione:</label>
            <textarea id="descrizione" name="descrizione" rows="3"><%= request.getAttribute("descrizione") %></textarea>
        </div>

        <div class="form-gruppo">
            <label for="prezzo">Prezzo (&euro;):</label>
            <input type="text" id="prezzo" name="prezzo" value="<%= request.getAttribute("prezzo") %>" required>
        </div>

        <div class="form-gruppo">
            <label for="tipoProdotto">Tipo Prodotto:</label>
            
            <% if ((Boolean) request.getAttribute("isModifica")) { %>
                <select id="tipoProdottoSelect" disabled>
                    <option value="stampa" <% if ("stampa".equals(request.getAttribute("tipoProdotto"))) { out.print("selected"); } %>>Stampa</option>
                    <option value="commissione" <% if ("commissione".equals(request.getAttribute("tipoProdotto"))) { out.print("selected"); } %>>Commissione</option>
                </select>
                <input type="hidden" name="tipoProdotto" value="<%= request.getAttribute("tipoProdotto") %>">
            <% } else { %>
                <select id="tipoProdotto" name="tipoProdotto" onchange="gestisciCampiTipo()" required>
                    <option value="">Seleziona Tipo</option>
                    <option value="stampa" <% if ("stampa".equals(request.getAttribute("tipoProdotto"))) { out.print("selected"); } %>>Stampa</option>
                    <option value="commissione" <% if ("commissione".equals(request.getAttribute("tipoProdotto"))) { out.print("selected"); } %>>Commissione</option>
                </select>
            <% } %>
        </div>

        <div id="campiStampa" class="box-stampa-admin">
            <div class="form-gruppo">
                <label for="dimensione">Dimensione:</label>
                <input type="text" id="dimensione" name="dimensione" value="<%= request.getAttribute("dimensione") %>">
            </div>
            <div class="form-gruppo">
                <label for="quantita">Quantita:</label>
                <input type="number" id="quantita" name="quantita" value="<%= request.getAttribute("quantita") %>">
            </div>
        </div>

        <div id="campiCommissione" class="box-commissione-admin">
            <div class="form-gruppo">
                <label for="tempo">Tempo di realizzazione (giorni):</label>
                <input type="text" id="tempo" name="tempo" value="<%= request.getAttribute("tempo") %>">
            </div>
        </div>

        <div class="form-gruppo">
            <label for="immagine">Seleziona Immagine dal PC:</label>
            <input type="file" id="immagine" name="immagine" accept="image/*">
            
            <% if ((Boolean) request.getAttribute("isModifica")) { %>
                <input type="hidden" name="immagineVecchia" value="<%= request.getAttribute("immagineAttuale") %>">
                <% if (request.getAttribute("immagineAttuale") != null && !((String)request.getAttribute("immagineAttuale")).trim().isEmpty()) { %>
                    <p class="testo-info-file">
                        File attuale: <strong><%= request.getAttribute("immagineAttuale") %></strong> (seleziona un file solo se desideri sostituirlo).
                    </p>
                <% } %>
            <% } %>
        </div>

        <div class="form-gruppo">
            <label for="disponibile">Disponibile subito:</label>
            <input type="checkbox" id="disponibile" name="disponibile" value="true" <% if ((Boolean) request.getAttribute("disponibile")) { out.print("checked"); } %>>
        </div>

        <div class="form-azioni">
            <button type="submit" class="btn-invio">
                <% if ((Boolean) request.getAttribute("isModifica")) { %>
                    Salva Modifiche
                <% } else { %>
                    Aggiungi Prodotto
                <% } %>
            </button>
            <a href="<%= request.getContextPath() %>/admin/prodotti" class="btn-indietro">Annulla</a>
        </div>

    </form>

</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/formProdotto.js" defer></script>
</body>
</html>