package com.film;

import com.film.dao.*;
import com.film.entity.*;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

@SpringBootTest
// 以CommunityApplication为配置类
@ContextConfiguration(classes = FilmApplication.class)
@MapperScan("com.film.dao")
class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SortMapper sortMapper;

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Test
    void UserMapperTests() {
        User user = userMapper.selectById(1);
        System.err.println(user);
        user = userMapper.selectByName("admin");
        System.err.println(user);
        user = userMapper.selectByEmail("2353220306@qq.com");
        System.err.println(user);

        /*user.setUsername("admin1");
        user.setPassword("123456");
        user.setEmail("admin1@qq.com");
        int i = userMapper.insertUser(user);
        System.err.println(userMapper.selectByName("admin1"));*/
    }

    @Test
    public void updatePasswordTest() {
        userMapper.updatePassword(1, "123456");
    }

    @Test
    public void SortMapperTest1() {
        List<Sort> sortList = sortMapper.selectSorts();
        for (Sort sort : sortList) {
            System.err.println(sort);
        }

        Sort sort = sortMapper.selectSortById(1);
        System.err.println(sort);

        sort = sortMapper.selectSortByName("喜剧");
        System.err.println(sort);
    }

    @Test
    public void SortMapperTest2() {
        sortMapper.updateNameById(2, "科幻");
    }

    @Test
    public void SortMapperTest3() {
        Sort sort = new Sort();
        sort.setName("纪录片");
        sort.setCreateTime(new Date());
        sortMapper.insertSort(sort);
    }

    @Test
    public void MovieTest1() {
        Movie movie = new Movie();
        movie.setName("战狼2");
        movie.setSortId(1);
        movie.setReleaseTime(new Date());
        movie.setPlace("美国");
        movieMapper.insertMovie(movie);
    }

    @Test
    public void MovieTest2() {
        List<Movie> movies = movieMapper.selectMovies(0, 5);
        for (Movie movie : movies) {
            System.out.println(movie);
        }

        Movie movie = movieMapper.selectMovieById(2);
        System.out.println(movie);

        movie = movieMapper.selectMovieByName("战狼2");
        System.out.println(movie);

        movies = movieMapper.selectMoviesBySort(1, 0, 5);
        for (Movie movie1 : movies) {
            System.out.println("sort: " + movie1);
        }
    }

    @Test
    public void MovieTest3() {
        /*Movie movie = new Movie();
        movie.setId(7);
        movie.setPlace("中国");
        movie.setPoster("吴京");
        movieMapper.updateMovieById(movie);*/

        movieMapper.updateMovieRating(2, 9.1);
    }

    @Test
    public void CommentMapperTest1() {
        Comment comment = new Comment();
        comment.setUserId(164);
        comment.setCreateTime(new Date());
        comment.setContent("说得好");
        comment.setEntityType(2);
        comment.setEntityId(2);
        comment.setTargetId(165);
        commentMapper.insertComment(comment);
    }

    @Test
    public void CommentMapperTest2() {
        System.out.println(commentMapper.selectCommentsByEntity(2, 165, 0, 2));

        System.out.println(commentMapper.selectCommentByUserId(166));

        //CommentMapper.deleteCommentById(239);

        System.out.println(commentMapper.selectCommentCountByMovieId(2));
    }

    @Test
    public void CommentMapperTest3() {
        Comment comment = new Comment();
        comment.setId(236);
        comment.setCreateTime(new Date());
        comment.setContent("你的评价很中肯");
        comment.setEntityType(2);
        comment.setEntityId(2);
        comment.setTargetId(165);
        commentMapper.updateComment(comment);
    }

    @Test
    public void ReviewMapperTest1() {
        Review review = new Review();
        review.setUserId(164);
        review.setMovieId(2);
        review.setTitle("蜘蛛侠：英雄无归评价，内含剧透");
        review.setContent("三蛛同框...");
        review.setType(0);
        review.setStatus(0);
        review.setCreateTime(new Date());
        review.setCommentCount(3);
        review.setScore(0);
        reviewMapper.insertReview(review);
    }

    @Test
    public void ReviewMapperTest2() {
        List<Review> reviewList = reviewMapper.selectReviews(0, 5);
        for (Review review : reviewList) {
            System.out.println(review);
        }

        reviewList = reviewMapper.selectReviewsByUserId(164);
        for (Review review : reviewList) {
            System.err.println(review);
        }

        Review review = reviewMapper.selectReviewById(287);
        System.out.println(review);

        int count = reviewMapper.selectReviewCountByMovieId(2);
        System.out.println(count);

        double avg = reviewMapper.selectMovieAvgRating(2);
        System.out.println(avg);
    }

    @Test
    public void ReviewMapperTest3() {
        reviewMapper.updateCommentCount(288, 10);

        reviewMapper.deleteReviewById(287);
    }

}
