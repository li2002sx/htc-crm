package com.htche.crm.model.query;

import com.htche.crm.model.PageQuery;
import lombok.Data;

/**
 * Created by jankie on 2017/5/20.
 */
@Data
public class AllyDealerQuery extends PageQuery {

    public String companyName;

    public String name;
}
