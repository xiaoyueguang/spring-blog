package com.ray.blog.controller;

import com.ray.blog.model.Article;
import com.ray.blog.model.User;
import com.ray.blog.repository.ArticleRepository;
import com.ray.blog.repository.UserRepository;
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

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private BlogProperties blogProperties;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String index (
            Model model,
            HttpSession session,
            @RequestParam(name="page", required = false, defaultValue = "0") String page
    ) {
        Object uid = session.getAttribute("uid");
        if (uid.equals("") || uid.equals(null)) {
            // 重定向到登录页
            return "redirect:/login";
        } else {
            User user = userRepository.findByUid(uid.toString());

            model.addAttribute("user", user);
            model.addAttribute("sitename", blogProperties.getTitle());

            int size = 10;
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            Pageable pabeable = PageRequest.of(Integer.parseInt(page), size, sort);
            Page<Article> articles = articleRepository.findAllByUid(uid.toString(), pabeable);
            model.addAttribute("articles", articles);

            return "user/index";
        }
    }
}
