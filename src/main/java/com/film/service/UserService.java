package com.film.service;

import com.film.dao.LoginTicketMapper;
import com.film.dao.UserMapper;
import com.film.entity.LoginTicket;
import com.film.entity.User;
import com.film.utils.FilmConstant;
import com.film.utils.FilmUtil;
import com.film.utils.MailClient;
import com.film.utils.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserService implements FilmConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /*@Autowired
    private LoginTicketMapper loginTicketMapper;*/

    public User findUserById(int id) {
//        return userMapper.selectById(id);
        User user = getCache(id);
        if (user == null) {
            user = initCache(id);
        }
        return user;
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        User user = userMapper.selectByName(username);

        // 用户不存在
        if (user == null) {
            map.put("usernameMsg", "用户未注册");
            return map;
        }
        // 判断密码
        String md5Password = FilmUtil.md5(password + user.getSalt());
        if (!md5Password.equals(user.getPassword())) {
            map.put("passwordMsg", "密码错误");
            return map;
        }

        // 生成登录凭据
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(FilmUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds*1000));
        // loginTicketMapper.insertLoginTicket(loginTicket);

        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        // loginTicketMapper.updateStatus(ticket, 1);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
    }

    public Map<String, Object> register(User user){
        Map<String, Object> map = new HashMap<>();
        // 空值处理
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在");
            return map;
        }
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册");
            return map;
        }

        // 补全数据
        user.setType(0);
        user.setStatus(1);
        user.setSalt(FilmUtil.generateUUID().substring(0, 5));
        user.setPassword(FilmUtil.md5(user.getPassword() + user.getSalt()));
        user.setCreateTime(new Date());

        userMapper.insertUser(user);

        return map;
    }

    public Map<String, Object> updatePassword(String username, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(newPassword)) {
            map.put("passwordMsg", "新密码不能为空");
            return map;
        }

        // 检查用户是否存在
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该用户还未注册！");
            return map;
        }

        // 检查新旧密码
        String md5NewPassword = FilmUtil.md5( newPassword + user.getSalt());
        if (md5NewPassword.equals(user.getPassword())) {
            map.put("passwordMsg", "原密码与新密码不能相同");
            return map;
        }

        userMapper.updatePassword(user.getId(), md5NewPassword);
        clearCache(user.getId());
        return map;
    }

    public LoginTicket findLoginTicket(String ticket) {
        //return loginTicketMapper.selectByTicket(ticket);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(redisKey);
        return loginTicket;
    }

    public int updateHeader(int userId, String headerUrl) {
        int rows = userMapper.updateHeaderUrl(userId, headerUrl);
        clearCache(userId);
        return rows;
    }

    // 1.优先从缓存中取值
    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }
    // 2.取不到时初始化缓存数据
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }
    // 3.数据变更时清楚缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

}
