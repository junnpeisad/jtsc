package com.jt.aop;

import com.jt.vo.SysResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 注解的作用:
 *   1.该注解只拦截Controller层抛出的异常信息!
 *      controller ---Service-----Mapper 异常向上抛出.
 *   2.需要配合指定异常的类型.
 */
@RestControllerAdvice
public class SystemAOP {

    //当前Controller层,只拦截运行时异常.
    //@ExceptionHandler({RuntimeException.class, SQLException.class})
    @ExceptionHandler({RuntimeException.class})
    public SysResult exception(Exception e){
        //控制台打印异常.
        e.printStackTrace();
        return SysResult.fail();
    }
}
