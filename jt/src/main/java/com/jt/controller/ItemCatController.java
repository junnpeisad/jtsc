package com.jt.controller;

import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/itemCat")
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @GetMapping("/getAll")
    public List<ItemCat> getAll(){

        return itemCatService.getAll();
    }

    /**
     * 业务说明: 实现商品分类查询
     * URL:  /itemCat/findItemCatList/{level}
     * 参数:  level 查询的层级
     * 返回值: SysResult(List)
     */
    @GetMapping("/findItemCatList/{level}")
    public SysResult findItemCatList(@PathVariable Integer level){

        List<ItemCat> itemCatList = itemCatService.findItemCatList(level);
        return SysResult.success(itemCatList);
    }

    /**
     * 业务需求: ItemCat状态修改
     * URL地址: /itemCat/status/{id}/{status}
     * 参数:    id/status
     * 返回值:  SysResult对象
     */
    @PutMapping("/status/{id}/{status}")
    public SysResult updateStatus(ItemCat itemCat){

        itemCatService.updateStatus(itemCat);
        return SysResult.success();
    }

    /**
     * 业务分析: 完成商品分类新增
     * 请求类型: POST请求
     * URL: /itemCat/saveItemCat
     * 参数: 利用ItemCat对象接收 JSON
     * 返回值: SysResult对象
     */
    @PostMapping("/saveItemCat")
    public SysResult saveItemCat(@RequestBody ItemCat itemCat){

        itemCatService.saveItemCat(itemCat);
        return SysResult.success();
    }

    /**
     * 需求: 实现商品分类修改操作
     * URL: /itemCat/updateItemCat
     * 参数: form表单  json  对象接收
     * 返回值: SysResult对象
     */
    @PutMapping("/updateItemCat")
    public SysResult updateItemCat(@RequestBody ItemCat itemCat){

        itemCatService.updateItemCat(itemCat);
        return SysResult.success();
    }

    /**
     * 业务需求: 实现商品分类删除
     * URL: /itemCat/deleteItemCat?id=xx&level=2
     * 参数: id/level
     * 返回值: SysResult对象
     */
    @DeleteMapping("/deleteItemCat")
    public SysResult deleteItemCats(ItemCat itemCat){

        itemCatService.deleteItemCats(itemCat);
        return SysResult.success();
    }

}
