### 使用规则

在mybatis 的sqlxml文件中配置
```
<resultMap>
<result column="type" property="type" jdbcType="VARCHAR" typeHandler="xxx.StringEnumTypeHandler" />
</resultMap>

以及插入、更新语句

<insert id="insert" >
    insert into xx_table (id, name)
    values (#{id,jdbcType=VARCHAR},
      #{name,jdbcType=VARCHAR,typeHandler=xx.util.StringArrayTypeHandler})
</insert>

```

或者直接在使用注解的方法上加入 
```
@Insert("INSERT INTO xxx_table(NAMEE)   
VALUES(#{name},typeHandler=xx.StringEnumTypeHandler")  
int insert(Object obj);  

```

使用上述handler需继承BaseEnum，也可以自己改写