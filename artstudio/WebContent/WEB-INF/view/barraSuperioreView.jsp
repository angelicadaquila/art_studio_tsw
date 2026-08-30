<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Utente" %>

<%
    Utente utenteNavigazione = (Utente) session.getAttribute("utente");
%>

<div style="display: flex; justify-content: flex-end; align-items: center; gap: 10px; padding: 10px 20px; width: 100%; box-sizing: border-box;">
    <% if (utenteNavigazione == null) { %>
        <a href="<%= request.getContextPath() %>/login" class="btn-opzione">Accedi</a>
    <% } else { %>
        <span class="testo-benvenuto">
            Ciao, <strong><%= utenteNavigazione.getNome() %></strong>
        </span>
        <% if ("admin".equalsIgnoreCase(utenteNavigazione.getRuolo())) { %>
            <a href="<%= request.getContextPath() %>/admin/prodotti" class="btn-opzione">Gestione</a>
        <% } %>
        <a href="<%= request.getContextPath() %>/logout" class="btn-indietro" style="background-color: #dc3545;">Esci</a>
    <% } %>
</div>