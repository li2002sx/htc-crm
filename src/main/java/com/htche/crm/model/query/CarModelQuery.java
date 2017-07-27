package com.htche.crm.model.query;

import com.htche.crm.model.PageQuery;
import lombok.Data;

/**
 * Created by jankie on 2017/5/20.
 */
@Data
public class CarModelQuery extends PageQuery {

    public Integer carModelId;

    public Integer specId;

    public Integer brandId;

    public String searchKey;

}
