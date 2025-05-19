package org.rhizome.server.domain.article.dto.response;

import org.rhizome.server.domain.article.domain.Article;

public record ReferenceArticleResponse(Long id, String title) {
    public static ReferenceArticleResponse of(Article article) {
        return new ReferenceArticleResponse(article.getId(), article.getTitle());
    }
}
