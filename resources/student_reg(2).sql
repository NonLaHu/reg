-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Aug 23, 2025 at 10:07 AM
-- Server version: 11.8.3-MariaDB
-- PHP Version: 8.4.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `student_reg`
--

-- --------------------------------------------------------

--
-- Table structure for table `applicant_academic`
--

CREATE TABLE `applicant_academic` (
  `id` bigint(20) NOT NULL,
  `academic_year` varchar(255) DEFAULT NULL,
  `roll_no` varchar(255) DEFAULT NULL,
  `uni_entrance_rollno` varchar(255) NOT NULL,
  `uni_entrance_year` varchar(255) DEFAULT NULL,
  `application_id` bigint(20) NOT NULL,
  `major_year_id` smallint(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `applicant_contact`
--

CREATE TABLE `applicant_contact` (
  `id` bigint(20) NOT NULL,
  `birth_place` varchar(255) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `full_address` text DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `usual_address_region` varchar(100) DEFAULT NULL,
  `usual_address_township` varchar(100) DEFAULT NULL,
  `usual_phone_number` varchar(20) DEFAULT NULL,
  `application_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `applicant_nrc`
--

CREATE TABLE `applicant_nrc` (
  `id` bigint(20) NOT NULL,
  `national_nrc` varchar(50) DEFAULT NULL,
  `nrc` varchar(50) DEFAULT NULL,
  `nrc_status` enum('AVAILABLE','PENDING') DEFAULT NULL,
  `nrc_type` varchar(20) DEFAULT NULL,
  `application_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `applicant_parents`
--

CREATE TABLE `applicant_parents` (
  `id` bigint(20) NOT NULL,
  `date_of_birth` date DEFAULT NULL,
  `date_of_death` date DEFAULT NULL,
  `ethnic` varchar(50) DEFAULT NULL,
  `full_name_en` varchar(100) NOT NULL,
  `full_name_mm` varchar(100) NOT NULL,
  `is_deceased` bit(1) NOT NULL,
  `occupation` varchar(100) DEFAULT NULL,
  `parent_type` enum('FATHER','MOTHER') NOT NULL,
  `religion` varchar(50) DEFAULT NULL,
  `application_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `applicant_parents_contact`
--

CREATE TABLE `applicant_parents_contact` (
  `id` bigint(20) NOT NULL,
  `full_address` text DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `applicant_parent_id` bigint(20) NOT NULL,
  `usual_address_region` smallint(6) DEFAULT NULL,
  `usual_address_township` smallint(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `applicant_parents_nrc`
--

CREATE TABLE `applicant_parents_nrc` (
  `id` bigint(20) NOT NULL,
  `national_nrc` varchar(50) DEFAULT NULL,
  `nrc` varchar(50) DEFAULT NULL,
  `nrc_status` enum('AVAILABLE','PENDING') NOT NULL,
  `applicant_parent_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `applicant_personal`
--

CREATE TABLE `applicant_personal` (
  `id` bigint(20) NOT NULL,
  `date_of_birth` date NOT NULL,
  `ethnic` varchar(50) DEFAULT NULL,
  `full_name_en` varchar(255) NOT NULL,
  `full_name_mm` varchar(255) NOT NULL,
  `gender` enum('FEMALE','MALE') DEFAULT NULL,
  `religion` varchar(50) DEFAULT NULL,
  `application_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `application_approvals`
--

CREATE TABLE `application_approvals` (
  `id` bigint(20) NOT NULL,
  `approved_at` datetime(6) DEFAULT NULL,
  `approver_role` enum('dorm_warden','finance','hod','student_affair') NOT NULL,
  `remarks` text DEFAULT NULL,
  `status` enum('approved','pending','rejected') NOT NULL,
  `application_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `enrollment_applications`
--

CREATE TABLE `enrollment_applications` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `is_fresher` bit(1) DEFAULT NULL,
  `status` enum('ACCEPTED','PENDING','REJECTED') DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `enrollment_dorm`
--

CREATE TABLE `enrollment_dorm` (
  `id` bigint(20) NOT NULL,
  `another_scholarship` varchar(100) DEFAULT NULL,
  `dorm_fee_responsible` enum('father','mother','other') DEFAULT NULL,
  `other_address` text DEFAULT NULL,
  `other_name` varchar(100) DEFAULT NULL,
  `other_relationship` varchar(50) DEFAULT NULL,
  `scholarship_support` bit(1) DEFAULT NULL,
  `scholarship_type` varchar(100) DEFAULT NULL,
  `application_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `enrollment_files`
--

CREATE TABLE `enrollment_files` (
  `id` bigint(20) NOT NULL,
  `file_path` varchar(255) NOT NULL,
  `uploaded_at` datetime(6) NOT NULL,
  `application_id` bigint(20) NOT NULL,
  `file_type_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `enrollment_majors`
--

CREATE TABLE `enrollment_majors` (
  `id` bigint(20) NOT NULL,
  `preference_rank` int(11) NOT NULL,
  `application_id` bigint(20) NOT NULL,
  `major_id` smallint(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ethnicity`
--

CREATE TABLE `ethnicity` (
  `id` smallint(6) NOT NULL,
  `name_en` varchar(50) NOT NULL,
  `name_mm` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `ethnicity`
--

INSERT INTO `ethnicity` (`id`, `name_en`, `name_mm`) VALUES
(1, 'Kachin', 'ကချင်'),
(2, 'Tarone', 'ကရို'),
(3, 'Dalaung', 'ဒလောင်'),
(4, 'Jinghpaw', 'ဂျိန်းဖော'),
(5, 'Guari', 'ဂေါ်ရစ်'),
(6, 'Hkahku', 'ခခု'),
(7, 'Duleng', 'ဒုရင်'),
(8, 'Maru', 'မရု(လော်ဂေါ်)'),
(9, 'Rawang', 'ရဝမ်'),
(10, 'Lashi', 'လရှီ(လာချစ်)'),
(11, 'Atsi', 'အဇီး'),
(12, 'Lisu', 'လီဆူး'),
(13, 'Kayah', 'ကယား'),
(14, 'Zayein', 'ဇယိမ်း'),
(15, 'Ka-Yun', 'ကယမ်း(ပဒေါင်)'),
(16, 'Gheko', 'ဂေခို'),
(17, 'Kebar', 'ဂေဘား'),
(18, 'Bre', 'ပရဲ(ကယော)'),
(19, 'Manu Manaw', 'ပရဲ(ကယော)'),
(20, 'Yin Talai', 'ယင်းတလဲ'),
(21, 'Yinbaw', 'ယင်းတော်'),
(22, 'Karen', 'ကရင်'),
(23, 'Kayinpyu', 'ကရင်ဖြူ'),
(24, 'Pa-Le-Chi', 'ပလေကီး(ပလေချီ)'),
(25, 'Mon Kayin', 'မွန်ကရင်'),
(26, 'Sgaw', 'စကောကရင်'),
(27, 'Ta-Lay-Pwa', 'တလှေပွာ'),
(28, 'Paku', 'ပကူး'),
(29, 'Bwe', 'ဘွဲ'),
(30, 'Monnepwa', 'မောနေပွား'),
(31, 'Monpwa', 'မိုးပွာ'),
(32, 'Shu', 'ပိုးကရင်'),
(33, 'Chin', 'ချင်း'),
(34, 'Meithei', 'မယ်တိုင်(ကသဲ)'),
(35, 'Saline', 'ဆလိုင်း'),
(36, 'Ka-Lin-Kaw', 'ကလင်တော(လူရှေ)'),
(37, 'Khumi', 'ခမီ'),
(38, 'Mro-Khimi', 'အောဝခမီ'),
(39, 'Khawno', 'ခေါနှိုး'),
(40, 'Kaungso', 'ခေါင်စို'),
(41, 'Kaung Saing Chin', 'ခေါင်ဆိုင်ချင်း'),
(42, 'Kwelshin', 'ခွာဆင်းမ်'),
(43, 'Kwangli', 'ခွန်လီ(ခေဧ)ဆင်မ်'),
(44, 'Gunte people', 'ဂန်ဘဲ'),
(45, 'Gwete', 'ဂွေးတဲ'),
(46, 'Ngorn', 'ငွန်း'),
(47, 'Siyin', 'ဆီစာန်'),
(48, 'Sentang', 'ဆင်ထန်'),
(49, 'Saing Zan', 'ဆိုင်းတန်'),
(50, 'Za-How', 'ဇာတောင်'),
(51, 'Zotung', 'ဇိုထုံး'),
(52, 'Zo-Pe', 'ဇိုဖေ'),
(53, 'Zo', 'ဇို'),
(54, 'zanniat', 'ဇန်ညှပ်(ဇန်နိယပ်)'),
(55, 'Tapong', 'တပေါင်'),
(56, 'Tiddim', 'တီးတိန်(တဲဒင်မ်)'),
(57, 'Tay-Zan', 'တေဇန် '),
(58, 'Taishon', 'တိုင်ချွန်း'),
(59, 'Thado', 'တာ့ဒိုး'),
(60, 'Torr', 'တောရ်'),
(61, 'Dim', 'ဒင်မ်'),
(62, 'Dai', 'ဒိုင်(ယင်ဒူး)'),
(63, 'Naga', 'နာဂ'),
(64, 'Tangkhul', 'တန်ဒူး'),
(65, 'Malin', 'မာရင်'),
(66, 'Panun', 'ပနမ်း'),
(67, 'Magun', 'မကန်း'),
(68, 'Matu', 'မဟူ'),
(69, 'Miram', 'မီရမ်(ခေါ်)မရာ'),
(70, 'Mi-er', 'မီအဲ'),
(71, 'Mgan', 'မွင်း'),
(72, 'Lushei', 'လူရှိုင်း(လူရှေ)'),
(73, 'Laymyo', 'လေးမြို'),
(74, 'Lyente', 'လင်တဲ့'),
(75, 'Lautu', 'လောက်ထူ'),
(76, 'Lai', 'လိုင်'),
(77, 'Laizao', 'လိုင်ဇို'),
(78, 'Mro', 'ပါကင်းမ်(ခေါ်)မရို'),
(79, 'Hualngo', 'ဟွာလ်ငို'),
(80, 'Anu', 'အနူး'),
(81, 'Anun', 'အနန်'),
(82, 'Oo-Pu', 'အူပူ'),
(83, 'Lhinbu', 'လျင်းတု'),
(84, 'Asho', 'အရှိုချင်း(မြေပြန့်)'),
(85, 'Rongtu', 'ရောင်ထု'),
(86, 'Burmar', 'ဗမာ'),
(87, 'Dawei', ' ထားဝယ်'),
(88, 'Beik', 'မြိတ်'),
(89, 'Yaw', 'ယော'),
(90, 'Yabein', 'ရဘိန်း'),
(91, 'Kadu (Kado)', 'ကဒူး(ခုံးကူး)'),
(92, 'Ganan', 'ဂဏန်း'),
(93, 'Salone', 'ဆလုံ'),
(94, 'Hpon', 'ဖုန်း(ဖွန်)'),
(95, 'Mon', 'မွန်'),
(96, 'Rahkine', 'ရခိုင်'),
(97, 'Kamein', 'ကမန်'),
(98, 'Khami', 'ခမီ'),
(99, 'Daingnet', 'ဒိုင်းနက်'),
(100, 'Maramagyi', 'မရမာကြီး'),
(101, 'Mro (Awa Khami)', 'မြို'),
(102, 'Thet', 'သက်'),
(103, 'Shan (Tai)', 'ရှမ်း'),
(104, 'Yun (Lao)', 'ယွန်း(လအို)'),
(105, 'Kwi', 'ကွီ'),
(106, 'Pyin', 'ဖျင်'),
(107, 'Yao', 'သအို'),
(108, 'Danaw', 'စနော'),
(109, 'Pale', 'ပလေး'),
(110, 'Eng (En)', 'အင်'),
(111, 'Son', 'စုံ(ဆံ)'),
(112, 'Khamu', 'ခမူ'),
(113, 'Kaw (Akha-E-Kaw)', 'ကော်(အခါအိကော)'),
(114, 'Kokant (Kokang)', 'ကိုးကန့်'),
(115, 'Khamti Shan', 'ခန္တီးရှမ်း'),
(116, 'Hkun', 'ဂုံ(ခွန်)'),
(117, 'Taungyo', 'တောင်ရိုး'),
(118, 'Danu', 'ဓနု'),
(119, 'Palaung', 'ပလောင်'),
(120, 'Man Zi', 'မြောင်ဇီး'),
(121, 'Yin Kya', 'ယင်းကြား'),
(122, 'Yin Net', 'ယင်းနက်'),
(123, 'Shan Gale', 'ရှမ်းကလေး'),
(124, 'Shan Gyi', 'ရှမ်းကြီး'),
(125, 'Lahu', 'လားဟူ'),
(126, 'Intha', 'အင်းသား'),
(127, 'Eik-swair', 'အိုက်တွယ်'),
(128, 'Pa-O', 'ပအို့ဝ်(တော်သူ)'),
(129, 'Tai-Loi', 'တိုင်းလွယ်'),
(130, 'Tai-Leng (Red Shan)', 'တိုင်းလျမ်'),
(131, 'Tai-Lon', 'တိုင်းလုံ'),
(132, 'Tai-Lay', 'တိုင်းလေ့'),
(133, 'Maingtha (Achang)', 'မိုင်းသာ'),
(134, 'Maw Shan', 'မောရှမ်း'),
(135, 'Wa', 'ဝ'),
(136, 'India', 'အိန္ဒိယ'),
(137, 'Chinese', 'တရုတ်'),
(138, 'Pan Thay', 'ပန်းသေး'),
(139, 'Other', 'အခြား'),
(140, 'Pa-O', 'ပအို၀့်');

-- --------------------------------------------------------

--
-- Table structure for table `file_types`
--

CREATE TABLE `file_types` (
  `id` int(11) NOT NULL,
  `type_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `file_types`
--

INSERT INTO `file_types` (`id`, `type_name`) VALUES
(11, 'community_recommendation'),
(10, 'father_signature'),
(16, 'house_hold_registration'),
(15, 'marks_certification'),
(13, 'medical_recommendation'),
(9, 'mother_signature'),
(7, 'nrc_back_father'),
(5, 'nrc_back_mother'),
(3, 'nrc_back_self'),
(6, 'nrc_front_father'),
(4, 'nrc_front_mother'),
(2, 'nrc_front_self'),
(14, 'pass_certification'),
(17, 'payment_slip'),
(12, 'police_recommendation'),
(1, 'self_picture'),
(8, 'self_signature');

-- --------------------------------------------------------

--
-- Table structure for table `major`
--

CREATE TABLE `major` (
  `id` smallint(6) NOT NULL,
  `code` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `major`
--

INSERT INTO `major` (`id`, `code`, `name`) VALUES
(1, 'IST', 'Information Science and Technology'),
(2, 'CE', 'Computer Engineering'),
(3, 'EcE', 'Electronics Engineering'),
(4, 'Pre', 'Precision'),
(5, 'AME', 'Advanced Material Engineering');

-- --------------------------------------------------------

--
-- Table structure for table `major_year`
--

CREATE TABLE `major_year` (
  `id` smallint(6) NOT NULL,
  `year_label` varchar(50) NOT NULL,
  `year_order` smallint(6) NOT NULL,
  `major_id` smallint(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `major_year`
--

INSERT INTO `major_year` (`id`, `year_label`, `year_order`, `major_id`) VALUES
(25, 'First Year', 1, 1),
(26, 'Second Year', 2, 1),
(27, 'Third Year', 3, 1),
(28, 'Fourth Year', 4, 1),
(29, 'Fifth Year', 5, 1),
(30, 'Sixth Year', 6, 1),
(31, 'First Year', 1, 2),
(32, 'Second Year', 2, 2),
(33, 'Third Year', 3, 2),
(34, 'Fourth Year', 4, 2),
(35, 'Fifth Year', 5, 2),
(36, 'Sixth Year', 6, 2),
(37, 'First Year', 1, 3),
(38, 'Second Year', 2, 3),
(39, 'Third Year', 3, 3),
(40, 'Fourth Year', 4, 3),
(41, 'Fifth Year', 5, 3),
(42, 'Sixth Year', 6, 3),
(43, 'First Year', 1, 4),
(44, 'Second Year', 2, 4),
(45, 'Third Year', 3, 4),
(46, 'Fourth Year', 4, 4),
(47, 'Fifth Year', 5, 4),
(48, 'Sixth Year', 6, 4),
(49, 'First Year', 1, 5),
(50, 'Second Year', 2, 5),
(51, 'Third Year', 3, 5),
(52, 'Fourth Year', 4, 5),
(53, 'Fifth Year', 5, 5),
(54, 'Sixth Year', 6, 5);

-- --------------------------------------------------------

--
-- Table structure for table `nrc_states`
--

CREATE TABLE `nrc_states` (
  `id` smallint(6) NOT NULL,
  `code` varchar(255) NOT NULL,
  `name_en` varchar(255) NOT NULL,
  `name_mm` varchar(255) NOT NULL,
  `number_en` varchar(255) NOT NULL,
  `number_mm` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `nrc_states`
--

INSERT INTO `nrc_states` (`id`, `code`, `name_en`, `name_mm`, `number_en`, `number_mm`) VALUES
(1, 'KAYIN', 'KAYIN', 'ကရင်', '3', '၃'),
(2, 'MANDALAY', 'MANDALAY', 'မန္တလေး', '9', '၉'),
(3, 'CHIN', 'CHIN', 'ချင်း', '4', '၄'),
(4, 'KACHIN', 'KACHIN', 'ကချင်', '1', '၁'),
(5, 'SHAN', 'SHAN', 'ရှမ်း', '13', '၁၃'),
(6, 'SAGAING', 'SAGAING', 'စစ်ကိုင်း', '5', '၅'),
(7, 'RAKHINE', 'RAKHINE', 'ရခိုင်', '11', '၁၁'),
(8, 'NAYPYITAW', 'NAYPYITAW', 'နေပြည်တော်', '9', '၉'),
(9, 'KAYAH', 'KAYAH', 'ကယား', '2', '၂'),
(10, 'AYEYAWADY', 'AYEYAWADY', 'ဧရာ၀တီ', '14', '၁၄'),
(11, 'MAGWAY', 'MAGWAY', 'မကွေး', '8', '၈'),
(12, 'MON', 'MON', 'မွန်', '10', '၁၀'),
(13, 'TANINTHARYI', 'TANINTHARYI', 'တနသာရီ', '6', '၆'),
(14, 'BAGO', 'BAGO', 'ပဲခူး', '7', '၇'),
(15, 'YANGON', 'YANGON', 'ရန်ကုန်', '12', '၁၂');

-- --------------------------------------------------------

--
-- Table structure for table `nrc_townships`
--

CREATE TABLE `nrc_townships` (
  `id` smallint(6) NOT NULL,
  `code` varchar(255) NOT NULL,
  `name_en` varchar(255) NOT NULL,
  `name_mm` varchar(255) NOT NULL,
  `short_en` varchar(255) DEFAULT NULL,
  `short_mm` varchar(255) DEFAULT NULL,
  `state_id` smallint(6) NOT NULL,
  `state_code` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `nrc_townships`
--

INSERT INTO `nrc_townships` (`id`, `code`, `name_en`, `name_mm`, `short_en`, `short_mm`, `state_id`, `state_code`) VALUES
(1, 'NGAYOKEKAUNG-TANINTHARYI', 'NGA YOKE KAUNG - TA NIN THAR YI', 'ငရုတ်ကောင်း - တနင်္သာရီ', 'NGAYAKA', 'ငရက', 13, '6'),
(2, 'DAWBON', 'DAW BON', 'ဒေါပုံ', 'DAPANA', 'ဒပန', 15, '12'),
(3, 'PATHEINGYI', 'PA THEIN GYI', 'ပုသိမ်ကြီး', 'PATHAKA', 'ပသက', 2, '9'),
(4, 'SOUTHOKKALAPA', 'SOUTH OKKALAPA', 'တောင်ဥက္ကလာ', 'OUKATA', 'ဥကတ', 15, '12'),
(5, 'HSAW', 'HSAW', 'ဆော', 'SAMANA', 'ဆမန', 11, '8'),
(6, 'MAUNG TAW', 'MAUNGTAW', 'မောင်းတော', 'BATHATA', 'ဘသတ', 7, '11'),
(7, 'NYAUNGTAYAR', 'NYAUNG TA YAR', 'ညောင်တရာ', '-', '-', 5, '13'),
(8, 'PANSAN-PANKHAM', 'PANSAN-PANKHAM', 'ပန်ဆန်း(PanKham)', 'PASANA', 'ပဆန', 5, '13'),
(9, 'PHAKANT', 'PHA KANT', 'ဖားကန့်', 'PHAKANA', 'ဖကန', 4, '1'),
(10, 'TOUNGOO', 'TOUNGOO', 'တောင်ငူ', 'TANGANA', 'တငန', 14, '7'),
(11, 'MONYO', 'MONYO', 'မိုးညို', 'MANYANA', 'မညန', 14, '7'),
(12, 'MINESAN-MONSAN', 'MINESAN-MONSAN', 'မိုင်းစံ', 'MASATA', 'မစတ', 5, '13'),
(13, 'MYOHLAMYO', 'MYOHLAMYO', 'မြို့လှမြို့', 'MALANA', 'မလန', 4, '1'),
(14, 'GYOBINGAUK', 'GYO BIN GAUK', 'ကြို့ပင်ကောက်', 'KAPAKA', 'ကပက', 14, '7'),
(15, 'MAWLAIK', 'MAWLAIK', 'မော်လိုက်', 'MALANA', 'မလန', 6, '5'),
(16, 'HINTHADA', 'HIN THA DA', 'ဟင်္သာတ', 'HATHATA', 'ဟသတ', 10, '14'),
(17, 'NGAPHAE', 'NGA PHAE', 'ငဖဲ', 'NGAPHANA', 'ငဖန', 11, '8'),
(18, 'DOTPHONEYAN', 'DOT PHONE YAN', 'ဒေါ့ဖုန်းယန်', 'DAPHAYA', 'ဒဖယ', 4, '1'),
(19, 'THINGANGYUN', 'THIN GAN GYUN', 'သင်္ဃန်းကျွန်း', 'THAGAKA', 'သဃက', 15, '12'),
(20, 'MINHLA-THAYAWADY', 'MIN HLA', 'မင်းလှ', 'MALANA', 'မလန', 14, '7'),
(21, 'LAHE', 'LAHE', 'လေဟေ', 'LAHANA', 'လဟန', 6, '5'),
(22, 'BAGO', 'BAGO', 'ပဲခူး', 'PAKHANA', 'ပခန', 14, '7'),
(23, 'BOTAHTAUNG', 'BO TA HTAUNG', 'ဗိုလ်တထောင်', 'BATAHTA', 'ဗတထ', 15, '12'),
(24, 'KENGLAT', 'KENG LAT', 'ကျိုင်းလပ်', 'KALAHTA', 'ကလထ', 5, '13'),
(25, 'AHMAR', 'AHMAR', 'အမာ', 'AHMATA', 'အမတ', 10, '14'),
(26, 'DEPAYIN', 'DE PA YIN', 'ဒီပဲယင်း', 'DAPAYA', 'ဒပယ', 6, '5'),
(27, 'YAYSAKYO', 'YAY SA KYO', 'ရေစကြို', 'YASAKA', 'ရစက', 11, '8'),
(28, 'MINKIN', 'MIN KIN', 'မင်းကင်း', 'MAKANA', 'မကန', 6, '5'),
(29, 'MINEYAUNG', 'MINE YAUNG', 'မိုင်းယောင်း', 'MAYATA', 'မယတ', 5, '13'),
(30, 'WUNDWIN', 'WUN DWIN', 'ဝမ်းတွင်း', 'WATANA', 'ဝတန', 2, '9'),
(31, 'NAUNGMOON', 'NAUNG MOON', 'နောင်မွန်း', 'NAMANA', 'နမန', 4, '1'),
(32, 'KAMINE', 'KA MINE', 'ကာမိုင်း', 'KAMATA', 'ကမန', 4, '1'),
(33, 'AUNGLAN', 'AUNG LAN', 'အောင်လံ', 'AHLANA', 'အလန', 11, '8'),
(34, 'NANHSAM-NORTH', 'NANHSAM-NORTH', 'နမ့်ဆန်', 'NAHSANA', 'နဆန', 5, '13'),
(35, 'BILIN', 'BI LIN', 'ဘီလင်း', 'BALANA', 'ဘလန', 12, '10'),
(36, 'OATWIN', 'OA TWIN', 'အုတ်တွင်း', 'AHTANA', 'အတန', 14, '7'),
(37, 'SINGAING', 'SIN GAING', 'စဉ့်ကိုင်', 'SAKATA', 'စကတ', 2, '9'),
(38, 'KHAMPAT', 'KHAM PAT', 'ခမ်းပတ်', 'KHAPANA', 'ခပန', 6, '5'),
(39, 'MYEIKANOUT', 'MYEIK A NOUT', 'မြိတ်အနောက်', 'MAAHNA', 'မအန', 13, '6'),
(40, 'TANINTHARYI', 'TA NIN THAR YI-THA', 'တနင်္သာရီ', 'TANATHA', 'တနသ', 13, '6'),
(41, 'NAUNGKHIO', 'NAUNG KHIO', 'နောင်ချို', 'NAKHATA', 'နခတ', 5, '13'),
(42, 'WUNTHO', 'WUNTHO', 'ဝန်းသို', 'WATHANA', 'ဝသန', 6, '5'),
(43, 'BAWLAKHE', 'BAW LA KHE', 'ဘော်လခဲ', 'BALAKHA', 'ဘလခ', 9, '2'),
(44, 'WALEMYAING', 'WALE MYAING', 'ေ၀ါလင်မြိုင်', 'WALAMA', '၀လမ', 1, '3'),
(45, 'TONZAUNG', 'TON ZAUNG', 'တွန်းဇံ', 'TAZANA', 'တဇန', 3, '4'),
(46, 'PAUK', 'PAUK', 'ပေါက်', 'PAMANA', 'ပမန', 11, '8'),
(47, 'KALEINAUNG', 'KA LEIN AUNG', 'ကလိန်အောင်', 'KALAAH', 'ကလအ', 13, '6'),
(48, 'SHARDAW', 'SHAR DAW', 'ရှားတော်', 'YATANA', 'ရတန', 9, '2'),
(49, 'MAHLAING', 'MA HLAING', 'မလှိုင်', 'MALANA', 'မလန', 2, '9'),
(50, 'TAMWAY', 'TA MWAY', 'တာမွေ', 'TAMANA', 'တမန', 15, '12'),
(51, 'NGWEHSAUNG', 'NGWE HSAUNG', 'ငွေဆောင်', 'NGASANA', 'ငဆန', 10, '14'),
(52, 'BHAMO', 'BHA MO', 'ဗန်းမော်', 'BAMANA', 'ဗမန', 4, '1'),
(53, 'MANTON', 'MAN TON', 'မန်တုံ', 'MATATA', 'မတတ', 5, '13'),
(54, 'GANGAW', 'GAN GAW', 'ဂန့်ဂေါ', 'GAGANA', 'ဂဂန', 11, '8'),
(55, 'KUAUKKONE', 'KUAUK KONE', '​ကျောက်ကုန်း', 'KAKANA', 'ကကန', 14, '7'),
(56, 'MOGOK', 'MOGOK', 'မိုးကုတ်', 'MAKANA', 'မကန', 2, '9'),
(57, 'NYAUNGDON', 'NYAUNG DON', 'ညောင်တုန်း', 'NYATANA', 'ညတန', 10, '14'),
(58, 'YAYSWAR', 'YAYSWAR', 'ရေဇွာ', 'YAZANA', 'ရဇန', 3, '4'),
(59, 'YWATHIT', 'YWATHIT', 'ရွာသစ်', 'YATHANA', 'ရသန', 9, '2'),
(60, 'HAKA', 'HAKA', 'ဟားခါး', 'HAKHANA', 'ဟခန', 3, '4'),
(61, 'MUDON', 'MUDON', 'မုဒုံ', 'MADANA', 'မဒန', 12, '10'),
(62, 'RIHKHUADAL', 'RIH KHUA DAL', 'ရခူးဒဲ', 'YAKHADA', 'ရခဒ', 3, '4'),
(63, 'MAWLAMYINEGYUN', 'MAWLAMYINEGYUN', 'မော်လမြိုင်ကျွန်း', 'MAMAKA', 'မမက', 10, '14'),
(64, 'SEIKKAN', 'SEIKKAN', 'ဆိပ်ကမ်း', 'SAKANA', 'ဆကန', 15, '12'),
(65, 'KYIMYINDINE', 'KYI MYIN DINE', 'ကြည့်မြင်တိုင်', 'KAMATA', 'ကမတ', 15, '12'),
(66, 'PYU', 'PYU', 'ဖြူး', 'PHAMANA', 'ဖမန', 14, '7'),
(67, 'MANKHAN', 'MAN KHAN', 'မန်းခမ်း', 'MAKHANA', 'မခန', 5, '13'),
(68, 'TATAR', 'TATAR', 'တံတား', 'TATAHTA', 'တတထ', 15, '12'),
(69, 'BUTHIDAUNG', 'BU THI DAUNG', 'ဘူးသီးတောင်', 'BATHATA', 'ဘသတ', 7, '11'),
(70, 'HLAING', 'HLAING', 'လှိုင်', 'LAMANA', 'လမန', 15, '12'),
(71, 'YANKIN', 'YAN KIN', 'ရန်ကင်း', 'YAKANA', 'ရကန', 15, '12'),
(72, 'NATMAUK', 'NAT MAUK', 'နတ်မောက်', 'NAMANA', 'နမန', 11, '8'),
(73, 'KENGTAUNG', 'KENG TAUNG', 'ကျိုင်းတောင်း', 'KATATA', 'ကတတ', 5, '13'),
(74, 'SAMI', 'SAMI', 'စမီး', 'SAMANA', 'ဆမန', 3, '4'),
(75, 'LESHI', 'LE SHI', 'လေရှီး', 'LAYANA', 'လရန', 6, '5'),
(76, 'TAMU', 'TAMU', 'တမူး', 'TAMANA', 'တမန', 6, '5'),
(77, 'MYINMU', 'MYIN MU', 'မြင်းမူ', 'MAMANA', 'မမန', 5, '13'),
(78, 'MYITKYINA', 'MYIT KYI NA', 'မြစ်ကြီးနား', 'MAKANA', 'မကန', 4, '1'),
(79, 'MAHAAUNGMYE', 'MA HA AUNG MYE', 'မဟာအောင်မြေ', 'MAHAMA', 'မဟမ', 2, '9'),
(80, 'KYAUKTAN', 'KYAUK TAN', 'ကျောက်တန်း', 'KATANA', 'ကတန', 15, '12'),
(81, 'KYAUNGON', 'KYAUN GON', 'ကျောင်းကုန်း', 'KAKANA', 'ကကန', 10, '14'),
(82, 'SHWEKYIN', 'SHWEKYIN', 'ရွှေကျင်', 'YAKANA', 'ရကန', 2, '9'),
(83, 'MYEBON', 'MYE BON', 'မြေပုံ', 'MAPATA', 'မပတ', 7, '11'),
(84, 'HAINGGYIKYUN', 'HAING GYI KYUN', 'ဟိုင်းကြီးကျွန်း', 'HAKAKA', 'ဟကက', 10, '14'),
(85, 'KARATHURI', 'KA RA THU RI', 'ကရသုရိ', 'KAYAYA', 'ကရရ', 13, '6'),
(86, 'KYONPYAW', 'KYON PYAW', 'ကျုံပျော်', 'KAPANA', 'ကပန', 10, '14'),
(87, 'PATHEIN-EAST', 'PATHEIN-EAST', 'ပုသိမ် (ရှေ)', 'PATHAYA', 'ပသရ', 10, '14'),
(88, 'ZEYARTHIRI', 'ZE YAR THIRI', 'ဇေယျာသီရိ', 'ZAYATHA', 'ဇယသ', 8, '9'),
(89, 'MINENAUNG', 'MINE NAUNG', 'မိုင်းနောင်', 'MANATA', 'မနန', 5, '13'),
(90, 'MINEPYIN', 'MINE PYIN', 'မိုင်းပျင်', 'MAPATA', 'မပတ', 5, '13'),
(91, 'THAPAUNG', 'THA PAUNG', 'သာပေါင်း', 'THAPANA', 'သပန', 10, '14'),
(92, 'TAMOENYE', 'TA MOE NYE', 'တာမိုးညဲ', 'TAMANYA', 'တမည', 5, '13'),
(93, 'WAKEMA', 'WA KE MA', 'ဝါးခယ်မ', 'WAKHAMA', 'ဝခမ', 10, '14'),
(94, 'YINMARPIN', 'YIN MAR PIN', 'ယင်းမာပင်', 'YAMAPA', 'ယမပ', 6, '5'),
(95, 'HSADONE', 'HSA DONE', 'ဆဒုံး', 'SADANA', 'ဆဒန', 4, '1'),
(96, 'MONEKHA', 'MONE KHA', 'မုန်းခ', '-', '-', 5, '13'),
(97, 'DANUBYU', 'DA NU BYU', 'ဓနုဖြူ', 'DANAPHA', 'ဓနဖ', 10, '14'),
(98, 'YAMETHIN', 'YA ME THIN', 'ရမည်းသင်း', 'YAMATHA', 'ရမသ', 2, '9'),
(99, 'MANNAUNG', 'MANN AUNG', 'မာန်အောင်', 'MAAHNA', 'မအန', 7, '11'),
(100, 'KUNHING', 'KUN HING', 'ကွန်ဟိန်း', 'KAHANA', 'ကဟန', 5, '13'),
(101, 'MUSE', 'MUSE', 'မူဆယ်', 'MASATA', 'မဆတ', 5, '13'),
(102, 'HSIPAW', 'HSI PAW', 'သီပေါ', 'THAPANA', 'သပန', 5, '13'),
(103, 'YAW', 'YAW', 'ယော', 'YAMANA', 'ယမန', 11, '8'),
(104, 'CHAUNGOO', 'CHAUNG OO', 'ချောင်းဦး', 'KHAOUTA', 'ခဥတ', 6, '5'),
(105, 'MYAUKU', 'MYAUK U', 'မြောက်ဦး', 'MAOUNA', 'မဥန', 7, '11'),
(106, 'MANNNAUKMYAUK', 'MANN NAUK MYAUK', 'မန်းနောက်မြောက်', 'MANAMA', 'မနမ', 2, '9'),
(107, 'KALAW', 'KA LAW', 'က‌လော', 'KALANA', 'ကလန', 5, '13'),
(108, 'INGAPU', 'IN GA PU', 'အင်္ဂပူ', 'AHGAPA', 'အဂပ', 10, '14'),
(109, 'THANBYUZAYAT', 'THAN BYU ZA YAT', 'သံဖြူဇရပ်', 'THAPHAYA', 'သဖရ', 12, '10'),
(110, 'PAUNDE', 'PAUN DE', 'ပေါင်းတည်', 'PATATA', 'ပတတ', 14, '7'),
(111, 'BAHAN', 'BAHAN', 'ဗဟန်း', 'BAHANA', 'ဗဟန', 15, '12'),
(112, 'DAIKU', 'DAIK U', 'ဒိုက်ဦး', 'DAOUNA', 'ဒဥန', 14, '7'),
(113, 'KANYEIKTI', 'KAN YEIK TI', 'ကမ်ပတီ', 'KAPATA', 'ကပတ', 4, '1'),
(114, 'PANYAN', 'PANYAN', 'တာချီလိတ်', 'PAYANA', 'တခလ', 5, '13'),
(115, 'PYIGYIMANDAING', 'PYI GYI MAN DAING', 'ပြည်ကြီးမဏ္ဍိုင်', 'PAKAMA', 'ပကမ', 13, '6'),
(116, 'PANGLONG', 'PANG LONG', 'ပင်လုံ', 'PALANA', 'ပလန', 5, '13'),
(117, 'THEGON', 'THE GON', 'သဲကုန်း', 'THAKANA', 'သကန', 14, '7'),
(118, 'NAPHANG', 'NA PHANG', 'နားဖန့်', 'NAPHANA', 'နဖန', 5, '13'),
(119, 'INDAW-SHAN', 'IN DAW-SHAN', 'အင်းတော်', 'AHTANA', 'အတန', 5, '13'),
(120, 'TAUNGPYO', 'TAUNG PYO', 'တောင်ပြိုလက်ဝဲဒ', 'TAPAWA', 'တပ၀', 7, '11'),
(121, 'FALAM', 'FA LAM', 'ဖလမ်း', 'PHALANA', 'ဖလန', 3, '4'),
(122, 'PALAUK', 'PA LAUK', 'ပလောက်', 'PALATA', 'ပလတ', 13, '6'),
(123, 'TATKON', 'TATKON', 'တပ်ကုန်း', 'TAKANA', 'တကန', 2, '9'),
(124, 'HTANTABIN', 'HTAN TA BIN', 'ထန်းတပင်', 'HTATAPA', 'ထတပ', 15, '12'),
(125, 'MYAYHTAE', 'MYAY HTAE', 'မြေထဲ', 'MAHTANA', 'မထန', 11, '8'),
(126, 'MYITTA', 'MYIT TA', 'မေတာ', 'MATANA', 'မတန', 13, '6'),
(127, 'YAYNANCHAUNG', 'YAY NAN CHAUNG', 'ရေနံချောင်း', 'YANAKHA', 'ရနခ', 11, '8'),
(128, 'PINLAUNG', 'PIN LAUNG', 'ပင်လောင်း', 'PALATA', 'ပလန', 5, '13'),
(129, 'KANI', 'KANI', 'ကနီ', 'KANANA', 'ကနန', 6, '5'),
(130, 'PYINSALU', 'PYIN SA LU', 'ပြင်စလူ', 'PASALA', 'ပစလ', 10, '14'),
(131, 'YAEDASHE', 'YAE DA SHE', 'ရေတာရှည်', 'YATAYA', 'ရတရ', 14, '7'),
(132, 'KYUNHLA', 'KYUNHLA', 'ကျွန်းလှ', 'KALANA', 'ကလန', 6, '5'),
(133, 'LINKHE', 'LIN KHE', 'လင်ခေး', 'LAKHATA', 'လခတ', 5, '13'),
(134, 'PAUNG', 'PAUNG', 'ပေါင်', 'PAMANA', 'ပမန', 12, '10'),
(135, 'NAUNGTAYAR', 'NAUNG TA YAR', 'နောင်တရား', 'NATAYA', 'နတရ', 5, '13'),
(136, 'MANNNAUKTAUNG', 'MANN NAUK TAUNG', 'မန်းနောက်တောင်', 'MANATA', 'မနတ', 2, '9'),
(137, 'CHANMYATHARZI', 'CHAN MYA THAR ZI', 'ချမ်းမြေ့သာစည်', 'KHAMASA', 'ခမစ', 2, '9'),
(138, 'HTILIN', 'HTI LIN', 'ထီးလင်း', 'HTALANA', 'ထလန', 11, '8'),
(139, 'ZABUTHIRI', 'ZA BU THIRI', 'ဇမ္ဗူသီရိ', 'ZABATHA', 'ဇဗသ', 2, '9'),
(140, 'KHAYAN', 'KHAYAN', 'ခရမ်း', 'KHAYANA', 'ခရန', 15, '12'),
(141, 'DEKKHINATHIRI', 'DEK KHI NA THIRI', 'ဒက္ခိဏသီရိ', 'DAKHATHA', 'ဒခသ', 2, '9'),
(142, 'KANPALET', 'KAN PA LET', 'ကန်ပက်လက်', 'KAPALA', 'ကပလ', 3, '4'),
(143, 'PYINOOLWIN', 'PYIN OO LWIN', 'ပြင်‌ဦးလွင်‌', 'PAOULA', 'ပဥလ', 2, '9'),
(144, 'DAGON', 'DAGON', 'ဒဂုံ', 'DAGANA', 'ဒဂန', 15, '12'),
(145, 'AN', 'AN', 'အမ်း', 'AHMANA', 'အမန', 7, '11'),
(146, 'HPA-AN', 'HPA-AN', 'ဘားအံ', 'BAAHNA', 'ဘအန', 1, '3'),
(147, 'TANTYAN', 'TANTYAN', 'တန့်ယန်း', 'TAYANA', 'တယန', 5, '13'),
(148, 'OTTARATHIRI', 'OT TA RA THIRI', 'ဥတ္တရသီရိ', 'AUTATHA', 'ဥတသ', 8, '9'),
(149, 'MINTONE', 'MIN TONE', 'မင်းတုန်း', 'MATANA', 'မတန', 11, '8'),
(150, 'OKPO', 'OKPO', 'အုတ်ဖို', 'AHPHANA', 'အဖန', 14, '7'),
(151, 'MINEYU', 'MINE YU', 'မိုင်းယု', '-', '-', 5, '13'),
(152, 'HTANTALANG', 'HTAN TA LANG', 'ထန်တလန်', 'HTATALA', 'ထတလ', 3, '4'),
(153, 'KHAUNGLANPHOO', 'KHAUNG LAN PHOO', 'ခေါင်လန်ဖူး', 'KHALAPHA', 'ခလဖ', 4, '1'),
(154, 'TONETAR', 'TONE TAR', 'တုန်တာ', 'TATANA', 'တတန', 5, '13'),
(155, 'MINE YAL', 'MINE YAL', 'မိုင်းရယ်', 'MARATA', 'မရတ', 5, '13'),
(156, 'TATKON', 'TATKON', 'တပ်ကုန်း', 'TAKANA', 'တကန', 8, '9'),
(157, 'KEHSI', 'KEHSI', 'ကျေးသီ', 'KATHANA', 'ကသန', 5, '13'),
(158, 'BAW GA LI', 'BAW GA LI', 'ဘော်ဂလိ', 'BAGALA', 'ဘဂလ', 1, '3'),
(159, 'MINEPAUK', 'MINE PAUK', 'မိုင်းပေါက်', 'MAPAHTA', 'မပထ', 5, '13'),
(160, 'TAUNGTHA', 'TAUNG THA', 'တောင်သာ', 'TATHANA', 'တသန', 2, '9'),
(161, 'KHINU', 'KHIN U', 'ခင်ဦး', 'KHAOUNA', 'ခဥန', 6, '5'),
(162, 'SAYTOTTARA', 'SAY TOT TA RA', 'စေတုတ္တရာ', 'SATAYA', 'စတရ', 11, '8'),
(163, 'TANINTHARYI-YA', 'TA NIN THAR YI', 'တနင်္သာရီ', 'TATHAYA', 'တသရ', 13, '6'),
(164, 'KATHA', 'KA THA', 'ကသာ', 'KATHANA', 'ကသန', 6, '5'),
(165, 'TAUNGDWINGYI', 'TAUNG DWIN GYI', 'တောင်တွင်းကြီး', 'TATAKA', 'တတက', 11, '8'),
(166, 'SINPAUNGWAE', 'SIN PAUNG WAE', 'ဆင်ပေါင်ဝဲ', 'SAPAWA', 'ဆပ၀', 11, '8'),
(167, 'MINBYA', 'MIN BYA', 'မင်းပြား', 'MAPANA', 'မပန', 7, '11'),
(168, 'SWAMMARA', 'SWAMMARA', 'ဆွမ္မရာ', 'HSAMARA', 'ဆမရ', 6, '5'),
(169, 'HOMALIN', 'HO MA LIN', 'ဟုမ္မလင်း', 'HAMALA', 'ဟမလ', 6, '5'),
(170, 'YEKYI', 'YE KYI', 'ရေကြည်', 'YAKANA', 'ရကန', 10, '14'),
(171, 'KHAMAUKKYI', 'KHA MAUK KYI', 'ခမောက်ကြီး', 'KHAMAKA', 'ခမက', 13, '6'),
(172, 'LOILIN', 'LOILIN', 'လွိုင်လင်', 'LALANA', 'လလန', 5, '13'),
(173, 'MINMU', 'MINMU', 'မြင်းမူ', 'MAMATA', 'မမတ', 6, '5'),
(174, 'OTTARATHIRI', 'OT TA RA THIRI', 'ဥတ္တရသီရိ', 'AUTATHA', 'ဥတသ', 2, '9'),
(175, 'KYAUKHTU', 'KYAUK HTU', 'ကျောက်ထု', 'KAHTANA', 'ကထန', 11, '8'),
(176, 'PAINGKYON', 'PAING KYON', 'ပိုင်ကျုံး', 'PAKANA', 'ပကန', 1, '3'),
(177, 'MAUBIN', 'MA U BIN', 'မအူပင်', 'MAAHPA', 'မအပ', 10, '14'),
(178, 'MAYANGON', 'MAYAN GON', 'မရမ်းကုန်း', 'MAYAKA', 'မရက', 15, '12'),
(179, 'WETLET', 'WET LET', 'ဝက်လက်', 'WALANA', 'ဝလန', 6, '5'),
(180, 'MINEYE', 'MINE YE', 'မိုင်းရယ်', 'MAYANA', 'မရန', 5, '13'),
(181, 'KYARINSEIKKYI', 'KYAR IN SEIK KYI', 'ကြာအင်းဆိပ်ကြီး', 'KASAKA', 'ကဆက', 1, '3'),
(182, 'DAYDAYE', 'DAY DA YE', 'ဒေးဒရဲ', 'DADAYA', 'ဒဒရ', 10, '14'),
(183, 'YEBYU', 'YE BYU', 'ရေဖြူ', 'YAPHANA', 'ရဖန', 13, '6'),
(184, 'PANLON', 'PAN LON', 'ပန်းလောင်', 'PALATA', 'ပလတ', 5, '13'),
(185, 'PATHEIN', 'PA THEIN (WEST)', 'ပုသိမ် (နောက်)', 'PATHAAH', 'ပသအ', 10, '14'),
(186, 'MINHLA-MAGWAY', 'MIN HLA-MAGWAY', 'မင်းလှ', 'MALANA', 'မလန', 11, '8'),
(187, 'COCOGYUN', 'CO CO GYUN', 'ကိုကိုးကျွန်း', 'KAKAKA', 'ကကက', 15, '12'),
(188, 'THABEIKKYIN', 'THA BEIK KYIN', 'သပိတ်ကျင်း', 'THAPAKA', 'သပက', 2, '9'),
(189, 'GWA', 'GWA', 'ဂွ', 'GAMANA', 'ဂမန', 7, '11'),
(190, 'KYAIKEMARAW', 'KYAIKE MA RAW', 'ကျိုက်မရော', 'KAMAYA', 'ကမရ', 12, '10'),
(191, 'NGATHAYAUK', 'NGA THA YAUK', 'ငါးသရောက်', 'NGATHAYA', 'ငသရ', 2, '9'),
(192, 'PALETWA', 'PA LET WA', 'ပလက်ဝ', 'PALAWA', 'ပလဝ', 3, '4'),
(193, 'PYUNTANSAR', 'PYUN TAN SAR', 'ပြွန်တန်ဆာ', 'PATASA', 'ပတဆ', 14, '7'),
(194, 'NGAZUN', 'NGAN ZUN', 'ငါန်းဇွန်', 'NGAZANA', 'ငဇန', 2, '9'),
(195, 'PHARPON-KAYIN', 'PHARPON-KAYIN', 'ဖာပွန်', 'PHAPANA', 'ဖပန', 1, '3'),
(196, 'KUME', 'KU ME', 'ကူမဲ', 'KAMANA', 'ကမန', 2, '9'),
(197, 'TANSEI', 'TAN SEI', 'တန့်ဆည်', 'TASANA', 'တဆန', 6, '5'),
(198, 'KAWLIN', 'KAWLIN', 'ကောလင်း', 'KALATA', 'ကလတ', 6, '5'),
(199, 'BUTALIN', 'BU TA LIN', 'ဘုတလင်', 'BATALA', 'ဘတလ', 6, '5'),
(200, 'TOUNGUP', 'TOUNGUP', 'တောင်ကုတ်', 'TAKANA', 'တကန', 7, '11'),
(201, 'PADAUNG', 'PA DAUNG', 'ပန်းတောင်း', 'PATANA', 'ပတန', 14, '7'),
(202, 'MANSI', 'MAN SI', 'မံစီ', 'MASANA', 'မစန', 4, '1'),
(203, 'SUGALI', 'SU GA LI', 'စုကလိ', 'SAKALA', 'စကလ', 1, '3'),
(204, 'KYAIKTO', 'KYAIK TO', 'ကျိုက်ထို', 'KAHTANA', 'ကထန', 12, '10'),
(205, 'DAGONMYOTHIT(EAST)', 'DAGON MYOTHIT(EAST)', 'ဒဂုံမြို့သစ် (အရှေ့)', 'DAGAYA', 'ဒဂရ', 15, '12'),
(206, 'MOMEIK', 'MO MEIK', 'မိုးမိတ်', 'MAMATA', 'မမတ', 5, '13'),
(207, 'YEU', 'YE U', 'ရေဦး', 'YAOUNA', 'ရဥန', 6, '5'),
(208, 'MINGALADON', 'MIN GA LA DON', 'မင်္ဂလာဒုံ', 'MAGADA', 'မဂဒ', 15, '12'),
(209, 'PYAWBWE', 'PYAW BWE', 'ပျော်ဘွယ်', 'PABANA', 'ပဘန', 2, '9'),
(210, 'CHINSHWEHAW', 'CHIN SHWE HAW', 'ချင်းရွှေဟော်', 'KHAYAHA', 'ခရဟ', 5, '13'),
(211, 'YE', 'YE', 'ရေး', 'YAMANA', 'ရမန', 12, '10'),
(212, 'SITTWAY', 'SIT TWAY', 'စစ်တွေ', 'SATANA', 'စတန', 7, '11'),
(213, 'DEKKHINATHIRI', 'DEK KHI NA THIRI', 'ဒက္ခိဏသီရိ', 'DAKHATHA', 'ဒခသ', 8, '9'),
(214, 'PAENWEKONE', 'PAE NWE KONE', 'ပဲနွယ်ကုန်း', 'PANAKA', 'ပနက', 14, '7'),
(215, 'BOGALE', 'BO GA LE', 'ဘိုကလေး', 'BAKALA', 'ဘကလ', 10, '14'),
(216, 'MABANE', 'MA BANE', 'မဘိန်း', 'MABANA', 'မဘန', 5, '13'),
(217, 'HSINBO', 'SIN BO', 'ဆင်ဘို', 'SABATA', 'ဆဘန', 4, '1'),
(218, 'MINELAR', 'MINE LAR', 'မိုင်းလား', 'MALANA', 'မလန', 5, '13'),
(219, 'THAYETCHAUNG', 'THA YET CHAUNG', 'သရက်ချောင်း', 'THAYAKHA', 'သရခ', 13, '6'),
(220, 'PINLEBU', 'PIN LE BU', 'ပင်လယ်ဘူး', 'PALABA', 'ပလဘ', 6, '5'),
(221, 'PYINMANA', 'PYIN MA NA', 'ပျဉ်းမနား', 'PAMANA', 'ပမန', 2, '9'),
(222, 'MINE HTONE', 'MINE HTONE', 'မိုင်းထုံ', 'MAHTANA', 'မထန', 5, '13'),
(223, 'YATHEDAUNG', 'YA THE DAUNG', 'ရသေ့တောင်', 'YATHATA', 'ရသတ', 7, '11'),
(224, 'SHWEBO', 'SHWE BO', 'ရွှေဘို', 'YABANA', 'ရဘန', 6, '5'),
(225, 'NANHSAM-SOUTH', 'NANHSAM-SOUTH', 'နမ့်စန်(တောင်)', 'NASANA', 'နစန', 5, '13'),
(226, 'NGAYOKAUNG', 'NGAYOKAUNG', 'ငရုတ်ကောင်း', 'NGAYAKA', 'ငရက', 10, '14'),
(227, 'LWEGE', 'LWE GE', 'လွယ်ဂေ', 'LAGANA', 'လဂန', 4, '1'),
(228, 'HOPON', 'HO PON', 'ဟိုပုန်း', 'HAPANA', 'ဟပန', 5, '13'),
(229, 'KYAUKTAW', 'KYAUK TAW', 'ကျောက်တော်', 'KATANA', 'ကတန', 7, '11'),
(230, 'MINE KOKE', 'MINE KOKE', 'မိုင်းခုတ်', 'MAKHATA', 'မခတ', 5, '13'),
(231, 'PHYAPON', 'PHYA PON', 'ဖျာပုံ', 'PHAPANA', 'ဖပန', 10, '14'),
(232, 'KENGTUNG', 'KENG TUNG', 'ကျိုင်းတုံ', 'KATANA', 'ကတန', 5, '13'),
(233, 'KALAY', 'KA LAY', 'ကလေး', 'KALAHTA', 'ကလထ', 6, '5'),
(234, 'THAKAYTA', 'THA KAY TA', 'သာကေတ', 'THAKATA', 'သကတ', 15, '12'),
(235, 'MINEPAN', 'MINEPAN', 'မိုင်းပန်', 'MAPANA', 'မပန', 5, '13'),
(236, 'PONNAGYUN', 'PON NA GYUN', 'ပုဏ္ဏားကျွန်း', 'PANAKA', 'ပဏက', 7, '11'),
(237, 'PONPARKYIN', 'PON PAR KYIN', 'ပုံပါကျင်', 'PAPAKA', 'ပပက', 5, '13'),
(238, 'MINESHU', 'MINESHU', 'မိုင်းရှူး', 'MAYANA', 'မရန', 5, '13'),
(239, 'INDAW', 'IN DAW-SAGAING', 'အင်းတော်', 'AHTANA', 'အတန', 6, '5'),
(240, 'HTIGYAING', 'HTI GYAING', 'ထီးချိုင့်', 'HTAKHANA', 'ထခန', 6, '5'),
(241, 'KONGYAN', 'KON GYAN', 'ကွမ်းယန်', 'KAKANA', 'ကကန', 5, '13'),
(242, 'KYAIKKHAMI', 'KYAIK KHA MI', 'ကျိုက်ခမီ', 'KAKHAMA', 'ကခမ', 12, '10'),
(243, 'MYAUNGMYA', 'MYAUNGMYA', 'မြောင်းမြ', 'MAMANA', 'မမန', 10, '14'),
(244, 'SANGYOUNG', 'SAN GYOUNG', 'စမ်းချောင်း', 'SAKHANA', 'စခန', 15, '12'),
(245, 'HMAWBY', 'HMAWBY', 'မှော်ဘီ', 'MABANA', 'မဘန', 15, '12'),
(246, 'SHWEPAUKKAN', 'SHWE PAUK KAN', 'ရွှေပေါက်ကံ', 'OUKAMA', 'ဥကမ', 15, '12'),
(247, 'MINENGAW', 'MINE NGAW', 'မိုင်းငေါ', 'MANGANA', 'မငန', 5, '13'),
(248, 'PANSAI', 'PANSAI(KYU KUTE)', 'ပန်ဆိုင်း', 'PASATA', 'ပဆတ', 5, '13'),
(249, 'TEDIM', 'TEDIM', 'တီးတိန်', 'TATANA', 'တတန', 3, '4'),
(250, 'MAUKME', 'MAUK ME', 'မောက်မယ်', 'MAMANA', 'မမန', 5, '13'),
(251, 'KAMBALU', 'KAM BA LU', 'ကန့်ဘလူ', 'KABALA', 'ကဘလ', 6, '5'),
(252, 'KANGYIDAUNT', 'KAN GYI DAUNT', 'ကန်ကြီးထောင့်', 'KAKAHTA', 'ကကထ', 10, '14'),
(253, 'CHAUNGZON', 'CHAUNG ZON', 'ချောင်းဆုံ', 'KHASANA', 'ခဆန', 12, '10'),
(254, 'ZEYARTHIRI', 'ZE YAR THIRI', 'ဇေယျာသီရိ', 'ZAYATHA', 'ဇယသ', 2, '9'),
(255, 'MONGKAI', 'MONG KAI', 'မောက်ခိုင်', 'MAKHANA', 'မခန', 5, '13'),
(256, 'PAYARTHONEZU', 'PA YAR THONE ZU', 'ဘုရားသုံးဆူ', 'BATHASA', 'ဘသဆ', 1, '3'),
(257, 'PAN LAUNG', 'PAN LONG', 'ပန်လုံ', 'PALAHTA', 'ပလထ', 5, '13'),
(258, 'LOUNGLON', 'LOUNG LON', 'လောင်းလုံ', 'LALANA', 'လလန', 13, '6'),
(259, 'KYAUKMYAUNG-SAGAING', 'KYAUKMYAUNG-SAGAING', 'ကျောက်မြောင်း', 'KAMANA', 'ကမန', 6, '5'),
(260, 'NAMTU', 'NA M TU', 'နမ္မတူ', 'NAMATA', 'နမတ', 5, '13'),
(261, 'CIKHA', 'CIKHA', 'ကျီခါး', 'KAKHANA', 'ကခန', 3, '4'),
(262, 'ZIGON', 'ZIGON', 'ဇီးကုန်း', 'ZAKANA', 'ဇကန', 14, '7'),
(263, 'MAEI', 'MAEI', 'မအီ', 'MAAHTA', 'မအတ', 7, '11'),
(264, 'LAUKINE', 'LAU KINE', 'လောက်ကိုင်', 'LAKANA', 'လကန', 5, '13'),
(265, 'PYINMANA', 'PYIN MA NA', 'ပျဉ်းမနား', 'PAMANA', 'ပမန', 8, '9'),
(266, 'PANSAUNG', 'PANSAUNG', 'ပန်ဆောင်း', 'PASANA', 'ပဆန', 6, '5'),
(267, 'KHAWBUDAE', 'KHAW BU DAE', 'ခေါ်ဘူဒဲ', 'KHABADA', 'ခဘဒ', 4, '1'),
(268, 'MOHNYIN', 'MOH NYIN', 'မိုးညှင်း', 'MANYANA', 'မညန', 4, '1'),
(269, 'BAWGALI', 'BAW GA LI', 'ဘောကလိ', 'BAKALA', 'ဘကလ', 1, '3'),
(270, 'KUNLON', 'KUN LON', 'ကွမ်းလုံ', 'KALATA', 'ကလတ', 5, '13'),
(271, 'YANBYE', 'YAN BYE', 'ရမ်းဗြဲ', 'YABANA', 'ရဗန', 7, '11'),
(272, 'PAUKTAW', 'PAUK TAW', 'ပေါက်တော', 'PATANA', 'ပတန', 7, '11'),
(273, 'PHAUNGPYIN', 'PHAUNG PYIN', 'ဖောင်းပြင်', 'PHAPANA', 'ဖပန', 6, '5'),
(274, 'TAGAUNG', 'TA GAUNG', 'တကောင်း', 'TAKATA', 'တကတ', 2, '9'),
(275, 'MONE', 'MONE', 'မိုးနဲ', 'MANANA', 'မနန', 5, '13'),
(276, 'MOMAUK', 'MOE MAUK', 'မိုးမောက်', 'MAMANA', 'မမန', 4, '1'),
(277, 'LASHIO', 'LASHIO', 'လားရှိုး', 'LAYANA', 'လရန', 5, '13'),
(278, 'SHANYWATHIT', 'SHAN YWA THIT', 'ရှမ်းရွာသစ်', 'YAYATHA', 'ရရသ', 1, '3'),
(279, 'HEHO', 'HEHO', 'ဟဲဟိုး', 'HAHANA', 'ဟဟန', 5, '13'),
(280, 'KYEINTALI', 'KYEI NTA LI', 'ကျိန္တလီ', 'KATALA', 'ကတလ', 7, '11'),
(281, 'KYAUKPYU', 'KYAUK PYU', 'ကျောက်ဖြူ', 'KAPHANA', 'ကဖန', 7, '11'),
(282, 'LANMADAW', 'LAN MA DAW', 'လမ်းမတော်', 'LAMATA', 'လမတ', 15, '12'),
(283, 'DIMAWSO', 'DI MAW SO', 'ဒီမောဆိုး', 'DAMASA', 'ဒမဆ', 9, '2'),
(284, 'HOMANE', 'HO MANE', 'ဟိုမိန်း', 'HAMANA', 'ဟမန', 5, '13'),
(285, 'NATTALIN', 'NAT TA LIN', 'နတ်တလင်း', 'NATALA', 'နတလ', 14, '7'),
(286, 'MANKAN', 'MANKAN', 'မန္ကန္', 'MAKAHTA', 'မကထ', 5, '13'),
(287, 'LAYMYETHNA', 'LAY MYETH NA', 'လေးမျက်နှာ', 'LAMANA', 'လမန', 10, '14'),
(288, 'NANTIT', 'NAN TIT', 'နန်တစ်', '-', '-', 5, '13'),
(289, 'MINBU', 'MIN BU', 'မင်းဘူး', 'MABANA', 'မဘန', 11, '8'),
(290, 'PAUKKHAUNG', 'PAUKKHAUNG', 'ပေါက်ခေါင်း', 'PAKHATA', 'ပခတ', 14, '7'),
(291, 'MANHERO', 'MAN HE RO', 'မဟီရိုး', 'MAHAYA', 'မဟရ', 5, '13'),
(292, 'KHAN TEE(UPPER)', 'DON HEE', 'ဒုံဟီး', 'DAHANA', 'ဒဟန', 6, '5'),
(293, 'YWARNGAN', 'YWARNGAN', 'ရွာငံ', 'YANGANA', 'ရငန', 5, '13'),
(294, 'NORTHOKKALAPA', 'NORTH OKKALAPA', 'မြောက်ဥက္ကလာ', 'OUKAMA', 'ဥကမ', 15, '12'),
(295, 'THAYAWADY', 'THA YA WA DY', 'သာယာဝတီ', 'THAWATA', 'သဝတ', 14, '7'),
(296, 'KHAWZAR', 'KHAW ZAR', 'ခေါဇာ', 'KHAZANA', 'ခဇန', 12, '10'),
(297, 'PABEDAN', 'PA BE DAN', 'ပန်းဘဲတန်း', 'PABATA', 'ပဘတ', 15, '12'),
(298, 'MINE YAUNG', 'MINE YAUNG', 'မိုင်းယောင်း', 'MAYAHTA', 'မယထ', 5, '13'),
(299, 'KYAUKTADA', 'KYAUK TA DA', 'ကျောက်တံတား', 'KATATA', 'ကတတ', 15, '12'),
(300, 'MINGALATAUNGNYUNT', 'MINGALA TAUNG NYUNT', 'မင်္ဂလာ‌တောင်ညွန့်', 'MAGATA', 'မဂတ', 15, '12'),
(301, 'KAMAMAUNG', 'KA MA MAUNG', 'ကမမောင်', 'KAMAMA', 'ကမမ', 1, '3'),
(302, 'YATSAUK', 'YAT SAUK', 'ရက်စောက်', 'YASANA', 'ရစန', 5, '13'),
(303, 'PYAY', 'PYAY', 'ပြည်', 'PAMANA', 'ပမန', 14, '7'),
(304, 'MYEIK', 'MYEIK', 'မြိတ်', 'MAMANA', 'မမန', 13, '6'),
(305, 'SAGAING', 'SA GAING', 'စစ်ကိုင်း', 'SAKANA', 'စကန', 6, '5'),
(306, 'DAGONMYOTHIT(SEIKKAN)', 'DAGON MYOTHIT(SEIKKAN)', 'ဒဂုံဆိပ်ကမ်း', 'DAGASA', 'ဒဂဆ', 15, '12'),
(307, 'WAW', 'WAW', 'ဝေါ', 'WAMANA', 'ဝမန', 14, '7'),
(308, 'MYAUNG', 'MYAUNG', 'မြောင်', 'MAMANA', 'မမန', 6, '5'),
(309, 'AHLON', 'AHLON', 'အလုံ', 'AHLANA', 'အလန', 15, '12'),
(310, 'MATUPI', 'MA TU PI', 'မတူပီ', 'MATAPA', 'မတပ', 3, '4'),
(311, 'PINDAYA', 'PIN DA YA', 'ပင်းတယ', 'PATAYA', 'ပတယ', 5, '13'),
(312, 'PUTAO', 'PU TA O', 'ပူတာအို', 'PATAAH', 'ပတအ', 4, '1'),
(313, 'SOUTHDAGON', 'SOUTH DAGON', 'ဒဂုံမြို့သစ် (တောင်)', 'DAGATA', 'ဒဂတ', 15, '12'),
(314, 'TWANTAY', 'TWAN TAY', 'တွံတေး', 'TATANA', 'တတန', 15, '12'),
(315, 'NYAUNGSHWE', 'NYAUNG SHWE', 'ညောင်ရွှေ', 'NYAYANA', 'ညရန', 5, '13'),
(316, 'MYEIKASHAE', 'MYEIK A SHE', 'မြိတ်အရှေ့', 'MAAHYA', 'မအရ', 13, '6'),
(317, 'KHOLAN', 'KHO LAN', 'ခိုလန်', 'KHALANA', 'ခလန', 5, '13'),
(318, 'HTANTAPIN', 'HTAN TA PIN', 'ထန်းတပင်', 'HTATAPA', 'ထတပ', 14, '7'),
(319, 'NYAUNGLEBIN', 'NYAUNG LE BIN', 'ညောင်လေးပင်', 'NYALAPA', 'ညလပ', 14, '7'),
(320, 'MINELON', 'MINE LON', 'မိုင်းလုံ', 'MALATA', 'မလတ', 5, '13'),
(321, 'SHWEPYITHA', 'SHWE PYI THA', 'ရွှေပြည်သာ', 'YAPATHA', 'ရပသ', 15, '12'),
(322, 'MAWHTIKE', 'MAW HTIKE', 'မော်ထိုက်', 'MAHTATA', 'မထတ', 5, '13'),
(323, 'SHWENYAUNG', 'SHWE NYAUNG', 'ရွှေညောင်', 'YANYANA', 'ရညန', 5, '13'),
(324, 'HTANPARKWAY', 'HTAN PAR KWAY', 'ထန်ပါခွေ', 'HTAPAKHA', 'ထပခ', 6, '5'),
(325, 'CHIPHWE', 'CHI PHWE', 'ချီဖွေ', 'KHAPHANA', 'ခဖန', 4, '1'),
(326, 'SAKU', 'SA KU', 'စကု', 'SAKANA', 'စကန', 11, '8'),
(327, 'MYOTHIT-MAGWAY', 'MYO THIT-MAGWAY', 'မြို့သစ်', 'MATHANA', 'မသန', 11, '8'),
(328, 'MONYWA', 'MON YWA', 'မုံရွာ', 'MAYANA', 'မရန', 6, '5'),
(329, 'KYAUKTAGA', 'KYAUK TA GA', 'ကျောက်တံခါး', 'KATAKHA', 'ကတခ', 14, '7'),
(330, 'THEINNI', 'THEIN NI', 'သိန္နီ', 'THANANA', 'သနန', 5, '13'),
(331, 'PALAE', 'PALAE', 'ပုလဲ', 'PALANA', 'ပလန', 6, '5'),
(332, 'WAINGMAW', 'WAING MAW', 'ဝိုင်းမော်', 'WAMANA', '၀မန', 4, '1'),
(333, 'LABUTTA', 'LA BUT TA', 'လပတ္တာ', 'LAPATA', 'လပတ', 10, '14'),
(334, 'NAMKHAM', 'NAM KHAM', 'နန့်ခမ်း', 'NAKHANA', 'နခန', 5, '13'),
(335, 'CHANAYETHARZAN', 'CHAN AYE THAR ZAN', 'ချမ်းအေးသာဇံ', 'KHAAHZA', 'ခအဇ', 2, '9'),
(336, 'TALAY', 'TA LAY', 'တာလေ', 'TALANA', 'တလန', 5, '13'),
(337, 'MINEYAN', 'MINE YAN', 'မိုင်းယန်', 'MAYANA', 'မယန', 5, '13'),
(338, 'THAYET', 'THA YET', 'သရက်', 'THAYANA', 'သရန', 11, '8'),
(339, 'SHINBWAYYAN', 'SHIN BWAY YAN', 'ရှင်ဘွီယန်', 'YABAYA', 'ရဘယ', 4, '1'),
(340, 'THAHTON', 'THAHTON', 'သထုံ', 'THAHTANA', 'သထန', 12, '10'),
(341, 'LECHAR', 'LE CHAR', 'လဲချား', 'LAKHANA', 'လခန', 5, '13'),
(342, 'KYAUKME', 'KYAUK ME', 'ကျောက်မဲ', 'KAMANA', 'ကမန', 5, '13'),
(343, 'PAZUNTAUNG', 'PA ZUN TAUNG', 'ပုဇွန်တောင်', 'PAZATA', 'ပဇတ', 15, '12'),
(344, 'INGYANYAN', 'ING YAN YAN', 'အင်ဂျန်းယန်', 'AHGAYA', 'အဂယ', 4, '1'),
(345, 'SHWETHAUNGYAN', 'SHWE THAUNG YAN', 'ရွှေသောင်ရံ', 'YATHAYA', 'ယသရ', 10, '14'),
(346, 'TANATPIN', 'TANATPIN', 'သနပ်ပင်', 'THANAPA', 'သနပ', 14, '7'),
(347, 'MADAUNK', 'MA DAUNK', 'မဒေါက်', 'MADANA', 'မဒန', 14, '7'),
(348, 'MAWLAMYINE', 'MAW LA MYINE', 'မော်လမြိုင်', 'MALAMA', 'မလမ', 12, '10'),
(349, 'MINEKOKE', 'MINE KOKE', 'မိုင်းကော', 'MAKANA', 'မကန', 5, '13'),
(350, 'PANWINE', 'PAN WINE', 'ပန်ဝိုင်း', 'PAWANA', 'ပဝန', 5, '13'),
(351, 'MANNSHAEMYAUK', 'MANN SHAE MYAUK', 'မန်းရှေ့မြောက်', 'MAYAMA', 'မရမ', 2, '9'),
(352, 'AUNGPAN', 'AUNG PAN', 'အောင်ပန်း', 'AHPANA', 'အပန', 5, '13'),
(353, 'SALINGYI', 'SA LIN GYI', 'ဆားလင်းကြီး', 'SALAKA', 'ဆလက', 6, '5'),
(354, 'SHWEGU', 'SHWE GU', 'ရွှေကူ', 'YAKANA', 'ရကန', 4, '1'),
(355, 'THAZI', 'THA ZI', 'သာစည်', 'THASANA', 'သစန', 2, '9'),
(356, 'MINEPHYAT', 'MINE PHYAT', 'မိုင်းဖြတ်', 'MAPHANA', 'မဖန', 5, '13'),
(357, 'KUNGYANGON', 'KUN GYAN GON', 'ကွမ်းခြံကုန်း', 'KAKHAKA', 'ကခက', 15, '12'),
(358, 'TAUNGGYI', 'TAUNGGYI', 'တောင်ကြီး', 'TAKANA', 'တကန', 5, '13'),
(359, 'KAWTHOUNG', 'KAW THOUNG', 'ကော့သောင်း', 'KATHANA', 'ကသန', 13, '6'),
(360, 'MADAYA', 'MA DA YA', 'မတ္တရာ', 'MATAYA', 'မတရ', 2, '9'),
(361, 'MYITTHAR', 'MYIT THAR', 'မြစ်သား', 'MATHANA', 'မသန', 2, '9'),
(362, 'MAUNGTAW', 'MAUNG TAW', 'မောင်းတော', 'MATANA', 'မတန', 7, '11'),
(363, 'THONSAE', 'THON SAE', 'သုံးဆယ်', 'THASANA', 'သဆန', 14, '7'),
(364, 'KAWKAREIK', 'KAW KA REIK', 'ကော့ကရိတ်', 'KAKAYA', 'ကကရ', 1, '3'),
(365, 'POBBATHIRI', 'POB BA THIRI', 'ပုဗ္ဗသီရိ', 'PABATHA', 'ပဗသ', 8, '9'),
(366, 'MONEKOE', 'MONE KOE', 'မုန်းကိုး', 'MAKATA', 'မကတ', 5, '13'),
(367, 'SALIN', 'SA LIN', 'စလင်း', 'SALANA', 'စလန', 11, '8'),
(368, 'MANNSHAEAUNG', 'MANN SHAE TAUNG', 'မန်းရှေ့တောင်', 'MAYATA', 'မရတ', 2, '9'),
(369, 'MINEKAT', 'MINE KAT', 'မိုင်းခပ်', 'MAKHANA', 'မခန', 5, '13'),
(370, 'KYAUKTALONGYI', 'KYAUK TA LON GYI', 'ကျောက်တလုံးကြီး', 'KATALA', 'ကတလ', 5, '13'),
(371, 'BOKEPYIN', 'BOKE PYIN', 'ဘုတ်ပြင်း', 'BAPANA', 'ဘပန', 13, '6'),
(372, 'OKPHAT', 'OK PHAT', 'အုတ်ဖျက်', 'AHPHANA', 'အဖန', 14, '7'),
(373, 'THANDAUNGGYI', 'THAN DAUNG GYI', 'သံတောင်ကြီး', 'THATAKA', 'သတက', 1, '3'),
(374, 'KYANGIN', 'KYANGIN', 'ကြံခင်း', 'KAKHANA', 'ကခန', 10, '14'),
(375, 'ZALUN', 'ZA LUN', 'ဇလွန်', 'ZALANA', 'ဇလန', 10, '14'),
(376, 'INSEIN', 'INSEIN', 'အင်းစိန်', 'AHSANA', 'အစန', 15, '12'),
(377, 'PONNAGYUN-1', 'PON NA GYUN', 'ပုဏ္ဏားကျွန်း', 'PANATA', 'ပဏတ', 7, '11'),
(378, 'MAGWAY', 'MA GWAY', 'မကွေး', 'MAKANA', 'မကန', 11, '8'),
(379, 'MOPINE LOOT', 'MOPINE LOOT', 'မိုပိုင်းလွတ်', 'MAPALA', 'မပလ', 6, '5'),
(380, 'PHA KANT', 'PHA KANT', 'ဖားကန့်', 'KAMANA', 'ဖကန', 4, '1'),
(381, 'KYAUKKYI', 'KYAUK KYI', 'ကျောက်ကြီး', 'KAKANA', 'ကကန', 14, '7'),
(382, 'DAWEI', 'DA WEI', 'ထားဝယ်', 'HTAWANA', 'ထဝန', 13, '6'),
(383, 'SEIKPHYU', 'SEIK PHYU', 'ဆိပ်ဖြူ', 'SAPHANA', 'ဆဖန', 11, '8'),
(384, 'KYAIKLATT', 'KYAIK LATT', 'ကျိုက်လတ်', 'KALANA', 'ကလန', 10, '14'),
(385, 'MINESAT', 'MINE SAT', 'မိုင်းဆတ်', 'MASANA', 'မဆန', 5, '13'),
(386, 'MANPHANT', 'MAN PHANT', 'မန်ဖန့်', 'MAPHATA', 'မဖတ', 5, '13'),
(387, 'PHEKON', 'PHE KON', 'ဖယ်ခုံ', 'PHAKHANA', 'ဖခန', 5, '13'),
(388, 'KAMMA', 'KAM MA', 'ကံမ', 'KAMANA', 'ကမန', 11, '8'),
(389, 'PANNANDIN', 'PAN NAN DIN', 'ပန်နန်းဒင်', 'PANADA', 'ပနဒ', 4, '1'),
(390, 'PAKOKKU', 'PA KOK KU', 'ပခုက္ကူ', 'PAKHAKA', 'ပခက', 11, '8'),
(391, 'NANYUN', 'NAN YUN', 'နန်းယွန်း', 'NAYANA', 'နယန', 6, '5'),
(392, 'PANTANAW', 'PAN TA NAW', 'ပန်းတနော်', 'PATANA', 'ပတန', 10, '14'),
(393, 'LEWE', 'LEWE', 'လယ်ဝေး', 'LAWANA', 'လဝန', 8, '9'),
(394, 'SHWEDAUNG', 'SHWE DAUNG', 'ရွှေတောင်', 'YATANA', 'ရတန', 14, '7'),
(395, 'HSIHSENG', 'HSI HSENG', 'ဆီဆိုင်', 'SASANA', 'ဆဆန', 5, '13'),
(396, 'HLINETHAYA', 'HLINE THA YA', 'လှိုင်သာယာ', 'LATHAYA', 'လသယ', 15, '12'),
(397, 'PAUNGTALAE', 'PAUNG TA LAE', 'ပေါင်းတလည်', 'PATALA', 'ပတလ', 14, '7'),
(398, 'KUTKAI', 'KUT KAI', 'ကွက်ခိုင်', 'KAKHANA', 'ကခန', 5, '13'),
(399, 'BAGAN', 'BAGAN', 'ပုဂံ', '-', '-', 2, '9'),
(400, 'SHWEGYIN', 'SHWEG YIN', 'ရွှေကျင်', 'YAKANA', 'ရကန', 14, '7'),
(401, 'MINETONE', 'MINE TONE', 'မိုင်းတုံ', 'MATANA', 'မတန', 5, '13'),
(402, 'PWINTPHYU', 'PWINT PHYU', 'ပွင့်ဖြူ', 'PAPHANA', 'ပဖန', 11, '8'),
(403, 'KAMAYUT', 'KA MA YUT', 'ကမာရွတ်', 'KAMAYA', 'ကမရ', 15, '12'),
(404, 'PARSAUNG', 'PAR SAUNG', 'ဖားဆောင်း', 'PHASANA', 'ဖဆန', 9, '2'),
(405, 'MEIKTILA', 'MEI KTI LA', 'မိတ္ထီလာ', 'MAHTALA', 'မထလ', 2, '9'),
(406, 'LEWE', 'LEWE', 'လယ်ဝေး', 'LAWANA', 'လဝန', 2, '9'),
(407, 'PALAW', 'PALAW', 'ပုလော', 'PALANA', 'ပလန', 13, '6'),
(408, 'THANTAUNG', 'THANTAUNG', 'သံတောင်', 'THATANA', 'သတန', 1, '3'),
(409, 'HSOTLAW', 'SAW LAW', 'ဆော့လော်', 'SALANA', 'ဆလန', 4, '1'),
(410, 'PANWAR', 'PANWAR', 'ပန်ဝါ', 'PAWANA', 'ပ၀န', 4, '1'),
(411, 'THANDWE', 'THAN DWE', 'သံတွဲ', 'THATANA', 'သတန', 7, '11'),
(412, 'AMARAPURA', 'A MA RA PU RA', 'အမရပူရ', 'AHMAYA', 'အမရ', 2, '9'),
(413, 'AYARTAW', 'A YAR TAW', 'အရာတော်', 'AHYATA', 'အရတ', 6, '5'),
(414, 'NATOGYI', 'NA TO GYI', 'နွားထိုးကြီး', 'NAHTAKA', 'နထက', 2, '9'),
(415, 'KYUNSU', 'KYUN SU', 'ကျွန်းစု', 'KASANA', 'ကစန', 13, '6'),
(416, 'NGANZUN', 'NGAN ZUN', 'ငါန်းဇွန်', 'NGAZANA', 'ငဇန', 6, '5'),
(417, 'HOPIN', 'HO PIN', 'ဟိုပင်', 'HAPANA', 'ဟပန', 4, '1'),
(418, 'PHRUSO', 'PHRU SO', 'ဖရူးဆိုး', 'PHAYASA', 'ဖရဆ', 9, '2'),
(419, 'DALA', 'DALA', 'ဒလ', 'DALANA', 'ဒလန', 15, '12'),
(420, 'PYIGYITAKHUN', 'PYI GYI TA KHUN', 'ပြည်ကြီးတံခွန်', 'PAKAKHA', 'ပကခ', 2, '9'),
(421, 'CHAUK', 'CHAUK', 'ချောက်', 'KHAMANA', 'ခမန', 11, '8'),
(422, 'AUNGMYETHARZAN', 'AUNG MYE THAR ZAN', 'အောင်မြေသာဇံ', 'AHMAZA', 'အမဇ', 2, '9'),
(423, 'TANAING', 'TA NAING', 'တနိုင်း', 'TANANA', 'တနန', 4, '1'),
(424, 'THANLYIN', 'THAN LYIN', 'သန်လျင်', 'THALANA', 'သလန', 15, '12'),
(425, 'BANMAUK', 'BAN MAUK', 'ဗန်းမောက်', 'BAMANA', 'ဗမန', 6, '5'),
(426, 'PA THEIN', 'PATHEIN (WEST)', 'ပုသိမ် (နောက်)', 'PATHANA', 'ပသန', 10, '14'),
(427, 'NYAUNGU', 'NYAUNG U', 'ညောင်ဦး', 'NYAOUNA', 'ညဥန', 2, '9'),
(428, 'NGATHAINGCHAUNG', 'NGA THAING CHAUNG', 'ငါးသိုင်းချောင်း', 'NGATHAKHA', 'ငသခ', 10, '14'),
(429, 'TAIKKYI', 'TAIKKYI', 'တိုက်ကြီး', 'TAKANA', 'တကန', 15, '12'),
(430, 'YWATHITGYI', 'YWA THIT GYI', 'ရွာသစ်ကြီး', 'YATHAKA', 'ရသက', 6, '5'),
(431, 'MYANAUNG', 'MYAN AUNG', 'မြန်အောင်', 'MAAHNA', 'မအန', 10, '14'),
(432, 'MAYMYO', 'MAY MYO', 'မေမြို့', 'MAMANA', 'မမန', 2, '9'),
(433, 'KALEWA', 'KALEWA', 'ကလေးဝ', 'KALAWA', 'ကလ၀', 6, '5'),
(434, 'SEIKKYI/KHANAUNGTO', 'SEIKKYI/KHANAUNGTO', 'ဆိပ်ကြီးခနောင်တို', 'SAKAKHA', 'ဆကခ', 15, '12'),
(435, 'MINDAT', 'MINDAT', 'မင်းတပ်', 'MATANA', 'မတန', 3, '4'),
(436, 'MYAWADY', 'MYA WA DY', 'မြဝတီ', 'MAWATA', 'မဝတ', 1, '3'),
(437, 'THONGWA', 'THON GWA', 'သုံးခွ', 'THAKHANA', 'သခန', 15, '12'),
(438, 'AYETHARYAR', 'AYE THAR YAR', 'အေးသာယာ', 'AHTHAYA', 'အသယ', 5, '13'),
(439, 'MYINGYAN', 'MYIN GYAN', 'မြင်းခြံ', 'MAKHANA', 'မခန', 2, '9'),
(440, 'POBBATHIRI', 'POB BA THIRI', 'ပုဗ္ဗသီရိ', 'PABATHA', 'ပဗသ', 2, '9'),
(441, 'KYAIDON', 'KYAI DON', 'ကျုံဒိုး', 'KADANA', 'ကဒန', 1, '3'),
(442, 'ZABUTHIRI', 'ZA BU THIRI', 'ဇမ္ဗူသီရိ', 'ZABATHA', 'ဇဗသ', 8, '9'),
(443, 'LOIKAW', 'LOI KAW', 'လွိုင်ကော်', 'LAKANA', 'လကန', 9, '2'),
(444, 'MEISI', 'MEISI', 'မယ်စီ', 'MASANA', 'မစန', 9, '2'),
(445, 'HOPAN', 'HO PAN', 'ဟိုပန်', 'HAPATA', 'ဟပတ', 5, '13'),
(446, 'TACHILEIK', 'TA CHI LEIK', 'တာချီလိတ်', 'TAKHALA', 'တခလ', 5, '13'),
(447, 'LATHA', 'LATHA', 'လသာ', 'LATHANA', 'လသန', 15, '12'),
(448, 'KARLI', 'KARLI', 'ကာလီ', 'KALADA', 'ကလဒ', 5, '13'),
(449, 'HLAINGBWE', 'HLAING BWE', 'လှိုင်းဘွဲ့', 'LABANA', 'လဘန', 1, '3'),
(450, 'KYAUKPADAUNG', 'KYAUK PA DAUNG', 'ကျောက်ပန်းတောင်း', 'KAPATA', 'ကပတ', 2, '9'),
(451, 'LEIKTHO', 'LEIKTHO', 'လိပ်သို', 'LATHANA', 'လသန', 1, '3'),
(452, 'MYOTHIT-SAGAING', 'MYO THIT-SAGAING', 'မြို့သစ်', 'MATHANA', 'မသန', 6, '5'),
(453, 'MOGAUNG', 'MO GAUNG', 'မိုးကောင်း', 'MAKATA', 'မကတ', 4, '1'),
(454, 'MYAING', 'MYAING', 'မြိုင်', 'MAMANA', 'မမန', 11, '8'),
(455, 'HLEGU', 'HLEGU', 'လှည်းကူး', 'LAKANA', 'လကန', 15, '12'),
(456, 'HKAMTI', 'HKAMTI', 'ခန္တီး', 'KHATANA', 'ခတန', 6, '5'),
(457, 'MACHANBAW', 'MA CHAN BAW', 'မချမ်းဘော', 'MAKHABA', 'မခဘ', 4, '1'),
(458, 'KAWHMU', 'KAW HMU', 'ကော့မှူး', 'KAMANA', 'ကမန', 15, '12'),
(459, 'KYAUKSE', 'KYAUK SE', 'ကျောက်ဆည်', 'KASANA', 'ကဆန', 2, '9'),
(460, 'LETPADAN', 'LET PA DAN', 'လက်ပံတန်း', 'LAPATA', 'လပတ', 14, '7'),
(461, 'SUMPRABUM', 'SUM PRA BUM', 'ဆွမ်ပရာဘွမ်', 'SAPABA', 'ဆပဘ', 4, '1'),
(462, 'MAKMAN', 'MAK MAN', 'မက်မန်း', 'MAMANA', 'မမန', 5, '13'),
(463, 'NGAPUTAW', 'NGA PU TAW', 'ငပုတော', 'NGAPATA', 'ငပတ', 10, '14'),
(464, 'SINKU', 'SIN KU', 'စဉ့်ကူး', 'SAKANA', 'စကန', 2, '9'),
(465, 'KAYTUMATI', 'KAY TU MA TI', 'ကေတုမတီ', 'KATATA', 'ကတတ', 14, '7'),
(466, 'TADAU', 'TADAU', 'တံတားဦး', 'TATAOU', 'တတဥ', 2, '9'),
(467, 'EINME', 'EIN ME', 'အိမ်မဲ', 'AHMANA', 'အမန', 10, '14'),
(468, 'MINEMAW', 'MINE MAW', 'မိုင်းမော', 'MAMAHTA', 'မမထ', 5, '13'),
(469, 'KAWA', 'KA WA', 'ကဝ', 'KAWANA', 'ကဝန', 14, '7'),
(470, 'NORTHDAGON', 'NORTH DAGON', 'ဒဂုံမြို့သစ် (မြောက်)', 'DAGAMA', 'ဒဂမ', 15, '12'),
(471, 'LAMINE', 'LA MINE', 'လမိုင်း', 'LAMANA', 'လမန', 12, '10');

-- --------------------------------------------------------

--
-- Table structure for table `nrc_types`
--

CREATE TABLE `nrc_types` (
  `id` smallint(6) NOT NULL,
  `name_en` varchar(255) NOT NULL,
  `name_mm` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `nrc_types`
--

INSERT INTO `nrc_types` (`id`, `name_en`, `name_mm`) VALUES
(1, 'Y', 'ယာယီ'),
(2, 'S', 'စ'),
(3, 'T', 'သာသနာ'),
(4, 'N', 'နိုင်'),
(5, 'E', 'ဧည့်'),
(6, 'P', 'ပြု');

-- --------------------------------------------------------

--
-- Table structure for table `permissions`
--

CREATE TABLE `permissions` (
  `id` bigint(20) NOT NULL,
  `code` varchar(64) NOT NULL,
  `description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `permissions`
--

INSERT INTO `permissions` (`id`, `code`, `description`) VALUES
(1, 'USER_READ', 'Read user info'),
(2, 'USER_WRITE', 'Modify user info'),
(3, 'ROLE_MANAGE', 'Manage roles'),
(4, 'PERMISSION_MANAGE', 'Manage permissions'),
(5, 'FINANCE_VIEW', 'View finance records'),
(6, 'FINANCE_EDIT', 'Edit finance records'),
(7, 'HOD_APPROVE', 'Approve HOD requests'),
(8, 'STUDENT_AFFAIRS_MANAGE', 'Manage student affairs');

-- --------------------------------------------------------

--
-- Table structure for table `pre_enrollment`
--

CREATE TABLE `pre_enrollment` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `full_name_mm` varchar(100) NOT NULL,
  `gender` enum('FEMALE','MALE') NOT NULL,
  `informed` bit(1) NOT NULL,
  `nrc` varchar(50) NOT NULL,
  `pass_certificate` varchar(255) DEFAULT NULL,
  `phone_number` varchar(20) NOT NULL,
  `status` enum('ACCEPTED','PENDING','REJECTED') NOT NULL,
  `uni_entrance_academic_year` varchar(20) NOT NULL,
  `uni_entrance_rollno` varchar(50) NOT NULL,
  `viber_phone_number` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `pre_enrollment`
--

INSERT INTO `pre_enrollment` (`id`, `created_at`, `email`, `full_name_mm`, `gender`, `informed`, `nrc`, `pass_certificate`, `phone_number`, `status`, `uni_entrance_academic_year`, `uni_entrance_rollno`, `viber_phone_number`) VALUES
(1, '2025-08-23 16:01:19.745089', '', 'd', 'MALE', b'0', '', 'uploads/1755941479743_screenshot-1755329945.png', 'd', 'PENDING', 'd', 'd', 'd'),
(2, '2025-08-23 16:15:18.028435', '', '်', 'MALE', b'0', '8/မတန(သာသနာ)၁၂၃၄၅၆', 'uploads/1755942318028_screenshot-1755339060.png', 'ိ', 'PENDING', 'ိ', 'ိ', 'ိ'),
(3, '2025-08-23 16:22:57.241717', '', 'sadf', 'MALE', b'0', '၁၂/ခရန(နိုင်)၁၂၃၄၅၆', 'uploads/1755942777241_screenshot-1755329945.png', '09', 'PENDING', 'sadf', 'sadf', '09'),
(4, '2025-08-23 16:32:33.343274', '', 'sadf', 'FEMALE', b'0', '၁၂/ဗဟန(ဧည့်)၁၂၃၄၅၆', 'uploads/1755943353338_screenshot-1755325610.png', '0909', 'PENDING', 'asdf', 'sdf', '0909'),
(5, '2025-08-23 16:33:31.551841', '', 'sdaf', 'MALE', b'0', '၁၂/ဗဟန(နိုင်)၂၉၈၇၇၈', 'uploads/1755943411551_screenshot-1755339060.png', '၂၃၉', 'PENDING', 'sadf', 'sad', '၁၂၃');

-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL,
  `description` text DEFAULT NULL,
  `name` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` (`id`, `description`, `name`) VALUES
(1, 'Admin role', 'ADMIN'),
(2, 'Super admin role', 'SUPERADMIN'),
(3, 'Head of Department', 'HOD'),
(4, 'Student Affair role', 'STUDENT_AFFAIR'),
(5, 'Finance role', 'FINANCE'),
(6, 'Student role', 'STUDENT'),
(7, 'Warden Role', 'WARDERN');

-- --------------------------------------------------------

--
-- Table structure for table `role_permissions`
--

CREATE TABLE `role_permissions` (
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `role_permissions`
--

INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(1, 2),
(2, 2),
(1, 3),
(2, 3),
(1, 4),
(2, 4),
(2, 5),
(5, 5),
(2, 6),
(5, 6),
(2, 7),
(3, 7),
(2, 8),
(4, 8);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `full_name` varchar(128) DEFAULT NULL,
  `last_login_at` datetime(6) DEFAULT NULL,
  `password_hash` text NOT NULL,
  `status` enum('ACTIVE','DISABLED','LOCKED') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_type` enum('ADMIN','STUDENT') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `created_at`, `email`, `full_name`, `last_login_at`, `password_hash`, `status`, `updated_at`, `user_type`) VALUES
(1, '2025-08-19 21:23:53.646317', 'alice.admin@example.com', 'Alice Admin', NULL, '$2a$10$o1vLEThc.X1rxvlcJR7X0.W3OmTxKS48pKv2QrWv2y1Me5Ti8flW6', 'ACTIVE', '2025-08-19 21:23:53.646337', 'ADMIN'),
(2, '2025-08-19 21:23:53.748558', 'bob.superadmin@example.com', 'Bob SuperAdmin', NULL, '$2a$10$LHf51YdsgkiWnRbnG3TKieKu/eVKcxzPdPmX6RebBMufdvh6olKEy', 'ACTIVE', '2025-08-19 21:23:53.748572', 'ADMIN'),
(3, '2025-08-19 21:23:53.815921', 'charlie.hod@example.com', 'Charlie HOD', NULL, '$2a$10$isoiHNEZIrHdmhG22U/CKOIbznWGGH7C0syCCS9Rz6pSit6meGmgi', 'ACTIVE', '2025-08-19 21:23:53.815945', 'ADMIN'),
(4, '2025-08-19 21:23:53.882269', 'diana.sa@example.com', 'Diana StudentAffair', NULL, '$2a$10$FEpYrjNIy34mAT2z5F.veu3EVE1SWy.rmJw5gRs3vJicI9U4CZB02', 'ACTIVE', '2025-08-19 21:23:53.882296', 'ADMIN'),
(5, '2025-08-19 21:23:53.949079', 'eve.finance@example.com', 'Eve Finance', NULL, '$2a$10$v7PCHTyOtnwvu4IYZhnCUOU3a.EeHS20ynHMyebq1jXrld1.YboJS', 'ACTIVE', '2025-08-19 21:23:53.949100', 'ADMIN'),
(6, '2025-08-19 21:23:54.014266', 'frank.multirole@example.com', 'Frank MultiRole', NULL, '$2a$10$D7QuY.p.AuZhfMXruEry3.ONVmaQRlBWugGuTJOaJ7JxwZgsBShkW', 'ACTIVE', '2025-08-19 21:23:54.014278', 'ADMIN'),
(7, '2025-08-19 21:33:25.113291', 'grace.student@example.com', 'Grace Student', NULL, '$2a$10$y9t0SN6MX8Vl1iZ5T7kYQ.97oSrW3A5EQw6Qs5z6xjRwh2wHpje5C', 'ACTIVE', '2025-08-19 21:33:25.113308', 'STUDENT'),
(8, '2025-08-19 21:33:25.186651', 'henry.student@example.com', 'Henry Student', NULL, '$2a$10$TYqfzrs3gUIopuKVqHrvd.8H9hzxwpOoPYtMfIBmub.FTWJO4JgeK', 'ACTIVE', '2025-08-19 21:33:25.186664', 'STUDENT');

-- --------------------------------------------------------

--
-- Table structure for table `user_roles`
--

CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user_roles`
--

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(1, 1),
(6, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 5),
(7, 6),
(8, 6);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `applicant_academic`
--
ALTER TABLE `applicant_academic`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKsjppeeqsupys2j94em8no4u0i` (`application_id`),
  ADD KEY `FKrhr4fb6w6165t7i0iypeqn2u5` (`major_year_id`);

--
-- Indexes for table `applicant_contact`
--
ALTER TABLE `applicant_contact`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKllv8drbovy3x2he2i57hbhcie` (`application_id`);

--
-- Indexes for table `applicant_nrc`
--
ALTER TABLE `applicant_nrc`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK9h2kahrjfascgs1ok32jnhhwd` (`application_id`);

--
-- Indexes for table `applicant_parents`
--
ALTER TABLE `applicant_parents`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKruj5i7ug53mvp3f3qwqmt81r5` (`application_id`);

--
-- Indexes for table `applicant_parents_contact`
--
ALTER TABLE `applicant_parents_contact`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKbf70gy4220a1ty3sg7e6d1aeg` (`applicant_parent_id`),
  ADD KEY `FKhspnm1fvoimw97qorsooxrafy` (`usual_address_region`),
  ADD KEY `FKrv6bj19eh4agdpqpqrkq5e82o` (`usual_address_township`);

--
-- Indexes for table `applicant_parents_nrc`
--
ALTER TABLE `applicant_parents_nrc`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK7he4oill3wp1rybhwcdbd2ybs` (`applicant_parent_id`);

--
-- Indexes for table `applicant_personal`
--
ALTER TABLE `applicant_personal`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKsdwtbhhl64b3ixep3fh4ctq36` (`application_id`);

--
-- Indexes for table `application_approvals`
--
ALTER TABLE `application_approvals`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK6uycjl83hfd3hlmgjke0lssjh` (`application_id`);

--
-- Indexes for table `enrollment_applications`
--
ALTER TABLE `enrollment_applications`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `enrollment_dorm`
--
ALTER TABLE `enrollment_dorm`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK6of9dkr0tsg4a702rw31t7muv` (`application_id`);

--
-- Indexes for table `enrollment_files`
--
ALTER TABLE `enrollment_files`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5vlh582j6nuexoqdfqy8b64mr` (`application_id`),
  ADD KEY `FKcr4n3vioa8mvpioxm8wubveq5` (`file_type_id`);

--
-- Indexes for table `enrollment_majors`
--
ALTER TABLE `enrollment_majors`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKbfitmmracstl3qajbwwpl9vu5` (`application_id`,`major_id`),
  ADD KEY `FKtj6xyfvu9v9ldcwk2nrsmpmbx` (`major_id`);

--
-- Indexes for table `ethnicity`
--
ALTER TABLE `ethnicity`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `file_types`
--
ALTER TABLE `file_types`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKorlvtgw8yhalek5yim4prvgyo` (`type_name`);

--
-- Indexes for table `major`
--
ALTER TABLE `major`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKpme78hquxj4fio2f880b2gaa9` (`code`),
  ADD UNIQUE KEY `UKoi0ctjbjvktdcfxws9w2exiwb` (`name`);

--
-- Indexes for table `major_year`
--
ALTER TABLE `major_year`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKa5ev462idd1dp7n24emwk7lk3` (`major_id`,`year_label`);

--
-- Indexes for table `nrc_states`
--
ALTER TABLE `nrc_states`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `nrc_townships`
--
ALTER TABLE `nrc_townships`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK6o1po38vgqvc7vmrgnd7xke9o` (`state_id`);

--
-- Indexes for table `nrc_types`
--
ALTER TABLE `nrc_types`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `permissions`
--
ALTER TABLE `permissions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK7lcb6glmvwlro3p2w2cewxtvd` (`code`);

--
-- Indexes for table `pre_enrollment`
--
ALTER TABLE `pre_enrollment`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK9yaygddca3hqsv9lrpqhxe6ny` (`nrc`);

--
-- Indexes for table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`);

--
-- Indexes for table `role_permissions`
--
ALTER TABLE `role_permissions`
  ADD PRIMARY KEY (`role_id`,`permission_id`),
  ADD KEY `FKegdk29eiy7mdtefy5c7eirr6e` (`permission_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`);

--
-- Indexes for table `user_roles`
--
ALTER TABLE `user_roles`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `applicant_academic`
--
ALTER TABLE `applicant_academic`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `applicant_contact`
--
ALTER TABLE `applicant_contact`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `applicant_nrc`
--
ALTER TABLE `applicant_nrc`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `applicant_parents`
--
ALTER TABLE `applicant_parents`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `applicant_parents_contact`
--
ALTER TABLE `applicant_parents_contact`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `applicant_parents_nrc`
--
ALTER TABLE `applicant_parents_nrc`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `applicant_personal`
--
ALTER TABLE `applicant_personal`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `application_approvals`
--
ALTER TABLE `application_approvals`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `enrollment_applications`
--
ALTER TABLE `enrollment_applications`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `enrollment_dorm`
--
ALTER TABLE `enrollment_dorm`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `enrollment_files`
--
ALTER TABLE `enrollment_files`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `enrollment_majors`
--
ALTER TABLE `enrollment_majors`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ethnicity`
--
ALTER TABLE `ethnicity`
  MODIFY `id` smallint(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=141;

--
-- AUTO_INCREMENT for table `file_types`
--
ALTER TABLE `file_types`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `major`
--
ALTER TABLE `major`
  MODIFY `id` smallint(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `major_year`
--
ALTER TABLE `major_year`
  MODIFY `id` smallint(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;

--
-- AUTO_INCREMENT for table `permissions`
--
ALTER TABLE `permissions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `pre_enrollment`
--
ALTER TABLE `pre_enrollment`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `applicant_academic`
--
ALTER TABLE `applicant_academic`
  ADD CONSTRAINT `FKrhr4fb6w6165t7i0iypeqn2u5` FOREIGN KEY (`major_year_id`) REFERENCES `major_year` (`id`),
  ADD CONSTRAINT `FKsjppeeqsupys2j94em8no4u0i` FOREIGN KEY (`application_id`) REFERENCES `enrollment_applications` (`id`);

--
-- Constraints for table `applicant_contact`
--
ALTER TABLE `applicant_contact`
  ADD CONSTRAINT `FK93tkh3l13gwtlu7yk90h92w00` FOREIGN KEY (`application_id`) REFERENCES `enrollment_applications` (`id`);

--
-- Constraints for table `applicant_nrc`
--
ALTER TABLE `applicant_nrc`
  ADD CONSTRAINT `FKjnglgpygjfgtl9wuvrqr4rdue` FOREIGN KEY (`application_id`) REFERENCES `enrollment_applications` (`id`);

--
-- Constraints for table `applicant_parents`
--
ALTER TABLE `applicant_parents`
  ADD CONSTRAINT `FKruj5i7ug53mvp3f3qwqmt81r5` FOREIGN KEY (`application_id`) REFERENCES `enrollment_applications` (`id`);

--
-- Constraints for table `applicant_parents_contact`
--
ALTER TABLE `applicant_parents_contact`
  ADD CONSTRAINT `FKbf70gy4220a1ty3sg7e6d1aeg` FOREIGN KEY (`applicant_parent_id`) REFERENCES `applicant_parents` (`id`),
  ADD CONSTRAINT `FKhspnm1fvoimw97qorsooxrafy` FOREIGN KEY (`usual_address_region`) REFERENCES `nrc_states` (`id`),
  ADD CONSTRAINT `FKrv6bj19eh4agdpqpqrkq5e82o` FOREIGN KEY (`usual_address_township`) REFERENCES `nrc_townships` (`id`);

--
-- Constraints for table `applicant_parents_nrc`
--
ALTER TABLE `applicant_parents_nrc`
  ADD CONSTRAINT `FK7he4oill3wp1rybhwcdbd2ybs` FOREIGN KEY (`applicant_parent_id`) REFERENCES `applicant_parents` (`id`);

--
-- Constraints for table `applicant_personal`
--
ALTER TABLE `applicant_personal`
  ADD CONSTRAINT `FK9c5hhnm4kn251v06pcnlbryhq` FOREIGN KEY (`application_id`) REFERENCES `enrollment_applications` (`id`);

--
-- Constraints for table `application_approvals`
--
ALTER TABLE `application_approvals`
  ADD CONSTRAINT `FK6uycjl83hfd3hlmgjke0lssjh` FOREIGN KEY (`application_id`) REFERENCES `enrollment_applications` (`id`);

--
-- Constraints for table `enrollment_dorm`
--
ALTER TABLE `enrollment_dorm`
  ADD CONSTRAINT `FK6of9dkr0tsg4a702rw31t7muv` FOREIGN KEY (`application_id`) REFERENCES `enrollment_applications` (`id`);

--
-- Constraints for table `enrollment_files`
--
ALTER TABLE `enrollment_files`
  ADD CONSTRAINT `FK5vlh582j6nuexoqdfqy8b64mr` FOREIGN KEY (`application_id`) REFERENCES `enrollment_applications` (`id`),
  ADD CONSTRAINT `FKcr4n3vioa8mvpioxm8wubveq5` FOREIGN KEY (`file_type_id`) REFERENCES `file_types` (`id`);

--
-- Constraints for table `enrollment_majors`
--
ALTER TABLE `enrollment_majors`
  ADD CONSTRAINT `FKf2c3ia4l85gu93w8tm0lepf3l` FOREIGN KEY (`application_id`) REFERENCES `enrollment_applications` (`id`),
  ADD CONSTRAINT `FKtj6xyfvu9v9ldcwk2nrsmpmbx` FOREIGN KEY (`major_id`) REFERENCES `major` (`id`);

--
-- Constraints for table `major_year`
--
ALTER TABLE `major_year`
  ADD CONSTRAINT `FKpyfelct7q1vgwi1dkky8nhdg6` FOREIGN KEY (`major_id`) REFERENCES `major` (`id`);

--
-- Constraints for table `nrc_townships`
--
ALTER TABLE `nrc_townships`
  ADD CONSTRAINT `FK6o1po38vgqvc7vmrgnd7xke9o` FOREIGN KEY (`state_id`) REFERENCES `nrc_states` (`id`);

--
-- Constraints for table `role_permissions`
--
ALTER TABLE `role_permissions`
  ADD CONSTRAINT `FKegdk29eiy7mdtefy5c7eirr6e` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`),
  ADD CONSTRAINT `FKn5fotdgk8d1xvo8nav9uv3muc` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);

--
-- Constraints for table `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  ADD CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
