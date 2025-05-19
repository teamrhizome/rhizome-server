package org.rhizome.server.domain.article.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.rhizome.server.domain.article.domain.Article;

public record ArticleResponse(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime publishedAt,
        List<ReferenceArticleResponse> relateArticles) {

    public static ArticleResponse of(Article article, List<ReferenceArticleResponse> relateArticles) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getCreatedAt(),
                article.getPublishedAt(),
                relateArticles);
    }
}
