package com.ray.blog.controller;

import com.ray.blog.model.User;
import com.ray.blog.repository.UserRepository;
import com.ray.blog.service.MailService;
import com.ray.blog.util.BlogProperties;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class AuthController {
    @Autowired
    private BlogProperties blogProperties;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginView (Model model) {
        model.addAttribute("title", blogProperties.getTitle());
        return "auth/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login (
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            HttpSession session
    ) {
        User user;

        user = userRepository.findByUsername(username);

        if (null == user) {
            model.addAttribute("msg", "找不到用户");
            return "auth/fail";
        } else {
            // 找到用户了
            user = userRepository.findByUsernameAndPassword(username, sha1Password(password, username));
            if (user == null) {
                model.addAttribute("msg", "密码不一样");
                return "auth/fail";
            } else {
                if (user.getStatus() == 0) {
                    model.addAttribute("msg", "当前账号还未激活! 请先去邮箱激活账号");
                    return "auth/fail";
                } else {
                    // 标记登录UID. 登录状态
                    session.setAttribute("uid", user.getUid());
                    return "redirect:";
                }
            }
        }
    }

    /*
    * 密码加密:
    * md5(md5(sha1(MD5(用户+MD5(密码)).substr(2)+salt).substr(2)))
    * */
    private String sha1Password (String password, String username) {
//        return DigestUtils.sha1Hex(blogProperties.getSalt() + DigestUtils.md5Hex(DigestUtils.md5Hex(password + blogProperties.getSalt()).substring(4)));
        return DigestUtils.md5Hex(
                DigestUtils.md5Hex(
                        DigestUtils.sha1Hex(
                                DigestUtils.md5Hex(username + DigestUtils.md5Hex(password)).substring(4) + blogProperties.getSalt()
                        ).substring(2)
                )
        );
    }
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerView () {

        return "auth/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register (
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("password2") String password2,
            @RequestParam("email") String email,
            Model model
    ) {
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        model.addAttribute("password2", password2);
        model.addAttribute("email", email);

        if (username.equals("")) {
            System.out.println("用户名");
            model.addAttribute("usernameError", "请输入用户名");
            return "auth/register";
        }
        if (password.equals("")) {
            System.out.println("password");
            model.addAttribute("passwordError", "请输入密码");
            return "auth/register";
        }
        if (!password.equals(password2)) {
            System.out.println("password2");
            model.addAttribute("password2Error", "请输入确认密码");
            return "auth/register";
        }
        if (email.equals("")) {
            System.out.println("email");
            model.addAttribute("emailError", "请输入电子邮箱");
            return "auth/register";
        }

        User user;

        user = userRepository.findByUsername(username);
        if (user == null) {
            user = userRepository.findByEmail(email);
            if (user == null) {
                user = new User(username, username, sha1Password(password, username), email);
                try {
                    UUID uuid = UUID.randomUUID();
                    stringRedisTemplate.opsForValue().set("USER-ID:" + uuid.toString(), user.getUid());
                    String url = "https://" + blogProperties.getHost() + "/" + "active/" + uuid;
                    String content = "点击<a href='" + url + "'>链接</a>激活! 如果不能点击, 请手动复制一下链接:";
                    mailService.sendHtmlMail(email, "注册成功! 请点击链接进行激活!", content + "<p>" + url + "</p>");
                    userRepository.save(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return "auth/register-done";
            } else {
                model.addAttribute("emailError", "已经在该邮箱");
                return "auth/register";
            }
        } else {
            model.addAttribute("usernameError", "已存在该用户");
            return "auth/register";
        }
    }

    @RequestMapping(value = "/active/{uuid}", method = RequestMethod.GET)
    public String active (@PathVariable String uuid) {
        String uid = stringRedisTemplate.opsForValue().get("USER-ID:" + uuid);
        User user = userRepository.findByUid(uid);
        if (user == null) {
            return "auth/active-fail";
        } else {
            user.setStatus(1);
            userRepository.save(user);
            return "auth/active";
        }

    }
    @RequestMapping("/lgout")
    public String lgout (HttpSession session) {
        session.removeAttribute("uid");
        return "redirect:/";
    }
}