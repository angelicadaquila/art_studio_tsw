<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="<%=request.getContextPath()%>/styles/base.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/styles/componenti.css" rel="stylesheet" type="text/css">
    <link href="<%=request.getContextPath()%>/styles/form.css" rel="stylesheet" type="text/css">
    <title>Login</title>
</head>
<body>

    <div style="padding: 20px;">
        <a href="<%=request.getContextPath()%>/catalogo?tipo=tutti" class="btn-indietro">Torna al Catalogo</a>
    </div>

    <div class="form-container-admin">

        <h2>Accedi al tuo Account</h2>

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

        <form id="formLogin" action="<%=request.getContextPath()%>/login" method="post" class="form-layout">
            
            <div class="form-gruppo">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>

            <div class="form-gruppo">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>

            <div class="form-azioni">
                <button type="submit" class="btn-invio">Accedi</button>
            </div>
        </form>

        <p class="testo-info-file">
            Non hai ancora un account? 
            <a href="<%=request.getContextPath()%>/registrazione">Registrati</a>
        </p>

    </div>

    <script src="<%=request.getContextPath()%>/scripts/validazioneLogin.js"></script>
</body>
</html>