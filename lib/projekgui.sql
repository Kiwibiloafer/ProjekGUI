-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 04, 2025 at 08:27 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `projekgui`
--

-- --------------------------------------------------------

--
-- Table structure for table `car`
--

CREATE TABLE `car` (
  `id_car` int(50) NOT NULL AUTO_INCREMENT,
  `merk` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `colour` varchar(50) NOT NULL,
  `frame_number` varchar(50) NOT NULL,
  `engine_number` varchar(50) NOT NULL,
  `reg_number` varchar(50) NOT NULL,
  `status` varchar(50) NOT NULL,
  `price` int(50) NOT NULL,
  PRIMARY KEY (`id_car`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `car`
--

INSERT INTO `car` (`id_car`, `merk`, `type`, `colour`, `frame_number`, `engine_number`, `reg_number`, `status`, `price`) VALUES
(1, 'toyota', 'zenix type q modelista', 'black', '12341213', '123143252', '12311412', 'ready', 500000),
(2, 'honda', 'crv gen3 awd', 'red', '121345', '324567', '21345', 'ready', 300000),
(3, 'toyota', 'raize turbo', 'white', '31456342145', '12345643214', '32456543245', 'ready', 400000);

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `id_customer` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `address` varchar(70) NOT NULL,
  `phone_number` varchar(15) NOT NULL,
  `national_id` varchar(15) NOT NULL,
  PRIMARY KEY (`id_customer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE `employees` (
  `id_employees` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `position` varchar(50) NOT NULL,
  PRIMARY KEY (`id_employees`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employees`
--

INSERT INTO `employees` (`id_employees`, `name`, `username`, `password`, `position`) VALUES
(1, 'Alif Jamaludin', 'Kiwibiloafer', 'admin', 'manager'),
(2, 'Nindita Ramya', 'ramyanindita', '1234', 'staff');

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `id_payment` int(20) NOT NULL AUTO_INCREMENT,
  `id_rent` int(50) NOT NULL,
  `total_price` varchar(50) NOT NULL,
  `status` enum('waiting','paid') NOT NULL,
  `method` enum('Cash','Debit','Qris') DEFAULT NULL,
  PRIMARY KEY (`id_payment`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `rents`
--

CREATE TABLE `rents` (
  `id_rent` int(50) NOT NULL AUTO_INCREMENT,
  `id_employees` int(50) NOT NULL,
  `id_customer` int(50) DEFAULT NULL,
  `id_car` int(50) NOT NULL,
  `rent_date` datetime NOT NULL,
  `duration` int(50) NOT NULL,
  `return_date` datetime DEFAULT NULL,
  `status` enum('done','on going','problem') NOT NULL,
  `explanation` text DEFAULT NULL,
  `id_payment` int(50) DEFAULT NULL,
  PRIMARY KEY (`id_rent`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

COMMIT;

DELIMITER $$

CREATE TRIGGER after_rent_insert
AFTER INSERT ON rents
FOR EACH ROW
BEGIN
    DECLARE car_price INT;
    
    -- Ambil harga mobil berdasarkan id_car
    SELECT price INTO car_price FROM car WHERE id_car = NEW.id_car;
    
    -- Masukkan data pembayaran otomatis ke tabel payments
    INSERT INTO payments (id_rent, total_price, status, method)
    VALUES (NEW.id_rent, NEW.duration * car_price, 'waiting', NULL);
END $$

DELIMITER ;

-- Buat event scheduler untuk update id_payment secara otomatis --
SET GLOBAL event_scheduler = ON;

CREATE EVENT update_rent_payment
ON SCHEDULE EVERY 1 SECOND
DO
    UPDATE rents
    SET id_payment = (SELECT id_payment FROM payments WHERE payments.id_rent = rents.id_rent)
    WHERE id_payment IS NULL;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
