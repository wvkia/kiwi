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

        String resource = "demo_1/mybatisconfig_1.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        inputStream.close();

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            PoJoMapper poJoMapper = sqlSession.getMapper(PoJoMapper.class);
            List<PoJo> poJos = poJoMapper.selectId("1");
            for (PoJo poJo : poJos) {
                System.out.println(poJo.getId()+"_"+poJo.getName());
            }
        }finally {
            sqlSession.commit();
            sqlSession.close();

        }

        //或者第二种
        sqlSession = sqlSessionFactory.openSession();
        try {
            String statement = "demo_1.PoJoMapper.selectAll";
            List<PoJo> poJos = sqlSession.selectList(statement);
            for (PoJo poJo : poJos) {
                System.out.println(poJo.getId() + "_" + poJo.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }


    }

}
