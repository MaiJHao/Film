package com.film.controller;

import com.film.annotation.LoginRequired;
import com.film.entity.*;
import com.film.service.*;
import com.film.utils.FilmConstant;
import com.film.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements FilmConstant {

    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private SortService sortService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping({"/index", "", "/"})
    public String index(Model model, Page page) {
        page.setRows(movieService.findMovieRows());
        page.setPath("/index");

        List<Movie> movies = movieService.findMovies(page.getOffset(), page.getLimit());
        List<Map<String, Object>> moviesVO = new ArrayList<>();
        if (movies != null) {
            for (Movie movie : movies) {
                Map<String, Object> map = new HashMap<>();
                map.put("movie", movie);
                // 根据sortId查询sort名
                Sort sort = sortService.findSortById(movie.getSortId());
                map.put("sortName", sort.getName());

                moviesVO.add(map);
            }
        }
        model.addAttribute("movies", moviesVO);

        List<Sort> sortList = sortService.findSorts();
        Map<String, List<Sort>> sortVO = new HashMap<>();
        model.addAttribute("sortList", sortList);

        return "/index";
    }

    @RequestMapping("/category")
    public String category(Model model) {
        List<Sort> sortList = sortService.findSorts();

        Map<String, List<Sort>> sortVO = new HashMap<>();

        model.addAttribute("sortList", sortList);

        return "/category2";
    }

    @RequestMapping("/details/{id}")
    public String details(@PathVariable("id") int movieId, Model model, Page page) {
        page.setPath("/details/" + movieId);
        // page.setRows(commentService.findCommentCountByMovieId(movieId));
        page.setRows(reviewService.findReviewCount(movieId));

        Movie movie = movieService.findMovieById(movieId);
        model.addAttribute("movie", movie);

        // 查询sort的name
        Sort sort = sortService.findSortById(movie.getSortId());
        model.addAttribute("sortName", sort.getName());

        // 查询影评
        List<Review> reviewList = reviewService.findReviewByMovieId(movieId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> reviewVOList = new ArrayList<>();
        for (Review review : reviewList) {
            Map<String ,Object> reviewVO = new HashMap<>();
            reviewVO.put("review", review);
            // 查询该影评所获赞数量
            long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, review.getId());
            reviewVO.put("likeCount", likeCount);
            // 查询发表该影评的用户信息
            User user = userService.findUserById(review.getUserId());
            reviewVO.put("username", user.getUsername());

            reviewVOList.add(reviewVO);
        }
        model.addAttribute("reviews", reviewVOList);

        // 查询影评数量
        int reviewCount = reviewService.findReviewCount(movieId);
        model.addAttribute("reviewCount", reviewCount);

        //查询最新电影
        List<Movie> newMovies = movieService.findNewMovie();
        model.addAttribute("newMovies", newMovies);

        return "/details";
    }

    @RequestMapping("/reviewDetails/{movieId}/{reviewId}")
    public String reviewDetail(@PathVariable("movieId") int movieId,
                               @PathVariable("reviewId") int reviewId,
                               Model model, Page page) {
        // 当前登录用户
        User currentLoginUser = hostHolder.getUser();

        page.setPath("/review-details/" + movieId + "/" + reviewId);
        page.setRows(commentService.findCommentCountByMovieId(reviewId));

        Movie movie = movieService.findMovieById(movieId);
        model.addAttribute("movie", movie);

        // 查询sort的name
        Sort sort = sortService.findSortById(movie.getSortId());
        model.addAttribute("sortName", sort.getName());

        // 查询影评内容
        Review review = reviewService.findReviewById(reviewId);
        model.addAttribute("review", review);

        // 查询评价数量
        int CommentCount = commentService.findCommentCountByMovieId(reviewId);
        model.addAttribute("commentCount", CommentCount);

        // 查询点赞数量和点赞状态
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, reviewId);
        model.addAttribute("likeCount", likeCount);
        int likeStatus = currentLoginUser==null ? 0 : likeService.findUserToEntityLikeStatus(currentLoginUser.getId(), ENTITY_TYPE_POST, reviewId);
        model.addAttribute("likeStatus", likeStatus);

        // 查询评价
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, reviewId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentVOList = new ArrayList<>();
        for (Comment comment : commentList) {
            Map<String, Object> commentVO = new HashMap<>();
            // 查询发表评论的用户信息
            User user = userService.findUserById(comment.getUserId());
            commentVO.put("user", user);
            commentVO.put("comment", comment);
            // 查询该评论的点赞数量和点赞状态
            likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
            likeStatus = currentLoginUser==null ? 0 : likeService.findUserToEntityLikeStatus(currentLoginUser.getId(), ENTITY_TYPE_COMMENT, comment.getId());
            commentVO.put("likeCount", likeCount);
            commentVO.put("likeStatus", likeStatus);
            // 查询该评论下的回复
            List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), page.getOffset(), page.getLimit());
            List<Map<String, Object>> replyVOList = new ArrayList<>();
            if (replyList != null) {
                for (Comment reply : replyList) {
                    Map<String, Object> replyVO = new HashMap<>();
                    replyVO.put("reply", reply);
                    // 发表回复的用户
                    replyVO.put("user", userService.findUserById(reply.getUserId()));
                    // 回复的目标用户
                    User target = reply.getTargetId()==0 ? null : userService.findUserById(reply.getTargetId());
                    replyVO.put("target", target);
                    // 查询该回复的 点赞数量 和 点赞状态
                    likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                    likeStatus = currentLoginUser==null ? 0 : likeService.findUserToEntityLikeStatus(currentLoginUser.getId(), ENTITY_TYPE_COMMENT, reply.getId());
                    replyVO.put("likeCount", likeCount);
                    replyVO.put("likeStatus", likeStatus);

                    replyVOList.add(replyVO);
                }
                commentVO.put("replys",replyVOList);
            }
            commentVOList.add(commentVO);
        }
        model.addAttribute("comments", commentVOList);

        return "/review-details";
    }

    // 个人主页
    @LoginRequired
    @RequestMapping("/profile")
    public String getProfile(Model model) {
        User currentLoginUser = hostHolder.getUser();
        model.addAttribute("user", currentLoginUser);

        // 查询用户发表的影评
        List<Review> reviewList = reviewService.findReviewsByUserId(currentLoginUser.getId());
        List<Review> latestReviews = new ArrayList<>();
        if (reviewList != null) {
            for (int i=0;i<5;i++){
                if (i == reviewList.size()) break;
                latestReviews.add(reviewList.get(i));
            }
        }
        model.addAttribute("reviews", latestReviews);

        // 查询用户发表的影评数量

        // 查询用户发表的讨论数量

        // 查询用户获赞数量

        // 查询用户发表的评论数
        return "/profile";
    }

    @RequestMapping("/profile/{id}")
    public String getProfileById(@PathVariable("id") int id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);

        // 查询用户发表的影评
        List<Review> reviewList = reviewService.findReviewsByUserId(user.getId());
        List<Review> latestReviews = new ArrayList<>();
        if (reviewList != null) {
            for (int i=0;i<5;i++){
                if (i == reviewList.size()) break;
                latestReviews.add(reviewList.get(i));
            }
        }
        model.addAttribute("reviews", latestReviews);

        // 查询用户发表的评论数
        return "/profile";
    }

    @RequestMapping("/setting")
    public String getSetting(Model model) {
        User user = hostHolder.getUser();
        model.addAttribute("user", user);

        return "/setting";
    }

    @RequestMapping("/index2")
    public String index2() {
        return "index2";
    }

    @RequestMapping("/index3")
    public String index3() {
        return "/index3";
    }

    @RequestMapping("/login")
    public String login() {
        return "/login";
    }

    @RequestMapping("/register")
    public String register() {
        return "/register";
    }

    @RequestMapping("/forget")
    public String forget() {
        return "/forget";
    }

    @RequestMapping("/contacts")
    public String contacts() {
        return "/contacts";
    }

    @RequestMapping("/interview")
    public String interview() {
        return "/interview";
    }

    @RequestMapping("/privacy")
    public String privacy() {
        return "/privacy";
    }

    @RequestMapping("/catalog")
    public String catalog() {
        return "/catalog";
    }

    @RequestMapping("/live")
    public String live() {
        return "/live";
    }

    @RequestMapping("/404")
    public String get404() {
        return "/404";
    }
}
