//package com.peijun.sharding.config.nokeyid;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.collect.Range;
//import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Collection;
//
///**
// * @program: capital-flow
// * @description:
// * @author: Wrecked
// * @create: 2020-11-27 16:05
// **/
//@Component
//public class MyRangeShardingAlgorithm implements RangeShardingAlgorithm<String> {
//
//    private Logger logger = LoggerFactory.getLogger(MyRangeShardingAlgorithm.class);
//
//
//    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("_yyyy_MM");
//
//    private static DateTimeFormatter dfs = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//    @Override
//    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<String> shardingValue) {
//        try {
//            logger.info("Range collection:" + new ObjectMapper().writeValueAsString(availableTargetNames) +
//                    ",rangeShardingValue:" + new ObjectMapper().writeValueAsString(shardingValue));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        Collection<String> collect = new ArrayList<>();
//
//        Range<String> valueRange = shardingValue.getValueRange();
//        String logicTableName = shardingValue.getLogicTableName();
//        String lower = valueRange.lowerEndpoint();
//        String upper = valueRange.upperEndpoint();
//
//
//        LocalDateTime lowerLocalDateTime = LocalDateTime.parse(lower, dfs);
//        LocalDateTime upperLocalDateTime = LocalDateTime.parse(upper, dfs);
//
//        // 这个判断有问题 懒得改
//        while (lowerLocalDateTime.isBefore(upperLocalDateTime)) {
//            String format = df.format(lowerLocalDateTime);
//            //拼接tb_coin_change_recd_2021_01
//            collect.add(logicTableName + format);
//            lowerLocalDateTime = lowerLocalDateTime.plusMonths(1);
//        }
//        return collect;
//    }
//
//    public static void main(String[] args) {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime time1 = now.plusMonths(2).withDayOfMonth(1);
//        System.out.println(time1);
//
//        LocalDateTime time2 = time1.plusDays(-1);
//        System.out.println(time2);
//    }
//}
