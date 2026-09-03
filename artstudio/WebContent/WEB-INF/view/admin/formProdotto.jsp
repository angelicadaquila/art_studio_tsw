<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Prodotto" %>
<%@ page import="model.Stampa" %>
<%@ page import="model.Commissione" %>

<%
    Prodotto prodotto = (Prodotto) request.getAttribute("prodotto");
    
    boolean isModifica = false;
    if (prodotto != null) {
        isModifica = true;
    }
    
    String titoloPagina = "";
    if (isModifica) {
        titoloPagina = "Modifica Prodotto";
    } else {
        titoloPagina = "Aggiungi Nuovo Prodotto";
    }
    
    String idProdotto = "";
    if (isModifica) {
        idProdotto = String.valueOf(prodotto.getIdProdotto());
    }
    
    String nome = "";
    if (isModifica) {
        if (prodotto.getNome() != null) {
            nome = prodotto.getNome();
        }
    }
    
    String descrizione = "";
    if (isModifica) {
        if (prodotto.getDescrizione() != null) {
            descrizione = prodotto.getDescrizione();
        }
    }
    
    double prezzo = 0.0;
    if (isModifica) {
        prezzo = prodotto.getPrezzo();
    }
    
    boolean disponibile = true;
    if (isModifica) {
        disponibile = prodotto.isDisponibile();
    }
    
    String immagineAttuale = "";
    if (isModifica) {
        if (prodotto.getImmagine() != null) {
            immagineAttuale = prodotto.getImmagine();
        }
    }
    
    String tipoProdotto = "";
    String dimensione = "";
    int quantita = 0;
    String tempo = "";
    
    if (isModifica) {
        if (prodotto instanceof Stampa) {
            tipoProdotto = "stampa";
            Stampa s = (Stampa) prodotto;
            if (s.getDimensione() != null) {
                dimensione = s.getDimensione();
            }
            quantita = s.getQuantita();
        } else if (prodotto instanceof Commissione) {
            tipoProdotto = "commissione";
            Commissione c = (Commissione) prodotto;
            if (c.getTempo() != null) {
                tempo = c.getTempo();
            }
        }
    }
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title><%= titoloPagina %></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/base.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/componenti.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/catalogo.css">
    
    <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/formProdotto.js"></script>
</head>
<body>

<jsp:include page="/WEB-INF/view/barraSuperioreView.jsp" />

<div style="padding: 20px; max-width: 600px; margin: 30px auto; background-color: #ffffff; border-radius: 8px; border: 1px solid #ccc;">

    <h2><%= titoloPagina %></h2>

    <form action="<%= request.getContextPath() %>/admin/prodotti?action=salva" method="post" enctype="multipart/form-data" style="display: flex; flex-direction: column; gap: 15px; margin-top: 20px;">

        <input type="hidden" name="idProdotto" value="<%= idProdotto %>">

        <div>
            <label for="nome" style="font-weight: bold;">Nome Prodotto:</label><br>
            <input type="text" id="nome" name="nome" value="<%= nome %>" required style="width: 100%; padding: 8px; margin-top: 5px; box-sizing: border-box;">
        </div>

        <div>
            <label for="descrizione" style="font-weight: bold;">Descrizione:</label><br>
            <textarea id="descrizione" name="descrizione" rows="3" style="width: 100%; padding: 8px; margin-top: 5px; box-sizing: border-box;"><%= descrizione %></textarea>
        </div>

        <div>
            <label for="prezzo" style="font-weight: bold;">Prezzo (&euro;):</label><br>
            <%
                String prezzoFormattato = "";
                if (isModifica) {
                    prezzoFormattato = String.format(java.util.Locale.US, "%.2f", prezzo);
                }
            %>
            <input type="number" step="0.01" id="prezzo" name="prezzo" value="<%= prezzoFormattato %>" required style="width: 100%; padding: 8px; margin-top: 5px; box-sizing: border-box;">
        </div>

        <div>
            <label for="tipoProdotto" style="font-weight: bold;">Tipo Prodotto:</label><br>
            <%
                String disabledAttr = "";
                if (isModifica) {
                    disabledAttr = "disabled";
                }
            %>
            <select id="tipoProdotto" name="tipoProdotto" onchange="gestisciCampiTipo()" <%= disabledAttr %> style="width: 100%; padding: 8px; margin-top: 5px; box-sizing: border-box;">
                <option value="">Seleziona Tipo</option>
                <option value="stampa" <% if ("stampa".equals(tipoProdotto)) { out.print("selected"); } %>>Stampa</option>
                <option value="commissione" <% if ("commissione".equals(tipoProdotto)) { out.print("selected"); } %>>Commissione</option>
            </select>
            
            <% if (isModifica) { %>
                <input type="hidden" name="tipoProdotto" value="<%= tipoProdotto %>">
            <% } %>
        </div>

        <div id="campiStampa" style="display: none; border-left: 3px solid #0d6efd; padding-left: 10px;">
            <div>
                <label for="dimensione" style="font-weight: bold;">Dimensione (es. A4, 50x70):</label><br>
                <input type="text" id="dimensione" name="dimensione" value="<%= dimensione %>" style="width: 100%; padding: 8px; margin-top: 5px; box-sizing: border-box;">
            </div>
            <div style="margin-top: 10px;">
                <label for="quantita" style="font-weight: bold;">Quantita:</label><br>
                <input type="number" id="quantita" name="quantita" value="<%= quantita %>" style="width: 100%; padding: 8px; margin-top: 5px; box-sizing: border-box;">
            </div>
        </div>

        <div id="campiCommissione" style="display: none; border-left: 3px solid #ffc107; padding-left: 10px;">
            <div>
                <label for="tempo" style="font-weight: bold;">Tempo di realizzazione (giorni / testo):</label><br>
                <input type="text" id="tempo" name="tempo" value="<%= tempo %>" style="width: 100%; padding: 8px; margin-top: 5px; box-sizing: border-box;">
            </div>
        </div>

        <div>
            <label for="immagine" style="font-weight: bold;">Seleziona Immagine dal PC:</label><br>
            <input type="file" id="immagine" name="immagine" accept="image/*" style="margin-top: 5px;">
            
            <% if (isModifica) { %>
                <input type="hidden" name="immagineVecchia" value="<%= immagineAttuale %>">
                <% if (!immagineAttuale.trim().isEmpty()) { %>
                    <p style="font-size: 0.85em; color: #555; margin-top: 5px;">
                        File attuale: <strong><%= immagineAttuale %></strong> (seleziona un file solo se desideri sostituirlo).
                    </p>
                <% } %>
            <% } %>
        </div>

        <div>
            <label for="disponibile" style="font-weight: bold;">Disponibile subito:</label>
            <input type="checkbox" id="disponibile" name="disponibile" value="true" <% if (disponibile) { out.print("checked"); } %> style="margin-left: 10px;">
        </div>

        <div style="display: flex; gap: 10px; margin-top: 15px;">
            <button type="submit" style="background-color: #28a745; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; font-weight: bold;">
                <% if (isModifica) { %>
                    Salva Modifiche
                <% } else { %>
                    Aggiungi Prodotto
                <% } %>
            </button>
            <a href="<%= request.getContextPath() %>/admin/prodotti" style="background-color: #6c757d; color: white; padding: 10px 15px; text-decoration: none; border-radius: 4px; display: inline-block;">Annulla</a>
        </div>

    </form>

</div>

</body>
</html>