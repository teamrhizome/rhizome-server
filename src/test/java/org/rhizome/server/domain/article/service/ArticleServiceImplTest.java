package org.rhizome.server.domain.article.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.tuple;

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
    void 게시글을_작성한다() {
        // given
        // when
        articleService.publishArticle("개발자의 삶", "개발자는 힘들다", List.of());
        // then
        then(articleRepository.findAll())
                .hasSize(1)
                .extracting(Article::getTitle, Article::getContent)
                .containsExactlyInAnyOrder(tuple("개발자의 삶", "개발자는 힘들다"));
    }

    @Test
    void 게시글을_수정한다() {
        // given
        Article article = Article.builder().title("개발자의 삶").content("개발자는 힘들다").build();
        Article savedArticle = articleRepository.save(article);
        // when
        articleService.updateArticle(savedArticle.getId(), "통근의 삶", "왕복 한시간반은 어렵다..", List.of());
        // then
        then(articleRepository.findAll())
                .hasSize(1)
                .extracting(Article::getTitle, Article::getContent)
                .containsExactlyInAnyOrder(tuple("통근의 삶", "왕복 한시간반은 어렵다.."));
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
