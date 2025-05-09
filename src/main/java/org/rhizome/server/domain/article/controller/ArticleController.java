package org.rhizome.server.domain.article.controller;

import org.rhizome.server.domain.article.service.ArticleServiceImpl;
import org.rhizome.server.support.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleServiceImpl articleServiceImpl;

    public ArticleController(ArticleServiceImpl articleServiceImpl) {
        this.articleServiceImpl = articleServiceImpl;
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시글 조회", description = "ID로 게시글을 조회합니다.")
    public ResponseEntity<ApiResponse<?>> getArticle(@PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success(articleServiceImpl.getArticle(id)));
    }
}
