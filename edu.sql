-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- 主机： localhost
-- 生成日期： 2021-06-16 08:21:22
-- 服务器版本： 8.0.23
-- PHP 版本： 7.4.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `test_jluslda_cn`
--

-- --------------------------------------------------------

--
-- 表的结构 `category`
--

CREATE TABLE `category` (
  `id` int NOT NULL,
  `object` enum('community','article','video','document') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

--
-- 转存表中的数据 `category`
--

INSERT INTO `category` (`id`, `object`, `name`) VALUES
(1, 'community', '学习讨论'),
(2, 'community', '测试板块'),
(3, 'community', '新鲜事'),
(4, 'article', '测试分类1'),
(5, 'article', '测试分类2'),
(6, 'video', '视频分类1'),
(7, 'video', '视频分类2'),
(8, 'document', '文档分类1'),
(9, 'document', '文档分类2'),
(10, 'video', '直播');

-- --------------------------------------------------------

--
-- 表的结构 `comment`
--

CREATE TABLE `comment` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `father_id` int NOT NULL,
  `object` enum('community','article','video','document') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `object_id` int NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `status` enum('normal','hide','delete','') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `extra` json NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

--
-- 转存表中的数据 `comment`
--

INSERT INTO `comment` (`id`, `user_id`, `father_id`, `object`, `object_id`, `content`, `create_time`, `status`, `extra`) VALUES
(1, 1, -1, 'community', 1, '回复测试', '2021-06-13 19:07:46', 'normal', '{}'),
(2, 1, -1, 'community', 1, '😁', '2021-06-13 19:07:53', 'normal', '{}'),
(3, 1, -1, 'article', 16, '评论测试', '2021-06-14 03:05:28', 'normal', '{}'),
(4, 1, -1, 'video', 18, '评论测试', '2021-06-14 03:55:30', 'normal', '{}'),
(5, 3, -1, 'video', 18, 'test', '2021-06-14 13:26:20', 'normal', '{}');

-- --------------------------------------------------------

--
-- 表的结构 `content`
--

CREATE TABLE `content` (
  `id` int NOT NULL,
  `object` enum('community','article','video','document') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` int NOT NULL,
  `category` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `cover` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `status` enum('normal','audit','hide','delete') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `extra` json NOT NULL
) ;

--
-- 转存表中的数据 `content`
--

INSERT INTO `content` (`id`, `object`, `user_id`, `category`, `title`, `description`, `cover`, `content`, `create_time`, `update_time`, `status`, `extra`) VALUES
(1, 'community', 1, '测试板块', '发帖测试', NULL, NULL, '<p>11111111111111111111111111</p>', '2021-06-13 18:40:03', '2021-06-13 18:40:03', 'normal', '{}'),
(4, 'article', 1, '测试分类1', '11111111', '12345', 'https://media.ownfiles.cn/image/ffff87adbcb67b4474aad14c001b236d.jpg', '<p>111111111111111</p>', '2021-06-14 01:04:27', '2021-06-14 01:04:27', 'normal', '{}'),
(5, 'article', 1, '测试分类1', '2222222222222', '2222222222222222', 'https://media.ownfiles.cn/image/bb0e1ca1ca02316e00fcb9b563dd816b.jpg', '<p>2222222222222222</p>', '2021-06-14 01:04:47', '2021-06-14 01:04:47', 'normal', '{}'),
(6, 'article', 1, '测试分类2', '333333333333333', '333333333333333', 'https://media.ownfiles.cn/image/de33f9bf2e826841d17493551e35aa9f.jpg', '<p>33333333333333333</p>', '2021-06-14 01:05:04', '2021-06-14 01:05:04', 'normal', '{}'),
(7, 'article', 1, '测试分类1', '4444444444444444444444444444444444', '444444444444', 'https://media.ownfiles.cn/image/b2a03f077f3b5df744a74981cb15105e.jpg', '<p><strong>44444444444444444</strong></p>', '2021-06-14 01:05:49', '2021-06-14 01:05:49', 'normal', '{}'),
(8, 'article', 1, '测试分类2', '75634534534', '3ewqedqwe', 'https://media.ownfiles.cn/image/a24fc1e9b932344db17525a9f5925537.jpg', '<p><em>23423434</em></p>', '2021-06-14 01:07:26', '2021-06-14 01:07:26', 'normal', '{}'),
(9, 'article', 1, '测试分类1', 'sdfsdfgsdfsdf', '12334', 'https://media.ownfiles.cn/image/bbb4f17ae892c4e062b9075e9f77164d.jpg', '<p>测试测试</p>', '2021-06-14 01:07:46', '2021-06-14 01:07:46', 'normal', '{}'),
(10, 'article', 1, '测试分类2', '423432434324', '11111', 'https://media.ownfiles.cn/image/65bcd155d48b1b28a56b640f44a7e053.jpg', '<p>1232133321</p>', '2021-06-14 01:08:08', '2021-06-14 01:08:08', 'normal', '{}'),
(11, 'article', 1, '测试分类2', 'dfsfsdfdsf', '1234', 'https://media.ownfiles.cn/image/300f181b081979b4de3fa42f2bce5a0b.jpg', '<p>sdfsdfsafds</p>', '2021-06-14 01:08:27', '2021-06-14 01:08:27', 'normal', '{}'),
(12, 'article', 1, '测试分类1', '11111111111111111111111111111111111111111111111111111111111111111111', '111111111111111111111111111111111111111111111111111111111111111111111111111111111', 'https://media.ownfiles.cn/image/32e1e9295f4f3f4a4d200bcc79549b95.jpg', '<p>111111111111111111111111111111111111111111111111111111111111111111111111111111111111</p>', '2021-06-14 01:09:16', '2021-06-14 01:09:16', 'normal', '{}'),
(13, 'article', 1, '测试分类1', '3333', '1111', 'https://media.ownfiles.cn/image/d945547313c8ae7ad2a76ce6f41efe36.jpg', '<p>12222</p>', '2021-06-14 01:09:35', '2021-06-14 01:09:35', 'normal', '{}'),
(14, 'article', 1, '测试分类2', '342421342', '234234234234', 'https://media.ownfiles.cn/image/858e2403ac865a6df0ef3d9ed2b7c552.jpg', '<p>324324324324</p>', '2021-06-14 01:09:56', '2021-06-14 01:09:56', 'normal', '{}'),
(15, 'community', 1, '学习讨论', '111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111', NULL, NULL, '<p>111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111</p>', '2021-06-14 01:25:15', '2021-06-14 01:25:15', 'normal', '{}'),
(16, 'article', 1, '测试分类2', '111111111111111111222222222222223333333333333334444444444444445555555555555555566666666666666666677777777777777777777777778888888888888888888889999999999999999999999', '测试测试', 'https://media.ownfiles.cn/image/89735cb0f7d1bbdee5b325b4be29f1ed.jpg', '<p>111111111111111111222222222222223333333333333334444444444444445555555555555555566666666666666666677777777777777777777777778888888888888888888889999999999999999999999111111111111111111222222222222223333333333333334444444444444445555555555555555566666666666666666677777777777777777777777778888888888888888888889999999999999999999999111111111111111111222222222222223333333333333334444444444444445555555555555555566666666666666666677777777777777777777777778888888888888888888889999999999999999999999111111111111111111222222222222223333333333333334444444444444445555555555555555566666666666666666677777777777777777777777778888888888888888888889999999999999999999999111111111111111111222222222222223333333333333334444444444444445555555555555555566666666666666666677777777777777777777777778888888888888888888889999999999999999999999111111111111111111222222222222223333333333333334444444444444445555555555555555566666666666666666677777777777777777777777778888888888888888888889999999999999999999999111111111111111111222222222222223333333333333334444444444444445555555555555555566666666666666666677777777777777777777777778888888888888888888889999999999999999999999111111111111111111222222222222223333333333333334444444444444445555555555555555566666666666666666677777777777777777777777778888888888888888888889999999999999999999999111111111111111111222222222222223333333333333334444444444444445555555555555555566666666666666666677777777777777777777777778888888888888888888889999999999999999999999111111111111111111222222222222223333333333333334444444444444445555555555555555566666666666666666677777777777777777777777778888888888888888888889999999999999999999999111111111111111111222222222222223333333333333334444444444444445555555555555555566666666666666666677777777777777777777777778888888888888888888889999999999999999999999</p>', '2021-06-14 01:41:53', '2021-06-14 01:41:53', 'normal', '{}'),
(18, 'video', 1, '视频分类1', '视频测试', '视频测试', 'https://media.ownfiles.cn/image/ed1ff4de9bee814e141bb54797b54fc9.jpg', 'https://media.ownfiles.cn/video/dbbf16fbd7d478e22b72ce61628ab246.mp4', '2021-06-14 03:32:35', '2021-06-14 03:32:35', 'delete', '{}'),
(20, 'community', 3, '学习讨论', 'test2', NULL, NULL, '<p>Java的泛型实现是泛型擦除机制</p>', '2021-06-14 11:27:26', '2021-06-14 11:27:26', 'normal', '{}'),
(21, 'community', 3, '学习讨论', 'test', NULL, NULL, '<p>hello world</p>', '2021-06-14 13:27:03', '2021-06-14 13:27:03', 'normal', '{}'),
(23, 'document', 1, '文档分类1', '文档测试', '文档测试', 'https://media.ownfiles.cn/image/ffdffde72de1936a5bccb1f1fc209090.jpg', 'https://media.ownfiles.cn/document/714c23a6bd6888538d6e4c3173b47309.doc', '2021-06-14 18:14:39', '2021-06-14 18:14:39', 'normal', '{}'),
(24, 'document', 1, '文档分类1', 'PPT测试', 'PPT测试', 'https://media.ownfiles.cn/image/8860905dca5945fec486414fdb7d5e92.jpg', 'https://media.ownfiles.cn/document/d940b3f1bd57ba88c4745cb472a8f0c9.ppt', '2021-06-14 18:15:41', '2021-06-14 18:15:41', 'normal', '{}'),
(25, 'document', 1, '文档分类2', 'PDF测试', 'PDF测试', 'https://media.ownfiles.cn/image/93660e5723c0465d4e45e20dbf22273c.jpg', 'https://media.ownfiles.cn/document/6fb570cf775782a81cfc6e2b776455f2.pdf', '2021-06-14 18:19:05', '2021-06-14 18:19:05', 'normal', '{}'),
(26, 'video', 1, '视频分类1', '视频测试', '视频测试', 'https://media.ownfiles.cn/image/c613296a42429eb8eb7cebc8de70f04c.jpg', 'https://media.ownfiles.cn/video/c0f38b73beeb4e6ebc5b04ab22218632.mp4', '2021-06-16 03:46:21', '2021-06-16 03:46:21', 'normal', '{}'),
(27, 'video', 1, '直播', '直播测试', '直播测试', 'https://media.ownfiles.cn/image/76f76d1ad88874d7bcf443586190f3ef.jpg', 'https://media.ownfiles.cn/live/081d4cd221badb8d.m3u8', '2021-06-16 03:55:14', '2021-06-16 03:55:14', 'delete', '{\"rtmpCode\": \"081d4cd221badb8d\"}'),
(28, 'video', 1, '直播', '1234', '1234', 'https://media.ownfiles.cn/image/40c1441bc5fc90f205782204beb664d9.jpg', 'https://media.ownfiles.cn/live/fbe8ac3bb3216893.m3u8', '2021-06-16 03:58:21', '2021-06-16 03:58:21', 'delete', '{\"rtmpCode\": \"fbe8ac3bb3216893\"}'),
(29, 'video', 1, '直播', '1234', '1234', 'https://media.ownfiles.cn/image/40c1441bc5fc90f205782204beb664d9.jpg', 'https://media.ownfiles.cn/live/2e6080b56eccf497.m3u8', '2021-06-16 04:05:06', '2021-06-16 04:05:06', 'delete', '{\"rtmpCode\": \"2e6080b56eccf497\"}'),
(30, 'video', 1, '直播', '1234', '1234', 'https://media.ownfiles.cn/image/40c1441bc5fc90f205782204beb664d9.jpg', 'https://media.ownfiles.cn/live/127016eec24c95ad.m3u8', '2021-06-16 04:09:27', '2021-06-16 04:09:27', 'delete', '{\"rtmpCode\": \"127016eec24c95ad\"}'),
(31, 'video', 1, '直播', '直播测试', '直播测试', 'https://media.ownfiles.cn/image/397ef6796cd6a481447f7a8eb358c765.jpg', 'https://media.ownfiles.cn/live/90cd49f5446f3d3c.m3u8', '2021-06-16 04:30:28', '2021-06-16 04:30:28', 'delete', '{\"rtmpCode\": \"90cd49f5446f3d3c\"}'),
(32, 'video', 1, '直播', '直播测试', '直播测试', 'https://media.ownfiles.cn/image/a437639d100c4ee5f5e48bd967ad6b27.jpg', 'https://media.ownfiles.cn/live/76de7377774c13e2.m3u8', '2021-06-16 04:34:08', '2021-06-16 04:34:08', 'delete', '{\"rtmpCode\": \"76de7377774c13e2\"}'),
(33, 'video', 1, '直播', '直播测试', '直播测试', 'https://media.ownfiles.cn/image/d77aafc4555e0c4181797589d176b1b5.jpg', 'https://media.ownfiles.cn/live/525a86568f80bbed.m3u8', '2021-06-16 04:37:08', '2021-06-16 04:37:08', 'delete', '{\"rtmpCode\": \"525a86568f80bbed\"}'),
(34, 'video', 1, '直播', '直播测试', '直播测试', 'https://media.ownfiles.cn/image/1de264bb1c28f41dbc7dab10d307a90b.jpg', 'https://media.ownfiles.cn/live/e9380a044007e5f5.m3u8', '2021-06-16 04:42:12', '2021-06-16 04:42:12', 'delete', '{\"rtmpCode\": \"e9380a044007e5f5\"}'),
(35, 'video', 1, '直播', '1234', '1234', 'https://media.ownfiles.cn/image/8c4678c14f8837fc8727df2d01fb580c.jpg', 'https://media.ownfiles.cn/live/c7faeeab8644e834.m3u8', '2021-06-16 04:43:20', '2021-06-16 04:43:20', 'delete', '{\"rtmpCode\": \"c7faeeab8644e834\"}'),
(36, 'video', 1, '直播', '直播测试', '直播测试', 'https://media.ownfiles.cn/image/fb4b48ee2b7286924e998804d91db8cb.jpg', 'https://media.ownfiles.cn/live/1336f0fc57f93452.m3u8', '2021-06-16 04:45:21', '2021-06-16 04:45:21', 'delete', '{\"rtmpCode\": \"1336f0fc57f93452\"}'),
(37, 'video', 1, '直播', '测试测试', '测试测试', 'https://media.ownfiles.cn/image/f5403375cb7a0ecce18f8641e4c84175.jpg', 'https://media.ownfiles.cn/live/a23422072c17b961.m3u8', '2021-06-16 04:47:59', '2021-06-16 04:47:59', 'delete', '{\"rtmpCode\": \"a23422072c17b961\"}'),
(38, 'video', 1, '直播', '直播测试', '直播测试', 'https://media.ownfiles.cn/image/989b90b6566abb01b72f20560de7876d.jpg', 'https://media.ownfiles.cn/live/d1ded0ec680f6486.m3u8', '2021-06-16 04:53:36', '2021-06-16 04:53:36', 'delete', '{\"rtmpCode\": \"d1ded0ec680f6486\"}'),
(39, 'video', 1, '直播', '直播测试', '直播测试', 'https://media.ownfiles.cn/image/c0cadaa8c8f4f7766c3e2907b1f01366.jpg', 'https://media.ownfiles.cn/live/0ad4d43bdaf908f0.m3u8', '2021-06-16 05:11:00', '2021-06-16 05:11:00', 'delete', '{\"rtmpCode\": \"0ad4d43bdaf908f0\"}'),
(40, 'video', 1, '直播', '1234', '1234', 'https://media.ownfiles.cn/image/a2725de4ab3a97e3b74b3adc5d3882a2.jpg', 'https://media.ownfiles.cn/live/f6454ccdd55384d7.m3u8', '2021-06-16 07:27:48', '2021-06-16 07:27:48', 'delete', '{\"rtmpCode\": \"f6454ccdd55384d7\"}'),
(41, 'video', 1, '直播', '直播测试', '直播测试', 'https://media.ownfiles.cn/image/5f0499799fb689e46a8793b1bac9d475.jpg', 'https://media.ownfiles.cn/live/bfbd8981f6bf971f.m3u8', '2021-06-16 08:00:57', '2021-06-16 08:00:57', 'delete', '{\"rtmpCode\": \"bfbd8981f6bf971f\"}');

-- --------------------------------------------------------

--
-- 表的结构 `favorite`
--

CREATE TABLE `favorite` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `object` enum('community','article','video','document') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `object_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `follow`
--

CREATE TABLE `follow` (
  `id` int NOT NULL,
  `from_user_id` int NOT NULL,
  `to_user_id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `global`
--

CREATE TABLE `global` (
  `id` int NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- --------------------------------------------------------

--
-- 表的结构 `letter`
--

CREATE TABLE `letter` (
  `id` int NOT NULL,
  `user_id` int NOT NULL,
  `from_user_id` int NOT NULL,
  `to_user_id` int NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `datetime` datetime NOT NULL,
  `status` enum('unread','readed','withdraw','delete') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

--
-- 转存表中的数据 `letter`
--

INSERT INTO `letter` (`id`, `user_id`, `from_user_id`, `to_user_id`, `content`, `datetime`, `status`) VALUES
(1, 1, 1, 2, '123', '2021-06-15 17:17:57', 'unread'),
(2, 2, 1, 2, '123', '2021-06-15 17:17:57', 'unread'),
(3, 1, 1, 3, '。。。', '2021-06-16 08:15:59', 'unread'),
(4, 3, 1, 3, '。。。', '2021-06-16 08:15:59', 'unread'),
(5, 3, 3, 1, '！！！', '2021-06-16 08:16:25', 'unread'),
(6, 1, 3, 1, '！！！', '2021-06-16 08:16:25', 'unread'),
(7, 1, 1, 3, 'ssassd', '2021-06-16 08:16:31', 'unread'),
(8, 3, 1, 3, 'ssassd', '2021-06-16 08:16:31', 'unread'),
(9, 3, 3, 1, 'aasaad', '2021-06-16 08:16:40', 'unread'),
(10, 1, 3, 1, 'aasaad', '2021-06-16 08:16:40', 'unread');

-- --------------------------------------------------------

--
-- 表的结构 `tag`
--

CREATE TABLE `tag` (
  `id` int NOT NULL,
  `object` enum('community','article','video','document') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `object_id` int NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

--
-- 转存表中的数据 `tag`
--

INSERT INTO `tag` (`id`, `object`, `object_id`, `name`) VALUES
(11, 'community', 1, '测试'),
(12, 'community', 15, '测试'),
(13, 'community', 19, 'Java'),
(14, 'community', 20, 'Java'),
(15, 'community', 21, 'Java');

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE `user` (
  `id` int NOT NULL,
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `signature` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` enum('normal','unverified','ban','delete') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `extra` json NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`id`, `nickname`, `email`, `password`, `create_time`, `update_time`, `signature`, `status`, `extra`) VALUES
(1, 'admin', '1228318968@qq.com', '96e79218965eb72c92a549dd5a330112', '2021-05-24 00:29:46', '2021-06-16 08:15:20', '测试', 'normal', '{\"isAdmin\": true}'),
(2, '11', '11@qq.com', '96e79218965eb72c92a549dd5a330112', '2021-05-26 00:17:37', '2021-06-16 08:15:20', '', 'normal', '{}'),
(3, 'xhl', '3535552159@qq.com', '25f9e794323b453885f5181f1b624d0b', '2021-06-14 11:21:05', '2021-06-16 08:15:20', '', 'normal', '{}');

--
-- 转储表的索引
--

--
-- 表的索引 `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `object` (`object`) USING BTREE;

--
-- 表的索引 `comment`
--
ALTER TABLE `comment`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `object` (`object`),
  ADD KEY `object_id` (`object_id`);

--
-- 表的索引 `content`
--
ALTER TABLE `content`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `object` (`object`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `category` (`category`);
ALTER TABLE `content` ADD FULLTEXT KEY `fulltext_tdc` (`title`,`description`,`content`);
ALTER TABLE `content` ADD FULLTEXT KEY `fulltext_td` (`title`,`description`);

--
-- 表的索引 `favorite`
--
ALTER TABLE `favorite`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `user_id` (`user_id`);

--
-- 表的索引 `follow`
--
ALTER TABLE `follow`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `from_user_id` (`from_user_id`),
  ADD KEY `to_user_id` (`to_user_id`);

--
-- 表的索引 `global`
--
ALTER TABLE `global`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD UNIQUE KEY `name` (`name`) USING BTREE;

--
-- 表的索引 `letter`
--
ALTER TABLE `letter`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `user_id` (`user_id`);

--
-- 表的索引 `tag`
--
ALTER TABLE `tag`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD UNIQUE KEY `object_id` (`object_id`),
  ADD KEY `object` (`object`),
  ADD KEY `name` (`name`);

--
-- 表的索引 `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD UNIQUE KEY `email` (`email`) USING BTREE;

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `category`
--
ALTER TABLE `category`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- 使用表AUTO_INCREMENT `comment`
--
ALTER TABLE `comment`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- 使用表AUTO_INCREMENT `content`
--
ALTER TABLE `content`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `favorite`
--
ALTER TABLE `favorite`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `follow`
--
ALTER TABLE `follow`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- 使用表AUTO_INCREMENT `global`
--
ALTER TABLE `global`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- 使用表AUTO_INCREMENT `letter`
--
ALTER TABLE `letter`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- 使用表AUTO_INCREMENT `tag`
--
ALTER TABLE `tag`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- 使用表AUTO_INCREMENT `user`
--
ALTER TABLE `user`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
