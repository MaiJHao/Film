package com.film.service.admin;

import com.film.dao.UserMapper;
import com.film.entity.LoginTicket;
import com.film.entity.User;
import com.film.utils.FilmUtil;
import com.film.utils.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
@Service
public class AdminUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

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
        if (user == null || user.getType()!=1) {
            map.put("usernameMsg", "该账号不是管理员，请联系管理员");
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
}
*/
