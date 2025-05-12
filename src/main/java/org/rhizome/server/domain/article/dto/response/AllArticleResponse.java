package org.rhizome.server.domain.article.dto.response;

import java.util.List;

public record AllArticleResponse(List<ArticleResponse> articles) {

    public record ArticleResponse(
            Long id, String title, String content, List<ReferenceArticleResponse> relateArticles) {}

    public record ReferenceArticleResponse(Long id, String title) {}
}
