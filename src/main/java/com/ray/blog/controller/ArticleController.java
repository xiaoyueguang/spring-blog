package com.ray.blog.controller;

import com.ray.blog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    public String index (Model model) {
        return "article/index";
    }
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create (@RequestParam String title, Model model) {
        model.addAttribute("title", title);
        return "article/create";
    }
}
