document.addEventListener("DOMContentLoaded", function() {
    const modulo = document.getElementById("formLogin");
    if (!modulo) return;

    const campoEmail = document.getElementById("email");
    const campoPassword = document.getElementById("password");
    const regexEmail = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

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
        }
        mostraErroreCampo(campoPassword, null);
        return true;
    }

    if (campoEmail) {
        campoEmail.addEventListener("change", validaEmail);
        campoEmail.addEventListener("blur", validaEmail);
    }
    if (campoPassword) {
        campoPassword.addEventListener("change", validaPassword);
        campoPassword.addEventListener("blur", validaPassword);
    }

    modulo.addEventListener("submit", function(evento) {
        const esitoEmail = validaEmail();
        const esitoPassword = validaPassword();

        if (!(esitoEmail && esitoPassword)) {
            evento.preventDefault();
            mostraMessaggioGenerale("Inserisci i dati obbligatori correttamente.");
        } else {
            mostraMessaggioGenerale(null);
        }
    });
});