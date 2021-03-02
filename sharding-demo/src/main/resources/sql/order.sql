CREATE TABLE `t_order_2021_02` (
  `order_id` bigint NOT NULL COMMENT '订单id',
  `price` decimal(10,2) NOT NULL COMMENT '订单价格',
  `user_id` int NOT NULL COMMENT '下单用户id',
  `status` int NOT NULL COMMENT '订单状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='订单表';