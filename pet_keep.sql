-- ============================================================
--  校园流浪动物图鉴与动态打卡系统 — 数据库初始化脚本
--  数据库：app_db（服务器 Docker MySQL，175.178.40.188:3309）
--  字符集：utf8mb4（支持 emoji）
--  引擎：InnoDB
--  作者：petKeepSystem
--  日期：2026-06-11
-- ============================================================

CREATE DATABASE IF NOT EXISTS `app_db`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE `app_db`;

-- ============================================================
-- 1. 用户表 tb_user
--    role: 0=普通用户(学生/教工)  1=管理员
-- ============================================================
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `username`    VARCHAR(32)  NOT NULL COMMENT '登录账号（唯一）',
    `password`    VARCHAR(128) NOT NULL COMMENT '加密密码（BCrypt）',
    `nickname`    VARCHAR(32)           DEFAULT NULL COMMENT '显示昵称',
    `role`        TINYINT      NOT NULL DEFAULT 0 COMMENT '角色：0=普通用户 1=管理员',
    `avatar`      VARCHAR(255)          DEFAULT NULL COMMENT '头像图片路径',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `deleted`     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '用户表';

-- 内置管理员账号（密码明文: admin123，BCrypt 加密）
INSERT INTO `tb_user` (`username`, `password`, `nickname`, `role`)
VALUES ('admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '系统管理员', 1);


-- ============================================================
-- 2. 动物档案表 tb_animal
--    type: 0=猫  1=狗
-- ============================================================
DROP TABLE IF EXISTS `tb_animal`;
CREATE TABLE `tb_animal`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `name`        VARCHAR(32)  NOT NULL COMMENT '动物暂定名字（如：小橘、肥肥）',
    `type`        TINYINT      NOT NULL DEFAULT 0 COMMENT '动物类型：0=猫 1=狗',
    `area`        VARCHAR(64)  NOT NULL COMMENT '常驻区域（如：图书馆草坪、西区食堂）',
    `cover_img`   VARCHAR(255)          DEFAULT NULL COMMENT '封面照片存储路径（对应 tb_file.save_path）',
    `description` VARCHAR(500)          DEFAULT NULL COMMENT '档案备注（可为空）',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建档时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `deleted`     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=已删除',
    PRIMARY KEY (`id`),
    KEY `idx_name` (`name`),           -- 支持按名字模糊查询
    KEY `idx_type` (`type`),           -- 支持按类型筛选
    KEY `idx_deleted` (`deleted`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '校园动物档案表';


-- ============================================================
-- 3. 偶遇打卡动态表 tb_checkin
--    与 tb_animal 为多对一（N:1）关系
--    与 tb_user   为多对一（N:1）关系
-- ============================================================
DROP TABLE IF EXISTS `tb_checkin`;
CREATE TABLE `tb_checkin`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `animal_id`   BIGINT       NOT NULL COMMENT '关联动物 ID（→ tb_animal.id）',
    `user_id`     BIGINT       NOT NULL COMMENT '打卡用户 ID（→ tb_user.id）',
    `content`     VARCHAR(500) NOT NULL COMMENT '打卡文字描述',
    `img_url`     VARCHAR(255)          DEFAULT NULL COMMENT '现场照片路径（可选，对应 tb_file.save_path）',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '打卡时间',
    `deleted`     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=正常 1=已删除',
    PRIMARY KEY (`id`),
    KEY `idx_animal_id` (`animal_id`),                     -- 按动物 ID 查时间轴（最核心查询）
    KEY `idx_user_id` (`user_id`),                         -- 按用户查自己的打卡记录
    KEY `idx_create_time` (`create_time`),                 -- 时间轴倒序排序
    KEY `idx_animal_time` (`animal_id`, `create_time`),    -- 联合索引：动物维度时间轴
    CONSTRAINT `fk_checkin_animal` FOREIGN KEY (`animal_id`) REFERENCES `tb_animal` (`id`),
    CONSTRAINT `fk_checkin_user` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '偶遇打卡动态表';


-- ============================================================
-- 4. 文件上传记录表 tb_file
--    统一管理所有上传的图片/文件
--    cover_img / img_url 字段存储的是本表 save_path
-- ============================================================
DROP TABLE IF EXISTS `tb_file`;
CREATE TABLE `tb_file`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `original_name` VARCHAR(128) NOT NULL COMMENT '原始文件名',
    `save_name`     VARCHAR(128) NOT NULL COMMENT '服务端保存文件名（UUID 重命名，防冲突）',
    `save_path`     VARCHAR(255) NOT NULL COMMENT '服务端相对存储路径（如：/upload/animal/xxx.jpg）',
    `access_url`    VARCHAR(255)          DEFAULT NULL COMMENT '可访问的完整 URL（部署后填写）',
    `file_size`     BIGINT                DEFAULT NULL COMMENT '文件大小（字节）',
    `file_type`     VARCHAR(64)           DEFAULT NULL COMMENT 'MIME 类型（如：image/jpeg）',
    `uploader_id`   BIGINT                DEFAULT NULL COMMENT '上传者用户 ID',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    PRIMARY KEY (`id`),
    KEY `idx_uploader` (`uploader_id`),
    KEY `idx_save_path` (`save_path`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  COMMENT = '文件上传记录表';


-- ============================================================
-- 测试数据（可选，调试用）
-- ============================================================

-- 插入几条动物档案
INSERT INTO `tb_animal` (`name`, `type`, `area`, `description`)
VALUES ('小橘', 0, '图书馆草坪', '橘色条纹猫，性格亲人，常在草坪晒太阳'),
       ('肥肥', 0, '西区食堂门口', '黑白花色，爱蹭饭，目测已绝育'),
       ('旺财', 1, '操场跑道旁', '黄色小土狗，会追人但不咬');

-- 插入测试普通用户（密码明文: user123）
INSERT INTO `tb_user` (`username`, `password`, `nickname`, `role`)
VALUES ('student01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8RgmBH3BEYfEiZtGSe', '张同学', 0);

-- 插入测试打卡记录
INSERT INTO `tb_checkin` (`animal_id`, `user_id`, `content`)
VALUES (1, 2, '今天在图书馆门口看到小橘了，精神状态超好，还让我摸了一下'),
       (1, 2, '小橘又来了，这次带了两只小猫跟着它，可能是它孩子？'),
       (2, 2, '肥肥在食堂门口等饭，我喂了它一块鸡胸肉，吃得很香');
