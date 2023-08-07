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
INSERT INTO `manual_exams` VALUES ('010103',_binary 'PK\0\0\0\0\0!\0ߤ\�lZ\0\0 \0\0\0[Content_Types].xml �(�\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0��\�n\�0E�����Ub袪*�>�-R\�{V��Ǽ��QU�\nl\"%3�\�3Vƃ\�ښl	�w%\�=���^i7+\�\�\�-d&\�0\�A\�6�l4��L60#�Ò\�S\nO����X�\0�*��V$z�3��3\���\��\�%p)O�^���\�5}nH\"d\�s\�Xg�L�`��\�|\�ԟ�|�P�rۃs��\Z?�PW�\�tt4Q+\�\�\"�wa���|T\\y���,N\�\��U�%��\�-D/�\�ܚ��X�ݞ�(���<E\�\�)�\�\Z\0;\�N�L?�F�˼��܉�\Z�<Fk\�	�h�y�\�\�\�ڜ��\�q�i�\�?\�ޯl�\�i\�\01\�\�]�H\�g\��m�@\�\�\��m�\0\0��\0PK\0\0\0\0\0!\0�\Z�\�\0\0\0N\0\0\0_rels/.rels �(�\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0���j\�0@\���ѽQ\���N/c\�\�\�[IL\�\�j\���<\�\�]\�aG\�\�ӓ\�zs�Fu\��]�\Z�U\r��	\��^\�[��x\0����1x\�p\��\��f�\�#I)ʃ�Y�\�\Z����\��\�*D�\�i\")\�\�c$���qU\���~3��1\�\�jH[{�=E���\�~\nf?��3-��\�޲]\�Tꓸ2�j)�,\Zl0/%��b�\n\Z�\�\�z���ŉ�,	�	�/�|f\\Z�\�\�?6\�!Y�_\�o�]A�\0\0��\0PK\0\0\0\0\0!\0)`�3\0\0\0\0\0\0\0word/document.xml��mo\� ��O\���=\�/��XM��Q�~�T�\� ��,�yٯ\�a\�/��\�q�(��{\�8��ۻ˭�*|c�7�mQND��dc��>\�V��4\�\���L�}���\��F���rm���X���j]�)�R�\�\rˈJ\����Dg�����ש�\n)U\n\�=`~�ʾ\�\�i-��\�8G$\�R\�S\�p��,\�\Z�� oV\�C�5*@ƫh>	^\rH�i�,.�F�\�4�?$���ǉ\r�((��XH�5te��\�e1p�u�\��L��\�\rg�}�G�\��]MX\"&\"��QC��<�\�\�Z}\�zX\�_�V�\�\�̂�5�\'�+\�\�\�1��\�w�\�RE\rI�CWiV�ՁM��`\�@\���fޱpG�\��Jۮކ8\��\�ޱ���c�\�\�M�h5Ƹ�\�\�\'�3<)4�\�#�O���Б�E\�X]�t\�m8\�ȴj8��N\�\�Y�v��ʫ�\��a\Z�\�c�HG\�u�f��\�\�\Z�X�Ic���.Zܙ�\�]$�K�\�R�EG\�>G{\�\�\�\�<t�`]��_0\�\�yIqU���)\�B\�}A�Y�-V�\��i�Oz�\�f�-S�\�-�\��\":����yX`��\�����\�]poWR�ߴ�.��\��\��!���\�q�½�[ѳ4Bw9w=�\�h�\�\\�F*�\�\�4�nr\���pP>{{�\��]�Q7{�\�1\�\�\�4S����\�=afiaA�ɻ���K�\�\��\�0/q^\�2]�jㆧ(\�\�r\�ګ&/�`\�\�\�\�~!\�\�`富�f\�l�Z@�w\�s�beI��\�^h-X\�\�i\�M)�(\�]:+Ӎ�нnR\�\�\�\�\�HU�	�\�Tbx��栄y\�\�s�	x\��j\�]}֧u�\�o\0\0\0��\0PK\0\0\0\0\0!\0\�d�Q�\0\0\01\0\0\0word/_rels/document.xml.rels �(�\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0��\�j\�0E�����}-;}PB\�lJ!\�\��\0E?�,	\��`HI\�\�`��r��sπ6\�\���w�\�{��\�r茯{\�*x��\�AkWk\�*�`[^^l�\�jNK\���D�8R\�1���d:4e>�K/���\�4\�Vm^u�r�\�w2NP�0ŮVw�5�j��o�\�\��7o:>S!?p��\�\�8JX[d�0KD�\�EVK�\��c2�P,���ũ�a���]��\�.��\�ﰘs�Yҡ�+����\�(!O>z�\0\0��\0PK\0\0\0\0\0!\0��g�\�\0\0\� \0\0\0\0\0word/theme/theme1.xml\�YK�G����\�z\�\�a�5\�H�k\�6޵���Rk���i\�\�ڵ0�`�r	��C�\�B1\�\�c�I��\�I3-�ď]�	��U?�������4s\�\���:G��[=Wq��ؘ$aǽ}0,�\\GH��e	\�,܋;�v�����8�:n$\�\�|�,F0�\�96\�	\�M���.\�c��AoL˵J�Q�I\\\'A1��1��v�Jwg�|@\�_\"�\ZQ��TcCBc\�Ӫ�P\�!\�qa�1;>���\�P$$Lt܊�s\�;\�k!*dsrC���[\n��5-\�\�õ�\��^��֯Tn\�\�Ac\�X\�\�\04\Z�NS.�\�f-�\�(mZt���z\��\��׷�]_}��Mo?�\rs��\�o\��^�\�7�kP\�llᛕn\�k\Zx\r�(I�[\�ߨ�ݮ!F/[\�m\�6kKx�*\�+�OdQ�\�\�\�C\0h\�\"IG.fx�F�%��8�$� �f(a�+�ʰR��\�\�\��(:�QN:\Z��!\�\�#Nf�\�^�n�\�ŋ����|��\�Ǐ_>�u���\�e��y�7?}�\�\�/����͓o\�x�ǿ�\�\���_\�A\�g��?{��\���\�\�rt���\�:>vn�6hY\0���8�\�Kt�P�)z #}}�(�\�zش\�\�\��4�gޏ�\\�Z�=\�h�q랮���V�\'�}q>\�\�n!td[;\���`>��\'6�A�\r�7)��8�\�Qsl��E\�.!�]�Ȉ3�&ҹK�\"V��C#�2�\�$�,l�߆m�\�8=Fm\���\�D\�\�@ԦSÌ�\�\\�\�\�\�4�\�E2��\�_�ap!�\�!�\���6�|aнi\�\��=��M$�djC\�\"\��\�>��gV\�$��\�+b\n!���LZI0�>�%�\�C�\�\�ې�\��f\�\�v$03\�\�N�)\��\�H�]N�\�ћ�Fh\�bL\�1\Zc\�ܾbó�a��\��\�el�\�Udƪ\�\'X@����c�0Bv���\�\�b#�,P#^���\��\\u�5^\�hj�R\�ա���!bc�ZoF\�+\��x]p\�\�r\�@\�\�\�\����\��ζ9@\�X �U�-݂�\��LD\'-6�\�M\�C����Q�\�$yk�Q�����\n\�\�O-\�өw\���T:E\�d��)\�mV5\�c�\�5}4Onb�G,г�欦�\�\�4E\����9�d\�*�\�G�d�\�E?Z=\�\�Z\�§>B\�\\P�+t\�#\�쏇0�;Zh��iAs���9\�m�3��\�~�f�LU���\�P83&�p\�\�V\�j�\�\�=6NG�\�\�sM@2�\�k5e�LG\�\�\�Z�\��A늀�}�\�Lu�\�j�-$�\�N�E\�¢�\��\�_K��\�\� �H\��RFn\�c\�T~\�\�S�t�1\�m\�,\�k+��\�i�D.\�L�0�\��\�>e_�3�\Z��)�i4[\�\�*�l\���=\�\�\\\�5#4\��\�\�x��\�T��I\�ɥ�?$�̸�}$���\��\�Db\�PC�\�\�@��[�\�T{�Dɵ+��\��W\�\�x2�#Y0�ua.Ub�=!Xu\�H\�G\�c\�\��-��Ue�1rm\�1\�\�ά����G\�xߒQDgZ\�(�d�\�u{M\'�\�tsWf��\�P9\�ķ\�ۅ\�D.i\\ \�ִ珏w\�\�Xey\�`��\�\�\\\�^庢[\�\�B�Z��AM1�P\�FMj�X\�[�f\�qڷ�fԪbUW\�\�֋mvx\"�\�\�J��¯��\�+\�4\�\�Uv�/�9\'�A\�\�zA\�J��?(yu�Rj�\�z�\���\���V��\�C0��⪟�=��t�|o�Ƿ\�\�ǫR�܈\�e�\�\�\�\�\�w�\�<hԆ\�z�\�(�\�\�a\�\��Z�v\�\����\���\�>t�#\r����kZ�F5J^��\�ڥ�W�u�f�5�������W\�ռv�\0\0��\0PK\0\0\0\0\0!\0�%\�%\0\0\�\0\0\0\0\0word/settings.xml�VMo\�8�/���\�y}Dvb�N;�&E�]TY\�)�)$e\�-��\�-�)\n�E/65o\�\�h�8Ի�Ϝ��Di*�y�E��4���\�̃W\�\�`�\rj0b�!�`Ot��\��\�\�\�2M�7=�Fg���12C]ք#}&$i\0��\�\���ڄ��V�K�%2����}�D\�4\�i\�<hU��cNK%���\r\�DUђ�>B����e\�Ic\\\�P5�F\�Tj\�\��\r�ړl��[μ\�.�NxݝP�qJy6@*Q�a�8�\�fH��\":\�>�\��+:*�#�:�|�6�\���$\�o\�\�9B�<\��m<\�\ZO��#�\r�\�Ē���6T#}P�e$o+jr�\��G����z��B�;��dx�\�o\Z�P���\�v䪳�\�D�\�\�\�\�m�+���\�]&�*\����� �\0\�ST�A(2-	cn┌ ȸ\�6\nq�\�\�b0�P\�\�#*r#$8m�\�E\�S�5R�4D\�����Q�y?,�f	sG��\�#\�\ZVy7\� �A^�ŔZLle���\�\rp\�\�\�qʯ	���b�h[��=#+(>��\�u�?�\�P`t�\�\'*�^���?�(���2-�\�%s;�bT��RB\�7��˒Ѫ\"\nP\�\�\Z\�C�ع>\��\�\��Ey[M�g8�\� ˧�0F𻽬�\�?��N\�\�|\��\�\�/>	a�Q|�\�I\�Uj\�S�\�t/ο��n����\�\�g噽��V~e�;\�]\��BQ4Z\�\�1��zZ\�\�\��	D���-<8w�戱4\��<\�T\�R�5[#�x{�M+̑.;���S�Vv\�N!\�Iһ\�i\�G\�\�<P\�\�-r\��\�<�\�\�*ק�=�\�������T\n�A\0H��\�^KL\�Vd��\�\�Tl\�y�\�6�U��\'Q\�\�$=�8,\�0��J�j\�\�/[\�mG~\�\�v>\�RoK\�\�\�&�m\�mSk�a�(�\�O�l���J0&v\�\r�+S\�]#In�a�����z�\�\�3\\%Sߦ�b��\�͒Lmx\�\�\�^�慯Ŭ�|\�`o\��,�/��ƿ�\�^B%=\�{^w\�YW8�\Z怄k\�\�?�彽)\�Ξ�8����\�˸Q���T�	\�1:\�B�\\ߦ��\"�\�I��\�i2��g��d��f�U�\\.\�\�\��)��\�W�\0\0��\0PK\0\0\0\0\0!\0\�Ś��\0\0s\0\0\0\0\0word/styles.xml��[sۺ\�\�;\�\��\�S��\�W9�\�\�$��v�O\�4\�	Y�AB\�ŗ~� %A^�\�[�$\�e\0�\��\�M����\�\�\�P\�\�h��\�(\�Y��ݟ�~\�}{�a%\�&U\�\�F/���\��\�\�(_$/\"\rȊ\�4>-\�ry:񂧬x��<\�\�U��R�\�\�\�)\��\�X�KV����|\�\�MF\r&\�CQ��W)\�J?ι\�D��,V��>�\'�\'\�\\ż(�F��\�Ldk\��\0�\"\�U�\�\�{�1M�,J�\�\�ٿR�\�\0\00��3��a�u�\�	�3YsD\�p\�:\�\0��L(\��j\\\�&��l���K\�N�q/��4>��\�T\�fR��\�.�`�\�~���?\��\�&�>i/$*�\�笒ea^\�y�ye�������NYq�;�[I�n��<+\�H\�YQ���~�0�~���g��\�شX�W�\�\�\�\�\�`�΅\��\�{�e�N\�g#���95ܙ&��X�nzn�:n6��\�\��\�\�W��%��%�yɵ��\'{*�\�#\�W/~Tf�YU����_c\�`��ߵ��uҟ����x2-�g#ۖ~�\�\�m.T�\�\�\�mS�9婸I\�3\�\�B$�ׂg?�l\���M\���2��\�\�\�\�.�\�\�s̗&�\�O3fT�n��v%6�\����`�\�ط\�/83�7\���\�G!LD\�lm;�z�\��[��ߪ���j\���\Z��UC\'o\�Ї�j\�b��\r�,ѩ\�~6��87�9��9/�9��9\'�9���\�x\�1�㙦N�b\�,t&��g�wsw\�#¸�w	a\�\�{�0\�\�\�ݝ\�ø�\�yww�\�\�N\�xn�Ԋ��Ͳr�\�\�J��*yT�\�\�4�i�-Jixf�\�s��$�ԙ�\��\�̾\�=C�I\��祩\�\"5�\�\�\�y1�\�<{\�R-yĒD��9/�\�3\"!s:\�s��,\��j*�(�\��\�\\�{2\�\�\�[I�\�zB\��yaL\"&u\�\�\\\r\�bd�\�Z\�\�\�@�ϕ�����f�Y\��\��b��3�2��ᅁ�\�54��jhD\�Јƭ��T\�\�Јƭ��[C>nw��6Ż����\�\�.�2��c*\�3�\0\�w7\�1\�\�\�\�>g\�Ed�C�c\�mƶ�Y%/\�\�>mM�Z\�\�)r��Zd\��ݢQ�k\�#�ךGd�5o�\�n�2\�,\�.i\�i5+[MkI�L;e���\�\�\�\�\�3lc�o\"/\�lЎ%��\�\�r\�\�I��6�ޱ\rk��^g%\�\�5H�^J?Ф\�˗%\�uY�0��MI��xBG����\�k�+I/\�M�V[+m!�\�\�W D7l9x�n%�n_ߥLȈnqywsݩ�)3\��\�\0?��T)�9��_|�w��\�\"8{!\�\�s�\�Cv!v25I%D$�\�� هZ\�?�\�L�<��\�漾\�\�D\�)K�����[:/>\��C�\Z���\\�\�BT��#�9�\r�j�oOu\�UDrd菪�\�\�R\�F\�\�/�p×VM�{0�`c�p\�7vG�����B\r\�Qm\�G��Ë����\�畤��lW@�!T�J��r�-�p�-�z{	��\����\�\"!\�¨��0*,�J#`�:l�e:l��:5�h	\���\�\�\�\�,���gF5\�,�j�Y\�<;���\\/�\�v1�j\�9H�MV�t�r��!�J~\��ִ\�\\\�\�\� *�/\�&@�cԒp�]\�D�\�gd]3,\�~eR*Etlm�ñ�\�׮\�\n��n\�­d1_(��ܳM�X]/O\�\�2^w\�v�\�a\�kq�(�\�b}�\�\�L�vF�\n���\�\r���duK[\�\rOD��:\no����3z+�hw�f%�y\�3�9\��Y%oE��m~\�i}�\�\�/,h�\']�g]\�y&\�I\�,Z�6\�5�֑mS�kmY%:�cs�\0�\�\�3��~\��\�c\\\�`\�\����\�e��Q�=;&i\��\�WO��oѽ2矕��\�o�p\�Sו^8e�Z9��O\\me�8�N7~D\�\�G�N@~D�L\�\rG�$?�wn�#z\')?��\���`<.[���l)!\�j�*��\��#\�F��Q���QAx�Q!mT�@\"\�F�0�Qa<Ψ0>Ĩ�bTHA\"\�F��Q!mT�@5pm\�\r2*���\nh�Bڨv�8��0gTbTH	1*���\nh�Bڨ�6*D��\n(��� �B\nڨ�6*D��Z\�jnT�3*�1*��R\�F��Q!mT�@\"\�F��QAx�Q!mT�@\"\�F�\'\�\�\n\�C�\n)!F��Q!mT�@\"\�F��Q!eTdTHA\"\�F����ٜ��]f��?\�\�b�����S?\�[�]\�aԪW~V�{>+��\�xxh\�~1�B\�CԞ\�\�.\�^�:��\�E�>.}\�C��{!\�9S\0?\�	��uMy7yG]3ݍ�Σ�\�\�F�\�\�QWҵ�\\]��wG �+\�8����l\�\�!\�\�\�N \�\�\�\�\�\�\�N\�qd��\�\�\�\�4Y__\n]\�\�!��	]\�j�J\�\�}E���\�\'��\�O@\�\�\�\����\�\n�QaRC�a�7����\Z���p�!*Xj�\n�\Z&F�Ԑ��:<9�	ARL�\�,5D�I\rweX�!+5$`��C�b¥��`�!*Lj��\�J\r	X�!+5$I\r0\�RCT�\�&5��\�RCVjH�J\r	ARL�\�,5DuIm��lI�R\�	\�-@\�\�	\�%g\'0�Zr��%�X-A�V�\�%W4?��z~B_���^^X?\n��&5�Zj�:ܨ~Vj\\�\�\ZW-uJ���:�\�UK~�q\�R�Ըj�M\��\�\�\'I���:�\�UK�R\�%�Ըj�Mj\\�\�&5�Zj�z\�ً	�\ZW-uJ����R㪥6�q\�R�Ըj�Mj\\�\�\ZW-uJ���:�\�UK~�q\�R�Ըj�Mj\\�\�&5�Z�J���:�\�UK�R㪥\"5MY^Ftϋ�dŢd\�N�3\�y�\�#O\"\�M�Fm\��i\�\�\��\Z��~�\�\�<ݹ])��\0\�\0\����\�T�`ӓ��Y�\�m\�\�\�tmݢ\r�M\�\�V\�<�\�\�T�\��MT�	���<�\�vd3W\�n�t3^���F��ߥ��}��\��\�3�~l���\��\�d��i���,р�\�\�\�\�&ϬF\�\�/��7���Z��*���?\�߳�,x���~��7>�i\�ow�~\��l�g�\�\��7\�x��\�E-\�m/f:қ���*>�\0\0��\0PK\0\0\0\0\0!\0\�\n)NN\0\0~\0\0\0\0\0word/webSettings.xml�\�_k\�0\0����Cɻ�\�)Va\�^\�`\��\�Ն%����\�\�\�کs�b����\�.!�\�\�\�\���\rS��\�X�\�\��\�j0	E\�e\�C.�@b����7Y\�W��OR�\�\�\\T1֙��+p��X�\�\��S��a#�\n\�z�\�\�*���&\�\�8M�\���k,K�\��ց�]�`YDO��\�5\�h\r�����\�q�\�s\��3����\�	\�8\�bu��\�n\�\�/0\��/���]?cv0$G�;�\�\�LO�)Μ�%sP���2>ޫlcUT��\�\\�~IMN\�޵w\�t���\�ڲį��\�%ܶ\\\�uC\�u\�m	b�\�h������\rA�\���\�G�\�?�f�\r\0\0��\0PK\0\0\0\0\0!\0��c\0\0�\0\0\0\0\0word/fontTable.xmlܓۊ\�0�\�}���e簩Yg\�(�^,\�Pd9\��h�8y��d\'\r���\�B\�[�G�y\�\�\��U��5%\�&�$\�p[I�-\��\��ݒ$\�����F�\�(�<�޿{\�\�\Z	\�(4/I\�}[�)�Fh\�\n��\�:\�<��m����k\�\�-�r#��\�4�tA�{\�ֵ\�\�\�;-����\n�\�@#[8Ѻ�\�:\�\�Y.\0�g�z�fҜ1\�\�\n�%wl\�\'\�\�PQDazF\�J�_��8@~Xpq\�X�3/9�\Z\�Y�9��\��^1\0�|Ռ�\�\'_Ӑ\�<k4�D1���w\��#͋/[c\�($\�_O�\�%\�\�xĥ8D=�@V\�($]a�\�\�OLɍ�1\�2cAd\�3U\�aM\�4��\���;I\�F\�0\"@����k��:�T\�$@h�\�\�I\�3\'C\�}\�;\�В<\�(͟\�k\�+V�\�;�\�?\rJ���2=+4(<r\�k\�sx\��\�7\�ށ+\'>bY\�Ot��ND7���\�T\��\�ë\��o�K^�f\�#9:2\�\�1�\'d:\����#Ì$_\��7\'%\�\�:)\�V?\0\0��\0PK\0\0\0\0\0!\0��L}\0\0�\0\0\0docProps/core.xml �(�\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0��\�J\�0�\�ߡ\�M�ʐ\�v��+�Ż���6\rI���!\"x\'�R}\�v\�\�»s�\�\�\�ēm�9P�\"A�\�#-�\�ͧ\�9r�!�����\n4���\'1�-ܨB�2�c����L\�\�a�\�r�=K+>*\'ƶj�%�+�\0<��1\��F���+{G��d���k���b\� a4�\0X*\�GZ\��sSI8�\�Ş\�jރeYzeآv�\0?̮o\�_u�h���Ҙ\�\�p�A\Z\�Ci+�~zj�㾱5U@L�\���~�?�����\�[j�4���*Ŵ�tc��\�\�؛\�\��Έ63{�\�\�E��C�fF��7o#\rZ�o\�]\�\�r�P\�ŹW\�\�˫��#��\�\r\�yp��\�����\�|���á\�ޠ�h�H\�o\0\0\0��\0PK\0\0\0\0\0!\0\�ò^q\0\0\�\0\0\0docProps/app.xml �(�\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0�R\�N\�0�#�Q\�\�)R��j�8�\�О-{�X8�eD��Mӆ n��3\�ͬ\rw_�\�>1D\�l�\�gE���Ni۔�[�pq�g1	��q\�|�1�\�\�g�\�cH\ZcF6�y���e,\�;gԶԩ]\�D\"\Z\�\�ZK\\9�ѡM\�(�~%�\nՅ�A��3�WT9\���\�j\�I�C��7\"!\�\'\�L�\�Y�\\�\��9\�#��h0�\�P�\��\r���e+����\��	�{\"\�b����EW�\��\�6\�ǁM�\0%ؠ�:\�9IM!<i;\�\n�D�o�\�F).);���쇀�뼰$\�Ɗ�\�㛯ܪ_\�q\�79ɸө\�x!\�\�\�z�vҀ\r��\��\�`$\���#�^�fm�\�t\�o�\�\�v��|~5+\�v\�(��a�7\0\0\0��\0PK-\0\0\0\0\0\0!\0ߤ\�lZ\0\0 \0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0[Content_Types].xmlPK-\0\0\0\0\0\0!\0�\Z�\�\0\0\0N\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0�\0\0_rels/.relsPK-\0\0\0\0\0\0!\0)`�3\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0�\0\0word/document.xmlPK-\0\0\0\0\0\0!\0\�d�Q�\0\0\01\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\n\0\0word/_rels/document.xml.relsPK-\0\0\0\0\0\0!\0��g�\�\0\0\� \0\0\0\0\0\0\0\0\0\0\0\0\0\0\0K\0\0word/theme/theme1.xmlPK-\0\0\0\0\0\0!\0�%\�%\0\0\�\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0P\0\0word/settings.xmlPK-\0\0\0\0\0\0!\0\�Ś��\0\0s\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0�\0\0word/styles.xmlPK-\0\0\0\0\0\0!\0\�\n)NN\0\0~\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0R#\0\0word/webSettings.xmlPK-\0\0\0\0\0\0!\0��c\0\0�\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\�$\0\0word/fontTable.xmlPK-\0\0\0\0\0\0!\0��L}\0\0�\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\'\0\0docProps/core.xmlPK-\0\0\0\0\0\0!\0\�ò^q\0\0\�\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0�)\0\0docProps/app.xmlPK\0\0\0\0\0\0�\0\0`,\0\0\0\0');
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
