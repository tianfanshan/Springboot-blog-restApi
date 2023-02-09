package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceBadRequestException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.CommentResponse;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    private CommentServiceImpl(CommentRepository commentRepository,PostRepository postRepository,ModelMapper modelMapper){
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","id",postId.toString()));

        Comment comment = mapToEntity(commentDto);

        comment.setPost(post);

        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }

    @Override
    public CommentResponse getCommentsByPostIdPagination(Long postId, int pageNumber, int pageSize) {

        postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id", postId.toString()));

        List<Comment> comments = commentRepository.findByPostId(postId);

        if ((pageNumber <= 0 && pageSize > comments.size()) || (pageNumber != 0 && (pageNumber + 1) * pageSize > comments.size())){
            throw new ResourceBadRequestException(pageNumber,pageSize);
        }

        List<Comment> subCommentsList = comments.subList(pageNumber * pageSize,(pageNumber+1) * pageSize);

        Pageable commentPageable = PageRequest.of(pageNumber,pageSize);

        Page<Comment> page = new PageImpl<>(subCommentsList,commentPageable,comments.size());

        List<Comment> commentContent = page.getContent();

        List<CommentDto> content = commentContent.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setContent(content);
        commentResponse.setLast(page.isLast());
        commentResponse.setPageNo(page.getNumber());
        commentResponse.setPageSize(page.getSize());
        commentResponse.setTotalPages(page.getTotalPages());
        commentResponse.setTotalElements(page.getTotalElements());

        return commentResponse;
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

        Comment comment = checkPostAndComment(postId,commentId);

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {

        Comment comment = checkPostAndComment(postId,commentId);

        comment.setName(commentDto.getName());
        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());

        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }

    @Override
    public void deleteCommentById(Long postId, Long commentId) {

        Comment comment = checkPostAndComment(postId,commentId);

        commentRepository.delete(comment);
    }

    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = modelMapper.map(commentDto, Comment.class);
        return comment;
    }

    private Comment checkPostAndComment(Long postId, Long commentId){

        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","id",postId.toString()));

        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment","id",commentId.toString()));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        return comment;
    }









}


















