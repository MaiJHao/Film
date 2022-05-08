package com.film.dao;

import com.film.entity.Review;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {

    List<Review> selectReviews(int offset, int limit);

    List<Review> selectReviewsByUserId(int userId);

    List<Review> selectReviewsByMovieId(int movieId, int offset, int limit);

    Review selectReviewById(int id);

    int selectReviewCountByMovieId(int movieId);

    int insertReview(Review review);

    int deleteReviewById(int id);

    int updateCommentCount(int id, int commentCount);

    double selectMovieAvgRating(int movieId);

    int selectCommentCountByMovieId(int movieId);

    int selectReviewCount();
}
