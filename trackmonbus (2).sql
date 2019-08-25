-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Aug 17, 2019 at 05:56 AM
-- Server version: 5.7.24
-- PHP Version: 7.2.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `trackmonbus`
--

-- --------------------------------------------------------

--
-- Table structure for table `activations`
--

CREATE TABLE `activations` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `code` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `completed` tinyint(1) NOT NULL DEFAULT '0',
  `completed_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `buses`
--

CREATE TABLE `buses` (
  `no_bus` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `no_tnkb` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipe_id` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tahun` int(11) NOT NULL,
  `kapasitas` int(11) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `buses`
--

INSERT INTO `buses` (`no_bus`, `no_tnkb`, `tipe_id`, `tahun`, `kapasitas`, `created_at`, `updated_at`, `deleted_at`) VALUES
('01', 'BA 0000 NN', 'HN', 2015, 40, '2019-05-15 11:39:32', '2019-05-26 19:26:00', NULL),
('02', 'Ba 1231 MN', 'MTB', 2019, 40, '2019-05-26 19:26:26', '2019-05-26 19:26:26', NULL),
('03', 'Ba 1221 MN', 'HN', 2019, 40, '2019-05-15 11:45:57', '2019-05-15 11:46:00', '2019-05-15 11:46:00'),
('04', 'BA 7987 NN', 'HN', 2015, 20, '2019-08-08 17:00:00', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `detail_jadwals`
--

CREATE TABLE `detail_jadwals` (
  `tgl` date NOT NULL,
  `no_bus` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `shift_id` char(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pramugara_nik` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id_supir` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `km_awal` float DEFAULT NULL,
  `km_akhir` float DEFAULT NULL,
  `keterangan` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `detail_jadwals`
--

INSERT INTO `detail_jadwals` (`tgl`, `no_bus`, `shift_id`, `pramugara_nik`, `user_id_supir`, `km_awal`, `km_akhir`, `keterangan`, `created_at`, `updated_at`, `deleted_at`) VALUES
('2019-07-16', '03', 'pg', '2', '4', 0, 0, '', '2019-08-08 17:00:00', NULL, NULL),
('2019-08-15', '03', 'sg', '2', '3', 0, 0, '', '2019-08-08 18:17:19', '2019-06-23 18:17:19', NULL),
('2019-08-16', '02', 'sg', '1', '4', 0, 0, '', NULL, NULL, NULL),
('2019-08-16', '03', 'pg', '1', '3', 0, 0, '', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `haltes`
--

CREATE TABLE `haltes` (
  `halte_id` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nama` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lat` varchar(22) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lng` varchar(22) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `haltes`
--

INSERT INTO `haltes` (`halte_id`, `nama`, `lat`, `lng`, `created_at`, `updated_at`, `deleted_at`) VALUES
('1', 'Elang Perkasa', '-0.909936', '100.355609', '2019-05-15 11:51:09', '2019-05-15 11:55:46', NULL),
('2', 'STIKES ALIFAH ', '-0.914688', '100.359032', '2019-05-15 11:56:56', '2019-05-15 11:56:59', NULL),
('3', 'Transmart', '-0.923862952093698', '100.36146461963654', '2019-05-16 20:24:09', '2019-05-16 20:47:05', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `jabatans`
--

CREATE TABLE `jabatans` (
  `jabatan_id` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `jabatan` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `jabatans`
--

INSERT INTO `jabatans` (`jabatan_id`, `jabatan`, `created_at`, `updated_at`, `deleted_at`) VALUES
('4', 'STAFF', '2019-05-13 00:41:57', '2019-05-13 01:24:18', NULL),
('6', 'KEPALA', '2019-05-13 00:45:41', '2019-05-13 01:26:12', '2019-05-13 01:26:12'),
('7', 'PEO', '2019-05-13 20:04:16', '2019-05-13 20:04:20', '2019-05-13 20:04:20'),
('8', 'FDS', '2019-05-13 20:06:00', '2019-05-13 20:06:00', NULL),
('9', 'IO', '2019-05-13 20:28:01', '2019-05-13 20:33:18', NULL),
('PRA', 'PRAMUGARA', '2019-05-16 21:44:10', '2019-05-16 21:44:10', NULL),
('SPR', 'SUPIR', '2019-05-16 21:41:24', '2019-05-16 21:41:24', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `jadwals`
--

CREATE TABLE `jadwals` (
  `tgl` date NOT NULL,
  `no_bus` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `jalur_awal_id` char(3) CHARACTER SET latin1 NOT NULL,
  `km_awal` float(8,2) NOT NULL DEFAULT '0.00',
  `km_akhir` float(8,2) NOT NULL DEFAULT '0.00',
  `keterangan` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `jadwals`
--

INSERT INTO `jadwals` (`tgl`, `no_bus`, `jalur_awal_id`, `km_awal`, `km_akhir`, `keterangan`, `created_at`, `updated_at`, `deleted_at`) VALUES
('2019-05-05', '02', '1', 0.00, 0.00, '-', '2019-05-27 01:10:06', '2019-05-27 01:10:06', NULL),
('2019-07-16', '02', '1', 0.00, 0.00, '+  database: rules for database trackmonbus-1557978707240 released successfully\r\ni  hosting[trackmonbus-1557978707240]: finalizing version...\r\n+  hosting[trackmonbus-1557978707240]: version finalized\r\ni  hosting[trackmonbus-1557978707240]: releasing new version...\r\n+  hosting[trackmonbus-1557978707240]: release complete\r\n\r\n+  Deploy complete!\r\n\r\nProject Console: https://console.firebase.google.com/project/trackmonbus-1557978707240/overview\r\nHosting URL: https://trackmonbus-1557978707240.firebaseapp.com\r\n\r\nD:\\PROJECT\\trackmonbus>firebase deploy\r\n\r\n=== Deploying to \'trackmonbus-1557978707240\'...\r\n\r\ni  deploying database, hosting\r\ni  database: checking rules syntax...\r\n+  database: rules syntax for database trackmonbus-1557978707240 is valid\r\ni  hosting[trackmonbus-1557978707240]: beginning deploy...\r\ni  hosting[trackmonbus-1557978707240]: found 2628 files in public\r\n⠹  hosting: adding files to version [0/2628] (0%)^CTerminate batch job (Y/N)? y\r\n', '2019-05-27 01:07:55', '2019-06-28 02:38:29', NULL),
('2019-08-15', '01', '1', 63.00, 0.00, 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', '2019-06-23 18:17:19', '2019-07-21 07:46:18', NULL),
('2019-08-15', '03', '1', 0.00, 78.00, '1sqs', '2019-05-26 22:49:30', '2019-06-28 02:38:29', NULL),
('2019-08-16', '03', '1', 0.00, 0.00, '', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `jalur`
--

CREATE TABLE `jalur` (
  `jalur_id` char(3) NOT NULL,
  `trayek_id` char(2) NOT NULL,
  `urut` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `jalur`
--

INSERT INTO `jalur` (`jalur_id`, `trayek_id`, `urut`) VALUES
('1', '1', 1),
('2', '1', 2);

-- --------------------------------------------------------

--
-- Table structure for table `karyawans`
--

CREATE TABLE `karyawans` (
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nik` varchar(16) COLLATE utf8mb4_unicode_ci NOT NULL,
  `jabatan_id` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `foto` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `karyawans`
--

INSERT INTO `karyawans` (`user_id`, `nik`, `jabatan_id`, `foto`, `created_at`, `updated_at`, `deleted_at`) VALUES
('2', '798798', 'PRA', NULL, NULL, '2019-08-13 04:13:01', '2019-08-13 04:13:01'),
('3', '80889', 'SPR', NULL, NULL, NULL, NULL),
('4', '6970808', 'SPR', NULL, NULL, NULL, NULL),
('5', '7869', 'PRA', NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `keluhans`
--

CREATE TABLE `keluhans` (
  `keluhan_id` char(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `perihal_id` char(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  `isi_keluhan` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `keluhans`
--

INSERT INTO `keluhans` (`keluhan_id`, `user_id`, `perihal_id`, `isi_keluhan`, `created_at`, `updated_at`, `deleted_at`) VALUES
('1', '1', '1', 'Kucing Tekno - Ini adalah tutorial lanjutan dari sebelumnya tentang Membuat Navigation Drawer dan Intent, kali ini kita akan membuat tombol back pada menu atas .\r\n\r\nkucingtekno-tombolback\r\n\r\n\r\nPada project sebelumnya kita sudah membuat satu aplikasi dimana jika akan menekan menu di Navigation Drawer akan muncul halaman baru, namun pada aplikasi tersebut menu untuk kembali ke halaman utama harus menggunakan tombol back pada perangkat android.\r\n\r\n\r\n\r\nkucing tekno\r\nawal sebelum masuk halaman\r\n-\r\n\r\nkucing tekno\r\ntombol back di atas , setelah masuk halaman\r\n\r\nAlangkah baiknya jika aplikasi tersebut memiliki tombol Back di menu atas untuk memudahkan user dalam mengoperasikan aplikasi kita. Tombol Back yang dimaksud adalah icon panah Back yang berfungsi sebagai navigasi kembali ke halaman sebelumnya atau ke halaman utama. \r\n\r\nUntuk membuatnya ikuti langkah-langkah berikut :\r\n\r\n\r\nApa Yang Diperlukan ?\r\n\r\nWAJIB membaca tutorial sebelumnya biar tidak bingung , karena ini adalah tutorial lanjutan Membuat Navigation Drawer dan Intent\r\n\r\n\r\nBUKA PROJECT DAN RUBAH STYLES\r\n\r\nBuka project yang sudah dibuat , buka folder Values – Styles.xml – pilih styles.xml (yang atas).\r\nRubah AppTheme menjadi NO ACTION BAR', '2019-05-17 01:28:17', '2019-05-17 01:28:17', NULL),
('18', '1', '1', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', '2019-07-19 01:06:22', '2019-07-19 01:06:22', NULL),
('19', '1', '1', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', '2019-07-19 01:07:55', '2019-07-19 01:07:55', NULL),
('2', '1', '5', 'layanan bagus sekali lih, saya sangat sukaaa... kasih bintang 5 atau 10 ', '2019-06-05 17:00:00', '2019-07-18 17:00:00', NULL),
('20', '5', '1', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', '2019-07-19 01:25:08', '2019-07-19 01:25:08', NULL),
('3', '2', '4', 'subscribe itu gratis ya kak..  jadi mohon suportnya.. thankyou..', '2019-06-15 01:44:17', '2019-05-14 17:00:00', NULL),
('4', '1', '5', 'Simple yet powerful skeleton animation for all view in android Simple yet powerful skeleton animation for all view in android ', '2019-06-15 01:44:50', '2019-05-06 17:00:00', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `keluhan_buses`
--

CREATE TABLE `keluhan_buses` (
  `keluhan_id` char(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  `no_bus` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `keluhan_buses`
--

INSERT INTO `keluhan_buses` (`keluhan_id`, `no_bus`, `created_at`, `updated_at`, `deleted_at`) VALUES
('19', '01', '2019-07-19 01:07:55', '2019-07-19 01:07:55', NULL),
('20', '02', '2019-07-19 01:25:08', '2019-07-19 01:25:08', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `keluhan_komentars`
--

CREATE TABLE `keluhan_komentars` (
  `keluhan_komentar_id` char(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  `keluhan_id` char(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `isi_komentar` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `keluhan_komentars`
--

INSERT INTO `keluhan_komentars` (`keluhan_komentar_id`, `keluhan_id`, `user_id`, `isi_komentar`, `created_at`, `updated_at`, `deleted_at`) VALUES
('1', '1', '1', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', '2019-07-23 17:00:00', NULL, NULL),
('18380', '20', '0', 'hsjs', '2019-08-16 16:45:26', '2019-08-16 16:45:26', NULL),
('2', '1', '2', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', '2019-07-20 21:08:39', '2019-07-20 21:08:39', NULL),
('22706', '20', '1608191751', '1608191751', '2019-08-16 16:44:22', '2019-08-16 16:44:22', NULL),
('28808', '20', '1608191751', '1608191751', '2019-08-16 16:43:29', '2019-08-16 16:43:29', NULL),
('3', '19', '3', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', '2019-07-20 21:10:24', '2019-07-20 21:10:24', NULL),
('37285', '19', '1', 'komentar', '2019-08-16 14:24:55', '2019-08-16 14:24:55', NULL),
('4', '19', '4', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.', '2019-07-20 21:14:58', '2019-07-20 21:14:58', NULL),
('41091', '20', '0', 'ti', '2019-08-16 16:41:37', '2019-08-16 16:41:37', NULL),
('47755', '19', '1', 'hd', '2019-08-16 15:54:19', '2019-08-16 15:54:19', NULL),
('5', '19', '1', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry\'s standard dummy text ever since', '2019-07-20 21:17:05', '2019-07-20 21:17:05', NULL),
('50781', '20', '1608191751', '1608191751', '2019-08-16 16:44:10', '2019-08-16 16:44:10', NULL),
('53919', '20', '0', 'hhh', '2019-08-16 16:40:35', '2019-08-16 16:40:35', NULL),
('57527', '20', '0', 'yu', '2019-08-16 16:40:37', '2019-08-16 16:40:37', NULL),
('6', '19', '2', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', '2019-07-20 21:17:13', '2019-07-20 21:17:13', NULL),
('62569', '19', '0', 'h', '2019-08-16 16:45:44', '2019-08-16 16:45:44', NULL),
('690', '20', '0', 'ininkomentarku', '2019-08-16 16:40:24', '2019-08-16 16:40:24', NULL),
('7', '19', '1', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', '2019-07-20 21:17:16', '2019-07-20 21:17:16', NULL),
('8', '20', '2', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', '2019-08-06 12:29:31', '2019-08-06 12:29:31', NULL),
('83155', '20', '1608191751', '1608191751', '2019-08-16 16:45:00', '2019-08-16 16:45:00', NULL),
('91891', '20', '0', 'AQ', '2019-08-16 16:47:49', '2019-08-16 16:47:49', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `migrations`
--

CREATE TABLE `migrations` (
  `id` int(10) UNSIGNED NOT NULL,
  `migration` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `batch` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `migrations`
--

INSERT INTO `migrations` (`id`, `migration`, `batch`) VALUES
(1, '2014_07_02_230147_migration_cartalyst_sentinel', 1),
(2, '2014_10_12_100000_create_password_resets_table', 1),
(4, '2019_05_13_070949_create_jabatans_table', 2),
(5, '2019_05_14_034215_create_karyawans_table', 3),
(7, '2019_05_14_042233_create_tipes_table', 4),
(8, '2019_05_14_042451_create_buses_table', 4),
(9, '2019_05_14_044041_create_perihals_table', 5),
(10, '2019_05_14_044306_create_keluhans_table', 6),
(11, '2019_05_14_044712_create_keluhan_buses_table', 7),
(12, '2019_05_14_050635_create_trayeks_table', 7),
(13, '2019_05_14_050739_create_haltes_table', 7),
(14, '2019_05_14_051028_create_trayek_details_table', 8),
(15, '2019_05_14_051222_create_shifts_table', 9),
(16, '2019_05_27_043236_create_keluhan_komentars_table', 10),
(17, '2019_05_27_044951_create_jadwals_table', 10),
(18, '2019_05_27_050418_create_detail_jadwals_table', 11),
(19, '2019_05_27_050711_create_rits_table', 12);

-- --------------------------------------------------------

--
-- Table structure for table `password_resets`
--

CREATE TABLE `password_resets` (
  `email` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `perihals`
--

CREATE TABLE `perihals` (
  `perihal_id` char(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  `perihal` varchar(60) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `perihals`
--

INSERT INTO `perihals` (`perihal_id`, `perihal`, `created_at`, `updated_at`, `deleted_at`) VALUES
('1', 'Layanan', '2019-05-17 01:09:07', '2019-05-17 01:13:05', NULL),
('3', 'Harga', '2019-05-17 01:13:36', '2019-05-17 01:13:38', '2019-05-17 01:13:38'),
('4', 'Fasilitas', '2019-05-17 01:14:15', '2019-05-17 01:15:53', '2019-05-17 01:15:53'),
('5', 'Keamanan', '2019-05-17 01:22:48', '2019-05-17 01:22:59', NULL),
('6', 'Kenyamanan', '2019-05-17 01:23:04', '2019-05-17 01:23:08', '2019-05-17 01:23:08');

-- --------------------------------------------------------

--
-- Table structure for table `persistences`
--

CREATE TABLE `persistences` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `code` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pramugara`
--

CREATE TABLE `pramugara` (
  `pramugara_nik` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `nama_pramugara` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pramugara`
--

INSERT INTO `pramugara` (`pramugara_nik`, `nama_pramugara`) VALUES
('1', 'a'),
('2', 's');

-- --------------------------------------------------------

--
-- Table structure for table `reminders`
--

CREATE TABLE `reminders` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `code` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `completed` tinyint(1) NOT NULL DEFAULT '0',
  `completed_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `rits`
--

CREATE TABLE `rits` (
  `rits_id` char(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tgl` date NOT NULL,
  `no_bus` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `jalur_Id` char(3) CHARACTER SET latin1 NOT NULL,
  `waktu_berangkat` time DEFAULT NULL,
  `waktu_datang` time DEFAULT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `rits`
--

INSERT INTO `rits` (`rits_id`, `tgl`, `no_bus`, `jalur_Id`, `waktu_berangkat`, `waktu_datang`, `status`, `created_at`, `updated_at`, `deleted_at`) VALUES
('1', '2019-07-16', '03', '2', '10:00:00', '11:04:00', 0, '2019-08-15 03:00:00', NULL, NULL),
('2', '2019-07-16', '02', '1', '06:00:00', NULL, 0, '2019-08-12 21:00:00', NULL, NULL),
('3', '2019-07-16', '01', '2', NULL, NULL, 0, '2019-08-13 04:00:00', NULL, NULL),
('4', '2019-07-16', '02', '2', NULL, NULL, 0, '2019-08-13 13:15:14', NULL, NULL),
('5', '2019-07-16', '03', '1', '11:14:00', '12:10:00', 0, '2019-08-15 04:14:08', NULL, NULL),
('6', '2019-07-16', '01', '1', NULL, NULL, 0, '2019-08-13 13:20:23', NULL, NULL),
('7', '2019-07-16', '01', '2', NULL, NULL, 0, '2019-08-13 13:46:20', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int(10) UNSIGNED NOT NULL,
  `slug` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `permissions` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `role_users`
--

CREATE TABLE `role_users` (
  `user_id` int(10) UNSIGNED NOT NULL,
  `role_id` int(10) UNSIGNED NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `role_users`
--

INSERT INTO `role_users` (`user_id`, `role_id`, `created_at`, `updated_at`) VALUES
(1, 1, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `shifts`
--

CREATE TABLE `shifts` (
  `shift_id` varchar(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  `shift` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `jam_awal` time NOT NULL,
  `jam_akhir` time NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `shifts`
--

INSERT INTO `shifts` (`shift_id`, `shift`, `jam_awal`, `jam_akhir`, `created_at`, `updated_at`, `deleted_at`) VALUES
('pg', 'Pagi', '08:00:00', '12:00:00', '2019-05-15 13:02:33', '2019-05-15 13:22:08', NULL),
('sg', 'Siang', '12:00:00', '23:00:00', '2019-05-15 13:23:06', '2019-05-15 13:23:06', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `throttle`
--

CREATE TABLE `throttle` (
  `id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED DEFAULT NULL,
  `type` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ip` varchar(191) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tipes`
--

CREATE TABLE `tipes` (
  `tipe_id` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipe` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tipes`
--

INSERT INTO `tipes` (`tipe_id`, `tipe`, `created_at`, `updated_at`, `deleted_at`) VALUES
('HN', 'Hino', '2019-05-15 11:11:31', '2019-05-15 11:11:31', NULL),
('MTB', 'Mitsubisi', '2019-05-16 21:51:48', '2019-05-16 21:51:48', NULL),
('SDN', 'Sedan', '2019-05-14 00:00:16', '2019-05-16 21:52:01', '2019-05-16 21:52:01');

-- --------------------------------------------------------

--
-- Table structure for table `trayeks`
--

CREATE TABLE `trayeks` (
  `trayek_id` char(2) CHARACTER SET latin1 NOT NULL,
  `trayek` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
  `km_rit` double(8,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `trayeks`
--

INSERT INTO `trayeks` (`trayek_id`, `trayek`, `km_rit`, `created_at`, `updated_at`, `deleted_at`) VALUES
('1', 'koridor 1', 2.00, '2019-05-15 12:05:45', '2019-05-15 12:05:45', NULL),
('2', 'koridor 2', 4.20, '2019-05-26 23:31:18', '2019-05-26 23:31:18', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `trayek_details`
--

CREATE TABLE `trayek_details` (
  `jalur_id` char(3) CHARACTER SET latin1 NOT NULL,
  `halte_id` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `urut` int(2) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `permissions` text COLLATE utf8mb4_unicode_ci,
  `last_login` timestamp NULL DEFAULT NULL,
  `name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role_id` int(11) NOT NULL,
  `remember_token` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `email`, `password`, `permissions`, `last_login`, `name`, `role_id`, `remember_token`, `created_at`, `updated_at`) VALUES
('1', 'dara@example.com', '$2y$10$1HNQgEjlzDdFNglzWjwKYewVJZns/nhHL12aiqfUkx4LdnzsbFheK', NULL, NULL, 'dara', 0, '', '2019-05-12 20:42:06', '2019-05-12 20:42:06'),
('160819103', 'i@i.com', '$2y$10$1XDZSLoYhpAQ1E8VVYcHrOWPemn4nffdYIs2DjmrLfsgiU2IJKcBG', NULL, NULL, 'i@i.com', 1, NULL, '2019-08-16 16:55:39', '2019-08-16 16:55:39'),
('1608191751', 'm@m.com', '$2y$10$gbDQmkKkEglr9AgcT7csVOeL9S0MXFBQAqh.PVH//pYztav2RSG0a', NULL, NULL, 'mj', 1, NULL, '2019-08-16 16:40:00', '2019-08-16 16:40:00'),
('16081918', 'ms1qm@mm.com', '$2y$10$1EC1mUSrqHORBEKOoODJNO3GufaidfvDCdJlqLg89YRkC18s5V9A.', NULL, NULL, 'm1', 1, NULL, '2019-08-16 16:53:16', '2019-08-16 16:53:16'),
('1608193344', 'mm@mm.com', '$2y$10$uQyxK7qTSAGVUiAF/mxrFOx/U3CSODlxqfrYQFqfINOQE7.U8go9S', NULL, NULL, 'm', 1, NULL, '2019-08-16 16:39:37', '2019-08-16 16:39:37'),
('160819391', 'm1qm@mm.com', '$2y$10$lO1.DDyMTS8AtkjpYhP3pOYVrVTHQvNM6HXWBq7H3ZyJK8za3PFaa', NULL, NULL, 'm1', 1, NULL, '2019-08-16 16:52:37', '2019-08-16 16:52:37'),
('1608194209', 'mm@jmm.com', '$2y$10$Lo.uKbemOSnzoeM27uouyewszKprlILPYDvo.vkrs3Uk2oMrbGfxy', NULL, NULL, 'mama', 1, NULL, '2019-08-16 16:49:18', '2019-08-16 16:49:18'),
('160819842', 'smsm@mm.com', '$2y$10$lPkC8JcQHMFWdnF6skwyQeI7MBScBdPVT49TMgLj2eHmcYJ5.B2y.', NULL, NULL, 'm1', 1, NULL, '2019-08-16 16:55:12', '2019-08-16 16:55:12'),
('1608198815', 'msm@mm.com', '$2y$10$P71HXjaaAYt.D3kLJSAL6Opjwir7i/5Pd4xvP4YIO2uIsUhp1JCeC', NULL, NULL, 'm1', 1, NULL, '2019-08-16 16:54:39', '2019-08-16 16:54:39'),
('1608198860', 'bus@com.com', '$2y$10$FtzqGr3ZojmaQgmJYLEiaOzsce5ukE/ygFd8aqQI4SPh8iviC/Oqm', NULL, NULL, 'bus', 1, NULL, '2019-08-16 16:50:34', '2019-08-16 16:50:34'),
('1608198974', 'ibnu@a.com', '$2y$10$9pUFC6BWi.tme7Yj3FV0veer.2q9u/6ZUTuOIwKgzPjYEhaLcpkjy', NULL, NULL, 'ibnu', 1, NULL, '2019-08-16 16:48:27', '2019-08-16 16:48:27'),
('160819898', 'ms1qsm@mm.com', '$2y$10$HL.y1U92GbvWfPJQANo08uv9BAJ8oWgZc9f5v4jxOjFxo3TZZS78y', NULL, NULL, 'm1', 1, NULL, '2019-08-16 16:53:38', '2019-08-16 16:53:38'),
('160819924', 'm1m@mm.com', '$2y$10$uJa5BkJGfwx2phpyAWTTveGLE.T2cXY9XGoxZ.YfflTDIyExeZdCi', NULL, NULL, 'm1', 1, NULL, '2019-08-16 16:51:39', '2019-08-16 16:51:39'),
('2', 'pramugara@example.com', '$2y$10$1HNQgEjlzDdFNglzWjwKYewVJZns/nhHL12aiqfUkx4LdnzsbFheK', NULL, NULL, 'Suci', 1, '', NULL, NULL),
('3', 'supir@example.com', '$2y$10$1HNQgEjlzDdFNglzWjwKYewVJZns/nhHL12aiqfUkx4LdnzsbFheK', NULL, NULL, 'Budi Hartono', 1, '', NULL, NULL),
('4', 'supir2@example.com', '$2y$10$1HNQgEjlzDdFNglzWjwKYewVJZns/nhHL12aiqfUkx4LdnzsbFheK', NULL, NULL, 'Handoko', 1, '', NULL, NULL),
('5', 'pramugara2@example.com', '$2y$10$1HNQgEjlzDdFNglzWjwKYewVJZns/nhHL12aiqfUkx4LdnzsbFheK', NULL, NULL, 'Putri', 1, '', NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `activations`
--
ALTER TABLE `activations`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `buses`
--
ALTER TABLE `buses`
  ADD PRIMARY KEY (`no_bus`),
  ADD UNIQUE KEY `buses_no_tnkb_unique` (`no_tnkb`),
  ADD KEY `tipe_id` (`tipe_id`);

--
-- Indexes for table `detail_jadwals`
--
ALTER TABLE `detail_jadwals`
  ADD PRIMARY KEY (`tgl`,`no_bus`,`shift_id`),
  ADD KEY `user_id_pramugara` (`pramugara_nik`,`user_id_supir`),
  ADD KEY `user_id_supir` (`user_id_supir`),
  ADD KEY `no_bus` (`no_bus`),
  ADD KEY `shift_id` (`shift_id`);

--
-- Indexes for table `haltes`
--
ALTER TABLE `haltes`
  ADD PRIMARY KEY (`halte_id`);

--
-- Indexes for table `jabatans`
--
ALTER TABLE `jabatans`
  ADD PRIMARY KEY (`jabatan_id`),
  ADD UNIQUE KEY `jabatan` (`jabatan`);

--
-- Indexes for table `jadwals`
--
ALTER TABLE `jadwals`
  ADD PRIMARY KEY (`tgl`,`no_bus`),
  ADD KEY `no_bus` (`no_bus`),
  ADD KEY `jalur_id` (`jalur_awal_id`);

--
-- Indexes for table `jalur`
--
ALTER TABLE `jalur`
  ADD PRIMARY KEY (`jalur_id`),
  ADD KEY `trayek_id` (`trayek_id`);

--
-- Indexes for table `karyawans`
--
ALTER TABLE `karyawans`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `karyawans_nik_unique` (`nik`),
  ADD KEY `jabatan_id` (`jabatan_id`);

--
-- Indexes for table `keluhans`
--
ALTER TABLE `keluhans`
  ADD PRIMARY KEY (`keluhan_id`),
  ADD KEY `perihal_id` (`perihal_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `keluhan_buses`
--
ALTER TABLE `keluhan_buses`
  ADD PRIMARY KEY (`keluhan_id`,`no_bus`),
  ADD KEY `no_bus` (`no_bus`);

--
-- Indexes for table `keluhan_komentars`
--
ALTER TABLE `keluhan_komentars`
  ADD PRIMARY KEY (`keluhan_komentar_id`),
  ADD KEY `keluhan_id` (`keluhan_id`);

--
-- Indexes for table `migrations`
--
ALTER TABLE `migrations`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `password_resets`
--
ALTER TABLE `password_resets`
  ADD KEY `password_resets_email_index` (`email`);

--
-- Indexes for table `perihals`
--
ALTER TABLE `perihals`
  ADD PRIMARY KEY (`perihal_id`),
  ADD UNIQUE KEY `perihal` (`perihal`);

--
-- Indexes for table `persistences`
--
ALTER TABLE `persistences`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `persistences_code_unique` (`code`);

--
-- Indexes for table `pramugara`
--
ALTER TABLE `pramugara`
  ADD PRIMARY KEY (`pramugara_nik`);

--
-- Indexes for table `reminders`
--
ALTER TABLE `reminders`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rits`
--
ALTER TABLE `rits`
  ADD PRIMARY KEY (`rits_id`),
  ADD KEY `tgl` (`tgl`),
  ADD KEY `no_bus` (`no_bus`),
  ADD KEY `jalur_Id` (`jalur_Id`),
  ADD KEY `jalur_Id_2` (`jalur_Id`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `roles_slug_unique` (`slug`);

--
-- Indexes for table `role_users`
--
ALTER TABLE `role_users`
  ADD PRIMARY KEY (`user_id`,`role_id`);

--
-- Indexes for table `shifts`
--
ALTER TABLE `shifts`
  ADD PRIMARY KEY (`shift_id`);

--
-- Indexes for table `throttle`
--
ALTER TABLE `throttle`
  ADD PRIMARY KEY (`id`),
  ADD KEY `throttle_user_id_index` (`user_id`);

--
-- Indexes for table `tipes`
--
ALTER TABLE `tipes`
  ADD PRIMARY KEY (`tipe_id`),
  ADD UNIQUE KEY `tipe` (`tipe`);

--
-- Indexes for table `trayeks`
--
ALTER TABLE `trayeks`
  ADD PRIMARY KEY (`trayek_id`),
  ADD UNIQUE KEY `trayek` (`trayek`);

--
-- Indexes for table `trayek_details`
--
ALTER TABLE `trayek_details`
  ADD PRIMARY KEY (`jalur_id`,`halte_id`),
  ADD KEY `halte_id` (`halte_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `users_email_unique` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `activations`
--
ALTER TABLE `activations`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `migrations`
--
ALTER TABLE `migrations`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `persistences`
--
ALTER TABLE `persistences`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `reminders`
--
ALTER TABLE `reminders`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `throttle`
--
ALTER TABLE `throttle`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `buses`
--
ALTER TABLE `buses`
  ADD CONSTRAINT `buses_ibfk_1` FOREIGN KEY (`tipe_id`) REFERENCES `tipes` (`tipe_id`) ON UPDATE CASCADE;

--
-- Constraints for table `detail_jadwals`
--
ALTER TABLE `detail_jadwals`
  ADD CONSTRAINT `detail_jadwals_ibfk_4` FOREIGN KEY (`user_id_supir`) REFERENCES `users` (`user_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `detail_jadwals_ibfk_6` FOREIGN KEY (`tgl`) REFERENCES `jadwals` (`tgl`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `detail_jadwals_ibfk_7` FOREIGN KEY (`no_bus`) REFERENCES `jadwals` (`no_bus`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `detail_jadwals_ibfk_8` FOREIGN KEY (`shift_id`) REFERENCES `shifts` (`shift_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `detail_jadwals_ibfk_9` FOREIGN KEY (`pramugara_nik`) REFERENCES `pramugara` (`pramugara_nik`) ON UPDATE CASCADE;

--
-- Constraints for table `jadwals`
--
ALTER TABLE `jadwals`
  ADD CONSTRAINT `jadwals_ibfk_2` FOREIGN KEY (`no_bus`) REFERENCES `buses` (`no_bus`) ON UPDATE CASCADE,
  ADD CONSTRAINT `jadwals_ibfk_3` FOREIGN KEY (`jalur_awal_id`) REFERENCES `jalur` (`jalur_id`) ON UPDATE CASCADE;

--
-- Constraints for table `jalur`
--
ALTER TABLE `jalur`
  ADD CONSTRAINT `jalur_ibfk_1` FOREIGN KEY (`trayek_id`) REFERENCES `trayeks` (`trayek_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `karyawans`
--
ALTER TABLE `karyawans`
  ADD CONSTRAINT `karyawans_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `karyawans_ibfk_3` FOREIGN KEY (`jabatan_id`) REFERENCES `jabatans` (`jabatan_id`) ON UPDATE CASCADE;

--
-- Constraints for table `keluhans`
--
ALTER TABLE `keluhans`
  ADD CONSTRAINT `keluhans_ibfk_1` FOREIGN KEY (`perihal_id`) REFERENCES `perihals` (`perihal_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `keluhans_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `keluhan_buses`
--
ALTER TABLE `keluhan_buses`
  ADD CONSTRAINT `keluhan_buses_ibfk_1` FOREIGN KEY (`no_bus`) REFERENCES `buses` (`no_bus`) ON UPDATE CASCADE,
  ADD CONSTRAINT `keluhan_buses_ibfk_2` FOREIGN KEY (`keluhan_id`) REFERENCES `keluhans` (`keluhan_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `keluhan_komentars`
--
ALTER TABLE `keluhan_komentars`
  ADD CONSTRAINT `keluhan_komentars_ibfk_1` FOREIGN KEY (`keluhan_id`) REFERENCES `keluhans` (`keluhan_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `rits`
--
ALTER TABLE `rits`
  ADD CONSTRAINT `rits_ibfk_5` FOREIGN KEY (`tgl`) REFERENCES `jadwals` (`tgl`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `rits_ibfk_6` FOREIGN KEY (`no_bus`) REFERENCES `jadwals` (`no_bus`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `rits_ibfk_7` FOREIGN KEY (`jalur_Id`) REFERENCES `jalur` (`jalur_id`) ON UPDATE CASCADE;

--
-- Constraints for table `trayek_details`
--
ALTER TABLE `trayek_details`
  ADD CONSTRAINT `trayek_details_ibfk_1` FOREIGN KEY (`jalur_id`) REFERENCES `jalur` (`jalur_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `trayek_details_ibfk_2` FOREIGN KEY (`halte_id`) REFERENCES `haltes` (`halte_id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
