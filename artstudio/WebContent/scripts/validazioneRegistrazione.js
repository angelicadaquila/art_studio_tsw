document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("formRegistrazione");
    if (!form) return;

    const campoNome = document.getElementById("nome");
    const campoCognome = document.getElementById("cognome");
    const campoEmail = document.getElementById("email");
    const campoPassword = document.getElementById("password");
    const campoConfermaPassword = document.getElementById("confermaPassword");
    const campoVia = document.getElementById("via");
    const campoCivico = document.getElementById("civico");
    const campoCitta = document.getElementById("citta");
    const campoRegione = document.getElementById("regione");

    const regexNomeCognome = /^[A-Za-zÀ-ÿ\s']{2,50}$/;
    const regexEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const regexPassword = /^(?=\S+$)(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,}$/;
    const regexIndirizzoTesto = /^[A-Za-zÀ-ÿ\s'.,\-]{2,50}$/;
    const regexCivico = /^\d+[A-Za-z\s/]*$/;

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

    function validaNome() {
        const valore = campoNome.value.trim();
        if (!valore) {
            mostraErroreCampo(campoNome, "Il nome è obbligatorio.");
            return false;
        } else if (!regexNomeCognome.test(valore)) {
            mostraErroreCampo(campoNome, "Il nome può contenere solo lettere (minimo 2 caratteri).");
            return false;
        }
        mostraErroreCampo(campoNome, null);
        return true;
    }

    function validaCognome() {
        const valore = campoCognome.value.trim();
        if (!valore) {
            mostraErroreCampo(campoCognome, "Il cognome è obbligatorio.");
            return false;
        } else if (!regexNomeCognome.test(valore)) {
            mostraErroreCampo(campoCognome, "Il cognome può contenere solo lettere (minimo 2 caratteri).");
            return false;
        }
        mostraErroreCampo(campoCognome, null);
        return true;
    }

    function validaEmail() {
        const valore = campoEmail.value.trim();
        if (!valore) {
            mostraErroreCampo(campoEmail, "L'email è obbligatoria.");
            return false;
        } else if (!regexEmail.test(valore)) {
            mostraErroreCampo(campoEmail, "Inserisci un indirizzo email valido (es. kitty.puppy@email.it).");
            return false;
        }
        mostraErroreCampo(campoEmail, null);
        return true;
    }

    function validaPassword() {
        const valore = campoPassword.value;
        if (!valore) {
            mostraErroreCampo(campoPassword, "La password è obbligatoria.");
            return false;
        } else if (!regexPassword.test(valore)) {
            mostraErroreCampo(campoPassword, "Almeno 8 caratteri, una maiuscola, un numero e un simbolo.");
            return false;
        }
        mostraErroreCampo(campoPassword, null);
        return true;
    }

    function validaConfermaPassword() {
        const passwordOriginale = campoPassword.value;
        const passwordConfermata = campoConfermaPassword.value;
        if (!passwordConfermata) {
            mostraErroreCampo(campoConfermaPassword, "La conferma della password è obbligatoria.");
            return false;
        } else if (passwordOriginale !== passwordConfermata) {
            mostraErroreCampo(campoConfermaPassword, "Le password non coincidono.");
            return false;
        }
        mostraErroreCampo(campoConfermaPassword, null);
        return true;
    }

    function validaVia() {
        const valore = campoVia.value.trim();
        if (!valore) {
            mostraErroreCampo(campoVia, "La via è obbligatoria.");
            return false;
        } else if (!regexIndirizzoTesto.test(valore)) {
            mostraErroreCampo(campoVia, "Inserisci una via valida (es. Via Roma, Corso Cavour).");
            return false;
        }
        mostraErroreCampo(campoVia, null);
        return true;
    }

    function validaCivico() {
        const valore = campoCivico.value.trim();
        if (!valore) {
            mostraErroreCampo(campoCivico, "Il numero civico è obbligatorio.");
            return false;
        } else if (!regexCivico.test(valore)) {
            mostraErroreCampo(campoCivico, "Inserisci un civico valido (es. 10, 4/B).");
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
        } else if (!regexIndirizzoTesto.test(valore)) {
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
        } else if (!regexIndirizzoTesto.test(valore)) {
            mostraErroreCampo(campoRegione, "Inserisci un nome di regione valido.");
            return false;
        }
        mostraErroreCampo(campoRegione, null);
        return true;
    }

    const controlliCampi = [
        [campoNome, validaNome],
        [campoCognome, validaCognome],
        [campoEmail, validaEmail],
        [campoPassword, validaPassword],
        [campoConfermaPassword, validaConfermaPassword],
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
        const esitoNome = validaNome();
        const esitoCognome = validaCognome();
        const esitoEmail = validaEmail();
        const esitoPassword = validaPassword();
        const esitoConfermaPassword = validaConfermaPassword();
        const esitoVia = validaVia();
        const esitoCivico = validaCivico();
        const esitoCitta = validaCitta();
        const esitoRegione = validaRegione();

        if (!(esitoNome && esitoCognome && esitoEmail && esitoPassword && esitoConfermaPassword && esitoVia && esitoCivico && esitoCitta && esitoRegione)) {
            evento.preventDefault();
            mostraMessaggioGenerale("Tutti i campi sono obbligatori o contengono errori di formattazione.");
        } else {
            mostraMessaggioGenerale(null);
        }
    });
});