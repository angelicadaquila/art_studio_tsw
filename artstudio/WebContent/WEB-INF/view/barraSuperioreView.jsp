<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Utente" %>

<%
    Utente utenteNavigazione = (Utente) session.getAttribute("utente");
%>

<link href="<%=request.getContextPath()%>/styles/navbar.css" rel="stylesheet" type="text/css">

<ul class="navbar">
    <li>
        <a href="<%=request.getContextPath()%>/catalogo">Home</a>
    </li>
    <li>
        <a href="<%=request.getContextPath()%>/catalogo?tipo=tutti">Catalogo</a>
    </li>

    <% if (utenteNavigazione!= null) { %>
        <li>
            <a href="<%=request.getContextPath()%>/carrello">Carrello</a>
        </li>
        <li>
            <a href="<%=request.getContextPath()%>/utente/profilo">Profilo</a>
        </li>
        <li>
            <a href="<%=request.getContextPath()%>/utente/mieiOrdini">I Miei Ordini</a>
        </li>

        <% if ("admin".equalsIgnoreCase(utenteNavigazione.getRuolo())) { %>
            <li>
                <a href="<%=request.getContextPath()%>/admin/prodotti">Gestione Catalogo</a>
            </li>
            <li>
                <a href="<%=request.getContextPath()%>/admin/ordini">Gestione Ordini</a>
            </li>
        <% } %>
    <% } %>

    <% if (utenteNavigazione == null) { %>
        <li class="nav-right">
            <a href="<%= request.getContextPath() %>/login">Accedi</a>
        </li>
    <% } else { %>
        <li class="nav-right">
            <a href="<%= request.getContextPath() %>/logout" class="btn-logout-nav">Esci</a>
        </li>
        <li class="nav-right">
            <span class="nav-testo">Ciao, <strong><%= utenteNavigazione.getNome() %></strong></span>
        </li>
    <% } %>
</ul>