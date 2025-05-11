package org.rhizome.server.domain.article.dto.request;

public record UpdateArticleRequest(Long id, String title, String content, RelateArticleRequest relateArticleIds) {}
