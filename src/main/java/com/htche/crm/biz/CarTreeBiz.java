package com.htche.crm.biz;

import com.htche.crm.domain.CarTree;
import com.htche.crm.domain.Region;
import com.htche.crm.mapper.CarTreeMapper;
import com.htche.crm.model.CxSelect;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by jankie on 16/6/25.
 */
@Component
public class CarTreeBiz {

    @Autowired
    CarTreeMapper carTreeMapper;

    /**
     * 保存
     *
     * @param carTree
     * @return
     */
    public boolean save(CarTree carTree) {
        int effectCount = 0;
        int id = carTree.getId();
        CarTree carTreeRow = this.selectByPrimaryKey(id);
        if (carTreeRow != null) {
            effectCount = carTreeMapper.updateByPrimaryKey(carTree);
        } else {
            effectCount = carTreeMapper.insertSelective(carTree);
        }
        return effectCount > 0;
    }

    /**
     * 列表
     *
     * @return
     */
    public List<CarTree> selectListByPids(List<Integer> pIds) {
        List<CarTree> carTrees = new ArrayList<>();
        if (pIds != null && pIds.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("pIds", pIds);
            carTrees = carTreeMapper.selectListByPids(map);
        }
        return carTrees;
    }

    public CarTree selectByPrimaryKey(int carTreeId) {
        return carTreeMapper.selectByPrimaryKey(carTreeId);
    }

    public List<CarTree> selectAllList() {
        return carTreeMapper.selectAllList();
    }

    public List<CxSelect.CxSelectFirst> getCarTreeList() {

        List<CxSelect.CxSelectFirst> firsts = new ArrayList<CxSelect.CxSelectFirst>();

        //获取品牌
        List<Integer> bids = new ArrayList<>();
        List<CarTree> carTrees = this.selectListByPids(Arrays.asList(0));
        carTrees.forEach(item -> {

            int regionId = item.getId();
            int pid = item.getPId();
            String title = item.getName();

            CxSelect.CxSelectFirst cxSelectFirst = null;

            cxSelectFirst = firsts.stream().filter(f -> f.getV() == regionId).findFirst().orElse(null);
            if (cxSelectFirst == null) {
                CxSelect.CxSelectFirst first = new CxSelect().new CxSelectFirst();
                first.setV(regionId);
                first.setN(title);
                Set<Integer> sIds = new HashSet<>();
                first.setSIds(sIds);
                firsts.add(first);
            }

            bids.add(regionId);
        });

        List<Integer> sids = new ArrayList<>();
        carTrees = this.selectListByPids(bids);
        carTrees.forEach(item -> {
            int regionId = item.getId();
            int pid = item.getPId();
            String title = item.getName();

            CxSelect.CxSelectFirst cxSelectFirst = null;

            cxSelectFirst = firsts.stream().filter(f -> f.getV() == pid).findFirst().orElse(null);
            if (cxSelectFirst != null) {
                if (cxSelectFirst.getS() == null) {
                    List<CxSelect.CxSelectSecond> cxSelectSeconds = new ArrayList<>();
                    cxSelectFirst.setS(cxSelectSeconds);
                }
                CxSelect.CxSelectSecond second = new CxSelect().new CxSelectSecond();
                second.setV(regionId);
                second.setN(title);
                cxSelectFirst.getS().add(second);
                cxSelectFirst.getSIds().add(regionId);
            }

            sids.add(regionId);
        });

        carTrees = this.selectListByPids(sids);
        carTrees.forEach(item -> {

            int regionId = item.getId();
            int pid = item.getPId();
            String title = item.getName();

            CxSelect.CxSelectFirst cxSelectFirst = null;

            cxSelectFirst = firsts.stream().filter(f -> f.getSIds().contains(pid)).findFirst().orElse(null);
            if (cxSelectFirst != null && cxSelectFirst.getS() != null) {
                CxSelect.CxSelectSecond second = cxSelectFirst.getS().stream().filter(s -> s.getV() == pid).findFirst().orElse(null);
                if (second != null) {
                    if (second.getS() == null) {
                        List<CxSelect.CxSelectThird> cxSelectThirds = new ArrayList<>();
                        second.setS(cxSelectThirds);
                    }
                    CxSelect.CxSelectThird third = new CxSelect().new CxSelectThird();
                    third.setV(regionId);
                    third.setN(title);
                    second.getS().add(third);
                }
            }
        });

        return firsts;
    }

    public Map<Integer, String> getCarTreeMap() {

        Map<Integer, String> map = new HashMap<>();
        List<CarTree> carTrees = this.selectAllList();
        carTrees.forEach(item -> {
            map.put(item.getId(), item.getName());
        });
        return map;
    }

    public Integer selectMaxId() {
        return carTreeMapper.selectMaxId();
    }

    public boolean updateStatus(int id, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("status", status);
        return carTreeMapper.updateStatus(map) > 0;
    }
}
