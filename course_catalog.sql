/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : course_catalog

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 16/06/2020 22:53:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `credit` int(0) NULL DEFAULT NULL,
  `prerequisite` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fee` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('1', 'Python编程', 2, NULL, 100);
INSERT INTO `course` VALUES ('10', '高等数学', 2, NULL, 100);
INSERT INTO `course` VALUES ('11', '数据结构', 2, NULL, 60);
INSERT INTO `course` VALUES ('12', '组成原理', 2, NULL, 120);
INSERT INTO `course` VALUES ('13', '操作系统', 3, NULL, 125);
INSERT INTO `course` VALUES ('2', '离散数学', 1, '', 80);
INSERT INTO `course` VALUES ('3', '数学建模', 2, NULL, 100);
INSERT INTO `course` VALUES ('4', '算法设计', 3, '11', 120);
INSERT INTO `course` VALUES ('5', '英语', 2, NULL, 75);
INSERT INTO `course` VALUES ('6', '软件工程', 3, NULL, 90);
INSERT INTO `course` VALUES ('7', '计算机导论', 1, NULL, 60);
INSERT INTO `course` VALUES ('8', 'Java编程', 2, NULL, 100);
INSERT INTO `course` VALUES ('9', '编译原理', 2, NULL, 100);

SET FOREIGN_KEY_CHECKS = 1;
