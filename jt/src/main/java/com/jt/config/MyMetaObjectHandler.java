package com.jt.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component //将该对象交给Spring容器管理
public class MyMetaObjectHandler implements MetaObjectHandler {

    //完成新增入库操作应该如何填充 created/updated
    //MetaObject代表自动填充的默认配置.
    @Override
    public void insertFill(MetaObject metaObject) {
        Date date = new Date();
        this.setFieldValByName("created",date,metaObject);
        this.setFieldValByName("updated",date,metaObject);
    }

    //完成修改操作应该如何填充 updated
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updated",new Date(),metaObject);
    }
}
