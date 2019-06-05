package com.ray.blog.controller;

import com.ray.blog.model.User;
import com.ray.blog.repository.UserRepository;
import com.ray.blog.util.BlogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogProperties blogProperties;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String index (Model model, HttpSession session) {
        Object uid = session.getAttribute("uid");
        if (uid.equals("") || uid.equals(null)) {
            // 重定向到登录页
            return "redirect:/login";
        } else {
            User user = userRepository.findByUid(uid.toString());

            model.addAttribute("user", user);
            model.addAttribute("sitename", blogProperties.getTitle());

            return "user/index";
        }
    }
}
