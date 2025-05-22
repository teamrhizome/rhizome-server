package org.rhizome.server.domain.article.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByIdInAndDeletedAtIsNull(List<Long> ids);

    List<Article> findByDeletedAtIsNull();
}
