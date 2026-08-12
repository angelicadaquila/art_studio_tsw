<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Quale prodotto desideri visualizzare</title>
    <style>
        .catalogo-selezione {
            max-width: 600px;
            margin: 50px auto;
            text-align: center;
            font-family: Arial, sans-serif;
        }
        .opzione {
            display: flex;
            justify-content: space-around;
            margin-top: 30px;
            gap: 15px;
        }
        .btn-opzione {
            display: inline-block;
            padding: 15px 25px;
            background-color: #eb9036;
            color: #ffffff;
            text-decoration: none;
            border-radius: 8px;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }
        .btn-opzione:hover {
            background-color: #d97716;
        }
    </style>
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