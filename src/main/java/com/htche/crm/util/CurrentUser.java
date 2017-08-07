package com.htche.crm.util;

import com.htche.crm.biz.UserBiz;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.domain.AdminUser;
import com.htche.crm.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by jankie on 16/5/26.
 */
public class CurrentUser {

    protected static final Logger logger = LoggerFactory.getLogger(CurrentUser.class);

    public static CurrentUser INSTANCE = null;

    private static UserBiz userBiz;

    private CurrentUser() {

    }

    /**
     * 获取实例
     *
     * @return
     */
    public static CurrentUser getInstance() {
        if (INSTANCE == null) {
            synchronized (CurrentUser.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CurrentUser();
                }
            }
        }

        return INSTANCE;
    }

    /**
     * 获取当前session里面放置的AdminUser对象
     *
     * @return
     */
    public AdminUser getAdminUser() {
        Subject currentUser = SecurityUtils.getSubject();
        AdminUser adminUser = (AdminUser) currentUser.getSession().getAttribute("adminUser");
        return adminUser;
    }

    private UserBiz getUserBiz() {
        if (userBiz == null) {
            logger.info("UserFacade is null, try to load it from IoC");
            userBiz = (UserBiz) SpringContextUtil.getBean("userBiz");

            if (userBiz == null) {
                logger.info("can't load UserFacade instance from IoC, so try to create remote proxy");
            }
        }
        return userBiz;
    }

    /**
     * 获取当前session里面放置的User对象
     *
     * @return
     */
    public User getUser() {
        User user = null;
        String token = this.getRequest().getHeader("token");
        if (StringUtils.isNotEmpty(token)) {
            //使用request对象的getSession()获取session，如果session不存在则创建一个
            HttpSession session = this.getRequest().getSession();
            if (session.getAttribute(token) == null) {
                user = this.getUserBiz().selectByToken(token);
                if (user != null) {
                    if (user.getStatus() == CommonStatus.Normal.getIndex()) {
                        session.setAttribute(token, user);
                    } else {
                        throw new RuntimeException("该账户已被禁用");
                    }
                }
            } else {
                user = (User) session.getAttribute(token);
            }
        } else {
            throw new RuntimeException("Token不能为空");
        }
        return user;
    }

    /**
     * 获取当前Request
     *
     * @return
     */
    public HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return requestAttributes.getRequest();
    }

    /**
     * 获取当前Response
     *
     * @return
     */
    public HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return requestAttributes.getResponse();
    }
}
