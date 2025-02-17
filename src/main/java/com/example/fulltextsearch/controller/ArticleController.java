package com.example.fulltextsearch.controller;

import com.example.fulltextsearch.model.Article;
import com.example.fulltextsearch.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public int createArticle(@RequestParam String title, @RequestParam String content) {
        return articleService.createArticle(title, content);
    }

    @GetMapping("/{id}")
    public Article getArticleById(@PathVariable Long id) {
        return articleService.getArticleById(id);
    }

    @GetMapping("/search")
    public List<Article> searchArticles(@RequestParam String query) {
        return articleService.searchArticles(query);
    }
}