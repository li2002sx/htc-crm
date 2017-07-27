package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.domain.RechargeProduct;
import com.htche.crm.mapper.RechargeProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class RechargeProductBiz {

    @Autowired
    RechargeProductMapper rechargeProductMapper;

    /**
     * 列表
     *
     * @return
     */
    public List<RechargeProduct> selectAllList() {
        List<RechargeProduct> rechargeProducts = rechargeProductMapper.selectAllList();
        return rechargeProducts;
    }

    public RechargeProduct selectByPrimaryKey(int rechargeProductId) {
        return rechargeProductMapper.selectByPrimaryKey(rechargeProductId);
    }

    public RechargeProduct selectByPrice(Integer price) {
        return rechargeProductMapper.selectByPrice(price);
    }
}
