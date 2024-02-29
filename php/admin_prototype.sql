-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jul 20, 2020 at 05:00 AM
-- Server version: 5.7.30-0ubuntu0.18.04.1
-- PHP Version: 7.2.24-0ubuntu0.18.04.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `admin_prototype`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin_mst`
--

CREATE TABLE `admin_mst` (
  `am_id` int(11) NOT NULL,
  `am_username` varchar(50) NOT NULL,
  `am_password` varchar(255) NOT NULL,
  `am_email` varchar(100) NOT NULL,
  `am_role` varchar(10) NOT NULL,
  `am_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `order_mst`
--

CREATE TABLE `order_mst` (
  `om_id` int(11) NOT NULL,
  `om_order_id` int(11) NOT NULL,
  `om_store_id` int(11) NOT NULL,
  `om_user_id` int(11) NOT NULL,
  `om_details` text NOT NULL,
  `om_user_details` text NOT NULL,
  `om_store_details` text NOT NULL,
  `om_total` int(11) NOT NULL,
  `om_status` tinyint(4) NOT NULL COMMENT '2=Cancel | 1=Complete | 0=Pending',
  `om_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `order_mst`
--

INSERT INTO `order_mst` (`om_id`, `om_order_id`, `om_store_id`, `om_user_id`, `om_details`, `om_user_details`, `om_store_details`, `om_total`, `om_status`, `om_date`) VALUES
(1, 832353, 1, 1, '[{\"pId\":\"7\",\"pName\":\"Nutella Hazelnut Spread with Cocoa, 290g\",\"pAmount\":\"290\",\"pQty\":\"1\",\"pImage\":\"1594099197.jpeg\"},{\"pId\":\"4\",\"pName\":\"Jivraj 9 tea 1kg\",\"pAmount\":\"440\",\"pQty\":\"1\",\"pImage\":\"1594098123.jpeg\"},{\"pId\":\"2\",\"pName\":\"Britannia Bourbon chocolate Cookie\",\"pAmount\":\"150\",\"pQty\":\"1\",\"pImage\":\"1594097559.jpeg\"}]', '{\"um_id\":\"1\",\"um_f_name\":\"Milan\",\"um_l_name\":\"sangani\",\"um_email\":\"milansangani@gmail.com\",\"um_contact\":\"9852542468\",\"um_img\":\"1594096195.jpeg\",\"um_date\":\"2020-07-07\"}', '{\"sm_id\":\"1\",\"sm_name\":\"D-mart\",\"sm_email\":\"zitenterprise1806@gmail.com\",\"sm_contact\":\"8881536185\",\"sm_address\":\"katargam , surat\",\"sm_img\":\"1594095093.jpeg\",\"sm_latitude\":\"21.224642523283773\",\"sm_longitude\":\"72.80711598694324\",\"sm_status\":\"1\",\"sm_date\":\"2020-07-07\"}', 880, 0, '2020-07-07');

-- --------------------------------------------------------

--
-- Table structure for table `product_mst`
--

CREATE TABLE `product_mst` (
  `pm_id` int(11) NOT NULL,
  `pm_store_id` int(11) NOT NULL,
  `pm_name` varchar(255) NOT NULL,
  `pm_img` varchar(255) NOT NULL,
  `pm_price` varchar(10) NOT NULL,
  `pm_qty` int(11) NOT NULL,
  `pm_description` text NOT NULL,
  `pm_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `product_mst`
--

INSERT INTO `product_mst` (`pm_id`, `pm_store_id`, `pm_name`, `pm_img`, `pm_price`, `pm_qty`, `pm_description`, `pm_date`) VALUES
(1, 1, 'Gowarthan cheese slice 200gm', '1594097111.jpeg', '110', 1, 'Fresh and healthy gowarthan cheese', '2020-07-07'),
(2, 1, 'Britannia Bourbon chocolate Cookie', '1594097559.jpeg', '150', 0, 'Britannia Bourbon has smooth chocolate cream spread in between crunchy chocolate', '2020-07-07'),
(3, 1, 'Madhur Sugar 500gm', '1594097856.jpeg', '160', 2, 'Sugar is the generic name for sweet-tasting, soluble carbohydrates, many of which are used in food. ', '2020-07-07'),
(4, 1, 'Jivraj 9 tea 1kg', '1594098123.jpeg', '440', 0, 'Tea is an integral part of our lives as, with each sip, it helps up to start a day full of new hopes. It is this very spirit that defines Jivraj 9 tea, making it the most popular product from the house of Jivraj. ', '2020-07-07'),
(5, 1, 'NESCAFE classic', '1594098396.jpeg', '490', 1, 'The NESCAFE Coffee Experience\n\nPure natural coffee beans go into making every granule of NESCAFE classic. With the new world class technology, you get richer aroma and an unmatched coffee experience to stimulate your senses with your favorite NESCAFE.', '2020-07-07'),
(6, 1, 'Saffola\n 586\nSaffola Tasty, Pro Fitness Conscious Edible Oil, Pouch, 1 L', '1594098835.jpeg', '130', 1, 'Complement your fitness efforts with Saffola Tasty, Pro Fitness Conscious Edible Oil:\n\nBlend of 60 % refined corn oil and 40% refined rice bran oil.\n\nGood balance of MUFA & PUFA to give you improved nutritional profile .', '2020-07-07'),
(7, 1, 'Nutella Hazelnut Spread with Cocoa, 290g', '1594099197.jpeg', '290', 0, 'Weekend mornings are when the family gathers around the breakfast table with the goal of making memories, not making it out the door on time.', '2020-07-07');

-- --------------------------------------------------------

--
-- Table structure for table `review_mst`
--

CREATE TABLE `review_mst` (
  `rm_id` int(11) NOT NULL,
  `rm_o_id` int(11) NOT NULL COMMENT 'Order ID',
  `rm_u_id` int(11) NOT NULL COMMENT 'User ID',
  `rm_s_id` int(11) NOT NULL COMMENT 'Store ID',
  `rm_p_id` int(11) NOT NULL COMMENT 'Product ID',
  `rm_q1` int(11) NOT NULL,
  `rm_q2` int(11) NOT NULL,
  `rm_review` text NOT NULL,
  `rm_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `store_mst`
--

CREATE TABLE `store_mst` (
  `sm_id` int(11) NOT NULL,
  `sm_name` varchar(255) NOT NULL,
  `sm_email` varchar(100) NOT NULL,
  `sm_password` varchar(255) NOT NULL,
  `sm_contact` varchar(20) NOT NULL,
  `sm_address` varchar(255) NOT NULL,
  `sm_img` varchar(255) NOT NULL,
  `sm_latitude` varchar(255) NOT NULL,
  `sm_longitude` varchar(255) NOT NULL,
  `sm_status` tinyint(4) NOT NULL COMMENT '1=Active | 0=Deactive',
  `sm_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `store_mst`
--

INSERT INTO `store_mst` (`sm_id`, `sm_name`, `sm_email`, `sm_password`, `sm_contact`, `sm_address`, `sm_img`, `sm_latitude`, `sm_longitude`, `sm_status`, `sm_date`) VALUES
(1, 'D-mart', 'zitenterprise1806@gmail.com', '3793b4fa37449a40858cbd8c2c0ee8f0edb79b52e7fc298603657955bb4f1a091f1317162bb7c186f90454331808fa10e6a56d106d7c9f3244e39ccf35381c19Yp2VpHUpK929SIVWGSfCIq09tAEVRyh4jb+xAcs53j4=', '8881536185', 'katargam , surat', '1594095093.jpeg', '21.224642523283773', '72.80711598694324', 1, '2020-07-07'),
(2, 'J K Grocery store', 'jkgrocery@gmail.com', '5ed111d5c7f68b73170d5229254d6fe56c6b58d4bcc07e18d454be02d7525009df7a52dc09aa8be2fd7aad07ceeaecb0463c5e44f66089df76d99b772b1dc798aB/N2o2Kpv7CkoGY7TlLgJvl3l+4gjyHtAwDw9Ph3jk=', '9827051294', 'Adajan, surat', '1594095751.jpeg', '21.19998749032356', '72.78935205191374', 1, '2020-07-07');

-- --------------------------------------------------------

--
-- Table structure for table `user_mst`
--

CREATE TABLE `user_mst` (
  `um_id` int(11) NOT NULL,
  `um_f_name` varchar(50) NOT NULL,
  `um_l_name` varchar(50) NOT NULL,
  `um_password` varchar(255) NOT NULL,
  `um_email` varchar(100) NOT NULL,
  `um_contact` varchar(20) NOT NULL,
  `um_img` varchar(255) NOT NULL,
  `um_status` tinyint(4) NOT NULL COMMENT '1=Active | 0=Dective',
  `um_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user_mst`
--

INSERT INTO `user_mst` (`um_id`, `um_f_name`, `um_l_name`, `um_password`, `um_email`, `um_contact`, `um_img`, `um_status`, `um_date`) VALUES
(1, 'Milan', 'sangani', '2a4d4f0076e517849536ee2485ce21927f4a413b5aaf632bce94a4858033e8ebc825802e8d1062657aef51b1911fe73bd8265073b7229709bd4e06b43d05102awXDQIrKmH+3I3zoXnPZT9cg2z4e/cljxXh34Ls5cFc4=', 'milansangani@gmail.com', '9852542468', '1594096195.jpeg', 0, '2020-07-07');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin_mst`
--
ALTER TABLE `admin_mst`
  ADD PRIMARY KEY (`am_id`);

--
-- Indexes for table `order_mst`
--
ALTER TABLE `order_mst`
  ADD PRIMARY KEY (`om_id`);

--
-- Indexes for table `product_mst`
--
ALTER TABLE `product_mst`
  ADD PRIMARY KEY (`pm_id`);

--
-- Indexes for table `review_mst`
--
ALTER TABLE `review_mst`
  ADD PRIMARY KEY (`rm_id`);

--
-- Indexes for table `store_mst`
--
ALTER TABLE `store_mst`
  ADD PRIMARY KEY (`sm_id`);

--
-- Indexes for table `user_mst`
--
ALTER TABLE `user_mst`
  ADD PRIMARY KEY (`um_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin_mst`
--
ALTER TABLE `admin_mst`
  MODIFY `am_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `order_mst`
--
ALTER TABLE `order_mst`
  MODIFY `om_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `product_mst`
--
ALTER TABLE `product_mst`
  MODIFY `pm_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `review_mst`
--
ALTER TABLE `review_mst`
  MODIFY `rm_id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `store_mst`
--
ALTER TABLE `store_mst`
  MODIFY `sm_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `user_mst`
--
ALTER TABLE `user_mst`
  MODIFY `um_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
