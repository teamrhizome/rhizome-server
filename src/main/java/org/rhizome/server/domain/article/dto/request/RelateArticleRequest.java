package org.rhizome.server.domain.article.dto.request;

import java.util.List;

public record RelateArticleRequest(List<Long> articleIds) {}
