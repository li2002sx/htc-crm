package com.htche.crm.controller.admin;

import com.htche.crm.biz.AdminUserBiz;
import com.htche.crm.biz.AllyDealerBiz;
import com.htche.crm.biz.CarDealerBiz;
import com.htche.crm.biz.CarOwnerBiz;
import com.htche.crm.domain.AdminUser;
import com.htche.crm.domain.CarDealer;
import com.htche.crm.util.CurrentUser;
import com.htche.crm.util.ImageUtil;
import com.htche.crm.util.ViewResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jankie on 16/5/1.
 */
@Controller
@RequestMapping("/")
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    AdminUserBiz adminUserBiz;

    @Autowired
    CarDealerBiz carDealerBiz;

    @Autowired
    AllyDealerBiz allyDealerBiz;

    @Autowired
    CarOwnerBiz carOwnerBiz;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "redirect:/welcome";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loginForm(@ModelAttribute("message") String message) {
        ModelAndView model = new ModelAndView("login2");
        model.addObject("msg", message);
        return model;
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public ModelAndView welcome() {
        ModelAndView model = new ViewResult("welcome");

        AdminUser adminUser = CurrentUser.getInstance().getAdminUser();
        int roleId = adminUser.getRoleId();

        int carDealerCount = carDealerBiz.selectCount();
        model.addObject("carDealerCount", carDealerCount);

        int allyDealerCount = allyDealerBiz.selectCount(roleId);
        model.addObject("allyDealerCount", allyDealerCount);

        int carOwnerCount = carOwnerBiz.selectCount(roleId);
        model.addObject("carOwnerCount", carOwnerCount);

        int monthBirthdayCount = carOwnerBiz.selectMonthBirthdayCount(roleId);
        model.addObject("monthBirthdayCount", monthBirthdayCount);

        int totalSmsCount = 0;
        int useSmsCount = 0;

        if (roleId > 0) {
            CarDealer carDealer = carDealerBiz.selectByPrimaryKey(roleId);
            totalSmsCount = carDealer.getSmsNum();
            useSmsCount = carDealer.getSendSmsNum();
        } else {
            totalSmsCount = carDealerBiz.selectSmsSum();
            useSmsCount = carDealerBiz.selectSendSmsSum();
        }
        model.addObject("totalSmsCount", totalSmsCount);
        model.addObject("useSmsCount", useSmsCount);

        return model;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String name, String password, boolean rememberMe
            , RedirectAttributes redirectAttributes
            , HttpServletRequest request) {

        String username = name;
        password = DigestUtils.md5Hex(password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        AdminUser adminUser = null;
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
//            logger.info("对用户[" + username + "]进行登录验证..验证开始");
            currentUser.login(token);
            adminUser = adminUserBiz.selectByName(name);
            adminUser.setUserPic(ImageUtil.getRealPicUrl(adminUser.getUserPic(), true));
            int roleId = adminUser.getRoleId();
            if (roleId > 0) {
                CarDealer carDealer = carDealerBiz.selectByPrimaryKey(roleId);
                if (carDealer != null) {
                    adminUser.setCompanyName(carDealer.getCompanyName());
                }
            }
            // 初始化功能栏

            Session session = currentUser.getSession();
            session.setAttribute("adminUser", adminUser);
//            logger.info("对用户[" + username + "]进行登录验证..验证通过");
            //更新最后登录时间
            adminUserBiz.updateLastLoginTime(adminUser.getAdminUserId());

        } catch (UnknownAccountException uae) {
//            logger.info("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
            redirectAttributes.addFlashAttribute("message", "未知账户");
        } catch (IncorrectCredentialsException ice) {
//            logger.info("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
            redirectAttributes.addFlashAttribute("message", "密码不正确");
        } catch (LockedAccountException lae) {
//            logger.info("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
            redirectAttributes.addFlashAttribute("message", "账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
//            logger.info("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
            redirectAttributes.addFlashAttribute("message", "用户名或密码错误次数过多");
        } catch (AuthenticationException ae) {
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
//            logger.info("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
//            ae.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");
        }

        //验证是否登录成功
        if (currentUser.isAuthenticated()) {
//            logger.info("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
            String redirect = "/welcome";
            SavedRequest saveRequest = WebUtils.getSavedRequest(request);
            if (saveRequest != null && saveRequest.getRequestUrl() != null) {
                String url = saveRequest.getRequestUrl();
                if (!StringUtils.isEmpty(url) && !url.endsWith(".js") && !url.endsWith(".css")) {
                    redirect = url;
                }
            }
            return "redirect:" + redirect;
        } else {
            token.clear();
            return "redirect:/login";
        }
    }

    @RequestMapping("403")
    public ModelAndView unauthorizedRole() {
        logger.info("------没有权限-------");
        ModelAndView model = new ViewResult("403");
        return model;
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(RedirectAttributes redirectAttributes) {
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
        SecurityUtils.getSubject().logout();
//        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return "redirect:/login";
    }

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public ModelAndView help() {
        ModelAndView model = new ViewResult("help");
        return model;
    }

//    @RequestMapping(value = "/message", method = RequestMethod.GET)
//    public ModelAndView message() {
//        ModelAndView model = new ModelAndView("message");
//        return model;
//    }
//
//    //当浏览器向服务器发送请求的时候通过@MessageMapping 映射/welcome 这个地址 类似 RequestMapping
//    @MessageMapping("/say")
//    //当服务器有消息的时候，会对订阅了@SendTo中的路径浏览器发送消息
//    @SendTo("/topic/getResponse")
//    public WiselyResponse say(WiselyMessage wiselyMessage) throws InterruptedException {
//        Thread.sleep(3000);
//        return new WiselyResponse("Welcome," + wiselyMessage.getName());
//    }
}
