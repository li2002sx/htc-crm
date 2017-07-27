package com.htche.crm.model;

import lombok.Data;

import java.util.List;
import java.util.Set;

public class CxSelect {

    @Data
    public class CxSelectFirst {

        public Integer v;

        public String n;

        public List<CxSelectSecond> s;

        public Set<Integer> sIds;

    }

    @Data
    public class CxSelectSecond {

        public Integer v;

        public String n;

        public List<CxSelectThird> s;

    }

    @Data
    public class CxSelectThird {

        public Integer v;

        public String n;

    }
}
