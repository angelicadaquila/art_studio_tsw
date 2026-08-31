function gestisciCampiTipo() {
    var tipoSelect = document.getElementById("tipoProdotto");
    if (!tipoSelect) {
        return;
    }
    
    var tipo = tipoSelect.value;
    var campiStampa = document.getElementById("campiStampa");
    var campiCommissione = document.getElementById("campiCommissione");

    if (tipo === "stampa") {
        campiStampa.style.display = "block";
        campiCommissione.style.display = "none";
    } else if (tipo === "commissione") {
        campiStampa.style.display = "none";
        campiCommissione.style.display = "block";
    } else {
        campiStampa.style.display = "none";
        campiCommissione.style.display = "none";
    }
}

window.addEventListener("DOMContentLoaded", function() {
    gestisciCampiTipo();
});