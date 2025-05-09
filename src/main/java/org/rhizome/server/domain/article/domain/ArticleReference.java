package org.rhizome.server.domain.article.domain;

import org.rhizome.server.support.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleReference extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_post_id", nullable = false)
    private Article sourceArticle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_post_id", nullable = false)
    private Article targetArticle;

    @Builder
    private ArticleReference(Article sourceArticle, Article targetArticle) {
        this.sourceArticle = sourceArticle;
        this.targetArticle = targetArticle;
    }

    public ArticleReference createArticleReference(Article sourceArticle, Article targetArticle) {
        return ArticleReference.builder()
                .sourceArticle(sourceArticle)
                .targetArticle(targetArticle)
                .build();
    }
}
