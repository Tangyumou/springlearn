### 一、mybatis-plus实体类常用注解

@TableName("tb_user")

@TableId("user_id")

@TableField(value = "user_name",exist = false)

当实体类与数据库不一致时使用,@TableFile exist属性默认为true,当数据库中没有改字段时改为false

```
@Data
@TableName("tb_user")
public class User {
    @TableId
    private Long userId;
    @TableField(value = "user_name",exist = false)
    private String name;
    private Integer age;
    private String email;
    private Long managerId;
    private LocalDateTime createTime;
}

```

