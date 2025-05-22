package org.rhizome.server.domain.article.controller;

import org.rhizome.server.domain.article.dto.request.CreateArticleRequest;
import org.rhizome.server.domain.article.dto.request.UpdateArticleRequest;
import org.rhizome.server.domain.article.dto.response.AllArticleResponse;
import org.rhizome.server.domain.article.dto.response.ArticleResponse;
import org.rhizome.server.domain.article.service.ArticleService;
import org.rhizome.server.support.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{id}")
    @Operation(summary = "게시글 조회", description = "ID로 게시글을 조회합니다.")
    public ApiResponse<ArticleResponse> getArticle(@PathVariable Long id) {

        return ApiResponse.success(articleService.getArticle(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ApiResponse<?> publishArticle(@RequestBody CreateArticleRequest request) {
        articleService.publishArticle(
                request.title(), request.content(), request.relateArticleIds().articleIds());
        return ApiResponse.success();
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {
        articleService.updateArticle(
                id,
                request.title(),
                request.content(),
                request.relateArticleIds().articleIds());
        return ApiResponse.success();
    }

    @GetMapping
    public ApiResponse<AllArticleResponse> getArticles() {
        return ApiResponse.success(articleService.getArticles());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ApiResponse.success();
    }
}
