package org.rhizome.server.domain.article.domain;

import static org.assertj.core.api.BDDAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ArticleTest {

    @Nested
    class update_메서드는 {
        // given
        Article article;

        @BeforeEach
        void setUp() {
            article = Article.builder()
                    .title("기존 제목")
                    .content("기존 내용")
                    .publishedAt(LocalDateTime.of(2025, 5, 19, 0, 0))
                    .build();
        }

        @Test
        void 모든_값이_들어오면_모두_수정된다() {
            // given
            LocalDateTime newTime = LocalDateTime.of(2025, 5, 20, 11, 0);
            // when
            article.update("새로운 제목", "새로운 내용", newTime);
            // then
            then(article)
                    .extracting(Article::getTitle, Article::getContent, Article::getPublishedAt)
                    .containsExactly("새로운 제목", "새로운 내용", newTime);
        }

        @Test
        void 제목이_null이면_기존_제목을_유지한다() {
            // when
            article.update(null, "새로운 내용", LocalDateTime.now());
            then(article).extracting(Article::getTitle, Article::getContent).containsExactly("기존 제목", "새로운 내용");
        }

        @Test
        void 내용이_null이면_기존_내용을_유지한다() {
            // when
            article.update("새로운 제목", null, LocalDateTime.now());
            // then
            then(article).extracting(Article::getTitle, Article::getContent).containsExactly("새로운 제목", "기존 내용");
        }

        @Test
        void 출판_시간은_null로_변경_할_수_없다() {
            BDDAssertions.thenThrownBy(() -> article.update("새로운 제목", "새로운 내용", null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("게시글 작성 시간은 null 일 수 없습니다.");
        }
    }
}
