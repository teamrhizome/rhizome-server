package org.rhizome.server.domain.article.service;

import static org.rhizome.server.support.error.ErrorType.*;

import org.rhizome.server.domain.article.domain.Article;
import org.rhizome.server.domain.article.domain.ArticleRepository;
import org.rhizome.server.domain.article.dto.ArticleResponseDto;
import org.rhizome.server.support.error.CoreException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Override
    public ArticleResponseDto getArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new CoreException(ARTICLE_NOT_FOUND));

        return ArticleResponseDto.of(article);
    }
}
