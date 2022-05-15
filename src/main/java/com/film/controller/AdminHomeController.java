package com.film.controller;

import com.film.annotation.AdminLoginRequired;
import com.film.entity.*;
import com.film.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @AdminLoginRequired
    @RequestMapping({"", "/", "/index"})
    public String getIndex() {
        return "/admin/index";
    }

    @RequestMapping("/getUserPage")
    public String getUserPage(Model model, Page page) {
        page.setPath("/admin/user");
        page.setRows(userService.findUserRows());

        List<User> userList = userService.findUsers(page.getOffset(), page.getLimit());
        page.setLimit(10);
        page.setRows(userList.size());
        page.setPath("/admin/user");

        model.addAttribute("userList", userList);
        return "/admin/user1";
    }

    @RequestMapping("/getEditUserPage/{id}")
    public String getEditUserPage(@PathVariable("id") int id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "/admin/editUser";
    }

    @RequestMapping("/movie")
    public String getMoviePage(Model model, Page page) {
        page.setPath("/admin/movie");
        page.setRows(movieService.findMovieRows());

        List<Movie> movies = movieService.findMovies(page.getOffset(), page.getLimit());
        model.addAttribute("movies", movies);

        return "/admin/movie";
    }

    @RequestMapping("/getEditMoviePage/{id}")
    public String getEditMoviePage(@PathVariable("id") int id, Model model) {
        Movie movie = movieService.findMovieById(id);
        model.addAttribute("movie", movie);
        List<Sort> sortList = sortService.findSorts();
        model.addAttribute("sortList", sortList);

        return "/admin/editMovie";
    }

    @RequestMapping("/comment")
    public String getCommentPage(Model model, Page page) {
        page.setPath("/admin/comment");
        page.setRows(commentService.findCommentRows());

        List<Comment> commentList = commentService.findComments(page.getOffset(), page.getLimit());
        model.addAttribute("commentList", commentList);

        return "/admin/comment";
    }

    @RequestMapping("/review")
    public String getReview(Model model, Page page) {
        page.setPath("/admin/review");
        page.setRows(reviewService.findReviewRows());

        List<Review> reviewList = reviewService.findReviews(page.getOffset(), page.getLimit());
        model.addAttribute("reviewList", reviewList);

        return "/admin/review";
    }

    @RequestMapping("/sort")
    public String getSort(Model model, Page page) {
        page.setPath("/admin/sort");
        page.setRows(sortService.findSortRows());

        List<Sort> sortList = sortService.findSortList(page.getOffset(), page.getLimit());
        model.addAttribute("sortList", sortList);

        return "/admin/sort";
    }

    @RequestMapping("/getEditSortPage/{id}")
    public String getEditSortPage(@PathVariable("id") int id, Model model) {
        Sort sort = sortService.findSortById(id);
        model.addAttribute("sort", sort);
        return "/admin/editSort";
    }

    @RequestMapping("/mainfunction")
    public String getMainPage() {
        return "/admin/mainfunction";
    }

    @RequestMapping("/login")
    public String getLoginPage() {
        return "/admin/login";
    }

    @RequestMapping("/addUser")
    public String getAddUserPage() {
        return "/admin/addUser";
    }

    @RequestMapping("/addMovie")
    public String getAddMoviePage(Model model) {
        List<Sort> sortList = sortService.findSortList(0, 100);
        model.addAttribute("sortList", sortList);
        return "/admin/addMovie";
    }

    @RequestMapping("/addSort")
    public String getAddSortPage() {
        return "/admin/addSort";
    }

}
