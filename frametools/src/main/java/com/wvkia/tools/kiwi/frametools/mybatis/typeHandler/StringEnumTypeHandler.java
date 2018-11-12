package com.wvkia.tools.kiwi.frametools.mybatis.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wukai
 * 针对string枚举值转换工具类
 */
@MappedTypes(List.class)
@MappedJdbcTypes({JdbcType.VARCHAR})
public class StringEnumTypeHandler<T extends  BaseEnum<?,String>> extends BaseTypeHandler<T> {

    private Class<T> type;
    private T[] enums;

    public StringEnumTypeHandler(Class<T> type) {

        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
        this.enums = type.getEnumConstants();

        if (this.enums == null) {
            throw new IllegalArgumentException(type.getSimpleName()
                    + " does not represent an enum type.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException {
        preparedStatement.setObject(i,t.getCode(),jdbcType.TYPE_CODE);
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String value = resultSet.getString(s);

        if (resultSet.wasNull()) {
            return null;
        }else {
            return getEnum(value);
        }
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String value = resultSet.getString(i);

        if (resultSet.wasNull()) {
            return null;
        }else {
            return getEnum(value);
        }
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String value = callableStatement.getString(i);

        if (callableStatement.wasNull()) {
            return null;
        }else {
            return getEnum(value);
        }
    }

    private T getEnum(String value) {
        for (T anEnum : enums) {
            if (anEnum.getCode().equals(value)) {
                return anEnum;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型：" + value + ",请核对" + type.getSimpleName());
    }
}
