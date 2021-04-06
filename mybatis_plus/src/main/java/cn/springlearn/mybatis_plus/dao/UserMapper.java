package cn.springlearn.mybatis_plus.dao;

import cn.springlearn.mybatis_plus.entity.User;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import java.util.List;

@Mapper
@Component
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user ${ew.customSqlSegment}")
    List<User> selectAll(@Param(Constants.WRAPPER) Wrapper<User> wrapper);
}
