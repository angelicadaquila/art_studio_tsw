<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="<%=request.getContextPath()%>/styles/base.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/styles/componenti.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/styles/form.css" rel="stylesheet" type="text/css">
    <title>Registrazione Utente</title>
</head>
<body>

    <div style="padding: 20px;">
        <a href="<%=request.getContextPath()%>/catalogo?tipo=tutti" class="btn-indietro">Torna al Catalogo</a>
    </div>

    <div class="form-container-admin">

        <h2>Registrazione Utente</h2>

        <%
            String errore = (String) request.getAttribute("errore");
            if (errore != null && !errore.trim().isEmpty()) {
        %>
            <p id="errore" class="msg-errore"><%= errore %></p>
        <%
            } else {
        %>
            <p id="errore" class="msg-errore" style="display: none;"></p>
        <%
            }
        %>

        <form id="formRegistrazione" action="<%=request.getContextPath()%>/registrazione" method="post" class="form-layout">
        
            <h3>Dati Personali</h3>
            
            <div class="form-gruppo">
                <label for="nome">Nome:</label>
                <input type="text" id="nome" name="nome">
            </div>

            <div class="form-gruppo">
                <label for="cognome">Cognome:</label>
                <input type="text" id="cognome" name="cognome">
            </div>

            <div class="form-gruppo">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email">
            </div>

            <div class="form-gruppo">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password">
            </div>

            <div class="form-gruppo">
                <label for="confermaPassword">Conferma Password:</label>
                <input type="password" id="confermaPassword" name="confermaPassword">
            </div>

            <h3>Indirizzo di Spedizione</h3>

            <div class="form-gruppo">
                <label for="via">Via / Piazza:</label>
                <input type="text" id="via" name="via">
            </div>

            <div class="form-gruppo">
                <label for="civico">Numero Civico:</label>
                <input type="text" id="civico" name="civico">
            </div>

            <div class="form-gruppo">
                <label for="citta">Citt&agrave;:</label>
                <input type="text" id="citta" name="citta">
            </div>

            <div class="form-gruppo">
                <label for="regione">Regione:</label>
                <input type="text" id="regione" name="regione">
            </div>

            <div class="form-azioni">
                <button type="submit" class="btn-invio">Registrati</button>
            </div>
        </form>

    </div>

<script src="<%=request.getContextPath()%>/scripts/validazioneRegistrazione.js" defer></script>
</body>
</html>