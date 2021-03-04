CREATE TABLE `t_order_2021_02` (
  `order_id` bigint NOT NULL COMMENT '订单id',
  `price` decimal(10,2) NOT NULL COMMENT '订单价格',
  `user_id` int NOT NULL COMMENT '下单用户id',
  `status` int NOT NULL COMMENT '订单状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单表';

CREATE TABLE `t_order_2021_03` (
  `order_id` bigint NOT NULL COMMENT '订单id',
  `price` decimal(10,2) NOT NULL COMMENT '订单价格',
  `user_id` int NOT NULL COMMENT '下单用户id',
  `status` int NOT NULL COMMENT '订单状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单表';

CREATE TABLE `t_order_2021_04` (
  `order_id` bigint NOT NULL COMMENT '订单id',
  `price` decimal(10,2) NOT NULL COMMENT '订单价格',
  `user_id` int NOT NULL COMMENT '下单用户id',
  `status` int NOT NULL COMMENT '订单状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单表';




CREATE TABLE `t_user` (
  `user_id` int NOT NULL COMMENT 'userId',
  `username` varchar(24) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `age` int NOT NULL DEFAULT '0' COMMENT '年龄',
  `phone` varchar(12) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO t_user VALUES(10000,"天",23,""),
(10002,"天",34,""),
(10004,"要",32,""),
(10006,"加",37,""),
(10008,"班",39,""),
(10010,"受",23,""),
(10012,"不",24,""),
(10014,"了",26,""),
(10016,"运",21,""),
(10018,"营",26,""),
(10020,"产",22,""),
(10022,"品",54,""),
(10024,"瞎",52,""),
(10026,"搞",55,""),
(10028,"上",52,""),
(10030,"不",51,""),
(10032,"了",56,""),
(10034,"线",11,""),
(10036,"就",13,""),
(10038,"不",15,""),
(10040,"要",12,""),
(10042,"上",66,""),
(10044,"线",62,""),
(10046,"又",61,""),
(10048,"不",67,""),
(10050,"能",63,""),
(10052,"勉",65,""),
(10054,"强",63,"");