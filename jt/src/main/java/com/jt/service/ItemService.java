package com.jt.service;

import com.jt.pojo.Item;
import com.jt.vo.ItemVO;
import com.jt.vo.PageResult;

import java.util.List;

public interface ItemService {
    List<Item> findAll();

    PageResult getItemList(PageResult pageResult);

    void saveItem(ItemVO itemVO);

    void updateStatus(Item item);
}
