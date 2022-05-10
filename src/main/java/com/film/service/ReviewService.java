package com.film.service;

import com.film.dao.ReviewMapper;
import com.film.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    public List<Review> findReviews(int offset, int limit) {
        return reviewMapper.selectReviews(offset, limit);
    }

    public List<Review> findReviewsByUserId(int userId) {
        return reviewMapper.selectReviewsByUserId(userId);
    }

    public List<Review> findReviewByMovieId(int movieId, int offset, int limit) {
        return reviewMapper.selectReviewsByMovieId(movieId, offset, limit);
    }

    public Review findReviewById(int id) {
        return reviewMapper.selectReviewById(id);
    }

    public int findReviewCount(int movieId) {
        return reviewMapper.selectReviewCountByMovieId(movieId);
    }

    public int addReview(Review review) {
        if (review == null) {
            throw new IllegalArgumentException("参数错误");
        }

        int rows = reviewMapper.insertReview(review);
        return rows;
    }

    public int updateCommentCount(int id, int commentCount) {
        if (id==0 || commentCount==0) {
            throw new IllegalArgumentException("参数错误");
        }

        int rows = reviewMapper.updateCommentCount(id, commentCount);
        return rows;
    }

    public double findMovieAvgRating(int movieId) {
        double rating = reviewMapper.selectMovieAvgRating(movieId);
        // 保留一位小数
        double result = (double)Math.round(rating*10)/10;
        return result;
    }

    public int findCommentCountByMovieId(int movieId) {
        return reviewMapper.selectCommentCountByMovieId(movieId);
    }

    public int deleteReviewById(int id) {
        return reviewMapper.deleteReviewById(id);
    }

    public int findReviewRows() {
        return reviewMapper.selectReviewCount();
    }

    public List<Review> searchReviews(String title, String content, int offset, int limit) {
        return reviewMapper.selectReviewByTitleAndContent(title, content, offset, limit);
    }
}
