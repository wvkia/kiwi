package demo_2;

public enum  ArticleTypeEnum {
    JAVA(1),SPRING(2),MYBATIS(4);
    private int code;

    ArticleTypeEnum(int code) {
        this.code = code;

    }

    public int getCode() {
        return code;
    }

    public static ArticleTypeEnum getByCode(int code) {
        for (ArticleTypeEnum value : ArticleTypeEnum.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return null;

    }
}
