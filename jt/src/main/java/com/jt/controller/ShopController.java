package com.jt.controller;

import com.jt.pojo.Shop;
import com.jt.service.ShopServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    private ShopServer shopServer;
    @GetMapping("/list")
   public List<Shop> getList(){
      return shopServer.getList();
   }
}
