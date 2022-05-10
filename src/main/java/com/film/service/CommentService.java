package com.film.service;

import com.film.controller.UserController;
import com.film.dao.CommentMapper;
import com.film.entity.Comment;
import com.film.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserService userService;

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
    }

    public List<Comment> findCommentsByUserId(int userId) {
        List<Comment> commentList = commentMapper.selectCommentByUserId(userId);
        return commentList;
    }

    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数错误");
        }

        int rows = commentMapper.insertComment(comment);
        return rows;
    }

    public int deleteCommentById(int id) {
        int rows = commentMapper.deleteCommentById(id);
        return rows;
    }

    public int updateComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数错误");
        }

        int rows = commentMapper.updateComment(comment);
        return rows;
    }

    public int findCommentCountByMovieId(int movieId) {
        int count = commentMapper.selectCommentCountByMovieId(movieId);
        return count;
    }

    public int findCommentCountByEntity(int entityType, int entityId) {
        int count = commentMapper.selectCommentCountByEntity(entityType, entityId);
        return count;
    }

    public List<Comment> findComments(int offset, int limit) {
        return commentMapper.selectComments(offset, limit);
    }

    public int findCommentRows() {
        return commentMapper.selectCommentCount();
    }

    public List<Comment> searchComments(String username, String content, int offset, int limit) {
        User user = new User();
        if (!StringUtils.isBlank(username)){
            user = userService.findUserByUsername(username);
            return commentMapper.selectCommentByUserIdAndContent(user.getId(), content, offset, limit);
        }
        return commentMapper.selectCommentByUserIdAndContent(0, content, offset, limit);
    }
}
