<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Quale prodotto desideri visualizzare</title>
    	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/base.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/componenti.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/styles/catalogo.css">
</head>
<body>

    <div class="catalogo-selezione">
        <h1>Seleziona una Categoria</h1>
        <p>Cosa desideri visualizzare nel catalogo?</p>

        <div class="opzione">
            <a href="${pageContext.request.contextPath}/catalogo?tipo=stampa" class="btn-opzione">
                Stampe
            </a>

            <a href="${pageContext.request.contextPath}/catalogo?tipo=commissione" class="btn-opzione">
                Commissioni
            </a>

            <a href="${pageContext.request.contextPath}/catalogo?tipo=tutti" class="btn-opzione">
                Tutti i Prodotti
            </a>
        </div>
    </div>

</body>
</html>