/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : administration_system

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 17/06/2020 09:18:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `cid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程id',
  `pid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '教授id',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `credit` int(0) NULL DEFAULT NULL,
  `prerequisite` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `semester` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fee` int(0) NULL DEFAULT NULL,
  `timeslot` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `number` int(0) NULL DEFAULT 0,
  PRIMARY KEY (`cid`) USING BTREE,
  INDEX `pid`(`pid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('1', '3', 'Python编程', 2, NULL, '2020年第二学期', 100, '周三上午10点', 0);
INSERT INTO `course` VALUES ('2', '1', '离散数学', 1, NULL, '2020年第二学期', 80, '周一上午10点', 0);
INSERT INTO `course` VALUES ('3', '1', '数学建模', 2, NULL, '2020年第二学期', 100, '周四上午10点', 0);
INSERT INTO `course` VALUES ('4', '2', '算法设计', 3, '11', '2020年第二学期', 120, '周四上午10点', 0);
INSERT INTO `course` VALUES ('5', '1', '英语', 2, NULL, '2020年第二学期', 75, '周二上午10点', 0);
INSERT INTO `course` VALUES ('6', '2', '软件工程', 3, NULL, '2020年第二学期', 90, '周二上午10点', 0);
INSERT INTO `course` VALUES ('7', '1', '计算机导论', 1, NULL, '2020年第二学期', 60, '周三上午10点', 0);
INSERT INTO `course` VALUES ('8', '1', 'Java编程', 2, NULL, '2020年第二学期', 100, '周五上午10点', 0);
INSERT INTO `course` VALUES ('9', '3', '编译原理', 2, NULL, '2020年第二学期', 100, '周二上午10点', 0);

-- ----------------------------
-- Table structure for course_selection
-- ----------------------------
DROP TABLE IF EXISTS `course_selection`;
CREATE TABLE `course_selection`  (
  `sid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学生id',
  `lesson1` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第一门主选课程id',
  `lesson2` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lesson3` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lesson4` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lesson5` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lesson6` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sid`) USING BTREE,
  CONSTRAINT `course_selection_ibfk_1` FOREIGN KEY (`sid`) REFERENCES `student` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for credit_card
-- ----------------------------
DROP TABLE IF EXISTS `credit_card`;
CREATE TABLE `credit_card`  (
  `sid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `fee` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of credit_card
-- ----------------------------
INSERT INTO `credit_card` VALUES ('1', NULL);
INSERT INTO `credit_card` VALUES ('2', NULL);
INSERT INTO `credit_card` VALUES ('3', NULL);
INSERT INTO `credit_card` VALUES ('4', NULL);
INSERT INTO `credit_card` VALUES ('5', NULL);
INSERT INTO `credit_card` VALUES ('6', NULL);
INSERT INTO `credit_card` VALUES ('7', NULL);

-- ----------------------------
-- Table structure for grade
-- ----------------------------
DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade`  (
  `sid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学生id',
  `pid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '教授id',
  `cid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '课程id',
  `cname` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程名称',
  `credit` int(0) NULL DEFAULT NULL COMMENT '课程学分',
  `semester` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'xx年第xx学期',
  `grade` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sid`, `pid`, `cid`) USING BTREE,
  INDEX `cid`(`cname`) USING BTREE,
  CONSTRAINT `grade_ibfk_1` FOREIGN KEY (`sid`) REFERENCES `student` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of grade
-- ----------------------------
INSERT INTO `grade` VALUES ('1', '3', '10', '高等数学', 2, '2019年第一学期', NULL);
INSERT INTO `grade` VALUES ('1', '4', '11', '数据结构', 2, '2019年第二学期', NULL);
INSERT INTO `grade` VALUES ('1', '4', '13', '操作系统', 3, '2019年第二学期', NULL);
INSERT INTO `grade` VALUES ('2', '3', '10', '高等数学', 2, '2019年第一学期', NULL);
INSERT INTO `grade` VALUES ('2', '3', '12', '组成原理', 3, '2020年第一学期', NULL);
INSERT INTO `grade` VALUES ('2', '4', '11', '数据结构', 2, '2019年第二学期', NULL);
INSERT INTO `grade` VALUES ('2', '4', '13', '操作系统', 3, '2019年第二学期', NULL);
INSERT INTO `grade` VALUES ('3', '3', '10', '高等数学', 2, '2019年第一学期', NULL);
INSERT INTO `grade` VALUES ('3', '4', '11', '数据结构', 2, '2019年第二学期', NULL);
INSERT INTO `grade` VALUES ('3', '4', '13', '操作系统', 3, '2019年第二学期', NULL);
INSERT INTO `grade` VALUES ('4', '3', '10', '高等数学', 2, '2019年第一学期', NULL);
INSERT INTO `grade` VALUES ('4', '4', '11', '数据结构', 2, '2019年第二学期', NULL);
INSERT INTO `grade` VALUES ('4', '4', '13', '操作系统', 3, '2019年第二学期', NULL);
INSERT INTO `grade` VALUES ('5', '3', '10', '高等数学', 2, '2019年第一学期', NULL);
INSERT INTO `grade` VALUES ('5', '4', '11', '数据结构', 2, '2019年第二学期', NULL);
INSERT INTO `grade` VALUES ('5', '4', '13', '操作系统', 3, '2019年第二学期', NULL);
INSERT INTO `grade` VALUES ('6', '3', '10', '高等数学', 2, '2019年第一学期', NULL);
INSERT INTO `grade` VALUES ('6', '4', '11', '数据结构', 2, '2019年第二学期', NULL);
INSERT INTO `grade` VALUES ('6', '4', '13', '操作系统', 3, '2019年第二学期', NULL);

-- ----------------------------
-- Table structure for professor
-- ----------------------------
DROP TABLE IF EXISTS `professor`;
CREATE TABLE `professor`  (
  `pid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `birthday` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ssn` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `department` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of professor
-- ----------------------------
INSERT INTO `professor` VALUES ('1', '123456', '李红', '19800101', '987654321', '副教授', '计算机');
INSERT INTO `professor` VALUES ('2', '123456', '王明', '19750202', '876543219', '教授', '软件');
INSERT INTO `professor` VALUES ('3', '123456', '陈芳', '19830303', '765432198', '讲师', '计算机');
INSERT INTO `professor` VALUES ('4', '123456', '张丽', '19900404', '654321987', '讲师', '数学');
INSERT INTO `professor` VALUES ('5', '123456', '马刚', '19880505', '543219876', '讲师', '英语');
INSERT INTO `professor` VALUES ('6', '123456', '王五', '19750606', '213456789', '教授', '人工智能');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `sid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `birthday` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ssn` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('1', '123456', '王一', '19990512', '145236987', '三年级');
INSERT INTO `student` VALUES ('2', '123456', '小明', '19980213', '1524639', '四年级');
INSERT INTO `student` VALUES ('3', '123456', '张三', '20000303', '345678912', '二年级');
INSERT INTO `student` VALUES ('4', '123456', '李四', '20000101', '456787845', '二年级');
INSERT INTO `student` VALUES ('5', '123456', '王五', '19990623', '745123698', '三年级');
INSERT INTO `student` VALUES ('6', '123456', '小华', '19990208', '145266987', '三年级');
INSERT INTO `student` VALUES ('7', '123456', '小芳', '20010101', '845692317', '一年级');

SET FOREIGN_KEY_CHECKS = 1;
