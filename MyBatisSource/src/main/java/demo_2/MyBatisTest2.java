package demo_2;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyBatisTest2 {
    public static void main(String[] args) throws IOException {
        SqlSessionFactory sqlSessionFactory;

        String resource = "demo_2/mybatisconfig_2.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        inputStream.close();

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {


            ArticleDao articleDao = sqlSession.getMapper(ArticleDao.class);
            Article article = articleDao.findOne(1);
            System.out.println(article.getId()+" "+article + " " + article.getAuthor());


            AuthorDao authorDao = sqlSession.getMapper(AuthorDao.class);
            Author author = authorDao.findOne(1);
            List<Article> articles = author.getArticles();

            System.out.println(author + " " + articles);
        }finally {
            sqlSession.commit();
            sqlSession.close();

        }

    }
}
