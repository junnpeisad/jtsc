package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.ItemVO;
import com.jt.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemDescMapper itemDescMapper;


    @Override
    public List<Item> findAll() {
        return itemMapper.selectList(null);
    }

    /**
     * 需求: 实现商品分页查询
     * @param pageResult
     * @return
     */
    @Override
    public PageResult getItemList(PageResult pageResult) {

        IPage page = new Page(pageResult.getPageNum(),pageResult.getPageSize());
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        //判断条件: 用户传递query 则添加where条件
        String query = pageResult.getQuery();
        boolean flag = StringUtils.hasLength(query);
        queryWrapper.like(flag,"title",query);

        //page接口原来只有2个,经过分页查询之后,有四个结果
        page = itemMapper.selectPage(page,queryWrapper);
        long total = page.getTotal();
        List<Item> rows = page.getRecords();
        return pageResult.setTotal(total).setRows(rows);
    }

    //实现商品入库操作
    @Override
    @Transactional
    public void saveItem(ItemVO itemVO) {
        //item 主键自增 数据库入库之后,才会有主键!!!
        Item item = itemVO.getItem();
        item.setStatus(true);
        itemMapper.insert(item);
        //问题: 入库之后才有ID,现阶段item.id=null
        //mybatis实现业务功能,自动回显主键!!!
        //MP自动的将主键的回显功能实现!!!

        //规则: itemId与ItemDescId是一样的.
        ItemDesc itemDesc = itemVO.getItemDesc();
        itemDesc.setId(item.getId());
        itemDescMapper.insert(itemDesc);
    }

    @Override
    public void updateStatus(Item item) {

        itemMapper.updateById(item);
    }
}
