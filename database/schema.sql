-- MySQL dump 10.13  Distrib 5.7.18, for Win64 (x86_64)
--
-- Host: localhost    Database: portfoliomng
-- ------------------------------------------------------
-- Server version	5.7.18-log

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
-- Current Database: `portfoliomng`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `portfoliomng` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `portfoliomng`;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `currency_code` varchar(5) NOT NULL,
  `credit` decimal(15,2) NOT NULL,
  `debit` decimal(15,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `balance_sheet`
--

DROP TABLE IF EXISTS `balance_sheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `balance_sheet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `period_id` int(11) DEFAULT NULL,
  `current_assets` decimal(15,0) DEFAULT NULL,
  `cash_and_equivalents` decimal(15,0) DEFAULT NULL,
  `inventories` decimal(15,0) DEFAULT NULL,
  `prepayments` decimal(15,0) DEFAULT NULL,
  `trade_receivables` decimal(15,0) DEFAULT NULL,
  `total_assets` decimal(15,0) DEFAULT NULL,
  `property_plant_equipment` decimal(15,0) DEFAULT NULL,
  `intangible_assets` decimal(15,0) DEFAULT NULL,
  `current_liabilities` decimal(15,0) DEFAULT NULL,
  `short_term_debt` decimal(15,0) DEFAULT NULL,
  `current_portion_of_long_term_debt` decimal(15,0) DEFAULT NULL,
  `trade_payables` decimal(15,0) DEFAULT NULL,
  `total_liabilities` decimal(15,0) DEFAULT NULL,
  `long_term_debt` decimal(15,0) DEFAULT NULL,
  `total_debt` decimal(15,0) DEFAULT NULL,
  `equity` decimal(15,0) DEFAULT NULL,
  `retained_earnings` decimal(15,0) DEFAULT NULL,
  `minority_interest` decimal(15,0) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=278 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bank_statement`
--

DROP TABLE IF EXISTS `bank_statement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bank_statement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `period_id` int(11) NOT NULL,
  `assets` decimal(15,0) DEFAULT NULL,
  `liabilities` decimal(15,0) DEFAULT NULL,
  `equity` decimal(15,0) DEFAULT NULL,
  `shares_outstanding` decimal(15,0) DEFAULT NULL,
  `net_income` decimal(15,0) DEFAULT NULL,
  `interest_income` decimal(15,0) DEFAULT NULL,
  `interest_expenses` decimal(15,0) DEFAULT NULL,
  `loans` decimal(15,0) DEFAULT NULL,
  `deposits` decimal(15,0) DEFAULT NULL,
  `intangible_assets` decimal(15,0) DEFAULT NULL,
  `dividends` decimal(15,0) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cash_flow_statement`
--

DROP TABLE IF EXISTS `cash_flow_statement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cash_flow_statement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `period_id` int(11) NOT NULL,
  `operating_activities_cash` decimal(15,0) NOT NULL,
  `dep_and_amrt_expenses` decimal(15,0) DEFAULT NULL,
  `investing_activities_cash` decimal(15,0) NOT NULL,
  `capital_expenditures` decimal(15,0) DEFAULT NULL,
  `financing_ativities_cash` decimal(15,0) NOT NULL,
  `dividend_payments` decimal(15,0) DEFAULT NULL,
  `debt_issued` decimal(15,0) DEFAULT NULL,
  `debt_payments` decimal(15,0) DEFAULT NULL,
  `cash` decimal(15,0) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=278 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `ticker_symbol` varchar(10) NOT NULL,
  `exchange_id` int(11) NOT NULL,
  `price` decimal(11,4) DEFAULT NULL,
  `price_date` datetime DEFAULT NULL,
  `stock_url` varchar(200) DEFAULT NULL,
  `kap_url` varchar(205) DEFAULT NULL,
  `website` varchar(100) DEFAULT NULL,
  `description` longtext,
  `industry_sector_id` int(11) DEFAULT NULL,
  `ev_ebit_max` decimal(11,4) DEFAULT NULL,
  `ev_ebit_min` decimal(11,4) DEFAULT NULL,
  `ebit` decimal(15,0) DEFAULT NULL,
  `ebit_last_period` decimal(15,0) DEFAULT NULL,
  `equity` decimal(15,0) DEFAULT NULL,
  `total_debt` decimal(15,0) DEFAULT NULL,
  `cash_equivalents` decimal(15,0) DEFAULT NULL,
  `shares_outstanding` decimal(15,0) DEFAULT NULL,
  `money_generated` decimal(15,0) DEFAULT NULL,
  `company_value` decimal(15,0) DEFAULT NULL,
  `gross_margin` decimal(15,0) DEFAULT NULL,
  `ebit_margin` decimal(15,0) DEFAULT NULL,
  `ebitda_margin` decimal(15,0) DEFAULT NULL,
  `net_profit_margin` decimal(15,0) DEFAULT NULL,
  `roe` decimal(15,0) DEFAULT NULL,
  `net_profit` decimal(15,0) DEFAULT NULL,
  `book_value` decimal(15,0) DEFAULT NULL,
  `ebit_growth` varchar(100) DEFAULT NULL,
  `buy_price` decimal(11,4) DEFAULT NULL,
  `sell_price` decimal(11,4) DEFAULT NULL,
  `minority_interest` decimal(15,0) DEFAULT NULL,
  `country_code` varchar(4) DEFAULT NULL,
  `status` varchar(10) DEFAULT 'N/A',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `company_note`
--

DROP TABLE IF EXISTS `company_note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `company_note` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(11) NOT NULL,
  `note` longtext NOT NULL,
  `note_date` datetime NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `debt`
--

DROP TABLE IF EXISTS `debt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `debt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` decimal(18,0) NOT NULL,
  `currency` varchar(3) NOT NULL,
  `interest_rate` decimal(3,2) NOT NULL,
  `period_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `exchange`
--

DROP TABLE IF EXISTS `exchange`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exchange` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `code` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `income_statement`
--

DROP TABLE IF EXISTS `income_statement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `income_statement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `period_id` int(11) NOT NULL,
  `revenue` decimal(15,0) NOT NULL,
  `gross_profit` decimal(15,0) NOT NULL,
  `general_administrative_expenses` decimal(15,0) DEFAULT NULL,
  `selling_marketing_distribution_expenses` decimal(15,0) DEFAULT NULL,
  `research_development_expenses` decimal(15,0) DEFAULT NULL,
  `other_operating_income` decimal(15,0) DEFAULT NULL,
  `other_operating_expense` decimal(15,0) DEFAULT NULL,
  `operating_profit` decimal(15,0) NOT NULL,
  `non_operating_profit` decimal(15,0) DEFAULT NULL,
  `financial_income` decimal(15,0) DEFAULT NULL,
  `financial_expenses` decimal(15,0) DEFAULT NULL,
  `tax_expenses` decimal(15,0) DEFAULT NULL,
  `net_profit` decimal(15,0) NOT NULL,
  `sales_abroad` decimal(15,0) DEFAULT NULL,
  `sales_local` decimal(15,0) DEFAULT NULL,
  `minority_interest` decimal(15,0) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=278 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `industry_sector`
--

DROP TABLE IF EXISTS `industry_sector`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `industry_sector` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL,
  `name` varchar(45) NOT NULL,
  `code` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `period`
--

DROP TABLE IF EXISTS `period`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `period` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `company_id` int(11) NOT NULL,
  `shares_outstanding` decimal(15,0) NOT NULL,
  `earnings_date` datetime DEFAULT NULL,
  `gross_margin` decimal(15,2) DEFAULT '0.00',
  `ebit_margin` decimal(15,2) DEFAULT '0.00',
  `ebitda_margin` decimal(15,2) DEFAULT NULL,
  `net_profit_margin` decimal(15,2) DEFAULT NULL,
  `roe` decimal(15,2) DEFAULT NULL,
  `gross_margin_TTM` decimal(15,2) DEFAULT NULL,
  `ebit_margin_TTM` decimal(15,2) DEFAULT NULL,
  `ebitda_margin_TTM` decimal(15,2) DEFAULT NULL,
  `net_profit_margin_TTM` decimal(15,2) DEFAULT NULL,
  `debt_to_net_profit_margin` decimal(15,2) DEFAULT NULL,
  `money_generated` decimal(15,0) DEFAULT NULL,
  `company_value` decimal(15,0) DEFAULT '0',
  `higher_price` decimal(15,2) DEFAULT NULL,
  `fair_price` decimal(15,2) DEFAULT NULL,
  `lower_price` decimal(15,2) DEFAULT NULL,
  `ebit_growth` decimal(15,2) DEFAULT NULL,
  `net_left_profit` decimal(15,0) DEFAULT NULL,
  `source` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=379 DEFAULT CHARSET=utf8 COMMENT='shares_outstanding at the end of the period';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `position`
--

DROP TABLE IF EXISTS `position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `position` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(11) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `average_bought_price` decimal(10,2) DEFAULT NULL,
  `current_price` decimal(10,2) DEFAULT NULL,
  `total_value` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `price_data`
--

DROP TABLE IF EXISTS `price_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `price_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(11) DEFAULT NULL,
  `first_date` datetime DEFAULT NULL,
  `last_date` datetime NOT NULL,
  `price_data` longtext,
  `modified` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `company_id_UNIQUE` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pricing_period`
--

DROP TABLE IF EXISTS `pricing_period`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pricing_period` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pricing_period_type_id` int(11) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `shares_outstanding` decimal(15,0) DEFAULT NULL,
  `period_id` int(11) NOT NULL,
  `dividend_amount` decimal(15,10) DEFAULT NULL,
  `lower_price` decimal(15,4) DEFAULT NULL,
  `higher_price` decimal(15,4) DEFAULT NULL,
  `average_price` decimal(15,4) DEFAULT NULL,
  `company_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pricing_period_type`
--

DROP TABLE IF EXISTS `pricing_period_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pricing_period_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `code` varchar(45) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `property`
--

DROP TABLE IF EXISTS `property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `property` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(45) NOT NULL,
  `value` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `key_UNIQUE` (`key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_position`
--

DROP TABLE IF EXISTS `stock_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock_position` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(11) NOT NULL,
  `price_bought` decimal(5,2) NOT NULL,
  `price_sold` decimal(5,2) NOT NULL,
  `date_bought` datetime DEFAULT NULL,
  `date_sold` datetime DEFAULT NULL,
  `quantity` decimal(6,0) NOT NULL,
  `description` varchar(245) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source_account` int(11) NOT NULL,
  `dest_account` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `commision` decimal(4,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-06 16:27:29
