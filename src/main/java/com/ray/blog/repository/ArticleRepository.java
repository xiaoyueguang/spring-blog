package com.ray.blog.repository;

import com.ray.blog.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findByTitle (String title);
//    Article findById (Long id);
    Page<Article> findAllByUid (String uid, Pageable pageable);
}
