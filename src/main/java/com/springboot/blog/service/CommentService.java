package com.springboot.blog.service;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.CommentResponse;


public interface CommentService {

    CommentDto createComment(Long postId, CommentDto commentDto);

    CommentResponse getCommentsByPostIdPagination(Long postId, int pageNumber, int pageSize);

    CommentDto getCommentById(Long postId,Long commentId);

    CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto);

    void deleteCommentById(Long postId, Long commentId);

}


























