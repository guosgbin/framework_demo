package com.peijun.sharding.config;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 原因是sharding-jdbc的这个版本是 JDK7的  不支持LocalDate
 */
@Component
@MappedTypes(LocalDate.class)
@MappedJdbcTypes(value = JdbcType.DATE, includeNullJdbcType = true)
public class CustomLocalDateTypeHandler extends BaseTypeHandler<LocalDate> {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
 
 
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter != null) {
            ps.setString(i, dateTimeFormatter.format(parameter));
        }
    }
 
    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String target = rs.getString(columnName);
        if (StringUtils.isEmpty(target)) {
            return null;
        } else {
            return LocalDate.parse(target, dateTimeFormatter);
        }
    }
 
    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String target = rs.getString(columnIndex);
        if (StringUtils.isEmpty(target)) {
            return null;
        }
        return LocalDate.parse(target, dateTimeFormatter);
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String target = cs.getString(columnIndex);
        if (StringUtils.isEmpty(target)) {
            return null;
        }
        return LocalDate.parse(target, dateTimeFormatter);
    }
}



