package com.ffsns.sns.controller;

import com.ffsns.sns.controller.request.PostCommentRequest;
import com.ffsns.sns.controller.request.PostCreateRequest;
import com.ffsns.sns.controller.request.PostModifyRequest;
import com.ffsns.sns.controller.response.CommentResponse;
import com.ffsns.sns.controller.response.PostResponse;
import com.ffsns.sns.controller.response.Response;
import com.ffsns.sns.model.Post;
import com.ffsns.sns.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 생성")
    @PostMapping
    public Response<Void> create (@RequestBody PostCreateRequest request, Authentication authentication){

        postService.create(request.getTitle(),request.getBody(), authentication.getName());
        return Response.success();
    }

    @Operation(summary = "게시글 수정")
    @PutMapping("/{postId}")
    public Response<PostResponse> modify( @PathVariable Integer postId, @RequestBody PostModifyRequest request, Authentication authentication){
        Post post = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);
        return Response.success(PostResponse.fromPost(post));
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication){
        postService.delete(authentication.getName(),postId);

        return Response.success();
    }

    @Operation(summary = "게시글 목록 가져오기")
    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable, Authentication authentication){

        return Response.success(postService.list(pageable).map(PostResponse::fromPost));
    }

    @Operation(summary = "사용자 본인이 작성한 게시글 가져오기")
    @GetMapping("/my")
    public Response<Page<PostResponse>> my(Pageable pageable, Authentication authentication){

        return Response.success(postService.my(authentication.getName(), pageable).map(PostResponse::fromPost));
    }

    @Operation(summary = "게시글 좋아요 하기")
    @PostMapping("/{postId}/likes")
    public Response<Void> like(@PathVariable Integer postId, Authentication authentication){

        postService.like(postId, authentication.getName());
        return Response.success();
    }

    @Operation(summary = "게시글 좋아요 숫자 세기")
    @GetMapping("/{postId}/likes")
    public Response<Long> likeCount(@PathVariable Integer postId){
        return Response.success(postService.likeCount(postId));
    }

    @Operation(summary = "댓글 작성하기")
    @PostMapping("/{postId}/comments")
    public Response<Void> comment(@PathVariable Integer postId, @RequestBody PostCommentRequest request, Authentication authentication){
        postService.comment(postId, authentication.getName(),request.getComment());
        return Response.success();
    }
    @Operation(summary = "댓글 가져오기")
    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> comment(@PathVariable Integer postId, Pageable pageable, Authentication authentication){
        return Response.success(postService.getComments(postId, pageable).map(CommentResponse::fromComment));
    }
}
