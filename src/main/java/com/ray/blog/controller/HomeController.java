package com.ray.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// 定义一个控制器
@Controller
public class HomeController {
    // 定义请求路径和方法. 路径带参数
    @RequestMapping(value = "/", method = RequestMethod.GET)
    // @PathVariable注解会从上面的路径中取出对应的值.
    public String Hello () {
        // 模板名称
        return "home/index";
    }
}
