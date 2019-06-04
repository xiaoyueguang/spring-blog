package com.ray.blog.controller;

import com.ray.blog.model.User;
import com.ray.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String index (Model model) {
//        User user = userRepository.findByUsernameOrEmail("linjilei", "linjilei@qq.com");
        User user = userRepository.findByUsername("linjilei");
        model.addAttribute("user", user);
//        model.addAttribute("name", user.getUsername());
        model.addAttribute("uuid", UUID.randomUUID().toString().replaceAll("-", ""));

        return "user/index";
    }
}
