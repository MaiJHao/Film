package com.film.controller;

import com.alibaba.fastjson.JSON;
import com.film.annotation.LoginRequired;
import com.film.entity.Page;
import com.film.entity.User;
import com.film.service.UserService;
import com.film.utils.FilmConstant;
import com.film.utils.FilmUtil;
import com.film.utils.HostHolder;
import com.film.utils.RedisKeyUtil;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/user")
public class UserController implements FilmConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${film.path.upload}")
    private String uploadPath;

    @Value("${film.path.domain}")
    private String domain;

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/login")
    public String login(String username, String password, String code, boolean remember,
                        Model model, HttpSession session, HttpServletResponse response,
                        @CookieValue("kaptchaOwner") String kaptchaOwner) {
        // 获取验证码
        // String kaptcha = (String) session.getAttribute("kaptcha");
        String kaptcha = null;
        if (StringUtils.isNotBlank(kaptchaOwner)) {
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
        }

        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确");
            return "/login";
        }

        // 设置登录凭证有效时间
        int expiredSeconds = remember ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        // 检查账号密码
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            // 设置cookie有效范围为整个项目
            cookie.setPath(contextPath);
            // 设置cookie有效期
            cookie.setMaxAge(expiredSeconds);
            // 响应cookie数据
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/login";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/index";
    }

    @RequestMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response/*, HttpSession session*/) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        // session.setAttribute("kaptcha", text);

        // 验证码的归属
        String kaptchaOwner = FilmUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(contextPath);
        response.addCookie(cookie);
        // 将验证码存入redis
        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        // 设置超时时间：超过60s失效
        redisTemplate.opsForValue().set(redisKey, text, 60, TimeUnit.SECONDS);


        // 设置响应结果类型
        response.setContentType("image/png");

        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("响应验证码失败"+e.getMessage());
            e.printStackTrace();
        }
    }

    @RequestMapping("/register")
    public String register(String username, String password, String email, Model model, HttpSession session) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            return "redirect:/login";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/register";
        }
    }

    @RequestMapping("/forget")
    public String forget(String username, String email, String newPassword, Model model) {
        // 发送邮件

        // 修改密码
        Map<String, Object> map = userService.updatePassword(username, newPassword);
        if (map == null || map.isEmpty()) {
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/forget";
        }
    }

    @LoginRequired
    @RequestMapping("/updatePassword")
    @ResponseBody
    public String updatePassword(String oldPassword, String newPassword, String confirmNewPassword, Model model) {
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword) || StringUtils.isBlank(confirmNewPassword)) {
            return FilmUtil.getJSONString(1, "密码参数不完整");
        }

        User user = hostHolder.getUser();
        String md5OldPassword = FilmUtil.md5(oldPassword + user.getSalt());
        user.getPassword().equals(md5OldPassword);
        if (!user.getPassword().equals(md5OldPassword)) {
            return FilmUtil.getJSONString(1, "原密码错误");
        }
        if (!newPassword.equals(confirmNewPassword)) {
            return FilmUtil.getJSONString(1, "两次密码输入不一致");
        }

        // 发送邮件

        // 修改密码
        Map<String, Object> map = userService.updatePassword(user.getUsername(), newPassword);
        if (map == null || map.isEmpty()) {
            return FilmUtil.getJSONString(0);
        } else {
            return FilmUtil.getJSONString(2, null, map);
            /*model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("usernameMsg", map.get("passwordMsg"));
            model.addAttribute("usernameMsg", map.get("emailMsg"));
            return "forward:/setting";*/
        }
    }

    @RequestMapping("/upload")
    @ResponseBody
    public String uploadHeader(@RequestParam("file") MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            return FilmUtil.getJSONString(1, "您还没有上传图片");
        }

        String fileName = headerImage.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            return FilmUtil.getJSONString(1, "您还没有上传图片");
        }
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            return FilmUtil.getJSONString(1, "文件格式不正确");
        }

        // 生成随机的文件名
        fileName = FilmUtil.generateUUID() + suffix;
        // 确认文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            // 将headerImage文件传入dest对象，写入磁盘中
            headerImage.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("上传文件失败" + e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常", e);
        }

        // 更新当前用户的头像路径
        // http://localhost:8080/film/user/header/xxx.jpg
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);

        HashMap<String, Object> map = new HashMap<>();
        map.put("headerUrl", headerUrl);
        return FilmUtil.getJSONString(0, null, map);
    }

    @RequestMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放的路径
        fileName = uploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);

        OutputStream os = null;
        FileInputStream fis = null;
        try {
            os = response.getOutputStream();
            fis = new FileInputStream(fileName);
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer ,0, b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @RequestMapping("/admin/login")
    public String adminLogin(String username, String password, String code, boolean remember,
                        Model model, HttpSession session, HttpServletResponse response,
                        @CookieValue("kaptchaOwner") String kaptchaOwner) {
        // 获取验证码
        // String kaptcha = (String) session.getAttribute("kaptcha");
        String kaptcha = null;
        if (StringUtils.isNotBlank(kaptchaOwner)) {
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
        }

        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确");
            return "/admin/login";
        }

        // 设置登录凭证有效时间
        int expiredSeconds = remember ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        // 检查账号密码
        Map<String, Object> map = userService.adminLogin(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            // 设置cookie有效范围为整个项目
            cookie.setPath(contextPath);
            // 设置cookie有效期
            cookie.setMaxAge(expiredSeconds);
            // 响应cookie数据
            response.addCookie(cookie);
            return "redirect:/admin/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "forward:/admin/login";
        }
    }

    @RequestMapping(value = "/admin/logout", method = RequestMethod.GET)
    public String adminLogout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/admin/index";
    }

    @RequestMapping("/admin/addUser")
    public String addUser(String username, String password, String email, int type, Model model, HttpSession session) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setType(type);
        Map<String, Object> map = userService.addUser(user);
        if (map == null || map.isEmpty()) {
            return "redirect:/admin/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "redirect:/admin/index";
        }
    }

    @RequestMapping("/admin/updateUser")
    @ResponseBody
    public String findUserById(int id, String email) {
        int rows = userService.updateEmail(id, email);
        if (rows != 1) {
            return FilmUtil.getJSONString(1);
        }
        return FilmUtil.getJSONString(0);
    }

    @RequestMapping("/admin/deleteUser/{id}")
    @ResponseBody
    public String deleteUserById(@PathVariable("id") int id) {
        int rows = userService.deleteUserById(id);
        if (rows != 1) {
            return FilmUtil.getJSONString(1);
        }
        return FilmUtil.getJSONString(0);
    }

    @RequestMapping("/admin/searchUsers")
    @ResponseBody
    public List<User> searchUsers(String username, String email, Page page) {
        if (username == "" && email == "") {
            return userService.findUsers(page.getOffset(), page.getLimit());
        }
        if (username == "") username = null;
        if (email == "") email = null;
        List<User> users = userService.searchUsers(username, email, page.getOffset(), page.getLimit());
        page.setRows(users.size());
        page.setPath("/admin/searUsers?username="+username+"&email="+email);
        for (User user : users) {
            System.out.println(user);
        }
        return users;
    }
}
