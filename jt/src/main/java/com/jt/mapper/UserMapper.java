package com.jt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 刘昱江
 * 时间 2021/2/2
 */
public interface UserMapper extends BaseMapper<User> {
    //注解方式查询数据库! 只适用简单Sql!!! 注解/映射文件二选一
    @Select("select * from user limit #{start},#{size}")
    /*@Insert()
    @Update()
    @Delete()*/
    List<User> findListByPage(int start,int size);
}
