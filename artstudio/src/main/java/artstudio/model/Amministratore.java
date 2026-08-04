package artstudio.model;

public class Amministratore extends Utente {

    public Amministratore() {
    }

    public Amministratore(int idUtente, String email, String password, String nome, String cognome) {
        super(idUtente, email, password, nome, cognome);
    }
}