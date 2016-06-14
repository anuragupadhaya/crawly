-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 14, 2016 at 05:01 PM
-- Server version: 10.1.13-MariaDB
-- PHP Version: 5.6.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `crawly`
--
CREATE DATABASE IF NOT EXISTS `crawly` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `crawly`;

-- --------------------------------------------------------

--
-- Table structure for table `url`
--
-- Creation: Jun 13, 2016 at 09:56 PM
--

DROP TABLE IF EXISTS `url`;
CREATE TABLE IF NOT EXISTS `url` (
  `url` varchar(500) NOT NULL DEFAULT '',
  `link` varchar(500) NOT NULL,
  `hashCode` int(100) NOT NULL,
  `id` int(100) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`hashCode`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2946603 DEFAULT CHARSET=utf8;

--
-- RELATIONS FOR TABLE `url`:
--
