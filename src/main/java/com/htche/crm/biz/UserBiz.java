package com.htche.crm.biz;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.htche.crm.constants.CommonStatus;
import com.htche.crm.constants.SexType;
import com.htche.crm.constants.SmsType;
import com.htche.crm.domain.Sms;
import com.htche.crm.domain.User;
import com.htche.crm.mapper.UserMapper;
import com.htche.crm.model.query.UserQuery;
import com.htche.crm.util.RandUtil;
import com.htche.crm.util.ValidateUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class UserBiz {

    @Autowired
    UserMapper userMapper;

    @Autowired
    SmsBiz smsBiz;

    /**
     * 保存
     *
     * @param user
     * @return
     */
    public boolean save(User user) {
        int effectCount = 0;
        if (user.getUserId() > 0) {
            effectCount = userMapper.updateByPrimaryKeySelective(user);
        } else {
            effectCount = userMapper.insertSelective(user);
        }
        return effectCount > 0;
    }

    /**
     * 列表
     *
     * @return
     */
    public Page<User> selectAllList(UserQuery query) {
        Page<User> userPage = PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        Map<String, Object> map = new HashMap<>();
        map.put("realName", query.getRealName());
        map.put("mobile", query.getMobile());
        List<User> users = userMapper.selectAllList(map);
        return userPage;
    }

    public User selectByPrimaryKey(int userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    public boolean updateStatus(int userId, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("status", status);
        return userMapper.updateStatus(map) > 0;
    }

    public User selectByMobile(String mobile) {
        return userMapper.selectByMobile(mobile);
    }

    public User selectByToken(String token) {
        return userMapper.selectByToken(token);
    }

    public boolean updateLastLoginTime(int userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("lastLoginTime", new Date());
        return userMapper.updateLastLoginTime(map) > 0;
    }

    public int updateExpireTime(int userId, Date expireTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("expireTime", expireTime);
        return userMapper.updateExpireTime(map);
    }

    /**
     * 注册
     *
     * @param mobile
     * @param password
     * @param code
     * @return
     */
    public User regist(String mobile, String password, String code) {
        User user = null;
        if (ValidateUtil.isMobile(mobile)) {
            if (StringUtils.isNotEmpty(password) && password.length() >= 6) {
                if (StringUtils.isNotEmpty(code) && code.length() == 4) {
                    Sms sms = smsBiz.getMsgCode(mobile, SmsType.Regist.getIndex());
                    if (sms != null) {
                        if (sms.getContent().equals(code)) {
                            user = this.selectByMobile(mobile);
                            if (user == null) {
                                user = new User();
                                user.setUserId(0);
                                user.setRealName(mobile);
                                user.setMobile(mobile);
                                user.setPassword(DigestUtils.md5Hex(password));
                                user.setSex(SexType.Woman.getIndex());
                                user.setStatus(CommonStatus.Normal.getIndex());
                                user.setToken(RandUtil.getUUID());
                                user.setLastLoginTime(new Date());
                                user.setCreateTime(new Date());
                                boolean flag = this.save(user);
                                if (!flag) {
                                    throw new RuntimeException("用户注册失败");
                                }
                            } else {
                                throw new RuntimeException("该手机号已注册");
                            }
                        } else {
                            throw new RuntimeException("验证码错误");
                        }
                    } else {
                        throw new RuntimeException("没有找到验证码");
                    }
                } else {
                    throw new RuntimeException("验证码格式错误");
                }
            } else {
                throw new RuntimeException("密码长度必须大于6位");
            }
        } else {
            throw new RuntimeException("手机号码格式不正确");
        }
        return user;
    }


    /**
     * 用户登录
     *
     * @param mobile
     * @param password
     * @return
     */
    public User login(String mobile, String password) {
        User user = null;
        if (ValidateUtil.isMobile(mobile)) {
            if (StringUtils.isNotEmpty(password) && password.length() >= 6) {
                user = userMapper.selectByMobile(mobile);
                if (user != null) {
                    if (user.getPassword().equals(DigestUtils.md5Hex(password))) {
                        if (user.getStatus() == CommonStatus.Normal.getIndex()) {
                            this.updateLastLoginTime(user.getUserId());
                        } else {
                            throw new RuntimeException("账号已被禁用");
                        }
                    } else {
                        throw new RuntimeException("登录密码错误");
                    }
                } else {
                    throw new RuntimeException("手机号不存在");
                }
            } else {
                throw new RuntimeException("密码长度必须大于6位");
            }
        } else {
            throw new RuntimeException("手机号码格式不正确");
        }
        return user;
    }

    /**
     * 找回密码
     *
     * @param mobile
     * @param password
     * @param code
     * @return
     */
    public User findPass(String mobile, String password, String code) {
        User user = null;
        if (ValidateUtil.isMobile(mobile)) {
            if (StringUtils.isNotEmpty(password) && password.length() >= 6) {
                if (StringUtils.isNotEmpty(code) && code.length() == 4) {
                    Sms sms = smsBiz.getMsgCode(mobile, SmsType.FindPass.getIndex());
                    if (sms != null) {
                        if (sms.getContent().equals(code)) {
                            user = this.selectByMobile(mobile);
                            if (user != null) {
                                password = DigestUtils.md5Hex(password);
                                String token = RandUtil.getUUID();
                                boolean flag = this.updatePasswordAndToken(user.getUserId(), password, token);
                                if (flag) {
                                    user.setToken(token);
                                } else {
                                    throw new RuntimeException("修改密码失败");
                                }
                            } else {
                                throw new RuntimeException("未找到改手机号码的用户");
                            }
                        } else {
                            throw new RuntimeException("验证码错误");
                        }
                    } else {
                        throw new RuntimeException("没有找到验证码");
                    }
                } else {
                    throw new RuntimeException("验证码格式错误");
                }
            } else {
                throw new RuntimeException("密码长度必须大于6位");
            }
        } else {
            throw new RuntimeException("手机号码格式不正确");
        }
        return user;
    }

    public boolean updatePasswordAndToken(int userId, String password, String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("password", password);
        map.put("token", token);
        return userMapper.updatePasswordAndToken(map) > 0;
    }

    public int updateInfoByUserId(User user) {
        return userMapper.updateInfoByUserId(user);
    }
}
