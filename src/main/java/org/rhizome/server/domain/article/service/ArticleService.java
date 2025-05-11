package org.rhizome.server.domain.article.service;

import java.util.List;

import org.rhizome.server.domain.article.dto.response.ArticleResponse;

public interface ArticleService {
    ArticleResponse getArticle(Long id);

    void publishArticle(String title, String content, List<Long> relateArticleIds);
}
