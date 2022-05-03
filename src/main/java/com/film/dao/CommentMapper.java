package com.film.dao;

import com.film.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    List<Comment> selectCommentByUserId(int userId);

    int insertComment(Comment comment);

    int deleteCommentById(int id);

    int updateComment(Comment comment);

    int selectCommentCountByMovieId(int movieId);

    int selectCommentCountByEntity(int entityType, int entityId);
}
