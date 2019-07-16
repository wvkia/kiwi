package springmybatis;

import demo_2.Article;
import demo_2.ArticleDao;
import demo_2.Author;
import demo_2.AuthorDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:springmybatis/application-mybatis.xml")
public class SpringMybatisTest implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Autowired
    private AuthorDao authorDao;
    @Autowired
    private ArticleDao articleDao;

    @Test
    public void test() {
            Article article = articleDao.findOne(1);
            System.out.println(article.getId()+" "+article + " " + article.getAuthor());

            Author author = authorDao.findOne(1);
            List<Article> articles = author.getArticles();

            System.out.println(author + " " + articles);

    }
}
