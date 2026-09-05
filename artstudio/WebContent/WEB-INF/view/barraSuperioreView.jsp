<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Utente" %>

<%
    Utente utenteNavigazione = (Utente) session.getAttribute("utente");
%>

<nav style="display: flex; justify-content: space-between; align-items: center; padding: 10px 20px; width: 100%; box-sizing: border-box; background-color: #f8f9fa; border-bottom: 1px solid #ddd;">
    <div style="display: flex; align-items: center; gap: 10px; flex: 1;">
        <a href="<%= request.getContextPath() %>/catalogo?tipo=tutti" class="btn-opzione">
            ← Catalogo
        </a>
        <a href="<%= request.getContextPath() %>/carrello" class="btn-opzione">
            Carrello
        </a>
    </div>

    <div style="display: flex; justify-content: center; align-items: center; flex: 1; text-align: center;">
        <a href="<%= request.getContextPath() %>/catalogo" class="btn-opzione" style="font-size: 1.1rem; font-weight: bold; text-decoration: none;">
            Home
        </a>
    </div>

    <div style="display: flex; justify-content: flex-end; align-items: center; gap: 10px; flex: 1;">
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
</nav>