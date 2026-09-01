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

        <a href="<%= request.getContextPath() %>/utente/profilo" class="btn-opzione">Profilo</a>

        <% if ("admin".equalsIgnoreCase(utenteNavigazione.getRuolo())) { %>
            <a href="<%= request.getContextPath() %>/admin/prodotti" class="btn-opzione">Gestione</a>
        <% } %>

        <a href="<%= request.getContextPath() %>/logout" 
           style="background-color: #dc3545; color: #ffffff; padding: 6px 12px; border-radius: 4px; text-decoration: none; font-weight: bold; display: inline-block;">
           Esci
        </a>
    <% } %>
</div>