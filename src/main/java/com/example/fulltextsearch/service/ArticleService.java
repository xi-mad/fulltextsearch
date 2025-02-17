package com.example.fulltextsearch.service;

import com.example.fulltextsearch.model.Article;
import com.example.fulltextsearch.repository.ArticleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Slf4j
@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JiebaSegmentationService jiebaSegmentationService;

    @Autowired
    TransactionTemplate transactionTemplate;

    public int createArticle(String title, String content) {
        // 分词并构造 tokens
        String titleTokens = jiebaSegmentationService.segmentText(title);
        String contentTokens = jiebaSegmentationService.segmentText(content);

        // 使用原生 SQL 插入语句
        String sql = """
                INSERT INTO articles (title, content, tokens)
                VALUES (:title, :content, setweight(to_tsvector('simple', :titleTokens), 'A') || setweight(to_tsvector('simple', :contentTokens), 'B'))
                """;

        return transactionTemplate.execute(transactionStatus -> {
            int i = entityManager.createNativeQuery(sql)
                    .setParameter("title", title)
                    .setParameter("content", content)
                    .setParameter("titleTokens", titleTokens)
                    .setParameter("contentTokens", contentTokens)
                    .executeUpdate();
            transactionStatus.flush();
            return i;
        });
    }


    public List<Article> searchArticles(String query) {
        long l = System.currentTimeMillis();
        String tsQuery = jiebaSegmentationService.segmentText(query).replace(" ", " & ");
        String sql = """
                SELECT
                    *,
                    ts_rank(tokens, query) as rank
                FROM articles, to_tsquery('simple', :query) query
                WHERE tokens @@ query
                ORDER BY rank DESC
                limit 10 offset 0
                """;
        Query nativeQuery = entityManager.createNativeQuery(sql, Article.class);
        nativeQuery.setParameter("query", tsQuery);
        List resultList = nativeQuery.getResultList();
        log.info("query: {}, after segement: {}, use time: {}", query, tsQuery, (System.currentTimeMillis() - l));
        return resultList;
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }
}