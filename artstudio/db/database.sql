-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: art_studio
-- ------------------------------------------------------
-- Server version	8.4.9

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bozza`
--

DROP TABLE IF EXISTS `bozza`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bozza` (
  `id_bozza` int NOT NULL AUTO_INCREMENT,
  `id_ordine` int NOT NULL,
  `id_prodotto` int NOT NULL,
  `file` varchar(512) NOT NULL,
  `stato` varchar(30) NOT NULL DEFAULT 'IN_ATTESA',
  `commento_cliente` text,
  PRIMARY KEY (`id_bozza`),
  KEY `id_ordine` (`id_ordine`,`id_prodotto`),
  CONSTRAINT `bozza_ibfk_1` FOREIGN KEY (`id_ordine`, `id_prodotto`) REFERENCES `riga_ordine` (`id_ordine`, `id_prodotto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bozza`
--

LOCK TABLES `bozza` WRITE;
/*!40000 ALTER TABLE `bozza` DISABLE KEYS */;
/*!40000 ALTER TABLE `bozza` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `commissione`
--

DROP TABLE IF EXISTS `commissione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `commissione` (
  `id_prodotto` int NOT NULL,
  `tempo` varchar(100) NOT NULL,
  PRIMARY KEY (`id_prodotto`),
  CONSTRAINT `commissione_ibfk_1` FOREIGN KEY (`id_prodotto`) REFERENCES `prodotto` (`id_prodotto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `commissione`
--

LOCK TABLES `commissione` WRITE;
/*!40000 ALTER TABLE `commissione` DISABLE KEYS */;
INSERT INTO `commissione` VALUES (2,'15 giorni');
/*!40000 ALTER TABLE `commissione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `indirizzo`
--

DROP TABLE IF EXISTS `indirizzo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `indirizzo` (
  `id_indirizzo` int NOT NULL AUTO_INCREMENT,
  `id_utente` int NOT NULL,
  `via` varchar(255) NOT NULL,
  `civico` varchar(20) NOT NULL,
  `citta` varchar(100) NOT NULL,
  `regione` varchar(100) NOT NULL,
  PRIMARY KEY (`id_indirizzo`),
  CONSTRAINT `indirizzo_ibfk_1` FOREIGN KEY (`id_indirizzo`) REFERENCES `utente` (`id_utente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `indirizzo`
--

LOCK TABLES `indirizzo` WRITE;
/*!40000 ALTER TABLE `indirizzo` DISABLE KEYS */;
/*!40000 ALTER TABLE `indirizzo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordine`
--

DROP TABLE IF EXISTS `ordine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordine` (
  `id_ordine` int NOT NULL AUTO_INCREMENT,
  `id_utente` int NOT NULL,
  `data_ordine` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `stato` varchar(50) NOT NULL DEFAULT 'Confermato',
  `totale_prodotti` decimal(10,2) NOT NULL DEFAULT '0.00',
  `spese_spedizione` decimal(10,2) NOT NULL DEFAULT '0.00',
  `totale_ordine` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id_ordine`),
  KEY `ordine_ibfk_1_idx` (`id_utente`),
  CONSTRAINT `ordine_ibfk_1` FOREIGN KEY (`id_utente`) REFERENCES `utente` (`id_utente`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordine`
--

LOCK TABLES `ordine` WRITE;
/*!40000 ALTER TABLE `ordine` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodotto`
--

DROP TABLE IF EXISTS `prodotto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prodotto` (
  `id_prodotto` int NOT NULL AUTO_INCREMENT,
  `is_fisico` tinyint(1) NOT NULL DEFAULT '1',
  `nome` varchar(255) NOT NULL,
  `descrizione` text,
  `prezzo` decimal(10,2) NOT NULL,
  `disponibile` tinyint(1) NOT NULL DEFAULT '1',
  `immagine` varchar(255) DEFAULT 'default.jpg',
  PRIMARY KEY (`id_prodotto`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodotto`
--

LOCK TABLES `prodotto` WRITE;
/*!40000 ALTER TABLE `prodotto` DISABLE KEYS */;
INSERT INTO `prodotto` VALUES (1,1,'Stampa Buon Compleanno','Stampa su foglio',15.00,1,'stampa1.png'),(2,0,'Commissione Fullbody','Commissione personalizzata',60.00,1,'commissione1.png');
/*!40000 ALTER TABLE `prodotto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riga_ordine`
--

DROP TABLE IF EXISTS `riga_ordine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `riga_ordine` (
  `id_ordine` int NOT NULL,
  `id_prodotto` int NOT NULL,
  `prezzo_og` decimal(10,2) NOT NULL,
  `quantita` int NOT NULL DEFAULT '1',
  `descrizione_comm` text,
  `ref_comm` varchar(512) DEFAULT NULL,
  `file_finale` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id_ordine`,`id_prodotto`),
  KEY `id_prodotto` (`id_prodotto`),
  CONSTRAINT `riga_ordine_ibfk_1` FOREIGN KEY (`id_ordine`) REFERENCES `ordine` (`id_ordine`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `riga_ordine_ibfk_2` FOREIGN KEY (`id_prodotto`) REFERENCES `prodotto` (`id_prodotto`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riga_ordine`
--

LOCK TABLES `riga_ordine` WRITE;
/*!40000 ALTER TABLE `riga_ordine` DISABLE KEYS */;
/*!40000 ALTER TABLE `riga_ordine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stampa`
--

DROP TABLE IF EXISTS `stampa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stampa` (
  `id_prodotto` int NOT NULL,
  `dimensione` varchar(50) NOT NULL,
  PRIMARY KEY (`id_prodotto`),
  CONSTRAINT `stampa_ibfk_1` FOREIGN KEY (`id_prodotto`) REFERENCES `prodotto` (`id_prodotto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stampa`
--

LOCK TABLES `stampa` WRITE;
/*!40000 ALTER TABLE `stampa` DISABLE KEYS */;
INSERT INTO `stampa` VALUES (1,'A5');
/*!40000 ALTER TABLE `stampa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utente` (
  `id_utente` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `cognome` varchar(100) NOT NULL,
  `ruolo` varchar(20) NOT NULL DEFAULT 'cliente',
  PRIMARY KEY (`id_utente`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES (1,'cliente@gmail.com','cliente123','Mario','Rossi','cliente'),(2,'admin@gmail.com','admin123','Giuseppe','Verdi','amministratore');
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-12 15:25:33
