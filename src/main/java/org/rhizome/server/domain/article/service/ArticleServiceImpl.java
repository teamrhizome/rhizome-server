package org.rhizome.server.domain.article.service;

import static org.rhizome.server.support.error.ErrorType.ARTICLE_NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.rhizome.server.common.utils.LocalDateTimeHolder;
import org.rhizome.server.domain.article.domain.*;
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
        Article article = findArticleBy(id);
        List<ArticleReference> references = articleReferenceRepository.findBySourceArticleAndDeletedAtIsNull(article);
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

        List<Article> referenceArticles = articleRepository.findByIdInAndDeletedAtIsNull(relateArticleIds);
        ArticleReferences articleReferences = ArticleReferences.createReferencesFrom(savedArticle, referenceArticles);
        articleReferenceRepository.saveAll(articleReferences.values());
    }

    @Transactional
    @Override
    public void updateArticle(Long id, String title, String content, List<Long> relateArticleIds) {
        Article article = findArticleBy(id);
        article.update(title, content, localDateTimeHolder.now());

        ArticleReferences existingReferences =
                new ArticleReferences(articleReferenceRepository.findBySourceArticleAndDeletedAtIsNull(article));

        ArticleReferences candidateRemoveReferences = existingReferences.filterNotIn(relateArticleIds);

        if (candidateRemoveReferences.hasReference()) {
            candidateRemoveReferences.deleteAll();
        }

        Set<Long> idsToAdd = existingReferences.findIdsToAdd(relateArticleIds);

        if (!idsToAdd.isEmpty()) {
            List<Article> candidateArticlesToAdd =
                    articleRepository.findByIdInAndDeletedAtIsNull(new ArrayList<>(idsToAdd));
            ArticleReferences newReferences = ArticleReferences.createReferencesFrom(article, candidateArticlesToAdd);
            articleReferenceRepository.saveAll(newReferences.values());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AllArticleResponse getArticles() {
        List<Article> articles = articleRepository.findByDeletedAtIsNull();

        List<ArticleResponse> articleResponses = articles.stream()
                .map(article -> {
                    List<ArticleReference> references =
                            articleReferenceRepository.findBySourceArticleAndDeletedAtIsNull(article);
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

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        Article article = findArticleBy(id);
        ArticleReferences articleReferences =
                new ArticleReferences(articleReferenceRepository.findBySourceArticleAndDeletedAtIsNull(article));
        articleReferences.deleteAll();
        article.delete();
    }

    private Article findArticleBy(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new CoreException(ARTICLE_NOT_FOUND));
    }
}
