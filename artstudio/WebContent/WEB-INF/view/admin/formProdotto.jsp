<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title><%= request.getAttribute("titoloPagina") %></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/componenti.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/catalogo.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/form.css">
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/formProdotto.js"></script>
</head>
<body>

<jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />

<div class="form-container-admin">

    <h2><%= request.getAttribute("titoloPagina") %></h2>

    <form action="<%= request.getContextPath() %>/admin/prodotti?action=salva" method="post" enctype="multipart/form-data" class="form-layout-admin">

        <input type="hidden" name="idProdotto" value="${idProdotto}">

        <div class="form-gruppo">
            <label for="nome">Nome Prodotto:</label>
            <input type="text" id="nome" name="nome" value="${nome}" required>
        </div>

        <div class="form-gruppo">
            <label for="descrizione">Descrizione:</label>
            <textarea id="descrizione" name="descrizione" rows="3">${descrizione}</textarea>
        </div>

        <div class="form-gruppo">
            <label for="prezzo">Prezzo (&euro;):</label>
            <input type="text" id="prezzo" name="prezzo" value="${prezzo}" required>
        </div>

        <div class="form-gruppo">
            <label for="tipoProdotto">Tipo Prodotto:</label>
            
            <% if ((Boolean) request.getAttribute("isModifica")) { %>
                <select id="tipoProdottoSelect" disabled>
                    <option value="stampa" <% if ("stampa".equals(request.getAttribute("tipoProdotto"))) { out.print("selected"); } %>>Stampa</option>
                    <option value="commissione" <% if ("commissione".equals(request.getAttribute("tipoProdotto"))) { out.print("selected"); } %>>Commissione</option>
                </select>
                <input type="hidden" name="tipoProdotto" value="${tipoProdotto}">
            <% } else { %>
                <select id="tipoProdotto" name="tipoProdotto" onchange="gestisciCampiTipo()" required>
                    <option value="">Seleziona Tipo</option>
                    <option value="stampa" <% if ("stampa".equals(request.getAttribute("tipoProdotto"))) { out.print("selected"); } %>>Stampa</option>
                    <option value="commissione" <% if ("commissione".equals(request.getAttribute("tipoProdotto"))) { out.print("selected"); } %>>Commissione</option>
                </select>
            <% } %>
        </div>

        <div id="campiStampa" class="box-stampa-admin" style="display: <% if ("stampa".equals(request.getAttribute("tipoProdotto"))) { out.print("block"); } else { out.print("none"); } %>;">
            <div class="form-gruppo">
                <label for="dimensione">Dimensione:</label>
                <input type="text" id="dimensione" name="dimensione" value="${dimensione}">
            </div>
            <div class="form-gruppo" style="margin-top: 10px;">
                <label for="quantita">Quantita:</label>
                <input type="number" id="quantita" name="quantita" value="${quantita}">
            </div>
        </div>

        <div id="campiCommissione" class="box-commissione-admin" style="display: <% if ("commissione".equals(request.getAttribute("tipoProdotto"))) { out.print("block"); } else { out.print("none"); } %>;">
            <div class="form-gruppo">
                <label for="tempo">Tempo di realizzazione (giorni):</label>
                <input type="text" id="tempo" name="tempo" value="${tempo}">
            </div>
        </div>

        <div class="form-gruppo">
            <label for="immagine">Seleziona Immagine dal PC:</label>
            <input type="file" id="immagine" name="immagine" accept="image/*">
            
            <% if ((Boolean) request.getAttribute("isModifica")) { %>
                <input type="hidden" name="immagineVecchia" value="${immagineAttuale}">
                <% if (request.getAttribute("immagineAttuale") != null && !((String)request.getAttribute("immagineAttuale")).trim().isEmpty()) { %>
                    <p class="testo-info-file">
                        File attuale: <strong>${immagineAttuale}</strong> (seleziona un file solo se desideri sostituirlo).
                    </p>
                <% } %>
            <% } %>
        </div>

        <div class="form-gruppo">
            <label for="disponibile" class="label-inline">Disponibile subito:</label>
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
            <a href="${pageContext.request.contextPath}/admin/prodotti" class="btn-indietro">Annulla</a>
        </div>

    </form>

</div>

</body>
</html>