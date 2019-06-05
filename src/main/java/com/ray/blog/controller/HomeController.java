package com.ray.blog.controller;

import com.ray.blog.util.BlogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.UUID;

// 定义一个控制器
@Controller
public class HomeController {
    @Autowired
    BlogProperties blogProperties;
    // 定义请求路径和方法. 路径带参数
    @RequestMapping(value = "/", method = RequestMethod.GET)
    // @PathVariable注解会从上面的路径中取出对应的值.
    public String Hello (Model model, HttpSession session) {
        Object uid = session.getAttribute("uid");

        model.addAttribute("sitename", blogProperties.getTitle());
        if (uid != null) {
            model.addAttribute("uid", uid.toString());
        }
        // 模板名称
        return "home/index";
    }
}
