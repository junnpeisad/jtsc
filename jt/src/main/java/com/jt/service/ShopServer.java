package com.jt.service;
import com.jt.pojo.Shop;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ShopServer {

    List<Shop> getList();
}
