package cn.springlearn.mybatis_plus.test;

import cn.springlearn.mybatis_plus.dao.UserMapper;
import cn.springlearn.mybatis_plus.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Wrapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
public class SimpleTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void select() {
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("avg(age) avg_age","max(age) max_age","min(age) min_age")
            .groupBy("manager_id").having("sum(age)<{0}",500);
        List<Map<String, Object>> maps = userMapper.selectMaps(wrapper);
        System.out.println(maps);
    }

    /**
     * 获得查询结果中的id
     */
    @Test
    public void selectObjs() {
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lt("age",30);
        List<Object> maps = userMapper.selectObjs(wrapper);
        System.out.println(maps);
    }
    @Test
    /**
     * sql语句和wrapper结合
     */
    public void selectBySql() {
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lt("age",30);
        List<User> userList = userMapper.selectAll(wrapper);
        userList.forEach(System.out::println);
    }
    @Test
    /**
     * sql语句和wrapper结合
     */
    public void selectByPage() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.gt("age",20);
        Page<User> page = new Page<>(1,2);
        IPage<Map<String, Object>> iPage = userMapper.selectMapsPage(page, wrapper);
        System.out.println("总页数:"+iPage.getPages());
        System.out.println("总记录数:"+iPage.getTotal());
        List<Map<String, Object>> userList = iPage.getRecords();
        userList.forEach(System.out::println);
    }
}
