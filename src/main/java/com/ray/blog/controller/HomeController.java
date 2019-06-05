package com.ray.blog.controller;

import com.ray.blog.model.Article;
import com.ray.blog.repository.ArticleRepository;
import com.ray.blog.config.BlogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

// 定义一个控制器
@Controller
public class HomeController {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    BlogProperties blogProperties;
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String Index (
            Model model,
            HttpSession session,
            @RequestParam(name="page", required = false, defaultValue = "0") String page
    ) {
        Object uid = session.getAttribute("uid");

        model.addAttribute("sitename", blogProperties.getTitle());
        if (uid != null) {
            model.addAttribute("uid", uid.toString());
        }
        int size = 10;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pabeable = PageRequest.of(Integer.parseInt(page), size, sort);
        Page<Article> articles = articleRepository.findAll(pabeable);

        model.addAttribute("articles", articles);

        // 模板名称
        return "home/index";
    }
}
