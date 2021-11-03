package com.jt.controller;

import com.jt.pojo.Rights;
import com.jt.service.RightsService;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/rights")
public class RightsController {

    @Autowired
    private RightsService rightsService;

    @GetMapping("/findAll")
    public List<Rights> findAll(){

        return rightsService.findAll();
    }

    /**
     * 查询左侧菜单列表.一级套二级的结构
     * URL: /rights/getRightsList
     * 参数: 没有参数
     * 返回值: SysResult(list)
     */
    @GetMapping("/getRightsList")
    public SysResult getRightsList(){

        List<Rights> rightsList = rightsService.getRightsList();
        return SysResult.success(rightsList);
    }
}
