function caricaDettaglioOrdine(idOrdine, contextPath) {
    var finestra = document.getElementById("finestraDettaglio");
    var lista = document.getElementById("listaArticoliFinestra");
    
    var finestraIdOrdine = document.getElementById("finestraIdOrdine");
    if (finestraIdOrdine != null) {
        finestraIdOrdine.innerText = "#" + idOrdine;
    }

    var formStatoIdOrdine = document.getElementById("formStatoIdOrdine");
    if (formStatoIdOrdine != null) {
        formStatoIdOrdine.value = idOrdine;
    }

    var formFotoIdOrdine = document.getElementById("formFotoIdOrdine");
    if (formFotoIdOrdine != null) {
        formFotoIdOrdine.value = idOrdine;
    }

    if (lista != null) {
        lista.innerHTML = "<li>Caricamento in corso...</li>";
    }

    if (finestra != null) {
        finestra.style.display = "flex";
    }

    var endpoint = (formStatoIdOrdine != null) ? "/admin/ordini" : "/utente/mieiOrdini";
    var url = contextPath + endpoint;

    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    xhr.setRequestHeader("Connection", "close");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                try {
                    var righe = JSON.parse(xhr.responseText);
                    mostraRigheOrdine(righe, contextPath);
                } catch (e) {
                    console.error("Errore nel parsing JSON:", e);
                    mostraErroreDettaglio("Errore nel formato dei dati ricevuti dal server.");
                }
            } else {
                console.error("Errore AJAX Dettaglio Ordine: " + xhr.status);
                mostraErroreDettaglio("Impossibile recuperare i dettagli dell'ordine.");
            }
        }
    };

    var params = "action=dettaglioAjax&idOrdine=" + encodeURIComponent(idOrdine) + "&ajax=true";
    xhr.send(params);
}
function mostraRigheOrdine(righe, contextPath) {
    var ul = document.getElementById("listaArticoliFinestra");
    if (ul == null) {
        return;
    }
    ul.innerHTML = "";

    if (righe == null || righe.length === 0) {
        ul.innerHTML = "<li>Nessun articolo trovato.</li>";
        return;
    }

    var isAdmin = document.getElementById("formStatoIdOrdine") != null;

    righe.forEach(function(item) {
        var li = document.createElement("li");
        li.style.marginBottom = "15px";
        li.style.paddingBottom = "12px";
        li.style.borderBottom = "1px solid #eee";

        var nome = (item.nomeProdotto != null && item.nomeProdotto !== "") ? item.nomeProdotto : "Prodotto #" + item.idProdotto;
        var quantita = item.quantita || 1;
        var prezzo = parseFloat(item.prezzo || 0).toFixed(2);

        var html = "<strong>" + nome + "</strong> (x" + quantita + ") — € " + prezzo;

        var isCommissione = (item.note && item.note.trim() !== "") || (item.ref && item.ref.trim() !== "");

        if (item.note && item.note.trim() !== "") {
            html += "<br><small><em>Note cliente:</em> " + item.note + "</small>";
        }

        if (item.ref && item.ref.trim() !== "") {
            var imgRefPath = contextPath + "/user_images/" + item.ref;
            html += "<br><small><em>Foto Riferimento Cliente:</em></small><br>" +
                    "<a href='" + imgRefPath + "' target='_blank'>" +
                    "<img src='" + imgRefPath + "' style='max-width:120px; border-radius:6px; margin-top:5px; border:1px solid #ddd;'>" +
                    "</a>";
        }

        if (item.fileFinale && item.fileFinale.trim() !== "") {
            var imgFinalePath = contextPath + "/admin_images/" + item.fileFinale;
            html += "<br><small style='color: #2e7d32;'><strong>Foto Consegna Caricata:</strong></small><br>" +
                    "<a href='" + imgFinalePath + "' target='_blank'>" +
                    "<img src='" + imgFinalePath + "' style='max-width:120px; border-radius:6px; margin-top:5px; border:2px solid #4CAF50;'>" +
                    "</a>";
        }

        if (isAdmin && isCommissione) {
            html += "<form action='" + contextPath + "/admin/ordini' method='post' enctype='multipart/form-data' style='margin-top:10px; padding: 8px; background-color: #f9f9f9; border-radius: 4px; border: 1px dashed #ccc;'>" +
                    "<input type='hidden' name='action' value='uploadFotoSingola'>" +
                    "<input type='hidden' name='idRiga' value='" + item.idRiga + "'>" +
                    "<label><small><strong>Carica Immagine Consegna (per questa commissione):</strong></small></label><br>" +
                    "<input type='file' name='fileFinale' accept='image/*' required style='font-size:0.85em; margin-top:4px;'> " +
                    "<button type='submit' class='btn-opzione' style='padding:4px 10px; font-size:0.85em; margin-top:4px;'>Carica Foto</button>" +
                    "</form>";
        }

        li.innerHTML = html;
        ul.appendChild(li);
    });
}

function mostraErroreDettaglio(messaggio) {
    var ul = document.getElementById("listaArticoliFinestra");
    if (ul != null) {
        ul.innerHTML = "<li style='color: red;'>" + messaggio + "</li>";
    }
}

function chiudiFinestra() {
    var finestra = document.getElementById("finestraDettaglio");
    if (finestra != null) {
        finestra.style.display = "none";
    }
}