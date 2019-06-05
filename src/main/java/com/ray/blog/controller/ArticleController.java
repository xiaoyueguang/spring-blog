package com.ray.blog.controller;

import com.ray.blog.model.Article;
import com.ray.blog.repository.ArticleRepository;
import com.ray.blog.repository.CommentRepository;
import com.ray.blog.util.BlogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private BlogProperties blogProperties;

    @Autowired
    private CommentRepository commentRepository;

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
            return "article/create";
        }
        if (content.equals("")) {
            model.addAttribute("contentError", "请输入内容");
            return "article/create";
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

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editView (
            @PathVariable("id") String id,
            Model model
    ) {
        try {
            model.addAttribute("sitename", blogProperties.getTitle());
            Article article = articleRepository.getOne(Long.parseLong(id));

            model.addAttribute("title", article.getTitle());
            model.addAttribute("content", article.getContent());
            return "article/edit";
        } catch (EntityNotFoundException e) {
            return "article/not-found";
        }
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String edit (
            @PathVariable("id") String id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            HttpSession session,
            Model model
    ) {
        try {
            model.addAttribute("sitename", blogProperties.getTitle());
            model.addAttribute("id", id);
            model.addAttribute("title", title);
            model.addAttribute("content", content);
            Article article = articleRepository.getOne(Long.parseLong(id));

            String uid = session.getAttribute("uid").toString();
            if (article.getUid().equals(uid)) {
                if (title.equals("")) {
                    model.addAttribute("titleError", "请输入标题");
                    return "article/edit";
                }
                if (content.equals("")) {
                    model.addAttribute("contentError", "请输入内容");
                    return "article/edit";
                }
                // 判断是否存在标题
                Article isArticleHas = articleRepository.findByTitle(title);
                if (isArticleHas == null) {
                    article.setTitle(title);
                    article.setContent(content);
                    articleRepository.save(article);
                    return "redirect:/user";
                } else {
                    model.addAttribute("titleError", "标题重复");
                    return "article/edit";
                }


            } else {
                return "article/other";
            }


        } catch (EntityNotFoundException e) {
            return "article/not-found";
        }
    }
    @RequestMapping(value = "/del/{id}", method = RequestMethod.GET)
    public String edit (
            @PathVariable("id") String id,
            HttpSession session,
            Model model
    ) {
        try {
            model.addAttribute("sitename", blogProperties.getTitle());

            Article article = articleRepository.getOne(Long.parseLong(id));

            if (article.getUid().equals(session.getAttribute("uid").toString())) {
                articleRepository.deleteById(Long.parseLong(id));
                return "article/del-success";
            } else {
                return "article/del-fail";
            }

        } catch (EntityNotFoundException e) {
            return "article/not-found";
        }
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detail (
            @PathVariable("id") String id,
            Model model
    ) {
        try {
            model.addAttribute("sitename", blogProperties.getTitle());
            model.addAttribute("id", id);

            Article article = articleRepository.getOne(Long.parseLong(id));

            model.addAttribute("title", article.getTitle());
            model.addAttribute("content", article.getContent());
            model.addAttribute("author", article.getUser().getNickname());

            Sort sort = new Sort(Sort.Direction.DESC, "id");
            Pageable pageable = PageRequest.of(0, 255, sort);
            model.addAttribute("comments", commentRepository.findAllByAid(Long.parseLong(id), pageable));
            return "article/detail";

        } catch (EntityNotFoundException e) {
            return "article/not-found";
        }
    }
}
