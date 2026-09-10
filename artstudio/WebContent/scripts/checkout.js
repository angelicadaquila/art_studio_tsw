function toggleIndirizzo(mostra) {
    var box = document.getElementById("boxNuovoIndirizzo");
    if (box) {
        if (mostra) {
            box.style.display = "block";
        } else {
            box.style.display = "none";
        }
    }
}