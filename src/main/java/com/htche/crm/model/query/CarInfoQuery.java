package com.htche.crm.model.query;

import com.htche.crm.model.PageQuery;
import lombok.Data;

/**
 * Created by jankie on 2017/5/20.
 */
@Data
public class CarInfoQuery extends PageQuery {

    public Integer specId;

    public Integer brandId;

    public Integer year;

    public Integer month;

    public Integer provinceId;

    public Integer cityId;

}
