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
				    console.log("Risposta dal server:", xhr.responseText);
				    var data = JSON.parse(xhr.responseText);
				    
				    if (data.carrelloVuoto) {
				        var container = document.getElementById("contenutoCarrello");
				        if (container) {
				            container.innerHTML = '<div class="nessuno-trovato">' +
				                                  '<p>Il tuo carrello è attualmente vuoto.</p>' +
				                                  '<a href="' + contextPath + '/catalogo?tipo=tutti" class="btn-opzione">Torna al Catalogo</a>' +
				                                  '</div>';
				        }
				    } else if (data.rimosso) {
				        var riga = document.getElementById("riga-prod-" + idProdotto);
				        if (riga) {
				            riga.remove();
				        }
				        aggiornaTotali(data);
						
						var righeRimaste = document.querySelectorAll('tr[id^="riga-prod-"]');
						    if (righeRimaste.length === 0) {					      
						        window.location.href = contextPath + "/carrello";
						    }
				    } else {
				        var elemQuantita = document.getElementById("qta-" + idProdotto);
				        var elemSubtotale = document.getElementById("subtotale-" + idProdotto);
				        
				        if (elemQuantita) elemQuantita.textContent = data.nuovaQuantita;
				        if (elemSubtotale) elemSubtotale.textContent = data.nuovoSubtotale.toFixed(2) + " €";
				        
				        aggiornaTotali(data);
				    }
				} catch (e) {
				    console.error("ERRORE CATTURATO:", e);
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

function rimuoviDinamico(button, contextPath) {
    var tr = button.closest('tr');
    var tbody = tr.parentNode;
    
    var righe = Array.from(tbody.querySelectorAll('tr'));
    var indiceReale = righe.indexOf(tr);
    
    tr.id = "riga-target-corrente";
    
    aggiornaQuantitaDaIndiceReale(indiceReale, 'elimina', contextPath, tr);
}

function aggiornaQuantitaDaIndiceReale(indice, azioneRichiesta, contextPath, rigaElemento) {
    var xhr = new XMLHttpRequest();
    var url = contextPath + "/carrello";
    
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            try {
                var data = JSON.parse(xhr.responseText);
                
                if (data.carrelloVuoto) {
                    var container = document.getElementById("contenutoCarrello");
                    if (container) {
                        container.innerHTML = '<div class="nessuno-trovato">' +
                                              '<p>Il tuo carrello è attualmente vuoto.</p>' +
                                              '<a href="' + contextPath + '/catalogo?tipo=tutti" class="btn-opzione">Torna al Catalogo</a>' +
                                              '</div>';
                    }
                } else if (data.rimosso) {
                    if (rigaElemento) {
                        rigaElemento.remove();
                    }
                    aggiornaTotali(data);
                }
            } catch (e) {
                console.error("Errore:", e);
            }
        }
    };

    var params = "azione=" + encodeURIComponent(azioneRichiesta) + 
                 "&idProdotto=" + encodeURIComponent(indice) + 
                 "&ajax=true";
                 
    xhr.send(params);
}