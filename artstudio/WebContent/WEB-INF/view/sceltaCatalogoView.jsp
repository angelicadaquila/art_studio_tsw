<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quale prodotto desideri visualizzare</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/base.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/componenti.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/catalogo.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/styles/form.css">
</head>
<body>
    <div class="form-container-admin">
        <div class="logo-container">
            <img src="<%=request.getContextPath()%>/images/logo.png" alt="ArtStudio Logo">
        </div>

        <div class="campi">
            <h4>Benvenuti su ArtStudio!</h4>
            <p>In questo sito è possibile acquistare stampe fisiche o commissioni personalizzate digitali</p>
        </div>

        <h2>Seleziona una Categoria</h2>
        <p class="testo-info-file">Clicca una di queste :)</p>

        <div class="form-layout">
            <a href="<%=request.getContextPath()%>/catalogo?tipo=stampa" class="btn-opzione">
                Stampe
            </a>

            <a href="<%=request.getContextPath()%>/catalogo?tipo=commissione" class="btn-opzione">
                Commissioni
            </a>

            <a href="<%=request.getContextPath()%>/catalogo?tipo=tutti" class="btn-opzione">
                Tutti i Prodotti
            </a>
        </div>

    </div>

</body>
</html>