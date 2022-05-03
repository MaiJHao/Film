package com.film.service;

import com.film.dao.MovieMapper;
import com.film.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieMapper movieMapper;

    public List<Movie> findMovies(int offset, int limit) {
        return movieMapper.selectMovies(offset, limit);
    }

    public int findMovieRows() {
        return movieMapper.selectMovieRows();
    }

    public Movie findMovieById(int id) {
        if (id == 0) {
            throw new IllegalArgumentException("参数错误");
        }
        Movie movie = movieMapper.selectMovieById(id);
        return movie;
    }

    public Movie findMovieByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("参数错误");
        }
        Movie movie = movieMapper.selectMovieByName(name);
        return movie;
    }

    public int addMovie(Movie movie) {
        // 查询该电影是否已存在
        Movie result = movieMapper.selectMovieByName(movie.getName());
        if (result != null) {
            throw new IllegalArgumentException("该电影已存在");
        }
        // 补全数据
        movie.setEditTime(new Date());

        int rows = movieMapper.insertMovie(movie);
        return rows;
    }

    public int updateMovie(int id, Movie movie) {
        // 此id为被修改的电影id
        movie.setId(id);
        int rows = movieMapper.updateMovieById(movie);
        return rows;
    }

    public int updateMovieRating(int id, double rating) {
        int rows = movieMapper.updateMovieRating(id, rating);
        return rows;
    }

    public List<Movie> findMoviesBySort(int sortId, int offset, int limit) {
        List<Movie> movies = movieMapper.selectMoviesBySort(sortId, offset, limit);
        return movies;
    }

    public List<Movie> findNewMovie() {
        return movieMapper.selectNewMovie();
    }
}
