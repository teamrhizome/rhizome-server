package org.rhizome.server.domain.article.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleReferenceRepository extends JpaRepository<ArticleReference, Long> {
    List<ArticleReference> findBySourceArticle(Article sourceArticle);

    List<ArticleReference> findBySourceArticleAndDeletedAtIsNull(Article sourceArticle);
}
