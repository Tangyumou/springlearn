package cn.springlearn.mybatis_plus.test;

import cn.springlearn.mybatis_plus.dao.UserMapper;
import cn.springlearn.mybatis_plus.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
public class SimpleTest {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void select(){
        User user = new User();
        user.setName("唐秦伟");
        user.setAge(25);
        user.setCreateTime(LocalDateTime.now());
        user.setManagerId(1088248166370832385L);
        int rows = userMapper.insert(user);
        System.out.println(rows);
    }
}
