package org.rhizome.server.domain.article.service;

import static org.rhizome.server.support.error.ErrorType.ARTICLE_NOT_FOUND;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.rhizome.server.common.utils.LocalDateTimeHolder;
import org.rhizome.server.domain.article.domain.Article;
import org.rhizome.server.domain.article.domain.ArticleReference;
import org.rhizome.server.domain.article.domain.ArticleReferenceRepository;
import org.rhizome.server.domain.article.domain.ArticleRepository;
import org.rhizome.server.domain.article.dto.response.AllArticleResponse;
import org.rhizome.server.domain.article.dto.response.ArticleResponse;
import org.rhizome.server.domain.article.dto.response.ReferenceArticleResponse;
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
        List<ArticleReference> references = articleReferenceRepository.findBySourceArticle(article);
        List<ReferenceArticleResponse> relateArticles = references.stream()
                .map(ArticleReference::getTargetArticle)
                .distinct()
                .map(ReferenceArticleResponse::of)
                .toList();

        return ArticleResponse.of(article, relateArticles);
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

    @Transactional
    @Override
    public void updateArticle(Long id, String title, String content, List<Long> relateArticleIds) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new CoreException(ARTICLE_NOT_FOUND));
        article.update(title, content, localDateTimeHolder.now());

        List<ArticleReference> existingReferences = articleReferenceRepository.findBySourceArticle(article);

        if (relateArticleIds == null) {
            relateArticleIds = List.of();
        }

        Set<Long> newIds = new HashSet<>(relateArticleIds);
        Set<Long> existingIds = existingReferences.stream()
                .map(ref -> ref.getTargetArticle().getId())
                .collect(Collectors.toSet());

        List<ArticleReference> referencesToRemove = existingReferences.stream()
                .filter(ref -> !newIds.contains(ref.getTargetArticle().getId()))
                .toList();
        if (!referencesToRemove.isEmpty()) {
            articleReferenceRepository.deleteAllInBatch(referencesToRemove);
        }

        Set<Long> idsToAdd =
                newIds.stream().filter(newId -> !existingIds.contains(newId)).collect(Collectors.toSet());
        if (!idsToAdd.isEmpty()) {
            List<Article> articlesToAdd = articleRepository.findByIdIn(new ArrayList<>(idsToAdd));
            List<ArticleReference> newReferences = articlesToAdd.stream()
                    .map(target -> ArticleReference.create(article, target))
                    .toList();
            articleReferenceRepository.saveAll(newReferences);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AllArticleResponse getArticles() {
        List<Article> articles = articleRepository.findAll();

        List<ArticleResponse> articleResponses = articles.stream()
                .map(article -> {
                    List<ArticleReference> references = articleReferenceRepository.findBySourceArticle(article);
                    List<ReferenceArticleResponse> relateArticles = references.stream()
                            .map(ArticleReference::getTargetArticle)
                            .distinct()
                            .map(ReferenceArticleResponse::of)
                            .toList();
                    return ArticleResponse.of(article, relateArticles);
                })
                .toList();

        return new AllArticleResponse(articleResponses);
    }
}
