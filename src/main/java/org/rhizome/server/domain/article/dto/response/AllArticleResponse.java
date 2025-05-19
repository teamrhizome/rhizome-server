package org.rhizome.server.domain.article.dto.response;

import java.util.List;

public record AllArticleResponse(List<ArticleResponse> articles) {
    public static AllArticleResponse of(List<ArticleResponse> articles) {
        return new AllArticleResponse(articles);
    }
}
