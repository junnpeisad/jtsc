package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author 刘昱江
 * 时间 2021/5/11
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userMapper.selectList(null);
    }

    /**
     * 思路:
     *      1.将密码进行加密处理
     *      2.根据username/password查询数据库
     *      3.有值:
     *          登录成功,返回秘钥
     *        没有值:
     *          登录失败,返回null
     * @param user
     * @return
     */
    @Override
    public String login(User user) {
        //1.获取明文
        String password = user.getPassword();
        //2.加密处理
        String md5 = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(md5);
        System.out.println(md5);
        //3.查询数据库
        QueryWrapper<User> queryWrapper= new QueryWrapper<>(user);
        //4.获取数据库对象
        User userDB = userMapper.selectOne(queryWrapper);
        //5.判断登录是否正确
        if(userDB == null){
            return null;
        }

        //6.使用UUID动态生成TOKEN,根据当前时间毫秒数+随机数利用hash算法生成
        //  几乎可以保证不重复.
        String token = UUID.randomUUID().toString()
                .replace("-","");
        return token;
    }

    /*
    * 业务说明: 利用MP方式查询数据库.
    * 步骤梳理:
    *       1.构建MP的分页对象
    *       2.根据分页对象查询数据.
    *       3.从分页对象中获取数据
    *       4.封装PageResult对象
    *       5.编辑配置类 封装分页拦截器
    * */
    @Override
    public PageResult getUserList(PageResult pageResult) {
        //1.定义分页对象
        IPage<User> page = new Page<>(pageResult.getPageNum(),
                                      pageResult.getPageSize());
        //2.定义条件构造器 指定动态查询Sql
        boolean flag = StringUtils.hasLength(pageResult.getQuery());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(flag, "username",pageResult.getQuery());

        //3.进行分页查询
        page = userMapper.selectPage(page,queryWrapper);
        //4.从封装后的分页对象中获取数据
        pageResult.setTotal(page.getTotal())
                  .setRows(page.getRecords());
        return pageResult;
    }

    //规则: 根据对象中不为null的元素当作set条件,
    //      Id当作唯一where条件
    //update user set status=true where id=xxx
    @Override
    @Transactional
    public void updateStatus(User user) {//id/status

        userMapper.updateById(user);
    }

    /**
     * 说明:
     *      1.用户入库操作需要手动补齐的数据有
     *        创建时间/修改时间 保证一致.
     *        status=true 手动填充.
     *      2.密码加密处理  新增和登录加密算法必须一致
     * @param user
     */
    @Override
    @Transactional
    public void addUser(User user) {
        String password = user.getPassword();
        //加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(password)
            .setStatus(true);
        userMapper.insert(user);
    }

    @Override
    public User getUserById(Integer id) {

        return userMapper.selectById(id);
    }

    //不为null的元素当作set条件,Id当作唯一where条件
    @Override
    @Transactional
    public void updateUser(User user) {

        userMapper.updateById(user);
    }

    /**
     * @Transactional规则说明:
     *      1.如果发生运行时异常时,则实现事务回滚.
     *      2.如图发生编译异常(检查异常),默认规则事务不回滚.
     *      3.属性说明:
     *           noRollbackFor = {} 遇到某种异常不回滚
     *           rollbackFor = {}   遇到某种异常回滚.
     * @param id
     */
    @Override
    @Transactional
    public void deleteUserById(Integer id) {
        userMapper.deleteById(id);
    }


    /**
     * 分页Sql:
     *   语法: select * from user limit 起始位置,每页条数
     *   规则: 数组 含头不含尾
     *   查询第一页:
     *         select * from user limit 0,10
     *   查询第二页:
     *         select * from user limit 10,10
     *   查询第三页:
     *         select * from user limit 20,10
     *   查询第N页:
     *         select * from user limit (页数-1)条数,条数
     * @param pageResult
     *   方式1:  手写Sql
     *   方式2:  MP的方式实现
     * @return
     */
   /* @Override
    public PageResult getUserList(PageResult pageResult) {
        //1.获取总记录数  Integer--long 自动转化
        long total = userMapper.selectCount(null);
        //2.获取分页结果
        int size = pageResult.getPageSize();
        int start = (pageResult.getPageNum() - 1) * size;
        List<User> userList = userMapper.findListByPage(start,size);
        pageResult.setTotal(total)
                  .setRows(userList);
        return pageResult;
    }*/
}
