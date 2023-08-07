-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: cems1
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `activated_exams`
--

DROP TABLE IF EXISTS `activated_exams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activated_exams` (
  `activationCode` varchar(45) NOT NULL,
  `examID` varchar(45) NOT NULL,
  `timeRequestStatus` varchar(45) DEFAULT 'none',
  `actualDuration` int DEFAULT NULL,
  `activeStatus` varchar(45) NOT NULL DEFAULT 'active',
  `activatedLecturerID` varchar(45) NOT NULL,
  `studentsTaken` int unsigned NOT NULL,
  `studentsSubmitted` int unsigned NOT NULL,
  `timeReq` int DEFAULT NULL,
  PRIMARY KEY (`activationCode`),
  KEY `examID_idx` (`examID`),
  KEY `activatedLecID_idx` (`activatedLecturerID`),
  CONSTRAINT `activatedLecID` FOREIGN KEY (`activatedLecturerID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `examID2` FOREIGN KEY (`examID`) REFERENCES `exams` (`examID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activated_exams`
--

LOCK TABLES `activated_exams` WRITE;
/*!40000 ALTER TABLE `activated_exams` DISABLE KEYS */;
INSERT INTO `activated_exams` VALUES ('1110','010101','none',60,'inActive','156134567',0,0,NULL),('1111','010101','approved',60,'inActive','156134567',0,0,20),('1112','010103','none',2,'inActive','156134567',0,0,NULL),('2222','010201','none',60,'inActive','156134567',0,0,NULL),('5556','010201','approved',60,'inActive','156134567',0,0,60),('555A','010103','none',2,'active','156134567',0,0,NULL);
/*!40000 ALTER TABLE `activated_exams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `courseName` varchar(45) NOT NULL,
  `courseID` varchar(45) NOT NULL,
  `professionID` varchar(45) NOT NULL,
  `numExams` varchar(45) DEFAULT '0',
  PRIMARY KEY (`courseID`,`professionID`),
  KEY `professionID_idx` (`professionID`),
  CONSTRAINT `professionID` FOREIGN KEY (`professionID`) REFERENCES `professions` (`professionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES ('Calculus','01','01','4'),('Algebra1','02','01','3'),('Algebra2','03','01','0'),('English A1','04','02','0'),('EnglishA2','05','02','0'),('English B1','06','02','0'),('Classical Mechanics','07','04','0'),('Electromagnetism','08','04','0'),('Introductory to CS','09','03','0'),('OOP','10','03','0');
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `duration_change_request`
--

DROP TABLE IF EXISTS `duration_change_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `duration_change_request` (
  `examActivationCode` varchar(45) NOT NULL,
  `additionalTimeRequested` int NOT NULL,
  `reason` varchar(1000) NOT NULL,
  `status` varchar(45) NOT NULL DEFAULT 'pending',
  PRIMARY KEY (`examActivationCode`),
  CONSTRAINT `activationCode` FOREIGN KEY (`examActivationCode`) REFERENCES `activated_exams` (`activationCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `duration_change_request`
--

LOCK TABLES `duration_change_request` WRITE;
/*!40000 ALTER TABLE `duration_change_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `duration_change_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exams`
--

DROP TABLE IF EXISTS `exams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exams` (
  `examID` varchar(45) NOT NULL,
  `duration` int NOT NULL,
  `authorID` varchar(45) NOT NULL,
  `professionID` varchar(45) NOT NULL,
  `courseID` varchar(45) NOT NULL,
  `examType` varchar(45) NOT NULL DEFAULT 'online',
  `lecturerNotes` varchar(45) DEFAULT NULL,
  `studentNotes` varchar(45) DEFAULT NULL,
  `activatedCounter` varchar(45) DEFAULT '0',
  PRIMARY KEY (`examID`),
  KEY `professionID1_idx` (`professionID`),
  KEY `courseID1_idx` (`courseID`),
  KEY `authorID_idx` (`authorID`),
  CONSTRAINT `authorID1` FOREIGN KEY (`authorID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `courseID1` FOREIGN KEY (`courseID`) REFERENCES `courses` (`courseID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `professionID1` FOREIGN KEY (`professionID`) REFERENCES `professions` (`professionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exams`
--

LOCK TABLES `exams` WRITE;
/*!40000 ALTER TABLE `exams` DISABLE KEYS */;
INSERT INTO `exams` VALUES ('010101',60,'156134567','01','01','online','','','2'),('010102',30,'156134567','01','01','online','','','0'),('010103',2,'156134567','01','01','manual','','','2'),('010201',60,'156134567','01','02','online','','','3');
/*!40000 ALTER TABLE `exams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exams_of_students`
--

DROP TABLE IF EXISTS `exams_of_students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exams_of_students` (
  `examActivationCode` varchar(45) NOT NULL,
  `studentID` varchar(45) NOT NULL,
  `submitted` varchar(45) NOT NULL DEFAULT 'false',
  `grade` int NOT NULL DEFAULT '0',
  `gradeChanged` varchar(45) NOT NULL DEFAULT 'false',
  `lecturerNotes` varchar(1000) DEFAULT NULL,
  `reasonOfGradeChange` varchar(45) DEFAULT NULL,
  `availableForViewing` varchar(45) NOT NULL DEFAULT 'false',
  `solvingTime` int DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`examActivationCode`,`studentID`),
  KEY `studentID_idx` (`studentID`),
  CONSTRAINT `examActivationCode` FOREIGN KEY (`examActivationCode`) REFERENCES `activated_exams` (`activationCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exams_of_students`
--

LOCK TABLES `exams_of_students` WRITE;
/*!40000 ALTER TABLE `exams_of_students` DISABLE KEYS */;
INSERT INTO `exams_of_students` VALUES ('1110','123456789','false',50,'false',NULL,NULL,'false',2,'finished'),('1111','123456789','false',0,'false',NULL,NULL,'false',0,'finished'),('2222','123456789','false',50,'false',NULL,NULL,'false',0,'finished'),('5556','123456789','false',50,'false',NULL,NULL,'false',3,'finished');
/*!40000 ALTER TABLE `exams_of_students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecturers_professions`
--

DROP TABLE IF EXISTS `lecturers_professions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecturers_professions` (
  `lecturerID` varchar(45) NOT NULL,
  `professionID` varchar(45) NOT NULL,
  PRIMARY KEY (`lecturerID`,`professionID`),
  KEY `professionID3_idx` (`professionID`),
  CONSTRAINT `lecID` FOREIGN KEY (`lecturerID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `professionID3` FOREIGN KEY (`professionID`) REFERENCES `professions` (`professionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecturers_professions`
--

LOCK TABLES `lecturers_professions` WRITE;
/*!40000 ALTER TABLE `lecturers_professions` DISABLE KEYS */;
INSERT INTO `lecturers_professions` VALUES ('156134567','01'),('100100100','02'),('211575790','03'),('156134567','04'),('211575790','04');
/*!40000 ALTER TABLE `lecturers_professions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manual_exams`
--

DROP TABLE IF EXISTS `manual_exams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manual_exams` (
  `examID` varchar(50) NOT NULL,
  `examFile` longblob,
  PRIMARY KEY (`examID`),
  CONSTRAINT `examId_inExams` FOREIGN KEY (`examID`) REFERENCES `exams` (`examID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manual_exams`
--

LOCK TABLES `manual_exams` WRITE;
/*!40000 ALTER TABLE `manual_exams` DISABLE KEYS */;
INSERT INTO `manual_exams` VALUES ('010103',_binary 'PK\0\0\0\0\0!\0ß¤\ÒlZ\0\0 \0\0\0[Content_Types].xml ¢( \0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0´”\Ën\Â0E÷•ú‘·Ubè¢ª*‹>–-R\é{Vı’Ç¼ş¾QU‘\nl\"%3÷\Ş3VÆƒ\ÑÚšl	µw%\ë=–“^i7+\Ù\×\ä-d&\á”0\ŞA\É6€l4¼½L60#µÃ’\ÍS\nOœ£œƒXø\0*•V$z3„ü3\à÷½\Ş—\Ş%p)Oµ^ “²\×5}nH\"d\Ùs\ÓXg•L„`´‰\ê|\éÔŸ”|—PrÛƒsğ\Z?˜PW\ìtt4Q+\È\Æ\"¦wa©‹¯|T\\y¹°¤,N\Û\àôU¥%´ú\Ú-D/‘\ÎÜš¢­X¡İÿ(¦¼<E\ã\Û)‘\à\Z\0;\çN„L?¯FñË¼¤¢Ü‰˜\Z¸<Fk\İ	‘h¡yö\Ï\æ\ØÚœŠ¤\Îqôi£\ã?\ÆŞ¯l­\Îi\à\01\é\Ó]›H\Ög\Ïõm @\È\æ\Ûûmø\0\0ÿÿ\0PK\0\0\0\0\0!\0‘\Z·\ï\0\0\0N\0\0\0_rels/.rels ¢( \0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0¬’Áj\Ã0@\ïƒıƒÑ½Q\ÚÁ£N/c\Ğ\Û\Ù[IL\Û\Øj\×şı<\Ø\Ø]\éaG\Ë\ÒÓ“\ĞzsœFu\à”]ğ\Z–U\rŠ½	\Öù^\Ã[û¼x\0•…¼¥1x\Öp\â›\æöfı\Ê#I)Êƒ‹YŠ\Ï\Z‘øˆ˜\ÍÀ\å*Dö\å§i\")\Ï\Ôc$³£qU\×÷˜~3 ™1\Õ\ÖjH[{ª=E¾†º\Î~\nf?±—3-\ÂŞ²]\ÄTê“¸2j)õ,\Zl0/%œ‘b¬\n\Zğ¼\Ñ\êz£¿§Å‰…,	¡	‰/û|f\\Zş\çŠ\æ?6\ï!Y´_\áoœ]Aó\0\0ÿÿ\0PK\0\0\0\0\0!\0)`3\0\0\0\0\0\0\0word/document.xml¤–mo\Û €¿O\Ú°ü=\Å/‰“XMªµQ§~˜T­\í ¿¨,ÀyÙ¯\ßa\Ç/›·\Êq¥(˜ƒ{\î8¸ƒÛ»Ë­•*|c»7mQND”ñdc¿½>\ÎV¶¥4\æ\Î§ûL•}·ıú\åöF‚”Œrm‚«ğXj]„)’R†\Õ\rËˆJ\Äú††Dg„¢£ò×©¾\n)U\n\ì=`~ÀÊ¾\à\Èi-’ø\Ê8G$\ÅR\ÓS\Çp¯†,\Ğ\Z­† oV\è¹C”5*@Æ«h>	^\rH‹i¤,.˜Fò†¤\å4’?$­¦‘Ç‰\r¸((‡ÁXH†5te‚–\ïe1pu¶\ÏòLŸ\é\rgü}‚G \Õ˜]MX\"&\"šûQC»”<¼\è\ÏZ}\ãzX\ë_šVƒ\æ\ãÌ‚¹5¢\'+\İ\è\Ê1±«\Õw—\ÂRE\rIšCWiV´ÕM¥Á`\Ú@\àÀòfŞ±pG¦\ÚÿJÛ®Ş†8\Æı\ËŞ±¼öüc¢\ëŒ\ØMƒh5Æ¸ğ§\Í\Æ\'¸3<)4½\àº#‹Oğ€€Ğ‘—E\ÃX]ˆt\Ùm8\ÙÈ´j8õ®N\Ö\ÖYÿv¦ˆÊ«\ßøa\Z£\Şc©HG\éu¸f\Ñ\Å\Z§XµIcˆôº.ZÜ™õ\â]$ŸKª\ïR”EG\Ë>G{\ê\Ê\ë\Ñ<t®`]’³_0\Ô\çœyIqU—‘ğ)\áB\â}AªY-Vµ\æiªOzª\äf¯-S¯\ì-¼\Ğö\":›¶€±yX`‰Ÿ\à€«û‡\Ç]poWR¸ß´‘.‹õ\ãò\Û¤!¼£Ÿ\ÛqÁÂ½÷[Ñ³4Bw9w=¯\îhŒ\Ë\\÷F*“\Ï\Ò4²nr\Ì˜ÀpP>{{±\Ñö]†Q7{¬\Ê1\Ô\Û\×4Sü°¥©\Ò=afiaA¸É»—œ˜K›\Û\×±\Å0/q^\Í2]³jã†§(\Ñ\Ïr\ìÚ«&/¿`\ê¨\ë®\Í\í~!\Í\Ü`å¯Œ«f\ÂlˆZ@¹w\çs§beIª»\î^h-X\×\Ïi\ÜM)(\Ø]:+Ó…Ğ½nR\êª\ë\Ô\æˆ\ÈHU	­\çTbx¥—æ „y\Æ\és¦	x\é•j\Ö]}Ö§uû\ío\0\0\0ÿÿ\0PK\0\0\0\0\0!\0\Öd³Qô\0\0\01\0\0\0word/_rels/document.xml.rels ¢( \0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0¬’\Ëj\Ã0E÷…şƒ˜}-;}PB\älJ!\Û\Öı\0E?¨,	\Íôá¿¯HI\ë\Ğ`ºğr®˜sÏ€6\Û\ÏÁŠwŒ\Ô{§ \ÈrèŒ¯{\×*x©¯\îAkWk\ë*‘`[^^l\ĞjNK\ÔõD¢8R\Ğ1‡µ”d:4e> K/ƒ\æ4\ÆVm^u‹r•\çw2NP0Å®Vwõ5ˆjø¶oš\Ş\àƒ7o:>S!?pÿŒ\Ì\é8JX[d“0KD\çEVKŠ\Ğ‹c2§P,ªÀ£Å©Àa«¿]²\Ó.ş¶\Æï°˜s¸YÒ¡ñ+½·Ÿ\è(!O>zù\0\0ÿÿ\0PK\0\0\0\0\0!\0¶ôg˜\Ò\0\0\É \0\0\0\0\0word/theme/theme1.xml\ìYK‹G¾ò†¹\Ëz\Í\èa¬5\ÒHòk\×6Şµƒ½Rk¦­i\Ñ\İÚµ0†`Ÿr	œC¹\åB1\Ä\ä’c°Iœ‘\êI3-õÄ]ƒ	»‚U?¾ªşºªºº4s\á\âı˜:G˜Â’[=WqœŒØ˜$aÇ½}0,µ\\GH”Œe	\î¸,Ü‹;Ÿv—±ò‰8:n$\å\ì|¹,F0Œ\Ä96\Ã	\ÌM‘„.\ËcAoLËµJ¥QI\\\'A1¨½1™v”Jwg¥|@\á_\"…\ZQ¾¯TcCBc\ÇÓªúP\î!\Úqa1;>À÷¥\ëP$$LtÜŠşs\Ë;\Êk!*dsrCı·”[\nŒ§5-\Ç\ÃÃµ \çù^£»Ö¯Tn\ã\ÍAc\ĞX\ë\Ó\04\ZÁNS.¦\Îf-ğ–\Ø(mZt÷›ız\ÕÀ\çô×·ğ]_}¼¥Mo?™\rs ´\éo\áı^»\×7õkP\Úllá›•n\ßk\Zx\rŠ(I¦[\èŠß¨«İ®!F/[\ám\ß6kKx†*\ç¢+•OdQ¬\Å\è\ãC\0h\ç\"IG.fx‚F€%‡œ8»$Œ ğf(a†+µÊ°R‡ÿ\ê\ã\é–ö(:QN:\Z‰­!\Å\Ç#Nf²\ã^­nò\êÅ‹—¿|ôû\ËÇ_>úu¹ö¶\Üe”„y¹7?}ó\Ï\Ó/¿ûñÍ“o\íx‘Ç¿ş\å«\×üù_\ê¥A\ë»g¯Ÿ?{õı\×ıü\Ä\ïrt˜‡\ç:>vn±6hY\0ò÷“8ˆ\ÉKt“P )z #}}(²\àzØ´\ã\é\Â¼4¿gŞø\\ğZÀ=\Æhqë®©µòV˜\'¡}q>\Ï\ãn!td[;\Øğò`>ƒ¸\'6•A„\rš7)¸…8Á\ÒQslŠ±E\ì.!†]÷Èˆ3Á&Ò¹Kœ\"V“C#š2¡\Ë$¿,lÁß†mö\î8=Fm\êûø\ÈD\Â\Ù@Ô¦SÃŒ—\Ğ\\¢\Ø\Ê\Å4\ÜE2²‘\Ü_ğ‘ap!Á\Ó!¦\ÌŒ±6™|aĞ½i\Æ\îö=ºˆM$—djC\î\"\Æò\È>›ŠgV\Î$‰ò\Ø+b\n!Šœ›LZI0ó„¨>ø%…\î¾C°\áî·Ÿ\íÛ†\ì¢f\æ\Üv$03\Ï\ã‚N¶)\ïò\ØH±]N¬\ÑÑ›‡Fh\ïbL\Ñ1\Zc\ìÜ¾bÃ³™aóŒô\Õ²\Êel³\ÍUdÆª\ê\'X@­¤Š‹c‰0Bv‡¬€\Ï\Şb#ñ,P#^¤ùú\Ô™\\u±5^\éhj¤R\ÂÕ¡µ“¸!bc…ZoF\È+\Õöx]p\Ã\ïr\Æ@\æ\Ş\È\à÷–\ÄşÎ¶9@\ÔX ˜U†-İ‚ˆ\áşLD\'-6·\ÊM\ÌC›¹¡¼Qô\Ä$yk´Qûø¯ö\n\ã\ÕO-\ØÓ©w\ìÀ“T:E\Éd³¾)\ÂmV5\ãcò\é5}4Onb¸G,Ğ³šæ¬¦ù\ß\×4E\çù¬’9«d\Î*»\ÈG¨d²\âE?Z=\è\ÑZ\âÂ§>B\é¾\\P¼+t\Ù#\àì‡0¨;ZhıiAs¹œ9\Òm‡3ù‘\Ñ~„f°LU¯Š¥\êP83& p\Ò\ÃV\İj‚\Î\ã=6NG«\Õ\ÕsM@2‡\Âk5ešLG\Í\ì\ŞZ½\î…úAëŠ€’}¹\ÅLu‰\æjğ-$ô\ÎN…E\ÛÂ¢¥\Ô²\Ğ_K¯À\å\ä õH\Ü÷RFn\Òc\å§T~\å\İS÷t‘1\Ím\×,\Ûk+®§\ãiƒD.\ÜL¹0Œ\àò\Ø>e_·3—\Zô”)¶i4[\Ã\×*‰l\äš˜=\ç\Î\\\İ5#4\ë¸ø\É\Íxú„\ÊTˆ†I\ÇÉ¥¡?$³Ì¸}$¢¦§\Òı\ÇDb\îPC¬\ç\İ@“Œ[µ\ÖT{üDÉµ+Ÿ\åôW\Ş\Éx2Á#Y0’ua.Ub=!Xu\ØH\ïG\ãc\ç\Îù-†ò›UeÀ1rm\Í1\á¹\àÎ¬¸‘®–G\Ñxß’QDgZ\Ş(ùd\Âu{M\'·\ÍtsWf¹™\ÃP9\éÄ·\îÛ…\ÔD.i\\ \êÖ´çw\É\çXey\ß`•¦\î\Í\\\×^åº¢[\â\äBZ¶˜AM1¶P\ËFMj§X\ä–[‡f\ÑqÚ·ÁfÔªbUW\ê\ŞÖ‹mvx\"¿\Õ\êœJ¡©Â¯‚\Õ+\É4\è\ÑUv¹/9\'÷A\Å\ïzA\ÍJ•–?(yu¯Rjù\İz©\ëûõ\êÀ¯Vú½\ÚC0ŠŒâªŸ®=„ût±|o¯Ç·\Ş\İÇ«RûÜˆ\Åe¦\ë\à²\Ö\ïî«µ\âw÷\Ë<hÔ†\íz»\×(µ\ë\İa\É\ë÷Z¥v\Ğ\è•ú \Ùö¿\Õ>t#\röºõÀkZ¥F5J^£¢\è·Ú¥¦W«u½f·5ğº—¶†¯¾W\æÕ¼vş\0\0ÿÿ\0PK\0\0\0\0\0!\0¥%\Í%\0\0\ì\0\0\0\0\0word/settings.xml´VMo\Û8½/°ÿÁ\Ğy}Dvb¡N;ñ&E¼]TY\ì™)‹)$e\Ç-ö¿\ï-§)\n§E/65o\æ\Íhø8Ô»÷Ïœ¶Di*šyŸEÁˆ4¥À´\ÙÌƒW\ã\Ë`¤\rj0b¢!ó`Otğş\ê÷\ß\Ş\í2MŒ7=ŠFg¼œµ12C]Ö„#}&$i\0¬„\â\ÈÀ£Ú„©§VKÁ%2´ Œš}˜D\Ñ4\èi\Ä<hU“õcNK%´¨Œ\r\ÉDUÑ’ô>B’·¹e\ËIc\\\ÆP5ˆF\×Tj\Ï\Æ”\rÀÚ“l¿÷[Î¼\ß.NxİPøqJy6@*Q­aƒ8ó\ÒfHœ¾\":\ä>ƒ\Üı+:*#·:®|ò6‚\äÁ´$\Ïo\ã¸\ì9Bˆ<\æ¡øm<\Ó\ZO¬˜#\r®\ßÄ’ø¾†6T#}P‘e$o+jr \Ûó¡Gš¢šz …Bª;“½dx™\İo\Z¡PÁ \Îväª³¿\ĞDû\ç–\ä\Ù\Ùm‚+˜Ÿ…\à£]&‰*\á À€‰¢ ´\0\ÈST¹A(2-	cnâ”Œ È¸\Ë6\nq˜\Ş\âb0©P\Ë\Ì#*r#$8m¼\ØE\ÒS–5R¨4D\å•À¶Q‚y?,şf	sGÁ±\è#\Ü\ZVy7\Ñ ¢A^õÅ”ZLle­¢§\ï‰\rp\Ù\ã\ÉqÊ¯	˜ÀŠbòh[œ›=#+(>§Ÿ\Éuƒ?´\ÚP`t³\ê\'*ø^¤±™?‚(÷’¬2-´\é%s;±bT®©RB\İ7´ñË’Ñª\"\nP\Ğ\Ú\Z\äC•Ø¹>\ß„\á\âûEy[Mşg8“\ç Ë§…0Fğ»½¬¡\×?·“N\ï\á±|\áú\Æ\Ú/>	a®Q|‘\ÆI\ÒUj\ÑS\Ét/Î¿…¬n£››´\Ï\ßgå™½úşV~e¥;\â]\ÄñBQ4Z\Û\Ë1´…zZ\Ğ\Æ\ã	D‘¼-<8w€æˆ±4\Ñ®<\ÃT\ËR¹5[#µx{õM+Ì‘.;—ˆúS‰Vv\èN!\ÙIÒ»\Äi\ÚG\Ò\Æ<P\î\íº-r\ÕÀ\Ì<‚\Ú\Ü*×§¡=»\ÌÀ»£ı€œT\nŠA\0Hó\ë^KL\åVd¤\ì\äTl\âyÀ\è¦6±U€\'Q\î¡\Ø$=–8,\é0÷€Jûj\à\İ/[\âmG~\ç\Şv>\ØRoK\Û\Ä\Û&ƒm\êmSk«a€(˜\æO l¿´öJ0&v\ß\rø+S\×]#Inºaú¡Ÿşz´\Í\È3\\%Sß¦’b\íÍ’Lmx\ï\Í\Ğ^´æ…¯Å¬³|\É`o\İş,‡/‚Æ¿ª\Å^B%=\æ{^w\ËYW8£\Zæ€„k\È\å±?§å½½)\ÓÎ¦8·³¸\ëË¸Qûş‰T¤	\î1:\éB¿\\ß¦ñô\"Š\ÆIºˆ\Çi2›Œg—³d¼Šf³U´\\.\Ó\Ë\Ùı)õŸ\éWÿ\0\0ÿÿ\0PK\0\0\0\0\0!\0\ÃÅš±\0\0s\0\0\0\0\0word/styles.xml¼[sÛº\Ç\ß;\Ó\ïÀ\ÑSû\ÈW9ñ\çŒ\ã$µ§vO\ä4\Ï	Y¨AB\åÅ—~ú %A^‚\â‚[¿$\Öe\0ñ\Ç‰\åM¿ışœ\Ê\è‘\ç…P\Ù\Ùhÿı\Ş(\âY¬‘İŸ~\Ş}{÷a%\Ë&U\Æ\ÏF/¼ış\é¯ù\í\é´(_$/\"\rÈŠ\Ó4>-\Êry:ñ‚§¬x¯–<\Ó\ÎU²R¿\Ì\ï\Ç)\Ëª\å»X¥KVŠ™¢|\ì\íMF\r&\ïCQó¹ˆùW)\ÏJ?Î¹\ÔD•±,V´§>´\'•\'\Ë\\Å¼(ôF§²\æ¥Ldk\Ìş\0¥\"\ÎU¡\æ\å{½1M,J‡\ï\ïÙ¿R¹\ã\0\00‰ù3ñ¡aŒu¤\Ë	3YsD\âp\Â:\ã\0Š¤L(\ÊÁj\\\Ç&–•lÁŠ…K\ä¸N¯q/©£4>½º\ÏT\ÎfR“´\ê‘.²`ó¯\Ş~óŸı“?\Û÷\Í&Œ>i/$*ş\Âç¬’ea^\æ·yó²yeÿû¦²²ˆNYq§;¨[I…nğò<+\ÄH\ÂYQ‚µ~¸0´~¥óög‘ˆ\ÑØ´XüWø\È\ä\Ù\è\à`õÎ…\éÁ\Ö{’e÷N\Ûg#½û95Ü™&Xşnzn¾:n6¥ş\ß\ÙÀ\å\ëW¶©%‹…%³yÉµ±÷\'{*…\É#\ÇW/~Tf¸YUª¦¨ÿ_c\Ç`Œµßµû§uÒŸòùµŠx2-õg#Û–~ó\ç\Õm.T®\Í\Ù\è£mS¿9å©¸I\Â3\ç‹\ÙB$ü×‚g?l\Şÿó›M\Í±ª2ı÷\á\É\Ä\ê.‹\ä\ësÌ—&õ\èO3fTøn¤ùv%6\Ûğÿ¬`û\ÍØ·\Å/83ù7\Ú°\İG!LD\álm;³zµ\íö[¨†ßª¡£·j\èø­\Zš¼UC\'o\ÕĞ‡·j\ÈbşŸ\r‰,Ñ©\Ş~6¨»87¢9³¡9/¡9« 9\' 9‰\æx\æ1šã™¦N©b\ß,t&û¡g¶wsw\ï#Â¸»w	a\Ü\İ{€0\î\î„\Æİ\ßÃ¸»\Óywwö\ã\îN\Öxn½ÔŠ®´Í²r°\Ë\æJ•™*yTò\ç\á4–i–-Jixf§\Çs’$ÀÔ™­\Ù¦\ÅÌ¾\Ş=C¬I\Ã÷ç¥©\í\"5\æ\â¾\Êy1¸\ã<{\äR-yÄ’Dó9/«\Ü3\"!s:\çsó,\æ”›j*Á(«\ÒÁ\Ü\\²{2\Ï\â\á[I’\ÂzB\ëúyaL\"&u\Ê\â\\\r\ïšbdù\áZ\Ã\Ç\Ê@¢Ï•”œˆõfŠY\Öğ\ÚÀb†—3¼2°˜á…£\Õ54¢‘jhD\ÖĞˆÆ­ŸT\ã\ÖĞˆÆ­¡[C>nw¢”6Å»«ış\Ç\î.¤2§÷c*\î3¦\0\Ãw7\Í1\Ó\è–\å\ì>g\ËEdC·c\İmÆ¶óY%/\Ñ\Å>mM¢Z\×\Û)r¡·Zd\Õğİ¢Q™k\Í#²×šGd°5o¸\Ånô2\Ù,\Ğ.i\ê™i5+[MkI½L;e²ª´\Ã\İ\Æ\Ê\á3lc€o\"/\ÈlĞ%˜Á\ß\Ír\Ö\ÈI‘ù6½Ş±\rk¸­^g%\Ò\î5H‚^J?Ğ¤\áË—%\ÏuYö0˜ôMI©xBGœ–¹ª\çškù+I/\ËM—V[+m!ú\ï\êW D7l9xƒn%n_ß¥LÈˆnqywsİ©¥)3\ÍÀ\Ğ\0?«²T)³9ø·_|öwš\ë\"8{!\Ú\Ús¢\ÃCv!v25I%D$½\Ì™ Ù‡Z\Ş?ù\ËL±<¡¡\İæ¼¾\æ§\äD\Ä)K—õ¢ƒÀ[:/>\éüC°\Z²¼±\\˜\ãBT¦º#9‡\r‹jöoOu\ßUDrdèª´\Ç\íR\×F\Ó\á†/¶pÃ—VM½{0ó—`c·p\Ã7vGµ±’…ğB\r\æQm\îŠG½½Ã‹¿†§¤\Êç•¤ÀlW@²!T²J³‚r‹-pƒ-z{	§Œ\å’³¼\ä\"!\ÃÂ¨”°0*,ŒJ#`ø:løe:løµ:5Œh	\àÀ¨\æ\é\îŸ\è,£šgF5\Ï,ŒjY\Õ<;üñù\\/‚\év1’j\Î9HºMVòt©r–¿!¿J~\ÏÖ´\Û\\\Í\Í\Í *«/\â&@šcÔ’p±]\ã¨Dş\Ågd]3,\Ê~eR*Etlm³Ã±‘\Û×®\í\n³÷n\îÂ­d1_(™ğÜ³MşX]/O\ë\Û2^w\ßv£\×a\Ïkq¿(£\éb}´\ß\ÅLövF®\nö­°\İ\r¶ùduK[\Ø\rOD•®:\no¦˜ö¶3z+øhwğf%±y\Ü3¶9\Ù¹Y%oEôŒ„m~\èi}º\Ù\å‡/,h\']óg]\ãy&\ßI\×,Z·6\Û5‘Ö‘mSğ¤kmY%:cs¶\0ª\Ó\Ï3şø~\æñ\Çc\\\ä§`\ì\ä§ôö•\Ñe°üQ˜=;&i\Úö\ÖWO€¼oÑ½2çŸ•ª\Ûop\êS×•^8eZ9‡ıO\\meÿ8öN7~D\ï¼\ãGôN@~D¯L\ä\rG¥$?¥wnò#z\')?­\à—­`<.[Áøl)!\ÙjÀ*À\è½ğ#\ĞF…´Q¬ü”QAxQ!mTˆ@\"\ĞF…0œQa<Î¨0>Ä¨bTHA\"\ĞF…´Q!mTˆ@5pm\ï\r2*¤ \nh£BÚ¨v½8À¨0gTbTH	1*¤ \nh£BÚ¨6*D \n(£‚ğ £B\nÚ¨6*D Z\ßjnT3*Œ1*¤„R\ĞF…´Q!mTˆ@\"\ĞF…”QAxQ!mTˆ@\"\ĞFµ\'\Æ\ãŒ\n\ãCŒ\n)!F…´Q!mTˆ@\"\ĞF…´Q!eTdTHA\"\ĞF…ˆ®ùÙœ¢ô]f¿?\ê\é½b¿ÿ©«¦S?\Ü[¹]\ÔaÔªW~Vÿ{>+õµ\Şxxh\ë~1“B\ÙCÔ\Ó\ê.\×^:ñù\ÇE÷>.}\àC—š{!\ì9S\0?\ê	©uMy7yG]3İ«Î£®\ì\ëF‚\İ\àQWÒµ¾\\]”¢wG ¸+\Í8Áûğ®l\í„\Ã!\î\Ê\ÑN \á®\Ì\ì\Â\î\Ê\ÇN\àqd’ó\ë\è\ã\ã4Y__\n]\Ó\Ñ!œø	]\ÓjµJ\Ç\Ğ}Eóúª\ç\'ô•\ÑO@\é\é\Å\à…õ£\Ğ\nûQaRC›a¥7ªŸ€•\Z‚¤˜p©!*Xjˆ\n“\Z&F¬Ô€•:<9û	ARL¸\Ô,5D…I\rweX©!+5$`¥¸CöbÂ¥†¨`©!*Lj¸¸\ÃJ\r	X©!+5$I\r0\áRCT°\Ô&5¨’\ÑRCVjHÀJ\r	ARL¸\Ô,5DuIm¢lIR\Ø	\Ç-Âœ@\Ü\Ù	\Ä%g\'0 Zr¢«%‡X-A­Vš\ãª%W4?¡¯z~B_ı”^^X?\n­°&5®Zj“:Ü¨~Vj\\µ\ä•\ZW-uJ«–:¥\ÆUK~©q\ÕR›Ô¸j©M\êğ\ä\ì\'I«–:¥\ÆUKR\ãª%¿Ô¸j©Mj\\µ\Ô&5®Zj“z\àÙ‹	—\ZW-uJ«–üRãª¥6©q\ÕR›Ô¸j©Mj\\µ\ä•\ZW-uJ«–:¥\ÆUK~©q\ÕR›Ô¸j©Mj\\µ\Ô&5®ZòJ«–:¥\ÆUKRãª¥\"5MY^FtÏ‹»dÅ¢d\ÃNø3\Ëy¡\ä#O\"\ÚM½Fm\åøi\ë\ç¯\Ûş\ZŸş~©\Ç\Ì<İ¹])©Ÿ\0\Û\0\í¯’õ\ÏT™`Ó“¨ùY°\æm\Û\á\ætmİ¢\r„M\Å\İV\Ü<»\Ê\ÓTó\ÚõMTö	´¯ö<¨\Övd3W\ßn†t3^õ÷¶F«³ß¥™ğ}¶†\è£\Ú3¾~l’À®\êş\Ìdı“iú«,Ñ€§\æ\ç\Â\ê&Ï¬F\é\Ï/¸”7¬ş¶Zú¿*ù¼¬?\İß³,xõù¬~ú7>·i\Úow¦~\Ùül›g¼\ë\çñ7\×x§¤\ÉE-\Ãm/f:Ò›¾­ş*>ı\0\0ÿÿ\0PK\0\0\0\0\0!\0\ï\n)NN\0\0~\0\0\0\0\0word/webSettings.xmlœ\Ó_k\Â0\0ğ÷Á¾CÉ»¦\Ê)Va\Ç^\Æ`\Ûˆ\éÕ†%¹’‹«\î\Ó\ïÚ©søb÷’ÿ÷\ã.!ó\å\Î\Ù\äô¹\rS‘€\×X¿\É\Åû\Ûj0	E\åe\ÑC.ö@b¹¸½™7Y\ëWˆ‘ORÂŠ§\Ì\é\\T1Ö™”¤+pŠ†Xƒ\ç\ÍƒS‘§a#\n\Ûz \Ñ\Õ*šµ±&\î\å8M§\âÀ„k,K£\áõÖ]¼`YDO•©\é¨5\×h\r†¢¨ˆ\ëqö\Çs\Êø3º»€œ\Ñ	\Ë8\äbu‡\Òn\ä\ì/0\éŒ/€©†]?cv0$G;¦\è\çLO)Îœÿ%sP‹ª—2>Ş«lcUT•¢\ê\\„~IMN\ÜŞµw\ätö´ñ\ÔÚ²Ä¯ğ\Ã%Ü¶\\\ÛuC\Øu\ëm	bÁ\ëhœù‚†û€\rA\í²²›—\çG\È?¿fñ\r\0\0ÿÿ\0PK\0\0\0\0\0!\0¸²c\0\0‚\0\0\0\0\0word/fontTable.xmlÜ“ÛŠ\Û0†\ï}£ûeç°©Yg\é¶(”^,\ÛPd9\ÕÁh”8yûd\'\r„Àº\ĞB\ë[şGóy\æ·\æ\áñ U²¤5%\É&”$\Âp[I³-\É÷\×õİ’$\à™©˜²F”\ä(€<®Ş¿{\èŠ\Ú\Z	\æ(4/I\ã}[¤)ğFh\Û\nƒÁ\Ú:\Í<¾ºmª™û±k\ï¸\Õ-ór#•ô\Ç4§tAŒ{\ÅÖµ\ä\â³\å;-Œù©\n‰\Ö@#[8Ñº·\Ğ:\ëª\ÖY.\0°g­zfÒœ1\Ù\ì\n¤%wl\í\'\Ø\ÌPQDazF\ãJ«_€ù8@~Xpq\ÇXŒ3/9²\Z\ÇYœ9²º\àü^1\0¨|ÕŒ¢\ä\'_Ó\Ë<k4—D1®¨ùw\ÔÁ#Í‹/[c\Û($\á_Oğ\Ç%\î\ØxÄ¥8D=´@V\Ã($]a˜\Æ\ÌOLÉ“1\Ğ2cAd\Û3U\ìaM\ç4ô’\Ó†;I\ÃF\Ş0\"@ú´—k¦¥:T\è$@h¥\ç\ÍI\ß3\'C\Õ}\ä;\ØĞ’<\Ï(ÍŸ\×k\Ò+V‡\ç;Ÿ\İ?\rJ¾¯ƒ2=+4(<r\âk\Ösx\äœ÷\à7\ÓŞ+\'>bY\ê†OtôND7ş¬ô\ÊT\î—ó¿\âÃ«\Ô’o¢K^¬f\æ†#9:2\Å\Ó1‹\'d:\Ê¹ÿ#ÃŒ$_\å¶ñ7\'%\Ì\Ç:)\ÃV?\0\0ÿÿ\0PK\0\0\0\0\0!\0±›L}\0\0ñ\0\0\0docProps/core.xml ¢( \0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0Œ’\ÑJ\Ã0†\ïß¡\ä¾M»Ê\Òv ²+‚Å»˜·¸6\rI¶®!\"x\'øR}\Óv\ë¬\îÂ»sò\ç\ï\éŸÄ“m9Pš\"A\ç#-‹\İÍ§\î9r´!‚‘¬ \n4š¤§\'1•-Ü¨B‚2´c„¨L\Ğ\Òa¬\ér¢=K+>*\'Æ¶j%¡+²\0<òı1\ÎÁFÁ¡+{G´³d´·”k•µŒb\È a4¼\0X*\×GZ\å™sSI8Š\îÅ\ŞjŞƒeYzeØ¢vÿ\0?Ì®o\Û_u¹h²¢€Ò˜\Ñ\Èp“A\Z\ãCi+½~zjºã¾±5U@L¡\Òú«~«?úµş¨\ß[j¯4™¯ *Å´tc ©\â\ÒØ›\ì\Ü–Îˆ63{µ\Ï\ØEõûCfFÁ†7o#\rZ¢o\ã]\Ğ\İrÀP\ÔÅ¹W\î\ÃË«ù¥#ºş\Ø\r\Âyp…£\È÷›ıó\Ã|·ÀÿÃ¡\ãŞ ‹høH\Óo\0\0\0ÿÿ\0PK\0\0\0\0\0!\0\ÍÃ²^q\0\0\Ç\0\0\0docProps/app.xml ¢( \0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0œR\ËN\Ã0¼#ñQ\î\Ô)R¡j…8ğ¨\ÔĞ-{“X8¶eDÿMÓ† nø´3\ëÍ¬\rw_\É>1D\íl™\ÏgE¡•NiÛ”ù[õpq“g1	«„q\Ë|1¿\ã\çg°\ÎcH\ZcF6–y›’¿e,\Ê;gÔ¶Ô©]\èD\"\Z\æ\êZK\\9ùÑ¡M\ì²(®~%´\nÕ…óAñö3ıWT9\Ùû‹\Ûj\ïIC…7\"!\é\'\ÍL¹\ÔY¨\\¦\Òò9\Ñ#€µh0ö\ÜPÀ\Îù\r°¡€e+‚‰ö\Ç°	„{ï–\"\Ñbù³–ÁEW§\ìõ\à6\ëÇM¯\0%Ø ü:\í9IM!<i;\Ø\n²D„o\ŞF).);¯…‰ì‡€¥ë¼°$\ÇÆŠô\Şã›¯Üª_\Ãq\ä79É¸Ó©\İx!\É\Â\âzšvÒ€\r±¨\Èş\è`$\à‘#˜^fmƒ\êt\ço£\ß\ßvø—|~5+\èv\â(öøaø7\0\0\0ÿÿ\0PK-\0\0\0\0\0\0!\0ß¤\ÒlZ\0\0 \0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0[Content_Types].xmlPK-\0\0\0\0\0\0!\0‘\Z·\ï\0\0\0N\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0“\0\0_rels/.relsPK-\0\0\0\0\0\0!\0)`3\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0³\0\0word/document.xmlPK-\0\0\0\0\0\0!\0\Öd³Qô\0\0\01\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\n\0\0word/_rels/document.xml.relsPK-\0\0\0\0\0\0!\0¶ôg˜\Ò\0\0\É \0\0\0\0\0\0\0\0\0\0\0\0\0\0\0K\0\0word/theme/theme1.xmlPK-\0\0\0\0\0\0!\0¥%\Í%\0\0\ì\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0P\0\0word/settings.xmlPK-\0\0\0\0\0\0!\0\ÃÅš±\0\0s\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0¤\0\0word/styles.xmlPK-\0\0\0\0\0\0!\0\ï\n)NN\0\0~\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0R#\0\0word/webSettings.xmlPK-\0\0\0\0\0\0!\0¸²c\0\0‚\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\Ò$\0\0word/fontTable.xmlPK-\0\0\0\0\0\0!\0±›L}\0\0ñ\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\'\0\0docProps/core.xmlPK-\0\0\0\0\0\0!\0\ÍÃ²^q\0\0\Ç\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0¹)\0\0docProps/app.xmlPK\0\0\0\0\0\0Á\0\0`,\0\0\0\0');
/*!40000 ALTER TABLE `manual_exams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manualexamofstudent`
--

DROP TABLE IF EXISTS `manualexamofstudent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manualexamofstudent` (
  `examID` varchar(50) NOT NULL,
  `studentID` varchar(50) NOT NULL,
  `activationCode` varchar(45) NOT NULL,
  `examFile` longblob,
  PRIMARY KEY (`examID`,`activationCode`,`studentID`),
  KEY `activationCodeMnEx_idx` (`activationCode`),
  CONSTRAINT `activCodeManExam` FOREIGN KEY (`activationCode`) REFERENCES `activated_exams` (`activationCode`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `examID_inManualexams` FOREIGN KEY (`examID`) REFERENCES `manual_exams` (`examID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manualexamofstudent`
--

LOCK TABLES `manualexamofstudent` WRITE;
/*!40000 ALTER TABLE `manualexamofstudent` DISABLE KEYS */;
/*!40000 ALTER TABLE `manualexamofstudent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `professions`
--

DROP TABLE IF EXISTS `professions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `professions` (
  `professionID` varchar(45) NOT NULL,
  `professionName` varchar(45) NOT NULL,
  `numQuestions` int DEFAULT '0',
  PRIMARY KEY (`professionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `professions`
--

LOCK TABLES `professions` WRITE;
/*!40000 ALTER TABLE `professions` DISABLE KEYS */;
INSERT INTO `professions` VALUES ('01','Math',5),('02','English',2),('03','Computer Science',0),('04','Physics',3),('05','Astronomy',0);
/*!40000 ALTER TABLE `professions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `questionID` varchar(45) NOT NULL,
  `professionID` varchar(45) DEFAULT NULL,
  `authorID` varchar(45) DEFAULT NULL,
  `question` varchar(45) DEFAULT NULL,
  `answer1` varchar(45) DEFAULT NULL,
  `answer2` varchar(45) DEFAULT NULL,
  `answer3` varchar(45) DEFAULT NULL,
  `answer4` varchar(45) DEFAULT NULL,
  `correctAnswer` varchar(45) DEFAULT NULL,
  `questionInstructions` varchar(45) DEFAULT NULL,
  `usedCounter` int DEFAULT '0',
  PRIMARY KEY (`questionID`),
  KEY `professionID2_idx` (`professionID`),
  KEY `authorID2_idx` (`authorID`),
  CONSTRAINT `authorID2` FOREIGN KEY (`authorID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `professionID4` FOREIGN KEY (`professionID`) REFERENCES `professions` (`professionID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` VALUES ('01000','01','156134567','What is Lim( e^x) where x --> infinity?','10000','0','1','infinity','4','Choose the correct answer.',1),('01001','01','156134567','Whats the identity matrix transpose?','5','The identity matrix','We cannot know','-1 * The identity matrix ','2','Choose the correct answer.',1),('01002','01','156134567','What is Lim(-10) where x--> infinity?','infinity','2','-10','10','3','Choose the correct answer.',1),('01003','01','156134567','What is Lim(1/x) where x--> infinity?','error','1','0','12','3','Choose the correct Answer.',0),('01004','01','156134567','What is Lim(e ^(-x)) where x-->infinity?','infinity','0','1','2','2','Choose the correct Answer',2),('02000','02','100100100','___ apple fell from the tree','The','An','A','While','1','Fill in the blank.',0),('02001','02','100100100','You _____ insane!','are','is','am','will','1','Fill in the blank.',0),('04000','04','156134567','What is a star?','A stone','A burning body','An alien','Light ','2','Answer the Question.',0),('04001','04','156134567','Why do humans walk weirdly on the moon?','Because theres no air','Weaker gravity than earth','Stronger gravity than earth','scientests havent found out yet','2','Choose the correct answer.',0),('04002','04','156134567','question is','a','a','a','a','0','',0);
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions_in_exams`
--

DROP TABLE IF EXISTS `questions_in_exams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions_in_exams` (
  `examID` varchar(45) NOT NULL,
  `questionID` varchar(45) NOT NULL,
  `points` varchar(45) DEFAULT NULL,
  `serialNumber` int NOT NULL,
  PRIMARY KEY (`examID`,`questionID`),
  KEY `questionID_idx` (`questionID`),
  CONSTRAINT `examID` FOREIGN KEY (`examID`) REFERENCES `exams` (`examID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `questionID` FOREIGN KEY (`questionID`) REFERENCES `questions` (`questionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions_in_exams`
--

LOCK TABLES `questions_in_exams` WRITE;
/*!40000 ALTER TABLE `questions_in_exams` DISABLE KEYS */;
INSERT INTO `questions_in_exams` VALUES ('010101','01000','50',1),('010101','01004','50',2),('010102','01004','100',1),('010201','01001','50',1),('010201','01002','50',2);
/*!40000 ALTER TABLE `questions_in_exams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students_answer_in_exam`
--

DROP TABLE IF EXISTS `students_answer_in_exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students_answer_in_exam` (
  `studentID` varchar(45) NOT NULL,
  `activationCode` varchar(45) NOT NULL,
  `questionID` varchar(45) NOT NULL,
  `studentAnswerIndx` int DEFAULT NULL,
  `correct` varchar(45) NOT NULL,
  PRIMARY KEY (`studentID`,`activationCode`,`questionID`),
  KEY `activationCode_idx` (`activationCode`),
  KEY `questionID1_idx` (`questionID`),
  CONSTRAINT `activationCode1` FOREIGN KEY (`activationCode`) REFERENCES `activated_exams` (`activationCode`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `questionID1` FOREIGN KEY (`questionID`) REFERENCES `questions` (`questionID`),
  CONSTRAINT `studentID` FOREIGN KEY (`studentID`) REFERENCES `users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `studentID2` FOREIGN KEY (`studentID`) REFERENCES `exams_of_students` (`studentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students_answer_in_exam`
--

LOCK TABLES `students_answer_in_exam` WRITE;
/*!40000 ALTER TABLE `students_answer_in_exam` DISABLE KEYS */;
INSERT INTO `students_answer_in_exam` VALUES ('123456789','1110','01000',4,'true'),('123456789','1110','01004',-1,'false'),('123456789','1111','01000',-1,'false'),('123456789','1111','01004',-1,'false'),('123456789','2222','01001',2,'true'),('123456789','2222','01002',2,'false'),('123456789','5556','01001',2,'true'),('123456789','5556','01002',-1,'false');
/*!40000 ALTER TABLE `students_answer_in_exam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `userID` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `firstName` varchar(45) NOT NULL,
  `lastName` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `role` varchar(45) NOT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('100100100','linda','123','Linda','Gregory','linda@gmail.com','Lecturer',0),('123456789','adan','123','Adan','Butto','adan@gmail.com','Student',0),('156134567','itay','123','Itay','Lumberg','ita@gmail.com','Lecturer',1),('211575790','abd','123','Abd','Hija','hija368@gmail.com','Lecturer',0),('222222222','nina','123','Nina','Green','niinaG@gmail.com','Student',0),('999999999','dvora','123','Dvora','Toledano','dvora@gmail.com','HeadDep',0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-15 13:26:49
