package com.ray.blog.controller;

import com.ray.blog.model.Comment;
import com.ray.blog.repository.CommentRepository;
import com.ray.blog.config.BlogProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BlogProperties blogProperties;

    @RequestMapping("/comment/{id}")
    public String commit (
            @PathVariable("id") String article_id,
            @RequestParam("content") String content,
            HttpSession session,
            Model model
    ) {
        model.addAttribute("sitename", blogProperties.getTitle());
        String uid = session.getAttribute("uid").toString();
        if (uid.equals(null) || uid.equals("")) {
            // 未登录
            return "comment/fail";
        } else {
            Comment comment = new Comment(
                    Long.parseLong(article_id),
                    uid,
                    content
            );
            commentRepository.save(comment);

            return "comment/success";
        }

    }
}
