package com.peijun.sharding.config.nokeyid;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Component
public class CreateTimeShardingTableAlgorithm
        implements PreciseShardingAlgorithm<String>, RangeShardingAlgorithm<LocalDateTime> {
    private static final DateTimeFormatter TABLE_SUFFIX_FORMAT = DateTimeFormatter.ofPattern("_yyyy_MM");
    private static final DateTimeFormatter ORIGIN_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Logger logger = LoggerFactory.getLogger(CreateTimeShardingTableAlgorithm.class);

    /**
     * 精确分片 路由指定日期
     *
     * @param availableTargetNames
     * @param shardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        logger.warn("--< 时间分片-精确分片>--: 开始...");
        logger.warn("--< 时间分片-精确分片>--: {}", availableTargetNames);

        LocalDateTime waitRouteTime = LocalDateTime.parse(shardingValue.getValue(), ORIGIN_FORMAT); // 获取待路由的时间
        String logicTableName = shardingValue.getLogicTableName(); // 逻辑表名
        String suffix = TABLE_SUFFIX_FORMAT.format(waitRouteTime);
        String tableName = logicTableName + suffix;

        logger.warn("--< 时间分片-精确分片>--: 真正路由到的表为[{}]", tableName);
        logger.warn("--< 时间分片-精确分片>--: 结束...");
        return tableName;
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<LocalDateTime> rangeShardingValue) {
        return null;
    }
}
