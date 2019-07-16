package demo_1;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyBatisTest {

    public static void main(String[] args) throws IOException {
        SqlSessionFactory sqlSessionFactory;

        String resource = "mybatisconfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);


        inputStream.close();

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            PoJoMapper poJoMapper = sqlSession.getMapper(PoJoMapper.class);
            List<PoJo> poJos = poJoMapper.selectAll();
            for (PoJo poJo : poJos) {
                System.out.println(poJo.getId()+"_"+poJo.getName());
            }
        }finally {
            sqlSession.commit();
            sqlSession.close();

        }


    }

}
