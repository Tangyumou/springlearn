### 一、mybatis-plus实体类常用注解

@TableName("tb_user")

@TableId("user_id")

@TableField(value = "user_name",exist = false)

**当实体类与数据库不一致时使用,@TableFile exist属性默认为true,当数据库中没有改字段时改为false**

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

### 二、复杂查询

**1.创建日期为2019年2月14日直属上级姓王的员工**

SELECT id,name,age,email,manager_id,create_time FROM user WHERE date_format(create_time,'%Y-%m-%d')=? AND manager_id IN (select id from user where name like '王%')

```
@Test
public void select(){
	QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.apply("date_format(create_time,'%Y-%m-%d')={0}","2019-02-14")
           .inSql("manager_id","select id from user where name like '王%'");
    List<User> userList = userMapper.selectList(wrapper);
    userList.forEach(System.out::println);
}
```

**2.姓王并且（年龄小于40或邮箱不为空）**

SELECT id,name,age,email,manager_id,create_time FROM user WHERE name LIKE ? AND ( age < ? OR email IS NOT NULL ) 

```
@Test
public void select(){
	QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.likeRight("name","王").and(qw->qw.lt("age",40).or().isNotNull("email"));
    List<User> userList = userMapper.selectList(wrapper);
    userList.forEach(System.out::println);
    }
```

**2.（年龄小于40或邮箱不为空）并且姓王**

SELECT id,name,age,email,manager_id,create_time FROM user WHERE ( age < ? OR email IS NOT NULL ) AND name LIKE ? 

```
wrapper.nested(qw->qw.lt("age",40).or().isNotNull("email")).likeRight("name","王");
```

**3.年龄为30，31，34，35**

SELECT id,name,age,email,manager_id,create_time FROM user WHERE ( age < ? OR email IS NOT NULL ) AND name LIKE ? 

```
wrapper.in("age", Arrays.asList(30, 31, 34, 35));
```

**3.只查询id,name两列**

```
wrapper.select("id","name");
```

**4.排除create_time,manager_id两列**

```
wrapper.select(User.class,info->!info.getColumn().equals("create_time")&&!info.getColumn().equals("manager_id"));
```

**5.动态sql,当字段不为空时拼装where子句**

```
wrapper.likeRight(StringUtils.isNotBlank(name),"name",name)
                .like(StringUtils.isNotBlank(email),"email",email);
```

**6.实体类构造where子句，查询name不等于"boss"**

SELECT id,name,age,email,manager_id,create_time FROM user WHERE name<>? 

```
@TableField(condition = SqlCondition.NOT_EQUAL)
private String name;

User whereUser = new User();
whereUser.setName("boss");
QueryWrapper<User> wrapper = new QueryWrapper<>(whereUser);
```

**7.map构造where子句**

SELECT id,name,age,email,manager_id,create_time FROM user WHERE name<>? 

```
public void select() {
	Map<String,Object> map = new HashMap<>();
    map.put("name","zhang");
    map.put("age",25);
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.allEq(map);
    List<User> userList = userMapper.selectList(wrapper);
    userList.forEach(System.out::println);
}
```

##### 8.按照直属上级分组，查询每组的平均年龄，最大年龄最小年龄，并且只取年龄总和小于500的组

SELECT avg(age) avg_age,max(age) max_age,min(age) min_age FROM user GROUP BY manager_id HAVING sum(age)<? 

```
public void select() {
	Map<String,Object> map = new HashMap<>();
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.select("avg(age) avg_age","max(age) max_age","min(age) min_age")
    	.groupBy("manager_id").having("sum(age)<{0}",500);
    List<Map<String, Object>> maps = userMapper.selectMaps(wrapper);
    System.out.println(maps);
}
```

