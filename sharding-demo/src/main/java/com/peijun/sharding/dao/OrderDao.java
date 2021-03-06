package com.peijun.sharding.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peijun.sharding.dto.OrderDTO;
import com.peijun.sharding.pojo.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author Kwok Dylan GSGB
 * @since 2021-03-02
 */
public interface OrderDao extends BaseMapper<Order> {

    @Select("SELECT SUM(price) AS sum FROM t_order")
//    @Select("SELECT IFNULL((SUM(price), 0) FROM t_order") // 不行的
    BigDecimal selectSumNoCondition();

    @Select("SELECT SUM(price) AS sum FROM t_order WHERE create_time >= #{startTime}")
    BigDecimal selectSumNoUpper(@Param("startTime") LocalDateTime startTime);

    @Select("SELECT SUM(price) AS sum FROM t_order WHERE create_time <= #{endTime}")
    BigDecimal selectSumNoLower(@Param("endTime") LocalDateTime endTime);

    @Select("SELECT SUM(price) AS sum FROM t_order WHERE create_time >= #{startTime} AND create_time <= #{endTime}")
    BigDecimal selectSumClosed(@Param("startTime") LocalDateTime startTime,
                               @Param("endTime") LocalDateTime endTime);

    @Select("SELECT AVG(price) AS avg FROM t_order")
    BigDecimal selectAvgNoCondition();

    @Select("SELECT AVG(price) AS avg FROM t_order WHERE create_time >= #{startTime} AND create_time <= #{endTime}")
    BigDecimal selectAvgClosed(@Param("startTime") LocalDateTime startTime,
                               @Param("endTime") LocalDateTime endTime);

    @Select("SELECT SUM(price) AS sum FROM t_order GROUP BY user_id")
    List<Map> selectGroupByNoCondition();

    @Select("SELECT SUM(price) AS sum FROM t_order WHERE create_time >= #{startTime} AND create_time <= #{endTime} GROUP BY user_id")
    List<Map> selectGroupBy(@Param("startTime") LocalDateTime startTime,
                            @Param("endTime") LocalDateTime endTime);

    @Select(" SELECT t1.*,t2.* " +
            " FROM t_order t1" +
            " LEFT JOIN t_user t2 ON t1.user_id = t2.user_id ")
    List<OrderDTO> selectJoinUser();

    @Select(" SELECT t1.*,t2.* " +
            " FROM t_order t1" +
            " LEFT JOIN t_user t2 ON t1.user_id = t2.user_id " +
            " WHERE t1.create_time >= #{startTime} AND t1.create_time <= #{endTime}")
    List<OrderDTO> selectJoinUserByCondition(@Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);
}