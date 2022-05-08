package com.film.controller;

import com.film.annotation.LoginRequired;
import com.film.entity.Review;
import com.film.entity.User;
import com.film.service.MovieService;
import com.film.service.ReviewService;
import com.film.utils.FilmUtil;
import com.film.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping("/addReview")
    @LoginRequired
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String addReview(Review review, Model model) {
        User user = hostHolder.getUser();

        review.setUserId(user.getId());
        review.setStatus(0);
        review.setCreateTime(new Date());
        // 插入评价
        reviewService.addReview(review);
        // 修改电影评分（需要事务）
        double rating = reviewService.findMovieAvgRating(review.getMovieId());
        movieService.updateMovieRating(review.getMovieId(),  rating);

        return "redirect:/details/" + review.getMovieId();
    }

    @RequestMapping("/admin/deleteReview/{id}")
    @ResponseBody
    public String deleteUserById(@PathVariable("id") int id) {
        int rows = reviewService.deleteReviewById(id);
        if (rows != 1) {
            return FilmUtil.getJSONString(1);
        }
        return FilmUtil.getJSONString(0);
    }
}
