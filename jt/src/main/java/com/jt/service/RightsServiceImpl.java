package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.RightsMapper;
import com.jt.pojo.Rights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RightsServiceImpl implements RightsService{

    @Autowired
    private RightsMapper rightsMapper;


    @Override
    public List<Rights> findAll() {

        return rightsMapper.selectList(null);
    }

    /**
     * 实现思路:
     *   1.先查询一级菜单信息 parent_id = 0
     *   2.将一级菜单循环遍历 一级菜单对象.
     *   3.根据一级菜单信息,查询当前菜单下的二级.
     *   4.将查询得到的二级菜单,封装到一级对象中
     * 实现思路二(扩展):
     *    利用左连接 实现关联查询 封装数据.
     * @return
     */
    @Override
    public List<Rights> getRightsList() {
        //1.查询一级列表信息
        QueryWrapper<Rights> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",0);
        List<Rights> oneList = rightsMapper.selectList(queryWrapper);
        //2.遍历一级列表
        for (Rights oneRights : oneList){
            //根据一级查询二级
            queryWrapper.clear(); //清除之前的条件
            queryWrapper.eq("parent_id",oneRights.getId());
            List<Rights> twoList = rightsMapper.selectList(queryWrapper);
            //将查询的结果封装一级对象中
            oneRights.setChildren(twoList);
        }
        return oneList;
    }
}
