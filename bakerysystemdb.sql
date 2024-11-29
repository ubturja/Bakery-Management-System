-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: bakerysystem
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `bakers`
--

DROP TABLE IF EXISTS `bakers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bakers` (
  `bakerID` int NOT NULL AUTO_INCREMENT,
  `userID` int NOT NULL,
  `specialty` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`bakerID`),
  KEY `userID` (`userID`),
  CONSTRAINT `bakers_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bakers`
--

LOCK TABLES `bakers` WRITE;
/*!40000 ALTER TABLE `bakers` DISABLE KEYS */;
INSERT INTO `bakers` VALUES (1,11,'Pastry'),(2,12,'Cakes');
/*!40000 ALTER TABLE `bakers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cashiers`
--

DROP TABLE IF EXISTS `cashiers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cashiers` (
  `cashierID` int NOT NULL AUTO_INCREMENT,
  `userID` int NOT NULL,
  `totalSales` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`cashierID`),
  KEY `userID` (`userID`),
  CONSTRAINT `cashiers_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cashiers`
--

LOCK TABLES `cashiers` WRITE;
/*!40000 ALTER TABLE `cashiers` DISABLE KEYS */;
INSERT INTO `cashiers` VALUES (1,7,0.00),(2,8,0.00);
/*!40000 ALTER TABLE `cashiers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `customerID` int NOT NULL AUTO_INCREMENT,
  `userID` int NOT NULL,
  `loyaltyPoints` int DEFAULT '0',
  PRIMARY KEY (`customerID`),
  KEY `fk_userID` (`userID`),
  CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`),
  CONSTRAINT `fk_userID` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,1,0),(2,2,0),(3,3,0),(4,4,0),(5,5,0),(6,6,0),(7,13,0);
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredients`
--

DROP TABLE IF EXISTS `ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingredients` (
  `ingredientID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `quantity` int NOT NULL,
  `status` varchar(50) NOT NULL,
  PRIMARY KEY (`ingredientID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredients`
--

LOCK TABLES `ingredients` WRITE;
/*!40000 ALTER TABLE `ingredients` DISABLE KEYS */;
INSERT INTO `ingredients` VALUES (1,'Flour',50,'Available'),(2,'Sugar',30,'Available'),(3,'Eggs',100,'Available'),(4,'Butter',20,'Available'),(5,'Chocolate Chips',15,'Available'),(6,'Vanilla Extract',55,'Available'),(7,'Baking Powder',8,'Low'),(8,'Salt',90,'Low'),(9,'Milk',25,'Low'),(10,'Blueberries',8,'Finished');
/*!40000 ALTER TABLE `ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `ingredientID` int NOT NULL AUTO_INCREMENT,
  `ingredientName` varchar(50) NOT NULL,
  `stockQuantity` int DEFAULT '0',
  `priority` enum('High','Medium','Low') DEFAULT 'Medium',
  PRIMARY KEY (`ingredientID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1,'Flour',50,'High'),(2,'Sugar',30,'High'),(3,'Eggs',20,'Medium'),(4,'Butter',10,'Low'),(5,'Vanilla',5,'Low');
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `managers`
--

DROP TABLE IF EXISTS `managers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `managers` (
  `managerID` int NOT NULL AUTO_INCREMENT,
  `userID` int NOT NULL,
  PRIMARY KEY (`managerID`),
  KEY `userID` (`userID`),
  CONSTRAINT `managers_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `managers`
--

LOCK TABLES `managers` WRITE;
/*!40000 ALTER TABLE `managers` DISABLE KEYS */;
INSERT INTO `managers` VALUES (1,9),(2,10);
/*!40000 ALTER TABLE `managers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `orderID` int NOT NULL AUTO_INCREMENT,
  `customerID` int NOT NULL,
  `productID` int NOT NULL,
  `quantity` int NOT NULL,
  `orderDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(20) DEFAULT 'Pending',
  PRIMARY KEY (`orderID`),
  KEY `customerID` (`customerID`),
  KEY `productID` (`productID`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customerID`) REFERENCES `users` (`userID`),
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`productID`) REFERENCES `products` (`productID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,1,1,1,'2024-11-23 19:28:11','Paid'),(2,1,2,1,'2024-11-23 19:28:11','Paid'),(3,1,1,1,'2024-11-23 21:47:34','Paid'),(4,1,2,1,'2024-11-23 21:47:34','Paid'),(5,1,1,1,'2024-11-23 22:11:00','Paid'),(6,1,3,1,'2024-11-23 22:11:00','Paid'),(8,1,1,2,'2024-11-24 09:31:55','Pending'),(9,1,3,1,'2024-11-24 09:31:55','Pending'),(10,1,1,2,'2024-11-24 10:26:07','Paid'),(11,1,2,1,'2024-11-24 10:26:07','Paid'),(12,1,1,2,'2024-11-24 10:42:05','Paid'),(13,1,2,2,'2024-11-24 11:08:56','Paid'),(14,1,1,2,'2024-11-24 12:11:14','Paid'),(15,1,3,1,'2024-11-24 12:11:14','Paid'),(16,1,1,2,'2024-11-24 15:03:07','Unpaid'),(17,1,2,1,'2024-11-24 15:03:07','Unpaid');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `productID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock` int NOT NULL,
  `recipe` text,
  PRIMARY KEY (`productID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Chocolate Cake',20.00,10,NULL),(2,'Vanilla Cupcake',5.00,50,NULL),(3,'Croissant',3.00,30,NULL),(4,'Blueberry Muffin',4.00,20,NULL),(5,'Baguette',2.50,15,NULL),(6,'Lemonade',3.00,40,NULL);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipes`
--

DROP TABLE IF EXISTS `recipes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recipes` (
  `recipeID` int NOT NULL AUTO_INCREMENT,
  `productID` int NOT NULL,
  `recipe` text NOT NULL,
  PRIMARY KEY (`recipeID`),
  KEY `productID` (`productID`),
  CONSTRAINT `recipes_ibfk_1` FOREIGN KEY (`productID`) REFERENCES `products` (`productID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipes`
--

LOCK TABLES `recipes` WRITE;
/*!40000 ALTER TABLE `recipes` DISABLE KEYS */;
INSERT INTO `recipes` VALUES (1,1,'Flour: 200g, Sugar: 100g, Eggs: 3'),(2,2,'Flour: 150g, Cocoa Powder: 50g, Sugar: 100g, Eggs: 2'),(3,3,'Flour: 250g, Butter: 150g, Salt: 5g'),(4,4,'Flour: 200g, Blueberries: 100g, Sugar: 80g, Eggs: 2'),(5,5,'Flour: 300g, Yeast: 10g, Water: 180ml, Salt: 5g'),(6,1,'Flour, Sugar, Eggs, Butter, Chocolate'),(7,2,'Flour, Sugar, Eggs, Vanilla Extract, Milk'),(8,3,'Flour, Butter, Yeast, Milk'),(9,4,'Flour, Sugar, Blueberries, Eggs, Butter'),(10,5,'Flour, Water, Salt, Yeast');
/*!40000 ALTER TABLE `recipes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `saleID` int NOT NULL AUTO_INCREMENT,
  `cashierID` int NOT NULL,
  `saleAmount` decimal(10,2) NOT NULL,
  `saleDate` date NOT NULL,
  PRIMARY KEY (`saleID`),
  KEY `cashierID` (`cashierID`),
  CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`cashierID`) REFERENCES `users` (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `userID` int NOT NULL AUTO_INCREMENT,
  `firstName` varchar(50) DEFAULT NULL,
  `lastName` varchar(50) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status` varchar(50) DEFAULT NULL,
  `registrationDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `securityQuestion` varchar(255) NOT NULL,
  `securityAnswer` varchar(255) NOT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'John','Doe','john.doe@example.com','password123','Customer','2024-11-23 18:08:22','What is your favorite color?','Blue'),(2,'Jane','Smith','jane.smith@example.com','password123','Customer','2024-11-23 18:08:22','What is your pet’s name?','Max'),(3,'Alice','Brown','alice.brown@example.com','password123','Customer','2024-11-23 18:08:22','What is your mother’s maiden name?','Brown'),(4,'Bob','Johnson','bob.johnson@example.com','password123','Customer','2024-11-23 18:08:22','What is the name of your first school?','Greenwood'),(5,'Charlie','Davis','charlie.davis@example.com','password123','Customer','2024-11-23 18:08:22','What was your first car?','Toyota'),(6,'Eve','Miller','eve.miller@example.com','password123','Customer','2024-11-23 18:08:22','What is your dream vacation destination?','Paris'),(7,'Max','Payne','max.payne@example.com','password123','Cashier','2024-11-23 18:08:22','What is your favorite movie?','Inception'),(8,'Sara','Connor','sara.connor@example.com','password123','Cashier','2024-11-23 18:08:22','What was your childhood nickname?','Connor'),(9,'Bruce','Wayne','bruce.wayne@example.com','password123','Manager','2024-11-23 18:08:22','What is your father’s middle name?','Thomas'),(10,'Diana','Prince','diana.prince@example.com','password123','Manager','2024-11-23 18:08:22','What was the name of your first pet?','Bella'),(11,'Gordon','Ramsay','gordon.ramsay@example.com','password123','Baker','2024-11-23 18:08:22','What is your favorite dish?','Pasta'),(12,'Nigella','Lawson','nigella.lawson@example.com','password123','Baker','2024-11-23 18:08:22','What city were you born in?','London'),(13,'Upanta Baidya','Baidya','ubturja01@gmail.com','password123','Customer','2024-11-23 19:24:34','','');
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

-- Dump completed on 2024-11-25  0:35:04
