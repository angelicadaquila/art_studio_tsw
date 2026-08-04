package artstudio.model;

public class Cliente extends Utente {

    public Cliente() {
    }

    public Cliente(int idUtente, String email, String password, String nome, String cognome) {
        super(idUtente, email, password, nome, cognome);
    }
}
