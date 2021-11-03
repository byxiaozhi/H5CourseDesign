package com.jluslda.edu.service;

import com.jluslda.edu.mapper.CommentMapper;
import com.jluslda.edu.model.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    CommentMapper commentMapper;

    public CommentService(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public void addComment(Comment comment) {
        commentMapper.addComment(comment);
    }

    public List<Comment> getCommentList(String object, Integer objectId, Integer userId, Integer beg, Integer num) {
        return commentMapper.getCommentList(object, objectId, userId, beg, num);
    }

    public Integer count(String object, Integer objectId, Integer userId) {
        return commentMapper.count(object, objectId, userId);
    }
}
