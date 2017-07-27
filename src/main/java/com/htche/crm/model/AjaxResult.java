package com.htche.crm.model;

import lombok.Data;

@Data
public class AjaxResult {
    private boolean success;
    private Object value;
    private String error;
}
