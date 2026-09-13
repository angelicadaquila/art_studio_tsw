document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("formModificaIndirizzo");
    if (!form) return;

    const campoVia = document.getElementById("via");
    const campoCivico = document.getElementById("civico");
    const campoCitta = document.getElementById("citta");
    const campoRegione = document.getElementById("regione");

	const regexTestoAccettabile = /^[A-Za-zÀ-ÿ\s'.,\-]{2,50}$/;
	const regexCivico = /^(?=.*\d)[A-Za-z0-9\s/]{1,10}$/;

    function mostraErroreCampo(elementoInput, messaggio) {
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
        const valore = campoVia.value.trim();
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
        const valore = campoCivico.value.trim();
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
        const valore = campoCitta.value.trim();
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
        const valore = campoRegione.value.trim();
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

    const controlliCampi = [
        [campoVia, validaVia],
        [campoCivico, validaCivico],
        [campoCitta, validaCitta],
        [campoRegione, validaRegione]
    ];

    controlliCampi.forEach(([elemento, funzioneValidazione]) => {
        if (elemento) {
            elemento.addEventListener("change", funzioneValidazione);
            elemento.addEventListener("blur", funzioneValidazione);
        }
    });

    form.addEventListener("submit", function(evento) {
        const esitoVia = validaVia();
        const esitoCivico = validaCivico();
        const esitoCitta = validaCitta();
        const esitoRegione = validaRegione();

        if (!(esitoVia && esitoCivico && esitoCitta && esitoRegione)) {
            evento.preventDefault();
            mostraMessaggioGenerale("Controlla i campi evidenziati prima di salvare l'indirizzo.");
        } else {
            mostraMessaggioGenerale(null);
        }
    });
});