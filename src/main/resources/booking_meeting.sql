/*
 Navicat Premium Data Transfer

 Source Server         : LOCAL
 Source Server Type    : MySQL
 Source Server Version : 50735
 Source Host           : localhost:3306
 Source Schema         : booking_meeting

 Target Server Type    : MySQL
 Target Server Version : 50735
 File Encoding         : 65001

 Date: 30/12/2021 16:18:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for booking
-- ----------------------------
DROP TABLE IF EXISTS `booking`;
CREATE TABLE `booking`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` int(11) NULL DEFAULT NULL,
  `created_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `end_time` datetime(0) NOT NULL,
  `is_notification` bit(1) NULL DEFAULT NULL,
  `members` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `modified_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `priority` int(11) NULL DEFAULT NULL,
  `room_id` bigint(20) NOT NULL,
  `start_time` datetime(0) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `admin_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKny5yf81kuufq5cnuomgc10tgq`(`admin_id`) USING BTREE,
  CONSTRAINT `FKny5yf81kuufq5cnuomgc10tgq` FOREIGN KEY (`admin_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of booking
-- ----------------------------
INSERT INTO `booking` VALUES (1, 1, '2021-12-19 13:42:12', 'Hop test 11', '2021-12-19 10:00:00', b'1', '16935,16936,16938,16939,16940', '2021-12-23 21:01:55', 0, 37, '2021-12-19 09:00:00', 'Ok Test nhé', 16937);
INSERT INTO `booking` VALUES (2, 0, '2021-12-19 23:49:19', 'QuangCV đặt phòng', '2021-12-19 10:00:00', b'1', '16937,16938,16939,16940', '2021-12-23 20:33:16', 0, 36, '2021-12-19 08:00:00', 'QuangCV đặt phòng', 16937);
INSERT INTO `booking` VALUES (5, 1, '2021-12-20 10:23:17', 'Quang CV đặt phòng họp dự án OneBSS, Cần gấp', '2021-12-20 15:00:00', b'1', '16939,16938,16936,16935', '2021-12-20 10:23:17', 1, 43, '2021-12-20 13:00:00', 'Quang CV đặt phòng họp dự án OneBSS', 16940);
INSERT INTO `booking` VALUES (6, 1, '2021-12-20 10:25:40', 'QuangCV đặt phòng tiếp', '2021-12-20 14:00:00', b'1', '16939,16938,16937', '2021-12-20 10:25:40', 1, 41, '2021-12-20 12:00:00', 'QuangCV đặt phòng tiếp', 16940);
INSERT INTO `booking` VALUES (7, 0, '2021-12-23 21:49:32', 'Cuộc họp video chất lượng. Giờ đây miễn phí cho tất cả mọi người.', '2021-12-23 10:00:00', b'1', '16936,16937,16938', '2021-12-25 01:02:30', 0, 36, '2021-12-23 08:00:00', 'Cuộc họp video chất lượng. Giờ đây miễn phí cho tất cả mọi người.', 16937);
INSERT INTO `booking` VALUES (9, 1, '2021-12-24 21:46:12', 'Chu Quốc Bảo đặt phòng họp đêm tại phòng họp 1', '2021-12-24 23:45:00', b'1', '16934,16935,16936', '2021-12-24 21:46:12', 0, 35, '2021-12-24 21:50:00', 'Chu Quốc Bảo đặt phòng họp đêm tại phòng họp 1', 16937);
INSERT INTO `booking` VALUES (11, 1, '2021-12-24 21:48:43', 'Quốc Bảo đặt test', '2021-12-24 23:45:00', b'1', '16936,16939,16940,16938', '2021-12-24 21:48:43', 0, 36, '2021-12-24 21:50:00', 'Quốc Bảo đặt test', 16937);
INSERT INTO `booking` VALUES (12, 1, '2021-12-24 21:50:06', 'Quốc bảo đặt tiếp', '2021-12-24 23:45:00', b'1', '16936,16938,16939', '2021-12-24 21:50:06', 0, 37, '2021-12-24 21:50:00', 'Quốc bảo đặt tiếp', 16937);
INSERT INTO `booking` VALUES (13, 1, '2021-12-24 21:55:11', 'Đặt lịch họp cho cuộc họp sắp tới', '2021-12-27 23:59:00', b'1', '16938,16939,16936,16934,16935,16940,16941', '2021-12-24 21:55:11', 0, 42, '2021-12-27 22:00:00', 'Đặt lịch họp cho cuộc họp sắp tới', 16937);

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `department_ext_id` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `department_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modified_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_id` bigint(20) NULL DEFAULT NULL,
  `short_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` bit(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK1t68827l97cwyxo9r1u6t4p7d`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (1, '2021-11-17 23:36:08', '0', '/VNPT IT', '2021-11-17 23:36:08', 'VNPT IT', 0, 'VNPT IT', b'1');
INSERT INTO `department` VALUES (2, '2021-11-17 23:38:40', '0,2', '/VNPT IT/Phòng hạ tầng', '2021-11-17 23:38:40', 'Phòng hạ tầng', 1, 'Phòng hạ tầng', b'1');
INSERT INTO `department` VALUES (5, '2021-11-18 00:01:42', '0,5', '/VNPT IT/Phòng Tiếp Thị Triển Khai', '2021-11-18 00:01:42', 'Phòng tiếp thị triển khai', 1, 'Phòng TTTK', b'1');
INSERT INTO `department` VALUES (8, '2021-12-28 01:33:26', '0,5,8', '/VNPT IT/Phòng Tiếp Thị Triển Khai/Phòng phát triển phần mềm update', '2021-12-28 01:33:54', 'Phòng phát triển phần mềm Update2', 5, 'PTPM', b'1');

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `mask` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modified_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (2, 'ACCESS', '2021-11-17 11:23:05', '[Quyền] Truy cập', '2021-11-17 11:23:05');
INSERT INTO `permission` VALUES (3, 'READ', '2021-11-17 11:26:59', '[Quyền] Đọc', '2021-11-17 11:26:59');
INSERT INTO `permission` VALUES (4, 'CREATE', '2021-11-17 11:27:37', '[Quyền] Tạo mới', '2021-11-17 11:27:37');
INSERT INTO `permission` VALUES (5, 'UPDATE', '2021-11-17 11:27:55', '[Quyền] Cập nhật', '2021-11-17 11:27:55');
INSERT INTO `permission` VALUES (6, 'DELETE', '2021-11-17 11:28:07', '[Quyền] Xóa', '2021-11-17 11:28:07');
INSERT INTO `permission` VALUES (7, 'manager_dept', '2021-11-17 11:28:47', 'Quản lý phòng ban', '2021-11-17 11:28:47');
INSERT INTO `permission` VALUES (8, 'manager_user', '2021-11-17 11:28:47', 'Quản lý người dùng', '2021-11-17 11:28:47');
INSERT INTO `permission` VALUES (9, 'manager_room', '2021-11-17 11:28:47', 'Quản lý phòng họp', '2021-11-17 11:28:47');
INSERT INTO `permission` VALUES (10, 'manager_role', '2021-11-17 11:28:47', 'Quản lý quyền', '2021-11-17 11:28:47');
INSERT INTO `permission` VALUES (11, 'manager_booking', '2021-11-17 11:28:47', 'Quản lý đặt phòng', '2021-11-17 11:28:47');

-- ----------------------------
-- Table structure for refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `refresh_token`;
CREATE TABLE `refresh_token`  (
  `token_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `expiry_dt` datetime(0) NOT NULL,
  `modified_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `refresh_count` bigint(20) NULL DEFAULT NULL,
  `token` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `user_device_id` bigint(20) NOT NULL,
  PRIMARY KEY (`token_id`) USING BTREE,
  UNIQUE INDEX `UK_8ogx3ejsbfbf2xsgl4758otrm`(`user_device_id`) USING BTREE,
  UNIQUE INDEX `UK_r4k4edos30bx9neoq81mdvwph`(`token`) USING BTREE,
  CONSTRAINT `FKr92opronarwe7pn1c41621grv` FOREIGN KEY (`user_device_id`) REFERENCES `user_device` (`user_device_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refresh_token
-- ----------------------------
INSERT INTO `refresh_token` VALUES (20, '2021-12-29 23:45:08', '2022-01-28 23:45:09', '2021-12-29 23:45:08', 0, 'fb517f62-227e-446e-b7e5-f55ee5a0e5b2', 20);
INSERT INTO `refresh_token` VALUES (21, '2021-12-30 11:09:31', '2022-01-29 11:09:31', '2021-12-30 11:09:31', 0, 'fb4ec934-c796-4405-bff6-b0a2f61fbe8d', 21);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12013 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (12010, 'Người dùng', 'ROLE_USER');
INSERT INTO `role` VALUES (12011, 'Quản trị', 'ROLE_ADMIN');
INSERT INTO `role` VALUES (12012, 'Điều chỉnh', 'ROLE_MODERATOR');

-- ----------------------------
-- Table structure for role_permissions
-- ----------------------------
DROP TABLE IF EXISTS `role_permissions`;
CREATE TABLE `role_permissions`  (
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  INDEX `FKh0v7u4w7mttcu81o8wegayr8e`(`permission_id`) USING BTREE,
  INDEX `FKlodb7xh4a2xjv39gc3lsop95n`(`role_id`) USING BTREE,
  CONSTRAINT `FKh0v7u4w7mttcu81o8wegayr8e` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKlodb7xh4a2xjv39gc3lsop95n` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_permissions
-- ----------------------------
INSERT INTO `role_permissions` VALUES (12010, 2);
INSERT INTO `role_permissions` VALUES (12011, 2);
INSERT INTO `role_permissions` VALUES (12011, 3);
INSERT INTO `role_permissions` VALUES (12011, 4);
INSERT INTO `role_permissions` VALUES (12011, 5);
INSERT INTO `role_permissions` VALUES (12011, 6);
INSERT INTO `role_permissions` VALUES (12011, 7);
INSERT INTO `role_permissions` VALUES (12011, 8);
INSERT INTO `role_permissions` VALUES (12011, 9);
INSERT INTO `role_permissions` VALUES (12011, 10);
INSERT INTO `role_permissions` VALUES (12011, 11);

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_projector` bit(1) NULL DEFAULT NULL,
  `is_water` bit(1) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `total_person` int(11) NULL DEFAULT NULL,
  `created_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `modified_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` VALUES (35, '35', b'1', b'1', 'Phòng họp 1', 10, '2021-11-26 16:39:10', '2021-11-26 16:39:10');
INSERT INTO `room` VALUES (36, '34', b'1', b'1', 'Phòng họp 2', 10, '2021-11-26 16:39:54', '2021-11-26 16:39:54');
INSERT INTO `room` VALUES (37, '30', b'1', b'1', 'Phòng họp 3', 10, '2021-11-26 16:40:02', '2021-11-26 16:40:02');
INSERT INTO `room` VALUES (38, '15', b'1', b'1', 'Phòng họp 4', 10, '2021-11-26 16:40:08', '2021-11-26 16:40:08');
INSERT INTO `room` VALUES (39, '50', b'1', b'1', 'Phòng họp 5', 10, '2021-11-26 16:40:16', '2021-11-26 16:40:16');
INSERT INTO `room` VALUES (40, '50', b'1', b'1', 'Phòng họp 6', 10, '2021-11-26 16:42:38', '2021-11-26 16:42:38');
INSERT INTO `room` VALUES (41, '50', b'1', b'1', 'Phòng họp 6', 10, '2021-11-26 16:42:39', '2021-11-26 16:42:39');
INSERT INTO `room` VALUES (42, '50', b'1', b'1', 'Phòng họp vip', 10, '2021-11-26 16:43:00', '2021-12-21 20:53:58');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modified_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `photo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKsb8bbouer5wak8vyiiy4pf2bx`(`username`) USING BTREE,
  UNIQUE INDEX `UKob8kqyqqgmefl0aco34akdtpe`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16952 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (16935, '2021-11-13 00:13:54', 'vinhlt@vnpt.vn', '0975280467', '2021-11-13 00:13:54', 'Lương Thế Vinh', '$2a$10$sk.6rbuuoHhdSbtk1rZcIOcq2H4gdQgNaELG.wzzXLQAYxqzCrsya', 'vinhlt', NULL, 0);
INSERT INTO `user` VALUES (16936, '2021-11-13 00:14:24', 'anhnv@vnpt.vn', '0975280467', '2021-11-13 00:14:24', 'Nguyễn Việt Anh', '$2a$10$CRaJmkoGU8TFRdDrKUOVfOPY5V87t0NbWlb2xqZ5SBmrGDosW5paG', 'anhnv', NULL, 0);
INSERT INTO `user` VALUES (16937, '2021-11-15 00:16:53', 'baocq@vnpt.vn', '0975280467', '2021-12-26 18:16:13', 'Chu Quoc Bao', '$2a$10$sG3q/InYJLTjmbXBvbQ7WeaQCXyJRvDATQ3oJ8cjzLrU4gleRhAcm', 'baocq', '/resources/images/baocq.png', 0);
INSERT INTO `user` VALUES (16938, '2021-11-15 00:17:10', 'thinhlt@vnpt.vn', '0975280467', '2021-11-15 00:17:10', 'Le Tien Thinh', '$2a$10$aBaXAeznAqgWZqMMdQcDY.mAC0vSt7B8FLF6tKf.BHO2h.7fOADXS', 'thinhlt', NULL, 0);
INSERT INTO `user` VALUES (16939, '2021-11-25 14:37:05', 'datlq@vnpt.vn', '0854670235', '2021-11-25 14:37:05', 'Lưu Quang Đạt', '$2a$10$nXi38eCbB6Fxc7P6iXfZM.jvvnflojAr4Mu7edgXdfNsPm3mByraK', 'datlq', NULL, 0);
INSERT INTO `user` VALUES (16940, '2021-11-25 15:10:47', 'tunc@vnpt.vn', '0854670235', '2021-11-25 15:10:47', 'Nguyễn CôngTú', '$2a$10$t6F8UvgcNWWTYhlLDDVBFer3TR8lugV.yz6PrqzE5s.3EYGhO6EAi', 'tunc', NULL, 0);
INSERT INTO `user` VALUES (16941, '2021-12-23 00:36:10', 'hienttc@vnpt.vn', '0975280467', '2021-12-26 12:24:19', 'Chu Hien', '$2a$10$aoMa5gp/CKE1PfZCOdv3huGLASan91cI92XlLfcP9ekzs7lO45T/.', 'hiennttc', '/resources/images/hiennttc.jpg', 0);
INSERT INTO `user` VALUES (16942, '2021-12-26 22:48:46', 'phuongnt@vnpt.vn', '0854670235', '2021-12-26 22:48:46', 'Ngô ThịPhượng', '$2a$10$0DkR1yM7oHm.TDJbZJm1gem3/aU06pBKmJRIlMe7T94r7.HacKkZu', 'phuongnt', NULL, 0);
INSERT INTO `user` VALUES (16943, '2021-12-26 23:33:18', 'haipv@vnpt.vn', '0854670235', '2021-12-26 23:33:18', 'Phan Văn Hải', '$2a$10$luVO4gNIEPiZ7t13vlsmZOIef9CddJxuVytiCHO3JBrAp.cT2F8rK', 'haipv', NULL, 0);
INSERT INTO `user` VALUES (16944, '2021-12-26 23:36:07', 'trangdc@vnpt.vn', '0854670235', '2021-12-26 23:36:27', 'Dương Công Tráng', '$2a$10$x2dGnjUMRu23GomwmOw3vuIVv5uRkMNg0wzveI2S62NKjUCU.pXiy', 'trangdc', '/resources/images/default.png', 0);
INSERT INTO `user` VALUES (16949, '2021-12-29 21:09:56', 'ocsen29@gmail.com', '0375866599', '2021-12-30 00:57:57', 'Trần Hoàng Thu Hà', '$2a$10$sS8FKMi/8GASd9aT/Sr6EecsflzZ9e3TbnOB8CuueRNgWDGr7N0Ey', 'Hahtt', '/resources/images/Hahtt.png', 0);
INSERT INTO `user` VALUES (16950, '2021-12-29 21:11:11', 'ocsen21@gmail.com', '0375866523', '2021-12-29 21:11:35', 'Hoàng Thu Hà', '$2a$10$zPsfd79uyoXKlc3uiWP2o.lVWhqd9jLEEURP.n7zNURkhTD0fueOK', 'Haht1', '/resources/images/Haht1.png', 0);
INSERT INTO `user` VALUES (16951, '2021-12-30 01:25:49', 'test2@vnpt.vn', '0975280467', '2021-12-30 01:25:49', 'Nguyễn Văn Testttt', '$2a$10$Uzc9Ha3ffCen/FJxbMMw2uW1JoxH7Um19AYDsR6SqGEC.FfFPlFw2', 'test_2', '/resources/images/default.png', 0);

-- ----------------------------
-- Table structure for user_device
-- ----------------------------
DROP TABLE IF EXISTS `user_device`;
CREATE TABLE `user_device`  (
  `user_device_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `device_id` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `device_type` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `is_refresh_active` bit(1) NULL DEFAULT NULL,
  `modified_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `notification_token` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_device_id`) USING BTREE,
  INDEX `FKd2lb0k09c4nnfpvku8r61g92n`(`user_id`) USING BTREE,
  CONSTRAINT `FKd2lb0k09c4nnfpvku8r61g92n` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_device
-- ----------------------------
INSERT INTO `user_device` VALUES (20, '2021-12-29 23:45:08', 'XIAOMI', 'DEVICE_TYPE_ANDROID', b'1', '2021-12-29 23:45:08', 'N1', 16940);
INSERT INTO `user_device` VALUES (21, '2021-12-30 11:09:31', 'M2101K7AG', 'DEVICE_TYPE_ANDROID', b'1', '2021-12-30 11:09:31', 'N1', 16937);

-- ----------------------------
-- Table structure for users_departments
-- ----------------------------
DROP TABLE IF EXISTS `users_departments`;
CREATE TABLE `users_departments`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `is_admin` tinyint(4) NULL DEFAULT 0,
  `is_root` tinyint(4) NULL DEFAULT 0,
  `job_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modified_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `position` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `department_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKd1ql6mo22pem6fyegus89cc2s`(`department_id`) USING BTREE,
  INDEX `FKbw32gkkto40ehyx11lbfjhqc`(`user_id`) USING BTREE,
  CONSTRAINT `FKbw32gkkto40ehyx11lbfjhqc` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKd1ql6mo22pem6fyegus89cc2s` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users_departments
-- ----------------------------
INSERT INTO `users_departments` VALUES (1, '2021-11-25 15:10:47', 1, 1, 'Giám Đốc', '2021-11-25 15:10:47', 'Giám Đốc', 2, 16940);
INSERT INTO `users_departments` VALUES (2, '2021-12-19 13:10:30', 1, 1, 'Trưởng phòng', '2021-12-19 13:10:30', 'Trưởng phòng', 2, 16939);
INSERT INTO `users_departments` VALUES (3, '2021-12-19 13:11:02', 1, 1, 'Chuyên viên', '2021-12-19 13:11:02', 'Chuyên viên', 2, 16937);
INSERT INTO `users_departments` VALUES (4, '2021-12-19 13:11:53', 1, 1, 'Chuyên viên', '2021-12-19 13:11:53', 'Chuyên viên', 2, 16936);
INSERT INTO `users_departments` VALUES (5, '2021-12-19 13:13:43', 1, 1, 'Chuyên viên', '2021-12-19 13:13:43', 'Chuyên viên', 2, 16935);
INSERT INTO `users_departments` VALUES (6, '2021-12-23 00:36:10', 0, 1, 'Chuyên viên', '2021-12-23 00:36:10', 'Chuyên viên', 2, 16941);
INSERT INTO `users_departments` VALUES (7, '2021-12-26 22:48:46', 1, 1, 'Kỹ sư lập trình', '2021-12-26 22:48:46', 'Kỹ sư lập trình', 2, 16942);
INSERT INTO `users_departments` VALUES (8, '2021-12-26 23:33:22', 1, 1, 'Chuyên viên', '2021-12-26 23:33:22', 'Chuyên viên', 2, 16943);
INSERT INTO `users_departments` VALUES (9, '2021-12-26 23:36:07', 1, 1, 'Chuyên viên', '2021-12-26 23:36:07', 'Chuyên viên', 2, 16944);
INSERT INTO `users_departments` VALUES (16, '2021-12-28 11:11:22', 1, 1, 'DEV4', '2021-12-28 11:11:22', 'DEV4', 8, 16938);
INSERT INTO `users_departments` VALUES (17, '2021-12-29 21:09:56', 1, 1, 'Thư kí', '2021-12-29 21:09:56', 'Thư kí', 2, 16949);
INSERT INTO `users_departments` VALUES (18, '2021-12-29 21:11:13', 0, 0, 'Chuyên viên', '2021-12-29 21:11:13', 'Chuyên viên', 5, 16950);
INSERT INTO `users_departments` VALUES (19, '2021-12-30 01:26:08', 1, 1, 'Chuyên viên', '2021-12-30 01:26:08', 'Chuyên viên', 2, 16951);

-- ----------------------------
-- Table structure for users_roles
-- ----------------------------
DROP TABLE IF EXISTS `users_roles`;
CREATE TABLE `users_roles`  (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  INDEX `FKt4v0rrweyk393bdgt107vdx0x`(`role_id`) USING BTREE,
  INDEX `FKgd3iendaoyh04b95ykqise6qh`(`user_id`) USING BTREE,
  CONSTRAINT `FKgd3iendaoyh04b95ykqise6qh` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKt4v0rrweyk393bdgt107vdx0x` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users_roles
-- ----------------------------
INSERT INTO `users_roles` VALUES (16935, 12010);
INSERT INTO `users_roles` VALUES (16936, 12010);
INSERT INTO `users_roles` VALUES (16937, 12011);
INSERT INTO `users_roles` VALUES (16938, 12011);
INSERT INTO `users_roles` VALUES (16939, 12011);
INSERT INTO `users_roles` VALUES (16940, 12011);
INSERT INTO `users_roles` VALUES (16941, 12010);
INSERT INTO `users_roles` VALUES (16942, 12010);
INSERT INTO `users_roles` VALUES (16943, 12010);
INSERT INTO `users_roles` VALUES (16944, 12010);
INSERT INTO `users_roles` VALUES (16949, 12011);
INSERT INTO `users_roles` VALUES (16950, 12011);
INSERT INTO `users_roles` VALUES (16951, 12010);

SET FOREIGN_KEY_CHECKS = 1;
