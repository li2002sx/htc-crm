package com.htche.crm.model.query;

import com.htche.crm.model.PageQuery;
import lombok.Data;

/**
 * Created by jankie on 2017/5/20.
 */
@Data
public class UserQuery extends PageQuery {

    public String realName;

    public String mobile;

}