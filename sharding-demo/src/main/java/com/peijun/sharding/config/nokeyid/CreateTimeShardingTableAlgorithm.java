package com.peijun.sharding.config.nokeyid;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 标准分片策略
 *
 * PreciseShardingAlgorithm 必选的，用于处理 = 和 IN 的分片
 * RangeShardingAlgorithm 可选的，用于处理 BETWEEN AND, >, <, >=, <=分片
 *      如果不配置 RangeShardingAlgorithm，SQL 中的 BETWEEN AND 将按照全库路由处理。
 */
@Component
public class CreateTimeShardingTableAlgorithm
        implements PreciseShardingAlgorithm<String>, RangeShardingAlgorithm<String> {
    private Logger logger = LoggerFactory.getLogger(CreateTimeShardingTableAlgorithm.class);

    private static final DateTimeFormatter TABLE_SUFFIX_FORMAT = DateTimeFormatter.ofPattern("_yyyy_MM");
    private static final DateTimeFormatter ORIGIN_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String LOWER_TIME_KEY = "lowerTime";
    private static final String UPPER_TIME_KEY = "upperTime";


    /**
     * 精确分片 路由指定日期 对应的 一张表
     *
     * @param availableTargetNames
     * @param shardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        logger.warn("--< 时间分片-精确分片 >--: 开始...");
        logger.warn("--< 时间分片-精确分片 >--: {}", availableTargetNames);
        String value = shardingValue.getValue();
        if (StringUtils.isEmpty(value)) {
            throw new UnsupportedOperationException("时间分片-精确分片 分片值为空");
        }
        // 获取待路由的时间
        LocalDateTime waitRouteTime = LocalDateTime.parse(value, ORIGIN_FORMAT);
        // 逻辑表名
        String logicTableName = shardingValue.getLogicTableName();
        String suffix = TABLE_SUFFIX_FORMAT.format(waitRouteTime);
        String tableName = logicTableName + suffix;
        logger.warn("--< 时间分片-精确分片 >--: 真正路由到的表为[{}]", tableName);
        logger.warn("--< 时间分片-精确分片 >--: 结束...");
        return tableName;
    }

    /**
     * 范围路由 根据范围 路由到一个或者多张表
     *
     * 需要注意 只给了一个时间 的情况
     *
     * @param availableTargetNames
     * @param rangeShardingValue
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<String> rangeShardingValue) {
        logger.warn("--< 时间分片-范围分片>--: 开始...");
        logger.warn("--< 时间分片-范围分片>--: {}", availableTargetNames);
        // 逻辑表名
        String logicTableName = rangeShardingValue.getLogicTableName();
        // 范围
        Map<String, LocalDateTime> timeMap = getLowerAndUpperTime(rangeShardingValue.getValueRange());
        LocalDateTime lowerTime = timeMap.get(LOWER_TIME_KEY);
        LocalDateTime upperTime = timeMap.get(UPPER_TIME_KEY);
        logger.warn("--< 时间分片-范围分片>--: [{}] <--> [{}]", lowerTime, upperTime);
        // 需要将上下限时间改为 当前月份的1日，方便比对日期大小
        LocalDate lowerDate = lowerTime.withDayOfMonth(1).toLocalDate();
        LocalDate upperDate = upperTime.withDayOfMonth(1).toLocalDate();
        // 添加路由表名
        List<String> routeTableList = new ArrayList<>();
        while (lowerDate.isBefore(upperDate) || lowerDate.equals(upperDate)) {
            String tableSuffix = lowerDate.format(TABLE_SUFFIX_FORMAT);
            String realTable = logicTableName + tableSuffix;
            // 判断待路由的表是否存在
            if (availableTargetNames.contains(realTable)) {
                routeTableList.add(realTable);
            }
            lowerDate = lowerDate.plusMonths(1);
        }
        logger.warn("--< 时间分片-范围分片>--: 范围路由表名 -> {}", routeTableList);
        return routeTableList; // 返回size为0的话 会直接 去查找逻辑表 eg. select * from t_order 而不是t_order_2021_02等
    }

    /**
     * 获取开始时间和结束时间
     */
    public Map<String, LocalDateTime> getLowerAndUpperTime(Range<String> rangeValue) {
        if (rangeValue == null) {
            throw new UnsupportedOperationException("rangeValue为空");
        }
        Map<String, LocalDateTime> timeMap = new HashMap<>();
        LocalDateTime lowerTime = null;
        LocalDateTime upperTime = null;
        // 判断是否传递下限时间
        if (rangeValue.hasLowerBound()) {
            // 获取下限时间
            String lowerTimeStr = rangeValue.lowerEndpoint();
            lowerTime = LocalDateTime.parse(lowerTimeStr, ORIGIN_FORMAT);
        }
        // 判断是否传递上限时间
        if (rangeValue.hasUpperBound()) {
            // 获取上限时间
            String upperTimeStr = rangeValue.upperEndpoint();
            upperTime = LocalDateTime.parse(upperTimeStr, ORIGIN_FORMAT);
        }
        if (lowerTime == null && upperTime == null) {
            throw new UnsupportedOperationException("时间分片-范围分片 上下限为空");
        }
        if (lowerTime == null) {
            // 没有传递下限 则拿上限的前两个月 当然可以1,2,3,4..个月
            // 当然也可以拿小于结束时间的所有存在的表
            lowerTime = upperTime.plusMonths(-2); // 拿前一个月和本月的
        }
        if (upperTime == null) {
            // 没有传递上限 则拿下限的后两个月
            upperTime = lowerTime.plusMonths(2);
        }
        timeMap.put(LOWER_TIME_KEY, lowerTime);
        timeMap.put(UPPER_TIME_KEY, upperTime);
        return timeMap;
    }
}
