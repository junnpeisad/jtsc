package com.jt.controller;

import com.jt.pojo.Item;
import com.jt.service.ItemService;
import com.jt.vo.ItemVO;
import com.jt.vo.PageResult;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@CrossOrigin
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/findAll")
    public List<Item> findAll(){

        return itemService.findAll();
    }

    /**
     * 需求: 查询商品列表 分页查询
     * URL:  /item/getItemList?query=&pageNum=1&pageSize=10
     * 参数:  pageResult
     * 返回值: SysResult(pageResult)
     */
    @GetMapping("/getItemList")
    public SysResult getItemList(PageResult pageResult){//3

        //参照user模块完成商品分页查询.
        pageResult = itemService.getItemList(pageResult);//+2
        return SysResult.success(pageResult);
    }

    /**
     * 业务分析: 实现商品新增操作
     * URL地址: http://localhost:8091/item/saveItem
     * 参数: ItemVO的JSON串 {item,itemDesc}
     * 返回值: SysResult对象
     */
    @PostMapping("/saveItem")
    public SysResult saveItem(@RequestBody ItemVO itemVO){

        itemService.saveItem(itemVO);
        return SysResult.success();
    }


    /**
     * 需求: 实现商品状态修改
     * URL:   /item/updateItemStatus
     * 参数:   {id,status} json串
     * 返回值: SysResult
     * 问题: 什么时候使用restFul,什么时候使用get/put/delete请求方式?
     *
     */
    @PutMapping("/updateItemStatus")
    public SysResult updateStatus(@RequestBody Item item){

        itemService.updateStatus(item);
        return SysResult.success();
    }

}
