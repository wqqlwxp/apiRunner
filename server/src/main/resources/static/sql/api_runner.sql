

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for case_log
-- ----------------------------
DROP TABLE IF EXISTS `case_log`;
CREATE TABLE `case_log` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `project_code` varchar(20) DEFAULT NULL,
  `module_code` varchar(20) DEFAULT NULL,
  `type` varchar(5) DEFAULT NULL COMMENT '执行用例类型，0 前置用例，1正常用例',
  `service_type` varchar(10) DEFAULT NULL COMMENT '调用类型，http/https，dubbo',
  `case_id` varchar(10) DEFAULT NULL COMMENT '测试用例id',
  `plan_id` varchar(5) DEFAULT NULL COMMENT '测试计划id',
  `test_env` varchar(20) DEFAULT NULL COMMENT '被测环境',
  `interface_desc` varchar(100) DEFAULT NULL COMMENT '用例下执行接口描述',
  `response_data` varchar(500) DEFAULT NULL COMMENT '原始响应数据',
  `verif_data` varchar(2048) DEFAULT NULL COMMENT '校验结果',
  `result` varchar(50) DEFAULT NULL COMMENT '测试结果',
  `msg` varchar(255) DEFAULT NULL COMMENT '其他信息',
  `operater` varchar(50) DEFAULT NULL COMMENT '最后操作人',
  `create_time` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `project_idx` (`project_code`) USING HASH,
  KEY `module_idx` (`module_code`) USING HASH,
  KEY `time_idx` (`create_time`) USING BTREE,
  KEY `type_idx` (`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=828 DEFAULT CHARSET=utf8 COMMENT='用例执行日志';

-- ----------------------------
-- Table structure for cipher
-- ----------------------------
DROP TABLE IF EXISTS `cipher`;
CREATE TABLE `cipher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_code` varchar(50) NOT NULL COMMENT '项目',
  `module_code` varchar(50) NOT NULL COMMENT '模块',
  `type` varchar(50) NOT NULL COMMENT '加解密类型',
  `key_data` varchar(50) NOT NULL COMMENT '密文',
  `iv` varchar(50) DEFAULT NULL COMMENT '偏移量',
  `execute_order` int(2) NOT NULL COMMENT '加解密顺序',
  `cipher_path` varchar(100) DEFAULT NULL COMMENT '加密字段取值jsonpath',
  `request_encrypt` varchar(5) DEFAULT NULL COMMENT '请求是否加密，0加密，1不加密',
  `response_decrypt` varchar(5) DEFAULT NULL COMMENT '响应是否解密，0解密，1不解密',
  `status` varchar(2) NOT NULL COMMENT '0有效，1失效',
  `operater` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_project_code` (`project_code`) USING HASH,
  KEY `idx_module_code` (`module_code`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for env_machine
-- ----------------------------
DROP TABLE IF EXISTS `env_machine`;
CREATE TABLE `env_machine` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_code` varchar(50) NOT NULL COMMENT '所属项目',
  `env_code` varchar(50) NOT NULL COMMENT '机器环境编码',
  `env_name` varchar(100) DEFAULT NULL COMMENT '机器环境名称',
  `ip` varchar(50) NOT NULL COMMENT '机器ip地址',
  `port` varchar(10) DEFAULT NULL COMMENT '服务端口',
  `user_name` varchar(100) DEFAULT NULL COMMENT '机器登录用户',
  `user_pwd` varchar(100) DEFAULT NULL COMMENT '机器登录密码',
  `status` varchar(5) DEFAULT '0' COMMENT '是否启用，0启用 1禁用',
  `operater` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `project_idx` (`project_code`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='环境配置';

-- ----------------------------
-- Table structure for interface
-- ----------------------------
DROP TABLE IF EXISTS `interface`;
CREATE TABLE `interface` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_code` varchar(100) NOT NULL COMMENT '所属项目',
  `module_code` varchar(100) NOT NULL COMMENT '所属模块',
  `interface_url` varchar(250) NOT NULL COMMENT '接口地址',
  `interface_type` varchar(50) NOT NULL COMMENT '接口类型，http\\https\\dubbo',
  `request_type` varchar(50) DEFAULT NULL COMMENT '请求类型，post\\get',
  `interface_desc` varchar(100) DEFAULT NULL COMMENT '接口描述信息',
  `interface_port` varchar(50) DEFAULT NULL COMMENT '接口服务端口',
  `status` varchar(5) DEFAULT '0' COMMENT '是否启用，0启用 1禁用',
  `operater` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `project_idx` (`project_code`) USING HASH,
  KEY `module_idx` (`module_code`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='测试接口';

-- ----------------------------
-- Table structure for mail
-- ----------------------------
DROP TABLE IF EXISTS `mail`;
CREATE TABLE `mail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_code` varchar(50) NOT NULL COMMENT '邮件发送维度',
  `mail_title` varchar(100) NOT NULL COMMENT '邮件标题',
  `mail_receive` varchar(250) NOT NULL COMMENT '收件人，逗号分隔',
  `status` varchar(5) NOT NULL COMMENT '状态',
  `operater` varchar(100) DEFAULT NULL COMMENT '操作人',
  `create_time` varchar(100) NOT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_name` varchar(50) NOT NULL COMMENT '项目名称',
  `project_code` varchar(50) NOT NULL COMMENT '项目编码',
  `status` varchar(5) NOT NULL DEFAULT '0' COMMENT '启用状态，0 启用 1禁用',
  `operater` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `code` (`project_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='测试项目';

-- ----------------------------
-- Table structure for project_module
-- ----------------------------
DROP TABLE IF EXISTS `project_module`;
CREATE TABLE `project_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_code` varchar(50) NOT NULL,
  `module_code` varchar(50) NOT NULL COMMENT '项目模块编码',
  `module_name` varchar(100) NOT NULL COMMENT '项目模块名称',
  `status` varchar(5) NOT NULL DEFAULT '0' COMMENT '状态， 0有效，1失效',
  `operater` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='项目模块';

-- ----------------------------
-- Table structure for schedule_task
-- ----------------------------
DROP TABLE IF EXISTS `schedule_task`;
CREATE TABLE `schedule_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_code` varchar(50) DEFAULT NULL COMMENT '项目编码',
  `cron` varchar(50) DEFAULT NULL COMMENT 'crontab表达式',
  `status` varchar(5) DEFAULT NULL COMMENT '状态，0有效，1失效',
  `operater` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for test_case
-- ----------------------------
DROP TABLE IF EXISTS `test_case`;
CREATE TABLE `test_case` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_code` varchar(50) NOT NULL,
  `module_code` varchar(50) NOT NULL,
  `env_code` varchar(20) DEFAULT NULL COMMENT '执行环境',
  `interface_ids` varchar(50) DEFAULT NULL COMMENT '用例包含接口id集合',
  `pre_data` varchar(255) DEFAULT NULL COMMENT '用例前置id',
  `case_data` varchar(5000) NOT NULL COMMENT '接口数据集合，支持单接口和多接口',
  `type` varchar(5) NOT NULL COMMENT '用例类型，0前置用例，1正常用例',
  `service_type` varchar(5) NOT NULL COMMENT '接口服务类型，0普通接口，1dubbo接口',
  `status` varchar(5) NOT NULL COMMENT '状态值，0有效，1禁用',
  `case_desc` varchar(100) DEFAULT NULL COMMENT '用例描述',
  `operater` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `project_idx` (`project_code`) USING HASH,
  KEY `module_idx` (`module_code`) USING HASH,
  KEY `type_idx` (`type`) USING BTREE,
  KEY `status_idx` (`status`) USING BTREE,
  KEY `env_idx` (`env_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='测试用例';

-- ----------------------------
-- Table structure for test_plan
-- ----------------------------
DROP TABLE IF EXISTS `test_plan`;
CREATE TABLE `test_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_code` varchar(50) NOT NULL COMMENT '项目编码',
  `module_code` varchar(50) NOT NULL COMMENT '模块编码',
  `service_type` varchar(5) NOT NULL COMMENT '调用类型，0普通类型，1=dubbo类型',
  `env_code` varchar(50) NOT NULL COMMENT '被测环境',
  `plan_desc` varchar(255) DEFAULT NULL COMMENT '计划描述',
  `status` varchar(100) NOT NULL COMMENT '状态',
  `operater` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='测试计划';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_account` varchar(50) NOT NULL,
  `user_password` varchar(100) NOT NULL,
  `nick_name` varchar(50) DEFAULT NULL,
  `permission` varchar(500) DEFAULT NULL,
  `status` varchar(5) NOT NULL,
  `operater` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx-idx_user_account` (`user_account`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_copy1
-- ----------------------------
DROP TABLE IF EXISTS `user_copy1`;
CREATE TABLE `user_copy1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_account` varchar(50) NOT NULL,
  `user_password` varchar(100) NOT NULL,
  `nick_name` varchar(50) DEFAULT NULL,
  `permission` varchar(500) DEFAULT NULL,
  `status` varchar(5) NOT NULL,
  `operater` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx-idx_user_account` (`user_account`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for zk_config
-- ----------------------------
DROP TABLE IF EXISTS `zk_config`;
CREATE TABLE `zk_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `zk_alias` varchar(255) NOT NULL COMMENT 'zk别名',
  `zk_ip` varchar(50) NOT NULL COMMENT 'zk地址',
  `zk_password` varchar(100) DEFAULT NULL COMMENT 'zi密码',
  `create_time` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for zk_data
-- ----------------------------
DROP TABLE IF EXISTS `zk_data`;
CREATE TABLE `zk_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `zk_id` int(5) NOT NULL COMMENT 'zk配置id',
  `app_name` varchar(50) DEFAULT NULL COMMENT '应用名',
  `interface_name` varchar(100) DEFAULT NULL COMMENT '接口名',
  `method_name` varchar(100) DEFAULT NULL COMMENT '方法名',
  `version` varchar(20) DEFAULT NULL COMMENT '版本',
  `group` varchar(50) DEFAULT NULL COMMENT '组',
  `param_types` varchar(255) DEFAULT NULL,
  `param_datas` varchar(2048) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_app_name` (`zk_id`,`interface_name`,`method_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=263768 DEFAULT CHARSET=utf8;




-- ----------------------------
-- Table structure for database_config
-- ----------------------------
DROP TABLE IF EXISTS `database_config`;
CREATE TABLE `database_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_code` varchar(50) NOT NULL,
  `module_code` varchar(50) NOT NULL,
  `connection_address` varchar(255) NOT NULL,
  `connection_user` varchar(255) NOT NULL,
  `connection_pwd` varchar(255) NOT NULL,
  `status` varchar(5) NOT NULL DEFAULT '0',
  `operater` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;





SET FOREIGN_KEY_CHECKS = 1;
