-- phpMyAdmin SQL Dump
-- version 4.0.10.7
-- http://www.phpmyadmin.net
--
-- Host: localhost:3306
-- Generation Time: Jun 20, 2018 at 08:51 PM
-- Server version: 10.0.27-MariaDB-cll-lve
-- PHP Version: 5.4.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `basuraju_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `bin`
--

CREATE TABLE IF NOT EXISTS `bin` (
  `bin_id` int(11) NOT NULL AUTO_INCREMENT,
  `bin_name` varchar(50) NOT NULL,
  `bin_status` text NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`bin_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=24 ;

--
-- Dumping data for table `bin`
--

INSERT INTO `bin` (`bin_id`, `bin_name`, `bin_status`, `user_id`) VALUES
(14, 'Basurero', 'Deployed', 87),
(15, 'Basura Juan', 'Undeployed', 87),
(16, 'BasuraJuan', 'Deployed', 91),
(18, 'Basura Pinoy', 'Undeployed', 91),
(20, 'Basura Juan', 'Undeployed', 88),
(21, 'Basura Juan', 'Deployed', 92),
(23, 'Basura Juan', 'Undeployed', 94);

-- --------------------------------------------------------

--
-- Table structure for table `deployment`
--

CREATE TABLE IF NOT EXISTS `deployment` (
  `user_id` int(11) NOT NULL,
  `bin_id` int(11) NOT NULL,
  `location` varchar(100) NOT NULL,
  `deployment_date_start` date NOT NULL,
  `deployment_date_end` date NOT NULL,
  `deployment_time_start` time NOT NULL,
  `deployment_time_end` time NOT NULL,
  KEY `bin_id` (`bin_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `deployment`
--

INSERT INTO `deployment` (`user_id`, `bin_id`, `location`, `deployment_date_start`, `deployment_date_end`, `deployment_time_start`, `deployment_time_end`) VALUES
(91, 16, '852 A. C. Cortes Ave, Mandaue City, 6014 Cebu, Philippines', '2018-02-23', '0000-00-00', '21:50:29', '00:00:00'),
(92, 21, '2285 Chino Roces Ave, Makati, 1231 Metro Manila, Philippines', '2018-03-08', '2018-03-08', '08:32:05', '21:51:44'),
(92, 21, '2285 Chino Roces Ave, Makati, 1231 Metro Manila, Philippines', '2018-03-08', '2018-03-08', '19:30:12', '21:51:44'),
(92, 21, '2285 Chino Roces Ave, Makati, 1231 Metro Manila, Philippines', '2018-03-08', '0000-00-00', '21:55:48', '00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `location`
--

CREATE TABLE IF NOT EXISTS `location` (
  `bin_id` int(11) NOT NULL AUTO_INCREMENT,
  `latitude` text NOT NULL,
  `longitude` text NOT NULL,
  PRIMARY KEY (`bin_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `location`
--

INSERT INTO `location` (`bin_id`, `latitude`, `longitude`) VALUES
(1, '14.536864', '121.021410');

-- --------------------------------------------------------

--
-- Table structure for table `notification`
--

CREATE TABLE IF NOT EXISTS `notification` (
  `notification_id` int(11) NOT NULL AUTO_INCREMENT,
  `notification_message` text NOT NULL,
  `notification_date` date NOT NULL,
  `notification_time` time NOT NULL,
  `notification_category` text NOT NULL,
  `notification_status` text NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`notification_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `notification`
--

INSERT INTO `notification` (`notification_id`, `notification_message`, `notification_date`, `notification_time`, `notification_category`, `notification_status`, `user_id`) VALUES
(1, 'Basura Juan is full ', '2018-02-23', '22:01:40', 'Bin Capacity', 'Unread', 0),
(2, 'Battery is low.', '2018-03-08', '10:24:40', 'Battery Status', 'Unread', 0);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_lname` text NOT NULL,
  `user_fname` text NOT NULL,
  `user_minitial` text NOT NULL,
  `user_email` varchar(50) NOT NULL,
  `user_username` varchar(100) NOT NULL,
  `user_password` varchar(100) NOT NULL,
  `user_image` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=95 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `user_lname`, `user_fname`, `user_minitial`, `user_email`, `user_username`, `user_password`, `user_image`) VALUES
(86, 'Basura', 'Juan', 'X', 'asdf@gmail.com', 'sam', 'abc', 'http://basurajuan.x10host.com/images/PhotoGrid_1516096482799.png'),
(87, 'Baguio', 'Rizaldy', 'T', 'rizaldybaguio.bsit@gmail.com', 'rbaguio', '123', 'http://basurajuan.x10host.com/images/FB_IMG_15194295763041395.jpg'),
(88, 'Baguio', 'Rizaldy', 'T', 'rt@gmail.com', 'rizaldy', 'abc', 'http://basurajuan.x10host.com/images/FB_IMG_1519320461499.jpg'),
(89, 'Baguio', 'Rizaldy', 'T', 'rt@gmail.com', 'rizaldys', 'abc', 'http://basurajuan.x10host.com/images/FB_IMG_1519320461499.jpg'),
(90, 'Lao', 'Jessamae', 'C', 'laojessamae@gmail.com', 'maeng', '123', 'http://basurajuan.x10host.com/images/FB_IMG_1519222798078.jpg'),
(91, 'Bucol', 'Alexander', 'Y', 'bucol@gmail.com', 'bucky', '123', 'http://basurajuan.x10host.com/images/FB_IMG_1519222798078.jpg'),
(92, 'Admin', 'Admin', 'A', 'admin@gmail.com', 'admin', 'admin', 'http://basurajuan.x10host.com/images/IMG-20180306-WA0001.jpg'),
(93, 'Punso', 'Nunu', 'S', 'nunu@gmail.com', 'nunu', 'punso', 'http://basurajuan.x10host.com/images/IMG-20180320-WA0001.jpg'),
(94, 'Apao', 'Devon', 'A', 'apaodevonrey@gmail.com', 'apaod', '123', 'http://basurajuan.x10host.com/images/received_1831234296897331.jpeg');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `deployment`
--
ALTER TABLE `deployment`
  ADD CONSTRAINT `deployment_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
