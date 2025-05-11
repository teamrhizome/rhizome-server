package org.rhizome.server.domain.article.service;

import org.rhizome.server.domain.article.dto.ArticleResponseDto;

public interface ArticleService {
    ArticleResponseDto getArticle(Long id);
}
