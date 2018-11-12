package com.wvkia.tools.kiwi.frametools.mybatis.typeHandler;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
 * 解析字符串根据逗号分割为list
 */
@MappedTypes(List.class)
@MappedJdbcTypes({JdbcType.VARCHAR})
public class StringArrayTypeHandler extends BaseTypeHandler<List<String>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<String> strings, JdbcType jdbcType) throws SQLException {
        StringBuilder sb = new StringBuilder();
        if (CollectionUtils.isNotEmpty(strings)) {
            for (String s : strings) {
                if (StringUtils.isNotEmpty(sb.toString())) {
                    sb.append(",");
                }
                sb.append(s);
            }

        }
        preparedStatement.setString(i,StringUtils.isEmpty(sb.toString())?null:sb.toString() );
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String str = resultSet.getString(s);
        if (resultSet.wasNull()) {
            return Lists.newArrayList();
        }

        String[] strs=str.split(",");
        return Lists.newArrayList(strs);
    }

    @Override
    public List<String> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String str = resultSet.getString(i);
        if (resultSet.wasNull()) {
            return Lists.newArrayList();
        }

        String[] strs=str.split(",");
        return Lists.newArrayList(strs);
    }

    @Override
    public List<String> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String str = callableStatement.getString(i);
        if (callableStatement.wasNull()) {
            return Lists.newArrayList();
        }

        String[] strs=str.split(",");
        return Lists.newArrayList(strs);
    }
}
