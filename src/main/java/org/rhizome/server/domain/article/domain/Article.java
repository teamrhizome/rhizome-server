package org.rhizome.server.domain.article.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import org.rhizome.server.support.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private LocalDateTime publishedAt;

    @Builder
    private Article(String title, String content, LocalDateTime publishedAt) {
        this.title = title;
        this.content = content;
        this.publishedAt = publishedAt;
    }

    public static Article create(String title, String content, LocalDateTime publishedAt) {
        return Article.builder()
                .title(title)
                .content(content)
                .publishedAt(publishedAt)
                .build();
    }

    public void update(String title, String content, LocalDateTime publishedAt) {
        this.title = Objects.requireNonNullElse(title, this.title);
        this.content = Objects.requireNonNullElse(content, this.content);
        this.publishedAt = publishedAt;
    }
}
