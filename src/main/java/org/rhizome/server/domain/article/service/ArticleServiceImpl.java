package org.rhizome.server.domain.article.service;

import static org.rhizome.server.support.error.ErrorType.*;

import java.util.List;

import org.rhizome.server.common.utils.LocalDateTimeHolder;
import org.rhizome.server.domain.article.domain.Article;
import org.rhizome.server.domain.article.domain.ArticleReference;
import org.rhizome.server.domain.article.domain.ArticleReferenceRepository;
import org.rhizome.server.domain.article.domain.ArticleRepository;
import org.rhizome.server.domain.article.dto.response.ArticleResponse;
import org.rhizome.server.support.error.CoreException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleReferenceRepository articleReferenceRepository;
    private final LocalDateTimeHolder localDateTimeHolder;

    @Transactional(readOnly = true)
    @Override
    public ArticleResponse getArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new CoreException(ARTICLE_NOT_FOUND));

        return ArticleResponse.of(article);
    }

    @Transactional
    @Override
    public void publishArticle(String title, String content, List<Long> relateArticleIds) {
        Article article = Article.create(title, content, localDateTimeHolder.now());
        Article savedArticle = articleRepository.save(article);
        List<Article> referenceArticles = articleRepository.findByIdIn(relateArticleIds);
        List<ArticleReference> articleReferences = referenceArticles.stream()
                .map(referenceArticle -> ArticleReference.create(savedArticle, referenceArticle))
                .toList();
        articleReferenceRepository.saveAll(articleReferences);
    }
}
