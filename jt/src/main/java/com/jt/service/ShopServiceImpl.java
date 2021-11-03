package com.jt.service;

import com.jt.mapper.ShopMapper;
import com.jt.pojo.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ShopServiceImpl implements ShopServer{
    @Autowired
    private ShopMapper shopMapper;
    @Override
    public List<Shop> getList() {
        return shopMapper.selectList(null);
    }
}
