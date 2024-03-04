-- MySQL dump 10.13  Distrib 8.0.33, for macos13 (arm64)
--
-- Host: 127.0.0.1    Database: digit_twin
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_devices`
--

DROP TABLE IF EXISTS `t_devices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_devices` (
  `did` int NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `d_number` varchar(255) DEFAULT NULL COMMENT '设备id',
  `d_name` varchar(255) DEFAULT NULL COMMENT '设备名称',
  `d_mark` text COMMENT '设备描述',
  `d_status` int DEFAULT NULL COMMENT '设备状态 0不在线 1在线',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '最近更新时间',
  `d_ip` varchar(255) DEFAULT NULL COMMENT '设备的网络IP地址',
  `version` int DEFAULT NULL COMMENT '乐观锁',
  `is_deleted` int DEFAULT NULL COMMENT '逻辑删除标识',
  PRIMARY KEY (`did`),
  UNIQUE KEY `d_number` (`d_number`),
  UNIQUE KEY `d_name` (`d_name`)
) ENGINE=InnoDB AUTO_INCREMENT=10023 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_devices`
--

LOCK TABLES `t_devices` WRITE;
/*!40000 ALTER TABLE `t_devices` DISABLE KEYS */;
INSERT INTO `t_devices` VALUES (10000,'04e92ad6-d479-11ee-8eb9-da892107c2b8','物联网设备ESP','非常好用捏',1,'2024-02-26 15:31:13','2024-03-03 23:14:38','192.168.1.101',1,0),(10001,'04ea3f16-d479-11ee-8eb9-da892107c2b8','不是我的设备','这应该是其他人的设备',1,'2024-02-26 15:31:13','2024-04-26 15:31:13','192.168.1.102',1,0),(10002,'28999bac-d4b3-11ee-8eb9-da892107c2b8','设备1','设备001的描述',0,'2024-02-26 21:27:24','2024-02-26 22:27:24','192.168.1.101',1,0),(10003,'289b8304-d4b3-11ee-8eb9-da892107c2b8','设备2','设备002的描述',0,'2024-02-26 20:27:24','2024-02-26 22:27:24','192.168.1.102',1,1),(10004,'289b9970-d4b3-11ee-8eb9-da892107c2b8','设备3','设备003的描述',0,'2024-02-26 19:27:24','2024-02-26 22:27:24','192.168.1.103',1,1),(10005,'289ba26c-d4b3-11ee-8eb9-da892107c2b8','设备4','设备004的描述',0,'2024-02-26 18:27:24','2024-02-26 22:27:24','192.168.1.104',1,1),(10006,'289ba334-d4b3-11ee-8eb9-da892107c2b8','设备5','设备005的描述',1,'2024-02-26 17:27:24','2024-02-26 22:27:24','192.168.1.105',1,0),(10007,'289ba3de-d4b3-11ee-8eb9-da892107c2b8','设备6','设备001的描述',0,'2024-02-26 16:27:24','2024-02-26 22:27:24','192.168.1.106',1,0),(10008,'289babb8-d4b3-11ee-8eb9-da892107c2b8','设备7','设备007的描述',0,'2024-02-26 15:27:24','2024-02-26 22:27:24','192.168.1.107',1,0),(10009,'289bac6c-d4b3-11ee-8eb9-da892107c2b8','设备8','设备008的描述',1,'2024-02-26 14:27:24','2024-02-26 22:27:24','192.168.1.108',1,0),(10010,'289bad3e-d4b3-11ee-8eb9-da892107c2b8','设备9','设备009的描述',1,'2024-02-26 13:27:24','2024-03-03 23:15:05','192.168.1.109',1,1),(10011,'289badf2-d4b3-11ee-8eb9-da892107c2b8','设备10','设备010的描述',1,'2024-02-26 12:27:24','2024-02-26 22:27:24','192.168.1.110',1,1),(10022,'2c61c3f0-4731-4249-8c0b-8b16fd87d06c','测试设备','这是一个测试的设备',0,'2024-03-02 11:03:51','2024-03-02 11:03:51','127.0.0.1',1,0);
/*!40000 ALTER TABLE `t_devices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_fields_map_devices`
--

DROP TABLE IF EXISTS `t_fields_map_devices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_fields_map_devices` (
  `m_id` int NOT NULL AUTO_INCREMENT,
  `d_id` int DEFAULT NULL COMMENT '设备id',
  `f_id` int DEFAULT NULL COMMENT '数据段id',
  PRIMARY KEY (`m_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_fields_map_devices`
--

LOCK TABLES `t_fields_map_devices` WRITE;
/*!40000 ALTER TABLE `t_fields_map_devices` DISABLE KEYS */;
INSERT INTO `t_fields_map_devices` VALUES (1,10000,1),(2,10000,3),(3,10001,1),(5,10001,2),(6,10005,3);
/*!40000 ALTER TABLE `t_fields_map_devices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_sensor_data`
--

DROP TABLE IF EXISTS `t_sensor_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sensor_data` (
  `sid` int NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `device_id` int DEFAULT NULL COMMENT '设备ID，关联设备表中的设备',
  `field_id` int DEFAULT NULL COMMENT '数据字段ID，关联数据字段表中的数据字段',
  `value_num` double DEFAULT NULL COMMENT '数值型数据值，如果该字段不可选定数值，则为空',
  `value_str` varchar(30) DEFAULT NULL COMMENT '字符串型数据值，如果该字段不可选定数值，则存储字符串型数据值',
  `update_time` datetime DEFAULT NULL COMMENT '数据更新的时间戳',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_sensor_data`
--

LOCK TABLES `t_sensor_data` WRITE;
/*!40000 ALTER TABLE `t_sensor_data` DISABLE KEYS */;
INSERT INTO `t_sensor_data` VALUES (30,10000,3,32.1,NULL,'2024-03-01 13:48:23'),(31,10000,1,22.1,NULL,'2024-03-01 13:48:23'),(32,10000,3,26.1,NULL,'2024-03-01 13:49:59'),(33,10000,1,24.1,NULL,'2024-03-01 13:49:59'),(34,10000,3,24.1,NULL,'2024-03-01 13:50:08'),(35,10000,1,25.1,NULL,'2024-03-01 13:50:08'),(36,10000,3,17.1,NULL,'2024-03-01 13:50:23'),(37,10000,1,29.1,NULL,'2024-03-01 13:50:23'),(38,10000,3,17.3,NULL,'2024-03-01 13:50:30'),(39,10000,1,21.1,NULL,'2024-03-01 13:50:30'),(40,10000,3,14.3,NULL,'2024-03-01 13:50:37'),(41,10000,1,25.1,NULL,'2024-03-01 13:50:37'),(42,10000,3,21.3,NULL,'2024-03-01 13:50:43'),(43,10000,1,23.1,NULL,'2024-03-01 13:50:43'),(44,10000,3,15.3,NULL,'2024-03-01 13:50:52'),(45,10000,1,14.1,NULL,'2024-03-01 13:50:52'),(46,10000,3,28.3,NULL,'2024-03-01 13:51:05'),(47,10000,1,13.1,NULL,'2024-03-01 13:51:05'),(48,10000,3,21.3,NULL,'2024-03-01 13:51:11'),(49,10000,1,25.1,NULL,'2024-03-01 13:51:11'),(50,10000,3,25.3,NULL,'2024-03-01 14:00:36'),(51,10000,1,22.1,NULL,'2024-03-01 14:00:36'),(52,10000,3,25.3,NULL,'2024-03-01 14:37:48'),(53,10000,1,22.1,NULL,'2024-03-01 14:37:48'),(54,10000,3,26.3,NULL,'2024-03-01 14:38:39'),(55,10000,1,15.1,NULL,'2024-03-01 14:38:39'),(56,10000,3,26.3,NULL,'2024-03-01 14:45:38'),(57,10000,1,15.1,NULL,'2024-03-01 14:45:38'),(58,10000,3,21.3,NULL,'2024-03-01 14:46:57'),(59,10000,1,23.1,NULL,'2024-03-01 14:46:57'),(60,10000,3,26.3,NULL,'2024-03-01 14:47:17'),(61,10000,1,24.1,NULL,'2024-03-01 14:47:17'),(62,10000,3,22.3,NULL,'2024-03-01 14:47:23'),(63,10000,1,25.1,NULL,'2024-03-01 14:47:23'),(64,10000,3,21.3,NULL,'2024-03-01 14:47:31'),(65,10000,1,28.1,NULL,'2024-03-01 14:47:31'),(66,10000,3,21.3,NULL,'2024-03-01 14:50:40'),(67,10000,1,21.1,NULL,'2024-03-01 14:50:40'),(68,10000,3,21.3,NULL,'2024-03-01 14:51:26'),(69,10000,1,21.1,NULL,'2024-03-01 14:51:26'),(70,10000,3,25.3,NULL,'2024-03-01 14:52:23'),(71,10000,1,11.1,NULL,'2024-03-01 14:52:23'),(72,10000,3,25.3,NULL,'2024-03-01 14:53:55'),(73,10000,1,11.1,NULL,'2024-03-01 14:53:55'),(74,10000,3,45.3,NULL,'2024-03-01 14:55:22'),(75,10000,1,21.1,NULL,'2024-03-01 14:55:22'),(76,10000,3,45.3,NULL,'2024-03-01 14:57:23'),(77,10000,1,21.1,NULL,'2024-03-01 14:57:23'),(78,10000,3,28.3,NULL,'2024-03-01 14:58:09'),(79,10000,1,23.1,NULL,'2024-03-01 14:58:09'),(80,10000,3,28.3,NULL,'2024-03-01 14:58:45'),(81,10000,1,23.1,NULL,'2024-03-01 14:58:45'),(82,10000,3,28.3,NULL,'2024-03-01 14:58:53'),(83,10000,1,22.1,NULL,'2024-03-01 14:58:53'),(84,10000,3,26.3,NULL,'2024-03-01 14:58:57'),(85,10000,1,22.1,NULL,'2024-03-01 14:58:57'),(86,10000,3,18.3,NULL,'2024-03-01 15:00:29'),(87,10000,1,32.1,NULL,'2024-03-01 15:00:29'),(88,10000,3,18.3,NULL,'2024-03-01 15:00:57'),(89,10000,1,32.1,NULL,'2024-03-01 15:00:57'),(90,10000,3,18.3,NULL,'2024-03-01 21:46:54'),(91,10000,1,32.1,NULL,'2024-03-01 21:46:54'),(92,10000,3,18.3,NULL,'2024-03-02 17:17:26'),(93,10000,1,32.1,NULL,'2024-03-02 17:17:26'),(102,10000,3,16.3,NULL,'2024-03-03 18:43:44'),(103,10000,1,25.1,NULL,'2024-03-03 18:43:44'),(108,10000,3,16.3,NULL,'2024-03-03 18:46:02'),(109,10000,1,25.1,NULL,'2024-03-03 18:46:02'),(110,10000,3,16.3,NULL,'2024-03-03 18:46:23'),(111,10000,1,35.1,NULL,'2024-03-03 18:46:23'),(112,10000,3,16.3,NULL,'2024-03-03 18:47:17'),(113,10000,1,35.1,NULL,'2024-03-03 18:47:17'),(118,10000,3,36.3,NULL,'2024-03-03 22:53:41'),(119,10000,1,15.1,NULL,'2024-03-03 22:53:41'),(132,10000,3,16.3,NULL,'2024-03-03 23:07:02'),(133,10000,1,25.1,NULL,'2024-03-03 23:07:02'),(134,10000,3,16.3,NULL,'2024-03-03 23:08:15'),(135,10000,1,25.1,NULL,'2024-03-03 23:08:15'),(138,10000,3,16.3,NULL,'2024-03-03 23:11:38'),(139,10000,1,25.1,NULL,'2024-03-03 23:11:38'),(144,10000,3,16.3,NULL,'2024-03-03 23:12:40'),(145,10000,1,25.1,NULL,'2024-03-03 23:12:40'),(146,10000,3,36.3,NULL,'2024-03-03 23:14:38'),(147,10000,1,27.1,NULL,'2024-03-03 23:14:38');
/*!40000 ALTER TABLE `t_sensor_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_sensor_fields`
--

DROP TABLE IF EXISTS `t_sensor_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sensor_fields` (
  `nid` int NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `n_name` varchar(255) NOT NULL COMMENT '字段名称',
  `n_unit` varchar(255) DEFAULT NULL COMMENT '字段单位',
  `n_description` text COMMENT '字段描述',
  `is_alter` int DEFAULT NULL COMMENT '是否启用告警',
  `alter_top` double DEFAULT NULL COMMENT '告警上限',
  `alter_down` double DEFAULT NULL COMMENT '告警下限',
  `alter_description` text COMMENT '告警描述',
  `alter_intensity` int DEFAULT NULL COMMENT '告警强度',
  `version` int DEFAULT NULL COMMENT '乐观锁',
  `is_deleted` int DEFAULT NULL COMMENT '逻辑删除标识',
  PRIMARY KEY (`nid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_sensor_fields`
--

LOCK TABLES `t_sensor_fields` WRITE;
/*!40000 ALTER TABLE `t_sensor_fields` DISABLE KEYS */;
INSERT INTO `t_sensor_fields` VALUES (1,'temp','℃','温度',1,40,-10,'温度异常',3,1,0),(2,'humi','%','湿度',1,80,20,'湿度异常',2,1,0),(3,'Force Sensor','N','力传感器',1,100,0,'力超过阈值',3,1,0),(4,'Light Intensity Sensor','lux','光照强度传感器',1,1000,100,'光照强度超过阈值',2,1,0),(5,'Pressure Sensor','Pa','压力传感器',1,100000,50000,'压力超过阈值',3,1,0),(6,'Alcohol Concentration Sensor','%','酒精浓度传感器',1,50,0,'酒精浓度超过阈值',2,1,0),(7,'Carbon Dioxide Concentration Sensor','ppm','二氧化碳浓度传感器',0,-1,-1,'无',0,1,0),(8,'Carbon Monoxide Concentration Sensor','ppm','一氧化碳浓度传感器',1,30,0,'一氧化碳浓度超过阈值',3,1,0);
/*!40000 ALTER TABLE `t_sensor_fields` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user` (
  `uid` int NOT NULL AUTO_INCREMENT COMMENT '用户主键',
  `user_name` varchar(20) NOT NULL COMMENT '用户名',
  `user_pwd` varchar(100) NOT NULL COMMENT '用户密码',
  `version` int DEFAULT '1' COMMENT '乐观锁',
  `is_deleted` int DEFAULT '0' COMMENT '是否被删除 1 删除  0 未删除',
  `user_nickname` varchar(15) NOT NULL,
  `login_ip` varchar(128) NOT NULL,
  `login_time` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES `t_user` WRITE;
/*!40000 ALTER TABLE `t_user` DISABLE KEYS */;
INSERT INTO `t_user` VALUES (1,'admin','$2a$10$pRwnzWnbVnhAhR8DBzvfrujFzguinBp13siEApFgasTbvqJxi3nrq',1,0,'root','127.0.0.1','2024-03-03 22:52:19'),(2,'xiaoming','$2a$10$pRwnzWnbVnhAhR8DBzvfrujFzguinBp13siEApFgasTbvqJxi3nrq',1,0,'小明','127.0.0.1','2024-03-02 11:36:57');
/*!40000 ALTER TABLE `t_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user_map_devices`
--

DROP TABLE IF EXISTS `t_user_map_devices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user_map_devices` (
  `m_id` int NOT NULL AUTO_INCREMENT,
  `d_id` int DEFAULT NULL COMMENT '设备ID',
  `u_id` int DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`m_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user_map_devices`
--

LOCK TABLES `t_user_map_devices` WRITE;
/*!40000 ALTER TABLE `t_user_map_devices` DISABLE KEYS */;
INSERT INTO `t_user_map_devices` VALUES (1,10000,1),(2,10001,2),(3,10002,1),(5,10004,1),(6,10005,1),(7,10006,1),(8,10007,1),(9,10008,1),(10,10009,1),(14,10022,1);
/*!40000 ALTER TABLE `t_user_map_devices` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-04 11:30:02
