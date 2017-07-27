package com.htche.crm.util;

import com.htche.crm.domain.AdminUser;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.*;

/**
 * 通用视图结果。
 */
public class ViewResult<T extends ViewResult<T>> extends ModelAndView {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    private Map<String, Object> jsVars;

    public ViewResult() {
        this.init();
    }

    public ViewResult(String viewName) {
        super(viewName);
        this.init();
    }

    public ViewResult(View view) {
        super(view);
        this.init();
    }

    public ViewResult(String viewName, Map<String, ?> model) {
        super(viewName, model);
        this.init();
    }

    public ViewResult(View view, Map<String, ?> model) {
        super(view, model);
        this.init();
    }

    public ViewResult(String viewName, String modelName, Object modelObject) {
        super(viewName, modelName, modelObject);
        this.init();
    }

    public ViewResult(View view, String modelName, Object modelObject) {
        super(view, modelName, modelObject);
        this.init();
    }

    protected void init() {
        Subject currentUser = SecurityUtils.getSubject();
        AdminUser adminUser = (AdminUser)currentUser.getSession().getAttribute("adminUser");
        if(adminUser == null) adminUser = new AdminUser();
        this.addObject("identity", adminUser);
    }

    /**
     * 引入静态类
     *
     * @param clazz
     */
    public T addObject(Class clazz) {
        super.addObject(clazz.getSimpleName(), clazz);
        return (T) this;
    }

    @Override
    public T addObject(String attributeName, Object attributeValue) {
        super.addObject(attributeName, attributeValue);
        return (T) this;
    }
}

