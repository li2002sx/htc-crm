package com.htche.crm.biz;

import com.htche.crm.domain.CarTree;
import com.htche.crm.domain.Region;
import com.htche.crm.mapper.RegionMapper;
import com.htche.crm.model.CxSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class RegionBiz {

    @Autowired
    RegionMapper regionMapper;

    public List<CxSelect.CxSelectFirst> getRegionList() {

        List<CxSelect.CxSelectFirst> firsts = new ArrayList<CxSelect.CxSelectFirst>();

        List<Region> regions = regionMapper.selectAllList();
        if (regions != null) {

            regions.forEach(region -> {

                int regionId = region.getRegionId();
                int pid = region.getPid();
                int depth = region.getDepth();
                String title = region.getTitle();

                CxSelect.CxSelectFirst cxSelectFirst = null;

                if (depth == 0) { //一级栏目
                    cxSelectFirst = firsts.stream().filter(f -> f.getV() == regionId).findFirst().orElse(null);
                    if (cxSelectFirst == null) {
                        CxSelect.CxSelectFirst first = new CxSelect().new CxSelectFirst();
                        first.setV(regionId);
                        first.setN(title);
                        Set<Integer> sIds = new HashSet<>();
                        first.setSIds(sIds);
                        firsts.add(first);
                    }
                } else if (depth == 1) {//二级栏目
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
                } else if (depth == 2) {//三级栏目
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
                }
            });
        }

        return firsts;
    }

    public Map<Integer, String> getRegionMap() {

        Map<Integer, String> map = new HashMap<>();
        List<Region> regions = regionMapper.selectAllList();
        regions.forEach(item -> {
            map.put(item.getRegionId(), item.getTitle());
        });
        return map;
    }

    public List<Region> selectListByDepth(int depth) {
        return regionMapper.selectListByDepth(depth);
    }
}
