
CREATE DATABASE IF NOT EXISTS art_studio;
USE art_studio;

CREATE TABLE utente (
    id_utente INT AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    ruolo VARCHAR (20) NOT NULL,
    PRIMARY KEY (id_utente)
);

CREATE TABLE indirizzo (
    id_indirizzo INT AUTO_INCREMENT,
    id_utente INT NOT NULL,
    via VARCHAR(255) NOT NULL,
    civico VARCHAR(20) NOT NULL,
    citta VARCHAR(100) NOT NULL,
    regione VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_indirizzo),
    FOREIGN KEY (id_utente) REFERENCES utente (id_utente) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE ordine (
    id_ordine INT AUTO_INCREMENT,
    id_utente INT NOT NULL,
    data_ordine DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    stato VARCHAR(50) NOT NULL DEFAULT 'Confermato',
    totale_prodotti DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    spese_spedizione DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    totale_ordine DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    PRIMARY KEY (id_ordine),
    FOREIGN KEY (id_utente) REFERENCES utente (id_utente) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE prodotto (
    id_prodotto INT AUTO_INCREMENT,
    is_fisico BOOLEAN NOT NULL DEFAULT TRUE,
    nome VARCHAR(255) NOT NULL,
    descrizione TEXT NULL,
    prezzo DECIMAL(10, 2) NOT NULL,
    disponibile BOOLEAN NOT NULL DEFAULT TRUE, 
    immagine VARCHAR(255),
    PRIMARY KEY (id_prodotto)
);

CREATE TABLE stampa (
    id_prodotto INT NOT NULL,
    dimensione VARCHAR(50) NOT NULL,
    quantita INT NOT NULL,
    PRIMARY KEY (id_prodotto),
    FOREIGN KEY (id_prodotto) REFERENCES prodotto (id_prodotto) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE commissione (
    id_prodotto INT NOT NULL,
    tempo VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_prodotto),
    FOREIGN KEY (id_prodotto) REFERENCES prodotto (id_prodotto) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE riga_ordine (
    id_ordine INT NOT NULL,
    id_prodotto INT NOT NULL,
    prezzo_og DECIMAL(10, 2) NOT NULL, 
    quantita INT NOT NULL DEFAULT 1,
    descrizione_comm TEXT NULL,
    ref_comm VARCHAR(512) NULL,
    file_finale VARCHAR(512) NULL,
    PRIMARY KEY (id_ordine, id_prodotto),
    FOREIGN KEY (id_ordine) REFERENCES ordine (id_ordine) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_prodotto) REFERENCES prodotto (id_prodotto) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE bozza (
    id_bozza INT AUTO_INCREMENT,
    id_ordine INT NOT NULL,
    id_prodotto INT NOT NULL,
    file VARCHAR(512) NOT NULL,
    stato VARCHAR(30) NOT NULL DEFAULT 'IN_ATTESA',
    commento_cliente TEXT NULL,
    PRIMARY KEY (id_bozza),
    FOREIGN KEY (id_ordine, id_prodotto) REFERENCES riga_ordine (id_ordine, id_prodotto) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO prodotto (is_fisico, nome, descrizione, prezzo, disponibile, immagine) 
VALUES (TRUE, 'Stampa BuonCompleanno', 'Stampa su cartoncino', 15.00, TRUE, 'stampa1.png');

INSERT INTO stampa (id_prodotto, dimensione) 
VALUES (LAST_INSERT_ID(), 'A5');

INSERT INTO prodotto (is_fisico, nome, descrizione, prezzo, disponibile, immagine) 
VALUES (FALSE, 'Commissione Fullbody', 'Ritratto digitale personalizzato', 60.00, TRUE, 'commissione1.jpg');

INSERT INTO commissione (id_prodotto, tempo) 
VALUES (LAST_INSERT_ID(), '7-10 giorni lavorativi');