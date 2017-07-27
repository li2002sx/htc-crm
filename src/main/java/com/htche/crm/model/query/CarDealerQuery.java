package com.htche.crm.model.query;

import com.htche.crm.model.PageQuery;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by jankie on 2017/5/20.
 */
@Data
public class CarDealerQuery extends PageQuery {

    public String companyName;

    public String name;

    public Integer provinceId;

    public Integer cityId;
}
