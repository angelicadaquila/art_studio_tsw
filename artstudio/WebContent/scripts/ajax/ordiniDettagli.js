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

    var endpoint = "";
    if (formStatoIdOrdine != null) {
        endpoint = "/admin/ordini";
    } else {
        endpoint = "/utente/mieiOrdini";
    }

    var url = contextPath + endpoint;
    var params = "action=dettaglioAjax&idOrdine=" + idOrdine;

    loadAjaxDoc(url, "GET", params, function(request) {
        handleDettaglioOrdine(request, contextPath);
    });
}

function handleDettaglioOrdine(request, contextPath) {
    var ul = document.getElementById("listaArticoliFinestra");
    if (ul == null) {
        return;
    }
    ul.innerHTML = "";

    try {
        var righe = JSON.parse(request.responseText);

        if (righe == null || righe.length === 0) {
            ul.innerHTML = "<li>Nessun articolo trovato.</li>";
            return;
        }

        righe.forEach(function(item) {
            var li = document.createElement("li");
            li.style.marginBottom = "15px";

            var nome = "";
            if (item.nomeProdotto != null && item.nomeProdotto !== "") {
                nome = item.nomeProdotto;
            } else {
                nome = "Prodotto #" + item.idProdotto;
            }

            var quantita = 1;
            if (item.quantita != null) {
                quantita = item.quantita;
            }

            var prezzoValore = 0;
            if (item.prezzo != null) {
                prezzoValore = item.prezzo;
            }
            var prezzo = parseFloat(prezzoValore).toFixed(2);

            var html = "<strong>" + nome + "</strong> (x" + quantita + ") — € " + prezzo;

            if (item.note != null && item.note !== "") {
                html += "<br><small><em>Note:</em> " + item.note + "</small>";
            }

            if (item.ref != null && item.ref !== "") {
                var imgPath = contextPath + "/user_images/" + item.ref;
                html += "<br><small><em>Foto Commissione:</em></small><br>" +
                        "<a href='" + imgPath + "' target='_blank'>" +
                        "<img src='" + imgPath + "' style='max-width:140px; border-radius:6px; margin-top:5px; border:1px solid #ddd;'>" +
                        "</a>";
            }

            li.innerHTML = html;
            ul.appendChild(li);
        });

    } catch (e) {
        ul.innerHTML = "<li style='color: red;'>Errore nel caricamento dei dati.</li>";
    }
}

function chiudiFinestra() {
    var finestra = document.getElementById("finestraDettaglio");
    if (finestra != null) {
        finestra.style.display = "none";
    }
}