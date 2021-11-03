package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 刘昱江
 * 时间 2021/2/2
 * Linux系统: 严格区分大小写!!
 * 注意:类中必须写表名 防止大小写问题导致异常
 */
@TableName("user")
@Data
@Accessors(chain = true)
public class User extends BasePojo{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Boolean status;
    @TableField(exist = false)  //该属性不存在
    private Role role;  //定义role角色数据
}
