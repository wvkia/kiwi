package com.wvkia.tools.kiwi.frametools.mybatis.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author wukai
 * 针对枚举值为int型转换工具类
 */
@MappedTypes(Enum.class)
@MappedJdbcTypes({JdbcType.INTEGER})
public class IntEnumTypeHandler<T extends BaseEnum> extends BaseTypeHandler<T> {

    private Class<T> type;
    private T [] enums;

    /**
     * 设置配置文件设置的转换类以及枚举类内容，供其他方法更便捷高效的实现
     * @param type 配置文件中设置的转换类
     */
    public IntEnumTypeHandler(Class<T> type) {
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
        int code = resultSet.getInt(s);

        if (resultSet.wasNull()) {
            return null;
        } else {
            // 根据数据库中的code值，定位EnumStatus子类
            return getEnum(code);
        }
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int code = resultSet.getInt(i);

        if (resultSet.wasNull()) {
            return null;
        } else {
            // 根据数据库中的code值，定位EnumStatus子类
            return getEnum(code);
        }
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int code = callableStatement.getInt(i);

        if (callableStatement.wasNull()) {
            return null;
        } else {
            // 根据数据库中的code值，定位EnumStatus子类
            return getEnum(code);
        }
    }

    private T getEnum(int code) {
        for (T anEnum : enums) {
            if (anEnum.getCode().equals(Integer.valueOf(code))) {
                return anEnum;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型：" + code + ",请核对" + type.getSimpleName());
    }
}
