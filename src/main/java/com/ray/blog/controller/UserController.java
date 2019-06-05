package com.ray.blog.controller;

import com.ray.blog.model.User;
import com.ray.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index (Model model, HttpSession session) {
        Object uid = session.getAttribute("uid");
        User user = userRepository.findByUid(uid.toString());

        return "user/index";
    }
}
