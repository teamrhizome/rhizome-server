package org.rhizome.server.domain.article.domain;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.BDDAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class ArticleReferencesTest {

    @Test
    @DisplayName("sourceArticle과 target과의 연결관계를 생성한다")
    void sourceArticle과_target과의_연결관계를_생성한다() {
        // given
        Article sourceArticle =
                Article.builder().title("source").content("source content").build();
        Article targetArticle1 =
                Article.builder().title("target1").content("target1 content").build();
        Article targetArticle2 =
                Article.builder().title("target2").content("target2 content").build();
        // when
        ArticleReferences references =
                ArticleReferences.createReferencesFrom(sourceArticle, List.of(targetArticle1, targetArticle2));
        // then
        then(references.values())
                .hasSize(2)
                .extracting(ArticleReference::getSourceArticle, ArticleReference::getTargetArticle)
                .containsExactlyInAnyOrder(tuple(sourceArticle, targetArticle1), tuple(sourceArticle, targetArticle2));
    }

    @Test
    void findIdsToAdd_메서드는_현재_참조에_없는_새로운_ID들을_반환한다() {
        // given
        Article sourceArticle =
                Article.builder().title("source").content("source content").build();
        Article targetArticle1 =
                Article.builder().title("target1").content("target1 content").build();
        setArticleId(targetArticle1, 1L);
        Article targetArticle2 =
                Article.builder().title("target2").content("target2 content").build();
        setArticleId(targetArticle2, 2L);

        ArticleReferences references =
                ArticleReferences.createReferencesFrom(sourceArticle, List.of(targetArticle1, targetArticle2));

        // when
        Set<Long> idsToAdd = references.findIdsToAdd(List.of(2L, 3L, 4L));

        // then
        then(idsToAdd).hasSize(2).containsExactlyInAnyOrder(3L, 4L);
    }

    @Test
    void filterNotIn_메서드는_주어진_ID_목록에_없는_참조들만_필터링한다() {
        // given
        Article sourceArticle =
                Article.builder().title("source").content("source content").build();
        Article targetArticle1 =
                Article.builder().title("target1").content("target1 content").build();
        Article targetArticle2 =
                Article.builder().title("target2").content("target2 content").build();
        Article targetArticle3 =
                Article.builder().title("target3").content("target3 content").build();

        setArticleId(targetArticle1, 1L);
        setArticleId(targetArticle2, 2L);
        setArticleId(targetArticle3, 3L);

        ArticleReferences references = ArticleReferences.createReferencesFrom(
                sourceArticle, List.of(targetArticle1, targetArticle2, targetArticle3));

        // when
        ArticleReferences filteredReferences = references.filterNotIn(List.of(1L, 2L));

        // then
        then(filteredReferences.values())
                .hasSize(1)
                .extracting(ArticleReference::getTargetArticle)
                .containsExactly(targetArticle3);
    }

    @Test
    void hasReference_메서드는_참조가_있으면_true를_반환한다() {
        // given
        Article sourceArticle =
                Article.builder().title("source").content("source content").build();
        Article targetArticle =
                Article.builder().title("target").content("target content").build();
        ArticleReferences references = ArticleReferences.createReferencesFrom(sourceArticle, List.of(targetArticle));

        // when
        // then
        then(references.hasReference()).isTrue();
    }

    @Test
    void hasReference_메서드는_참조가_없으면_false를_반환한다() {
        // given
        ArticleReferences references = new ArticleReferences(List.of());

        // when
        // then
        then(references.hasReference()).isFalse();
    }

    private void setArticleId(Article article, Long id) {
        ReflectionTestUtils.setField(article, "id", id);
    }
}
