package com.film.controller.admin;

import com.film.entity.*;
import com.film.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private SortService sortService;

    @RequestMapping({"", "/", "/index"})
    public String getIndex() {
        return "/admin/index";
    }

    @RequestMapping("/user")
    public String getUserPage(Model model, Page page) {

        List<User> userList = userService.findUsers(page.getOffset(), page.getLimit());
        page.setLimit(10);
        page.setRows(userList.size());
        page.setPath("/admin/user");

        model.addAttribute("userList", userList);
        return "/admin/user";
    }

    @RequestMapping("/movie")
    public String getMoviePage(Model model, Page page) {
        List<Movie> movies = movieService.findMovies(page.getOffset(), page.getLimit());
        model.addAttribute("movies", movies);

        return "/admin/movie";
    }

    @RequestMapping("/comment")
    public String getCommentPage(Model model, Page page) {
        List<Comment> commentList = commentService.findComments(page.getOffset(), page.getLimit());
        model.addAttribute("commentList", commentList);

        return "/admin/comment";
    }

    @RequestMapping("/review")
    public String getReview(Model model, Page page) {
        List<Review> reviewList = reviewService.findReviews(page.getOffset(), page.getLimit());
        model.addAttribute("reviewList", reviewList);

        return "/admin/review";
    }

    @RequestMapping("/sort")
    public String getSort(Model model, Page page) {
        List<Sort> sortList = sortService.findSortList(page.getOffset(), page.getLimit());
        model.addAttribute("sortList", sortList);

        return "/admin/sort";
    }

    @RequestMapping("/mainfunction")
    public String getMainPage() {
        return "/admin/mainfunction";
    }

    @RequestMapping("/login")
    public String getLoginPage() {
        return "/admin/login";
    }

}
