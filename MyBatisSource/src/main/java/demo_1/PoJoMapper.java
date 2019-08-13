package demo_1;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PoJoMapper {
    List<PoJo> selectId(@Param("id") String id);
}
