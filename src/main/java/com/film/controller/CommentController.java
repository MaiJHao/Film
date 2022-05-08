package com.film.controller;

import com.film.annotation.LoginRequired;
import com.film.entity.Comment;
import com.film.entity.Review;
import com.film.entity.User;
import com.film.service.CommentService;
import com.film.service.ReviewService;
import com.film.utils.FilmConstant;
import com.film.utils.FilmUtil;
import com.film.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements FilmConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private HostHolder hostHolder;


    @RequestMapping("/addReply/{movieId}/{reviewId}")
    public String addReply(@PathVariable("movieId") int movieId,
                           @PathVariable("reviewId") int reviewId,
                           Comment comment, Model model) {
        User user = hostHolder.getUser();
        comment.setStatus(0);
        comment.setUserId(user.getId());
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        // 同步Review表中的commentCount字段
        int commonCount = commentService.findCommentCountByEntity(ENTITY_TYPE_POST, reviewId);
        reviewService.updateCommentCount(reviewId, commonCount);
        return "redirect:/reviewDetails/" + movieId + "/" + reviewId;
    }

    @RequestMapping("/admin/deleteComment/{id}")
    @ResponseBody
    public String deleteUserById(@PathVariable("id") int id) {
        int rows = commentService.deleteCommentById(id);
        if (rows != 1) {
            return FilmUtil.getJSONString(1);
        }
        return FilmUtil.getJSONString(0);
    }
}
