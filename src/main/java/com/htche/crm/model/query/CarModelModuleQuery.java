package com.htche.crm.model.query;

import com.htche.crm.model.PageQuery;
import com.htche.crm.util.StringUtil;
import lombok.Data;

import java.util.List;

/**
 * Created by jankie on 2017/5/20.
 */
@Data
public class CarModelModuleQuery extends PageQuery {

    private int carModelId;

    private List<String> moduleIds;
}
