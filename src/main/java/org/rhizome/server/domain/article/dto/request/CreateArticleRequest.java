package org.rhizome.server.domain.article.dto.request;

import java.util.List;

public record CreateArticleRequest(String title, String content, RelateArticleRequest relateArticleIds) {
    public record RelateArticleRequest(List<Long> articleIds) {}
}
