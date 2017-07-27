package com.htche.crm.mapper;

import com.htche.crm.domain.RechargeProduct;

import java.util.List;

public interface RechargeProductMapper {

    RechargeProduct selectByPrimaryKey(Integer productId);

    List<RechargeProduct> selectAllList();

    RechargeProduct selectByPrice(Integer price);
}