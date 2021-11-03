package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemCatServiceImpl implements ItemCatService{

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public List<ItemCat> getAll() {

        return itemCatMapper.selectList(null);
    }

    /**
     * 1.数据结构:  Map<K,V>  key=parentId   value="List<ItemCat>"
     * 2.封装Map的数据类型
     * 3.如果level=1 只获取一级.
     * 4.如果level=2 获取一级,一级嵌套二级
     * 5.如果level=3 获取一级,一级嵌套二级,二级嵌套三级.
     * @param level
     * @return
     */
    @Override
    public List<ItemCat> findItemCatList(Integer level) {
        long startTime = System.currentTimeMillis();
        //1.封装Map集合
        Map<Integer,List<ItemCat>> map = getMap();

        //2.判断level的值
        if(level == 1){
            return map.get(0);
        }

        if(level == 2){

            return getTwoList(map);
        }

        //如果level不是1-2级则一定是三级
        List<ItemCat> list = getThreeList(map);
        long endTime = System.currentTimeMillis();
        System.out.println("耗时:"+(endTime - startTime)+"毫秒");
        return list;
    }

    @Override
    @Transactional //事务控制
    public void updateStatus(ItemCat itemCat) {

        itemCatMapper.updateById(itemCat);
    }

    @Override
    @Transactional
    public void saveItemCat(ItemCat itemCat) {
        itemCat.setStatus(true) ;
        itemCatMapper.insert(itemCat);
    }

    @Override
    @Transactional
    public void updateItemCat(ItemCat itemCat) {

        itemCatMapper.updateById(itemCat);
    }

    /**
     * 需求: 删除商品分类信息
     * 条件: 如果有子级,应该先删除子级.
     * Sql:
     *  DELETE FROM item_cat WHERE (parent_id IN (?,?) OR parent_id = ? OR id = ?)
     * @param itemCat
     */
    @Override
    @Transactional
    public void deleteItemCats(ItemCat itemCat) {
        int level = itemCat.getLevel();
        if(level == 3){
            //表示需要删除的数据是三级菜单,可以直接删除
            itemCatMapper.deleteById(itemCat.getId());
        }

        if(level == 2){
            QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id",itemCat.getId())
                        .or()
                        .eq("id",itemCat.getId());
            itemCatMapper.delete(queryWrapper);
        }

        if(level == 1){
            //1.必须获取二级ID
            QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id",itemCat.getId());
            //2获取结果的第一列字段(主键)  二级Id
            List twoIdsList = itemCatMapper.selectObjs(queryWrapper);

            //3.删除三级的数据
            queryWrapper.clear();
            //删除parent_id中包含二级Id的数据,实则删除的是三级数据
            queryWrapper.in(twoIdsList.size()>0,"parent_id",twoIdsList)
                        .or()
                        //删除parent_id 等于一级ID的,实则删除的是二级数据
                        .eq("parent_id",itemCat.getId())
                        .or()
                        //删除id=一级Id  则删除一级数据.
                        .eq("id",itemCat.getId() );
            itemCatMapper.delete(queryWrapper);
        }
    }

    private List<ItemCat> getThreeList(Map<Integer, List<ItemCat>> map) {
        //获取一级和二级
        List<ItemCat> oneList = getTwoList(map);
        //封装三级,遍历二级菜单,之后封装.
        for(ItemCat oneItemCat : oneList){
            //获取二级集合
            List<ItemCat> twoList = oneItemCat.getChildren();
            if(twoList == null || twoList.size() == 0){
                System.out.println("执行跳过循环操作");
                //由于业务数据不合理,跳过本次循环,执行下一次
                continue;
            }
            for (ItemCat twoItemCat : twoList){
                //查询三级列表,需要parentId=二级Id
                int parentId = twoItemCat.getId();
                List<ItemCat> threeList = map.get(parentId);
                twoItemCat.setChildren(threeList);
            }
        }
        return oneList;
    }

    private List<ItemCat> getTwoList(Map<Integer, List<ItemCat>> map) {
        //1.先获取一级列表
        List<ItemCat> oneList = map.get(0);
        //2.根据一级查询二级
        for(ItemCat oneItemCat :oneList){
            //查询二级,所以parentId是一级的Id
            int parentId = oneItemCat.getId();
            List<ItemCat> twoList = map.get(parentId);
            //封装数据
            oneItemCat.setChildren(twoList);
        }
        return oneList;
    }

    /**
     * 1.查询所有的商品分类列表.   查询一次数据库.
     * 2.循环遍历所有的数据,按照parentId,List<ItemCat>方式封装数据.
     * @return
     */
    private Map<Integer, List<ItemCat>> getMap() {
        Map<Integer,List<ItemCat>> map = new HashMap<>();
        List<ItemCat> list = itemCatMapper.selectList(null);
        for(ItemCat itemCat : list){
            //获取parentId
            int parentId = itemCat.getParentId();
            if(map.containsKey(parentId)){
                //key存在
                map.get(parentId).add(itemCat);
            }else{
                //key不存在
                List<ItemCat> childrenList = new ArrayList<>();
                childrenList.add(itemCat);
                //将第一个元素封装到map中
                map.put(parentId,childrenList);
            }
        }
        return map;
    }


    /**
     * 1.查询所有的一级菜单
     * 2.遍历一级查询所有的二级菜单
     * 3.遍历二级查询所有的三级菜单
     * 4. 2级封装3级菜单, 1级封装2级菜单
     * @param level
     * @return
     */
    /*@Override
    public List<ItemCat> findItemCatList(Integer level) {
        //记录开始时间
        long startTime = System.currentTimeMillis();

        QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",0);
        List<ItemCat> oneList = itemCatMapper.selectList(queryWrapper);
        //2.遍历一级菜单 根据一级查询二级菜单
        for (ItemCat oneItemCat : oneList){
            queryWrapper.clear();
            queryWrapper.eq("parent_id",oneItemCat.getId());
            List<ItemCat> twoList = itemCatMapper.selectList(queryWrapper);

            //3.将二级列表进行遍历,根据二级ID查询三级
            for(ItemCat twoItemCat : twoList){
                queryWrapper.clear();
                queryWrapper.eq("parent_id",twoItemCat.getId());
                List<ItemCat> threeList = itemCatMapper.selectList(queryWrapper);
                //数据的封装
                twoItemCat.setChildren(threeList);
            }
            //将二级数据封装到一级中
            oneItemCat.setChildren(twoList);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("程序耗时:"+(endTime - startTime));
        return oneList;
    }*/
}
