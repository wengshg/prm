-- MySQL dump 10.13  Distrib 5.5.37, for Linux (x86_64)
--
-- Host: localhost    Database: test
-- ------------------------------------------------------
-- Server version	5.5.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bom`
--

DROP TABLE IF EXISTS `bom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bom` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `enable` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bom_item`
--

DROP TABLE IF EXISTS `bom_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bom_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bid` bigint(20) DEFAULT NULL,
  `mid` bigint(20) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `tolerance` float DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `container`
--

DROP TABLE IF EXISTS `container`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `container` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equipment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `enable` int(11) DEFAULT NULL,
  `lid` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `equipment_gate`
--

DROP TABLE IF EXISTS `equipment_gate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equipment_gate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `eid` bigint(20) DEFAULT NULL,
  `enable` int(11) DEFAULT NULL,
  `lid` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `line`
--

DROP TABLE IF EXISTS `line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `line` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `enable` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `material`
--

DROP TABLE IF EXISTS `material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `material` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `container` varchar(255) DEFAULT NULL,
  `container_weight` float DEFAULT NULL,
  `enable` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `process_flow`
--

DROP TABLE IF EXISTS `process_flow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `process_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bid` bigint(20) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `enable` int(11) DEFAULT NULL,
  `lid` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `process_flow_item`
--

DROP TABLE IF EXISTS `process_flow_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `process_flow_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bid` bigint(20) DEFAULT NULL,
  `enable` int(11) DEFAULT NULL,
  `eqpt_type` varchar(255) DEFAULT NULL,
  `fid` bigint(20) DEFAULT NULL,
  `gate_type` varchar(255) DEFAULT NULL,
  `interval` int(11) DEFAULT NULL,
  `lid` bigint(20) DEFAULT NULL,
  `mid` bigint(20) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appr_time` bigint(20) DEFAULT NULL,
  `appr_uid` bigint(20) DEFAULT NULL,
  `bid` bigint(20) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `fid` bigint(20) DEFAULT NULL,
  `lid` bigint(20) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `schd_edate` bigint(20) NOT NULL,
  `schd_sdate` bigint(20) NOT NULL,
  `schd_time` bigint(20) DEFAULT NULL,
  `schd_uid` bigint(20) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `store_material`
--

DROP TABLE IF EXISTS `store_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `store_material` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mid` bigint(20) DEFAULT NULL,
  `original_code` varchar(255) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `rid` bigint(20) DEFAULT NULL,
  `sid` bigint(20) DEFAULT NULL,
  `signed_date` bigint(20) DEFAULT NULL,
  `signed_uid` bigint(20) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `wid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `store_requisition`
--

DROP TABLE IF EXISTS `store_requisition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `store_requisition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `created_date` bigint(20) DEFAULT NULL,
  `created_uid` bigint(20) DEFAULT NULL,
  `sid` bigint(20) DEFAULT NULL,
  `signed_date` bigint(20) DEFAULT NULL,
  `signed_uid` bigint(20) DEFAULT NULL,
  `wid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `store_requisition_item`
--

DROP TABLE IF EXISTS `store_requisition_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `store_requisition_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mid` bigint(20) DEFAULT NULL,
  `qid` bigint(20) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `sid` bigint(20) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `wid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dept` varchar(32) NOT NULL,
  `enable` int(11) DEFAULT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `passwd` varchar(64) NOT NULL,
  `role` varchar(32) NOT NULL,
  `username` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `weighing_room`
--

DROP TABLE IF EXISTS `weighing_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `weighing_room` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `workorder`
--

DROP TABLE IF EXISTS `workorder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workorder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bid` bigint(20) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `eid` bigint(20) DEFAULT NULL,
  `fid` bigint(20) DEFAULT NULL,
  `lid` bigint(20) DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `owner_uid` bigint(20) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  `sid` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `weighing_uid` bigint(20) DEFAULT NULL,
  `work_edate` bigint(20) DEFAULT NULL,
  `work_sdate` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `workorder_container`
--

DROP TABLE IF EXISTS `workorder_container`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workorder_container` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bid` bigint(20) DEFAULT NULL,
  `eid` bigint(20) DEFAULT NULL,
  `fid` bigint(20) DEFAULT NULL,
  `gid` bigint(20) DEFAULT NULL,
  `lid` bigint(20) DEFAULT NULL,
  `mid` bigint(20) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  `sid` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `total` float DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `wid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `workorder_log`
--

DROP TABLE IF EXISTS `workorder_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workorder_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_time` bigint(20) DEFAULT NULL,
  `created_uid` bigint(20) DEFAULT NULL,
  `eid` bigint(20) DEFAULT NULL,
  `lid` bigint(20) DEFAULT NULL,
  `mid` bigint(20) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  `sid` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `uid` bigint(20) DEFAULT NULL,
  `wid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `workorder_material`
--

DROP TABLE IF EXISTS `workorder_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workorder_material` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `actl_quantity` float DEFAULT NULL,
  `actl_total` float DEFAULT NULL,
  `container_qty` int(11) DEFAULT NULL,
  `eid` bigint(20) DEFAULT NULL,
  `lid` bigint(20) DEFAULT NULL,
  `mid` bigint(20) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `replenish` int(11) DEFAULT NULL,
  `sid` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `tolerance` float DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `wid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-07-12 15:38:07
