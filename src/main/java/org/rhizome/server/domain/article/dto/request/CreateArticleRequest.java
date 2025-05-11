package org.rhizome.server.domain.article.dto.request;

public record CreateArticleRequest(String title, String content, RelateArticleRequest relateArticleIds) {}
