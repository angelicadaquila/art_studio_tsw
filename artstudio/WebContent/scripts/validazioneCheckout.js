document.addEventListener("DOMContentLoaded", function() {
    const form = document.querySelector("form"); 
    if (!form) return;

    const campoVia = document.getElementById("via");
    const campoCivico = document.getElementById("civico");
    const campoCitta = document.getElementById("citta");
    const campoRegione = document.getElementById("regione");
    const radioNuovoIndirizzo = document.getElementById("tipo_nuovo");
    const radioProfiloIndirizzo = document.getElementById("tipo_profilo");
    const campoIntestatario = document.getElementById("intestatario");
    const campoNumeroCarta = document.getElementById("numeroCarta");
    const campoScadenza = document.getElementById("scadenza");
    const campoCvv = document.getElementById("cvv");

    const regexTestoAccettabile = /^[A-Za-zÀ-ÿ\s'.,\-]{2,50}$/;
    const regexCivico = /^(?=.*\d)[A-Za-z0-9\s/]{1,10}$/;
    const regexIntestatario = /^[A-Za-zÀ-ÿ\s'-]{3,50}$/;
    const regexNumeroCarta = /^\d{4}\s?\d{4}\s?\d{4}\s?\d{4}$/; 
    const regexScadenza = /^(0[1-9]|1[0-2])\/([0-9]{2})$/;
    const regexCvv = /^\d{3}$/;

    function mostraErroreCampo(elementoInput, messaggio) {
        if (!elementoInput) return;
        let elementoPadre = elementoInput.parentElement;
        let etichettaErrore = elementoPadre.querySelector(".msg-errore");

        if (messaggio) {
            if (!etichettaErrore) {
                etichettaErrore = document.createElement("small");
                etichettaErrore.className = "msg-errore";
                elementoPadre.appendChild(etichettaErrore);
            }
            etichettaErrore.textContent = messaggio;
            elementoInput.style.borderColor = "#dc3545";
        } else {
            if (etichettaErrore) {
                etichettaErrore.remove();
            }
            elementoInput.style.borderColor = "";
        }
    }

    function mostraMessaggioGenerale(messaggio) {
        const pannelloErrore = document.getElementById("errore");
        if (pannelloErrore) {
            if (messaggio) {
                pannelloErrore.textContent = messaggio;
                pannelloErrore.style.display = "block";
            } else {
                pannelloErrore.style.display = "none";
            }
        }
    }

    function validaVia() {
        if (!radioNuovoIndirizzo || !radioNuovoIndirizzo.checked) {
            return true;
        }
        let valore = "";
        if (campoVia) {
            valore = campoVia.value.trim();
        }
        if (!valore) {
            mostraErroreCampo(campoVia, "La via è obbligatoria.");
            return false;
        } else if (!regexTestoAccettabile.test(valore)) {
            mostraErroreCampo(campoVia, "Inserisci un nome di via valido.");
            return false;
        }
        mostraErroreCampo(campoVia, null);
        return true;
    }

    function validaCivico() {
        if (!radioNuovoIndirizzo || !radioNuovoIndirizzo.checked) {
            return true;
        }
        let valore = "";
        if (campoCivico) {
            valore = campoCivico.value.trim();
        }
        if (!valore) {
            mostraErroreCampo(campoCivico, "Il civico è obbligatorio.");
            return false;
        } else if (!regexCivico.test(valore)) {
            mostraErroreCampo(campoCivico, "Inserisci un numero civico valido");
            return false;
        }
        mostraErroreCampo(campoCivico, null);
        return true;
    }

    function validaCitta() {
        if (!radioNuovoIndirizzo || !radioNuovoIndirizzo.checked) {
            return true;
        }
        let valore = "";
        if (campoCitta) {
            valore = campoCitta.value.trim();
        }
        if (!valore) {
            mostraErroreCampo(campoCitta, "La città è obbligatoria.");
            return false;
        } else if (!regexTestoAccettabile.test(valore)) {
            mostraErroreCampo(campoCitta, "Inserisci un nome di città valido.");
            return false;
        }
        mostraErroreCampo(campoCitta, null);
        return true;
    }

    function validaRegione() {
        if (!radioNuovoIndirizzo || !radioNuovoIndirizzo.checked) {
            return true;
        }
        let valore = "";
        if (campoRegione) {
            valore = campoRegione.value.trim();
        }
        if (!valore) {
            mostraErroreCampo(campoRegione, "La regione è obbligatoria.");
            return false;
        } else if (!regexTestoAccettabile.test(valore)) {
            mostraErroreCampo(campoRegione, "Inserisci una regione valida.");
            return false;
        }
        mostraErroreCampo(campoRegione, null);
        return true;
    }

    function validaIntestatario() {
        const valore = campoIntestatario.value.trim();
        if (!valore) {
            mostraErroreCampo(campoIntestatario, "L'intestatario è obbligatorio.");
            return false;
        } else if (!regexIntestatario.test(valore)) {
            mostraErroreCampo(campoIntestatario, "Nome intestatario non valido.");
            return false;
        }
        mostraErroreCampo(campoIntestatario, null);
        return true;
    }

    function validaNumeroCarta() {
        const valore = campoNumeroCarta.value.trim();
        if (!valore) {
            mostraErroreCampo(campoNumeroCarta, "Il numero di carta è obbligatorio");
            return false;
        } else if (!regexNumeroCarta.test(valore)) {
            mostraErroreCampo(campoNumeroCarta, "Numero carta non valido (16 cifre).");
            return false;
        }
        mostraErroreCampo(campoNumeroCarta, null);
        return true;
    }

    function validaScadenza() {
        const valore = campoScadenza.value.trim();
        if (!valore) {
            mostraErroreCampo(campoScadenza, "La scadenza è obbligatoria");
            return false;
        } else if (!regexScadenza.test(valore)) {
            mostraErroreCampo(campoScadenza, "Usa il formato MM/AA.");
            return false;
        }
        mostraErroreCampo(campoScadenza, null);
        return true;
    }

    function validaCvv() {
        const valore = campoCvv.value.trim();
        if (!valore) {
            mostraErroreCampo(campoCvv, "Il CVV è obbligatorio");
            return false;
        } else if (!regexCvv.test(valore)) {
            mostraErroreCampo(campoCvv, "CVV non valido (3 cifre)");
            return false;
        }
        mostraErroreCampo(campoCvv, null);
        return true;
    }

    const controlliPagamento = [
        [campoIntestatario, validaIntestatario],
        [campoNumeroCarta, validaNumeroCarta],
        [campoScadenza, validaScadenza],
        [campoCvv, validaCvv]
    ];

    controlliPagamento.forEach(([elemento, funzioneValidazione]) => {
        if (elemento) {
            elemento.addEventListener("change", funzioneValidazione);
            elemento.addEventListener("blur", funzioneValidazione);
        }
    });

    const controlliIndirizzo = [
        [campoVia, validaVia],
        [campoCivico, validaCivico],
        [campoCitta, validaCitta],
        [campoRegione, validaRegione]
    ];

    controlliIndirizzo.forEach(([elemento, funzioneValidazione]) => {
        if (elemento) {
            elemento.addEventListener("change", funzioneValidazione);
            elemento.addEventListener("blur", funzioneValidazione);
        }
    });

    if (radioProfiloIndirizzo) {
        radioProfiloIndirizzo.addEventListener("change", function() {
            if (this.checked) {
                mostraErroreCampo(campoVia, null);
                mostraErroreCampo(campoCivico, null);
                mostraErroreCampo(campoCitta, null);
                mostraErroreCampo(campoRegione, null);
            }
        });
    }

    form.addEventListener("submit", function(evento) {
        const esitoIntestatario = validaIntestatario();
        const esitoCarta = validaNumeroCarta();
        const esitoScadenza = validaScadenza();
        const esitoCvv = validaCvv();

        let esitoIndirizzo = true;
        if (radioNuovoIndirizzo && radioNuovoIndirizzo.checked) {
            const esitoVia = validaVia();
            const esitoCivico = validaCivico();
            const esitoCitta = validaCitta();
            const esitoRegione = validaRegione();
            esitoIndirizzo = (esitoVia && esitoCivico && esitoCitta && esitoRegione);
        }

        if (!(esitoIntestatario && esitoCarta && esitoScadenza && esitoCvv && esitoIndirizzo)) {
            evento.preventDefault();
            mostraMessaggioGenerale("Per favore, correggi i campi evidenziati prima di procedere con il pagamento.");
        } else {
            mostraMessaggioGenerale(null);
        }
    });
});