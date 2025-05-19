package org.rhizome.server.domain.article.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public class ArticleReferences {
    private final List<ArticleReference> values;

    public ArticleReferences(List<ArticleReference> values) {
        this.values = List.copyOf(values);
    }

    public static ArticleReferences createReferencesFrom(Article source, List<Article> targets) {
        List<ArticleReference> references = targets.stream()
                .map(target -> ArticleReference.create(source, target))
                .toList();
        return new ArticleReferences(references);
    }

    public Set<Long> findIdsToAdd(List<Long> candidateTargetIds) {
        Set<Long> currentTargetIds = extractTargetIds();
        Set<Long> candidateTargetIdSet = new HashSet<>(candidateTargetIds);

        return candidateTargetIdSet.stream()
                .filter(candidateId -> !currentTargetIds.contains(candidateId))
                .collect(Collectors.toSet());
    }

    public ArticleReferences filterNotIn(List<Long> targetIds) {
        Set<Long> newIds = new HashSet<>(targetIds);

        return new ArticleReferences(values.stream()
                .filter(ref -> !newIds.contains(ref.getTargetArticle().getId()))
                .toList());
    }

    public boolean hasReference() {
        return !values.isEmpty();
    }

    private Set<Long> extractTargetIds() {
        return values.stream().map(ref -> ref.getTargetArticle().getId()).collect(Collectors.toSet());
    }
}
