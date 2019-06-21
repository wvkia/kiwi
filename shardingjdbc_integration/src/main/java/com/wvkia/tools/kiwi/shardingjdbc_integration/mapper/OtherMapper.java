package com.wvkia.tools.kiwi.shardingjdbc_integration.mapper;

import com.wvkia.tools.kiwi.shardingjdbc_integration.model.OtherDo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OtherMapper {
    int insert(OtherDo record);

    void deleteById(String id);

    List<OtherDo> selectName(String name);
}