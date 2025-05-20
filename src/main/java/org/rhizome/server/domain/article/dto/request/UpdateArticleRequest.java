package org.rhizome.server.domain.article.dto.request;

public record UpdateArticleRequest(String title, String content, RelateArticleRequest relateArticleIds) {}
