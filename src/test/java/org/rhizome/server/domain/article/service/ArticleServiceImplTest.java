package org.rhizome.server.domain.article.service;

import static org.assertj.core.api.BDDAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.rhizome.server.IntegrationTestSupport;
import org.rhizome.server.domain.article.domain.Article;
import org.rhizome.server.domain.article.domain.ArticleReference;
import org.rhizome.server.domain.article.domain.ArticleReferenceRepository;
import org.rhizome.server.domain.article.domain.ArticleRepository;
import org.rhizome.server.domain.article.dto.response.AllArticleResponse;

class ArticleServiceImplTest extends IntegrationTestSupport {
    private final ArticleService articleService;
    private final ArticleReferenceRepository articleReferenceRepository;
    private final ArticleRepository articleRepository;

    ArticleServiceImplTest(
            ArticleService articleService,
            ArticleReferenceRepository articleReferenceRepository,
            ArticleRepository articleRepository) {
        this.articleService = articleService;
        this.articleReferenceRepository = articleReferenceRepository;
        this.articleRepository = articleRepository;
    }

    @AfterEach
    void tearDown() {
        articleReferenceRepository.deleteAllInBatch();
        articleRepository.deleteAllInBatch();
    }

    @Test
    void 모든_게시글을_조회한다() {
        // given
        Article article1 = Article.builder().title("1번 게시글").build();
        Article article2 = Article.builder().title("2번 게시글").build();
        Article article3 = Article.builder().title("3번 게시글").build();
        articleRepository.saveAll(List.of(article1, article2, article3));

        ArticleReference reference = ArticleReference.builder()
                .sourceArticle(article1)
                .targetArticle(article2)
                .build();
        articleReferenceRepository.save(reference);
        // when
        AllArticleResponse articles = articleService.getArticles();
        // then
        then(articles.articles())
                .hasSize(3)
                .extracting(
                        AllArticleResponse.ArticleResponse::title, AllArticleResponse.ArticleResponse::relateArticles)
                .containsExactlyInAnyOrder(
                        tuple(
                                "1번 게시글",
                                List.of(new AllArticleResponse.ReferenceArticleResponse(
                                        article2.getId(), article2.getTitle()))),
                        tuple("2번 게시글", List.of()),
                        tuple("3번 게시글", List.of()));
    }
}
