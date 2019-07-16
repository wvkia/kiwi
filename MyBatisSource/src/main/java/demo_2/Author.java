package demo_2;

import java.util.List;

public class Author {
    private Integer id;
    private String name;
    private SexEnum sex;
    private List<Article> articles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (null != articles) {
            for (Article article : articles) {
                stringBuilder.append(article).append(",");
            }
        }
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", articles=" + stringBuilder +
                '}';
    }
}
