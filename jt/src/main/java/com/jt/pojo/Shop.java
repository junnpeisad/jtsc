package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("shop")
@Data
@Accessors(chain = true)
public class Shop{
    private String name;
    private String key1;
    private String icon;
    private String cat;
}
