function aggiornaQuantita(idProdotto, azioneRichiesta, contextPath) {
    var xhr = new XMLHttpRequest();
    var url = contextPath + "/carrello";
    
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    xhr.setRequestHeader("Connection", "close");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                rimuoviMessaggioErrore();
                try {
                    var data = JSON.parse(xhr.responseText);
                    
                    if (data.carrelloVuoto) {
                        var container = document.getElementById("contenutoCarrello");
                        if (container) {
                            container.innerHTML = '<div class="carrello-vuoto">' +
                                                  '<p>Il tuo carrello è attualmente vuoto.</p>' +
                                                  '<a href="' + contextPath + '/catalogo?tipo=tutti" class="btn-catalogo">Torna al Catalogo</a>' +
                                                  '</div>';
                        }
                    } else if (data.rimosso) {
                        var riga = document.getElementById("riga-prod-" + idProdotto);
                        if (riga) {
                            riga.remove();
                        }
                        aggiornaTotali(data);
                    } else {
                        var elemQuantita = document.getElementById("qta-" + idProdotto);
                        var elemSubtotale = document.getElementById("subtotale-" + idProdotto);
                        
                        if (elemQuantita) elemQuantita.textContent = data.nuovaQuantita;
                        if (elemSubtotale) elemSubtotale.textContent = data.nuovoSubtotale.toFixed(2) + " €";
                        
                        aggiornaTotali(data);
                    }
                } catch (e) {
                    console.error("Errore nel parsing JSON:", e);
                }
            } else if (xhr.status === 400) {
                mostraMessaggioErrore("Attenzione! La quantità richiesta supera la disponibilità attuale in magazzino.");
            }
        }
    };

    var params = "azione=" + encodeURIComponent(azioneRichiesta) + 
                 "&idProdotto=" + encodeURIComponent(idProdotto) + 
                 "&ajax=true";
                 
    xhr.send(params);
}

function mostraMessaggioErrore(messaggio) {
    var containerMessaggi = document.getElementById("contenitoreMessaggiErrore");
    if (containerMessaggi) {
        containerMessaggi.innerHTML = '<div class="messaggio-errore" style="background-color: #f8d7da; color: #721c24; padding: 12px; border: 1px solid #f5c6cb; border-radius: 5px; margin-bottom: 20px; text-align: center;">' +
                                      '<strong>Attenzione!</strong> ' + messaggio +
                                      '</div>';
    }
}

function rimuoviMessaggioErrore() {
    var containerMessaggi = document.getElementById("contenitoreMessaggiErrore");
    if (containerMessaggi) {
        containerMessaggi.innerHTML = "";
    }
}

function aggiornaTotali(data) {
    var elemSpese = document.getElementById("speseSpedizione");
    var elemTotaleOrdine = document.getElementById("totaleOrdine");

    if (elemSpese && data.speseSpedizione !== undefined) {
        elemSpese.textContent = data.speseSpedizione.toFixed(2) + " €";
    }

    if (elemTotaleOrdine && data.totaleOrdine !== undefined) {
        elemTotaleOrdine.textContent = data.totaleOrdine.toFixed(2);
    }
}