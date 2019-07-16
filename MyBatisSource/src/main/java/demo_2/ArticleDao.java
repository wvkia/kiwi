package demo_2;

import org.apache.ibatis.annotations.Param;

public interface ArticleDao {
    Article findOne(@Param("id") int id);

}
