CREATE DATABASE IF NOT EXISTS `statusmonitor` DEFAULT CHARACTER SET utf8;

USE `statusmonitor`;

-- 日志表
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log`
(
    `id`      int(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user`    varchar(50)    DEFAULT NULL COMMENT '请求人',
    `method`  varchar(255)   DEFAULT NULL COMMENT '方法名',
    `start`   timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
    `end`     timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '结束时间',
    `success` SMALLINT(0)    DEFAULT NULL COMMENT '是否成功',
    `caused`  TEXT           DEFAULT NULL COMMENT '失败原因',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`    varchar(20) DEFAULT NULL COMMENT '工号',
    `password`    varchar(30) DEFAULT NULL COMMENT '密码',
    `name`        varchar(20) DEFAULT NULL COMMENT '真实姓名',
    `sex`         varchar(20) DEFAULT NULL COMMENT '性别',
    `phone`       varchar(20) DEFAULT NULL COMMENT '联系电话',
    `department_id`  int(20)  DEFAULT NULL COMMENT  '部门ID',
    `role_id`     int(20)     NOT NULL COMMENT     '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `face_info`;
CREATE TABLE `face_info`
(
    `id`                int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `face_encodings`    varchar(4096) NOT NULL COMMENT '人脸信息编码',
    `base64_image`      LONGTEXT NOT NULL COMMENT '标准照片的base64编码',
    `username`          varchar(20) NOT NULL COMMENT '工号',
    PRIMARY KEY (`id`),
    KEY `username` (`username`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


-- role
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`          int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_name`    varchar(20) DEFAULT NULL COMMENT '角色名',
    `role_info`    varchar(20) DEFAULT NULL COMMENT '角色',
    PRIMARY KEY (`id`),
    UNIQUE KEY `role_name` (`role_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
INSERT INTO statusmonitor.role (role_name, role_info) VALUES ('普通员工', '最低权限');
INSERT INTO statusmonitor.role (role_name, role_info) VALUES ('部门主管', '中等权限');
INSERT INTO statusmonitor.role (role_name, role_info) VALUES ('人事主管', '最高权限');



-- page_permission
DROP TABLE IF EXISTS `page_permission`;
CREATE TABLE `page_permission`
(
    `id`          int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `page_name`    varchar(20) DEFAULT NULL COMMENT '页面名',
    `page_url`    varchar(20) DEFAULT NULL COMMENT '页面URL',
    PRIMARY KEY (`id`),
    UNIQUE KEY `page_name` (`page_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

  -- page_permission
DROP TABLE IF EXISTS `action_permission`;
CREATE TABLE `action_permission`
(
    `id`          int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `action_name`    varchar(20) DEFAULT NULL COMMENT 'action名',
    `action_url`    varchar(20) DEFAULT NULL COMMENT 'action URL',
    PRIMARY KEY (`id`),
    UNIQUE KEY `action_name` (`action_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

  -- data_permission
DROP TABLE IF EXISTS `data_permission`;
CREATE TABLE `data_permission`
(
    `id`          int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `data_name`    varchar(20) DEFAULT NULL COMMENT '数据名',
    `data_url`    varchar(20) DEFAULT NULL COMMENT '数据URL',
    PRIMARY KEY (`id`),
    UNIQUE KEY `data_name` (`data_name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `punch_record`;
CREATE TABLE `punch_record`
(
    `id`                    int(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `sequence_no`           varchar(20) NOT NULL COMMENT '流水号',
    `username`              varchar(20) NOT NULL COMMENT '工号',
    `date`                  date NOT NULL COMMENT '打卡日期',
    `time`                  time NOT NULL COMMENT '打卡时间',
    PRIMARY KEY (`id`),
    KEY (`username`),
    KEY (`date`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;