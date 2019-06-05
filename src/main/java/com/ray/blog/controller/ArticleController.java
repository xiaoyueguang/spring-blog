package com.ray.blog.controller;

import com.ray.blog.model.Article;
import com.ray.blog.repository.ArticleRepository;
import com.ray.blog.util.BlogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private BlogProperties blogProperties;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createView (Model model) {
        model.addAttribute("sitename", blogProperties.getTitle());
        return "article/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create (
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            Model model,
            HttpSession session
    ) {
        Object uid = session.getAttribute("uid");
        if (uid.equals("") || uid.equals(null)) {
            return "auth/go-to-login";
        }
        model.addAttribute("sitename", blogProperties.getTitle());

        model.addAttribute("title", title);
        model.addAttribute("content", content);

        if (title.equals("")) {
            model.addAttribute("titleError", "请输入标题");
        }
        if (content.equals("")) {
            model.addAttribute("contentError", "请输入内容");
        }

        Article article = articleRepository.findByTitle(title);

        if (article == null) {
            article = new Article(title, content, uid.toString());
            articleRepository.save(article);
            return "redirect:/user";
        } else {
            model.addAttribute("titleError", "标题重复");
        }

        return "article/create";
    }
}
