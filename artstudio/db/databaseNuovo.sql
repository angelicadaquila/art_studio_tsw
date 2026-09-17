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
  KEY `indirizzo_ibfk_1` (`id_utente`),
  CONSTRAINT `indirizzo_ibfk_1` FOREIGN KEY (`id_utente`) REFERENCES `utente` (`id_utente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `indirizzo`
--

LOCK TABLES `indirizzo` WRITE;
/*!40000 ALTER TABLE `indirizzo` DISABLE KEYS */;
INSERT INTO `indirizzo` VALUES (2,4,'via Prova','88','Milano','Lombardia'),(4,6,'Via Speranza','54','Pontecagnano Faiano','Campania'),(6,4,'Via Piave','54','San Marzano sul Sarno','Campania'),(12,4,'Via Gattini','89','Agropoli','Campania'),(13,4,'a','a','a','a'),(14,4,'Via Piave','13','San Marzano sul Sarno','Salerno'),(15,7,'Via Mango','66','San Cipriano Picentino','Campania'),(16,8,'a','a','a','a'),(17,9,'a','a','a','a'),(18,9,'4','4','4','4'),(19,10,'5','5','5','5'),(20,11,'5','a','a','a'),(21,4,'a','a','a','a'),(22,4,'v','v','v','v'),(23,4,'Via Piave','53','San Marzano sul Sarno','Campania'),(24,12,'Via Sabato','7','Avellino','Campania');
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
  `id_indirizzo` int DEFAULT NULL,
  `via_spedizione` varchar(255) DEFAULT NULL,
  `civico_spedizione` varchar(20) DEFAULT NULL,
  `citta_spedizione` varchar(100) DEFAULT NULL,
  `regione_spedizione` varchar(100) DEFAULT NULL,
  `immagine_consegna` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_ordine`),
  KEY `ordine_ibfk_1_idx` (`id_utente`),
  KEY `fk_ordine_indirizzo` (`id_indirizzo`),
  CONSTRAINT `fk_ordine_indirizzo` FOREIGN KEY (`id_indirizzo`) REFERENCES `indirizzo` (`id_indirizzo`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `ordine_ibfk_1` FOREIGN KEY (`id_utente`) REFERENCES `utente` (`id_utente`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordine`
--

LOCK TABLES `ordine` WRITE;
/*!40000 ALTER TABLE `ordine` DISABLE KEYS */;
INSERT INTO `ordine` VALUES (2,4,'2026-09-10 17:17:00','In lavorazione',6.00,0.00,6.00,2,NULL,NULL,NULL,NULL,NULL),(3,4,'2026-09-10 17:17:48','In lavorazione',1.00,0.00,1.00,NULL,'Via Gattini','1','Milano','Lombardia',NULL),(4,4,'2026-09-10 18:57:29','In lavorazione',60.00,0.00,60.00,2,NULL,NULL,NULL,NULL,NULL),(5,4,'2026-09-11 14:33:50','Completato',15.00,0.00,15.00,2,NULL,NULL,NULL,NULL,'5432b222-2453-4dc9-afd6-b3abfe172d42.png'),(6,4,'2026-09-11 20:29:29','Completato',75.00,3.00,78.00,2,NULL,NULL,NULL,NULL,NULL),(7,4,'2026-09-12 11:31:07','Completato',60.00,0.00,60.00,2,NULL,NULL,NULL,NULL,'079914c6-686e-4580-8852-c44e0fe15f7e.png'),(8,6,'2026-09-13 11:18:58','In lavorazione',15.00,3.00,18.00,4,NULL,NULL,NULL,NULL,NULL),(9,4,'2026-09-13 17:58:59','In lavorazione',15.00,3.00,18.00,NULL,'Via Piave','13','San Marzano','Campania',NULL),(10,4,'2026-09-14 15:36:46','In lavorazione',120.00,0.00,120.00,NULL,'via Prova','88','Milano','Lombardia',NULL),(11,4,'2026-09-14 15:37:22','In lavorazione',120.00,0.00,120.00,NULL,'via Prova','88','Milano','Lombardia',NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodotto`
--

LOCK TABLES `prodotto` WRITE;
/*!40000 ALTER TABLE `prodotto` DISABLE KEYS */;
INSERT INTO `prodotto` VALUES (1,1,'Stampa Buon Compleanno','Stampa su foglio',15.00,1,'stampa1.png'),(2,0,'Commissione Fullbody','Commissione personalizzata',60.00,1,'commissione1.png'),(3,1,'Stampa Maneki Neko Miku','stampa dimensione a5',15.00,1,'miku_maneki_neko.png'),(4,1,'ciao','studies show that hi',15.00,1,'lea21.png'),(5,1,'a','a',4.00,1,'miku.png'),(6,1,'a','b',5.00,1,'55e0f398-ae8d-4a54-9139-7efd46aff13e.png'),(7,1,'m','m',6.00,0,'d4843b2c-e506-45fd-97f2-878f54aca583.png'),(8,1,'a','a',1.00,1,'0b64fdc4-af4e-4ca8-866b-c16007afebcc.png'),(9,1,'SC','aaaaa',20.00,1,'d42d4e5f-a306-44ee-8b4e-98dac770e6fd.png'),(10,1,'hi','hi',6.00,1,'c397cfbe-cab8-4b4f-bbda-27963c3b68f5.png');
/*!40000 ALTER TABLE `prodotto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riga_ordine`
--

DROP TABLE IF EXISTS `riga_ordine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `riga_ordine` (
  `id_riga` int NOT NULL AUTO_INCREMENT,
  `id_ordine` int NOT NULL,
  `id_prodotto` int NOT NULL,
  `prezzo_og` decimal(10,2) NOT NULL,
  `quantita` int NOT NULL DEFAULT '1',
  `descrizione_comm` text,
  `ref_comm` varchar(512) DEFAULT NULL,
  `file_finale` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id_riga`),
  KEY `riga_ordine_ibfk_1` (`id_ordine`),
  KEY `riga_ordine_ibfk_2` (`id_prodotto`),
  CONSTRAINT `riga_ordine_ibfk_1` FOREIGN KEY (`id_ordine`) REFERENCES `ordine` (`id_ordine`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `riga_ordine_ibfk_2` FOREIGN KEY (`id_prodotto`) REFERENCES `prodotto` (`id_prodotto`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riga_ordine`
--

LOCK TABLES `riga_ordine` WRITE;
/*!40000 ALTER TABLE `riga_ordine` DISABLE KEYS */;
INSERT INTO `riga_ordine` VALUES (1,2,10,6.00,1,NULL,NULL,NULL),(2,3,8,1.00,1,NULL,NULL,NULL),(3,4,2,60.00,1,'a','e4fa7ee9-5a3a-4dde-9a67-7328bdaa0eec.png',NULL),(4,5,9,15.00,1,NULL,NULL,NULL),(5,6,2,60.00,1,'ciao','8d4fd3ba-4219-43aa-8ad4-dfeb8c310bf3.png',NULL),(6,6,9,15.00,1,NULL,NULL,NULL),(7,7,2,60.00,1,'ciaoooooo','1bd1768e-113a-4bda-a13e-cd496389eae9.png',NULL),(8,8,9,15.00,1,NULL,NULL,NULL),(9,9,9,15.00,1,NULL,NULL,NULL),(10,10,2,60.00,1,'aa','777f07b8-8b27-4d9c-9da4-ba2bdcdd1960.png',NULL),(11,11,2,60.00,1,'aa','777f07b8-8b27-4d9c-9da4-ba2bdcdd1960.png',NULL);
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
  `quantita` int DEFAULT '0',
  PRIMARY KEY (`id_prodotto`),
  CONSTRAINT `stampa_ibfk_1` FOREIGN KEY (`id_prodotto`) REFERENCES `prodotto` (`id_prodotto`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stampa`
--

LOCK TABLES `stampa` WRITE;
/*!40000 ALTER TABLE `stampa` DISABLE KEYS */;
INSERT INTO `stampa` VALUES (1,'A5',0),(3,'',0),(4,'',0),(5,'',10),(6,'',0),(7,'',0),(8,'a4',1),(9,'a4',10),(10,'a4',10);
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES (2,'admin@gmail.com','a77967f5c95cb1f7c50985e6926f577d1da69b21fa5d2a6d47befb2c2dd78a74473dd54c2a2d3fd6db4865cafead40baf53ae9077455b82fcc3c06cb86757ec7','Angelica','DAquila','admin'),(4,'gesp@gmail.com','b92a27d59912d702c86f6160a9be5f1d9ac9803a34ea805106805d222e7ca6cf7c89ad2948cb70f80bededba7cde78ba27f717ca9110e7b5119c2a4306d9aaa6','Gennaro','Esposito','cliente'),(6,'mv@gmail.com','6f4deba189e6c8039573a00ac98d498b71698da53bc1a85c89b535cf409d54ca2b4e2ad76b2cbf26248a2f7e3fbf8aa55220ecebb0f635efec0e4df5eeeca1e0','Marco','Verdi','cliente'),(7,'cb@gmail.com','d708877b8350416558aefaa72f6b405ac948a5bc1a8c55eab0fbee6d97e1469728f7f33ea8ea8de14f8bfcaf7fa43595b63d0f687f0cb02911b6f10976c6535d','Cristina','Bianchi','cliente'),(8,'mr@gmail.com','16cbbbb18c3226b49e138f973f36dd124349205a9a6f71508b37dc1d7cef4e03532db1db8aebc36dc4b2fe4891807108d47e3e29426f06a6f30a5aa3a8c4c0aa','Mario','Rossi','cliente'),(9,'a@gmail.com','32643a91f369dc930c2cacd22b365fd5f93695ae24489b3fe6729121046e0a312d76ae48f76bda66570534730738b012dd14c473e174ed253519332fe53bf953','aaaaaa','aaaaaaaa','cliente'),(10,'b@gmail.com','25e15fd4c78b5e1e280307c727c4a9a1b31d33adf9ed606d63e848d0736f122d9ed378a7f0717e9c45148a6d2728e4a6d7792d2a22bde8c99dad35b5685cd575','bbb','bbb','cliente'),(11,'c@gmail.com','bf40756f02fcea91b697819215416dd36c2880d91dba37e98113e5532c74bfe0003439838b1e457f2d31d1e28be8674f2df74e1d7d459a48afbaab43e885869a','ccc','ccc','cliente'),(12,'sabato@gmail.com','c9df758294b7776d70b4255b2315a3935fae962049ac587e2bd35b47f5ba6855676e2a721d22846c857676f4e83fde9944bf9a56da73331359312f7e19d14263','Sabato','Settimana','cliente');
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

-- Dump completed on 2026-09-14 16:45:53
