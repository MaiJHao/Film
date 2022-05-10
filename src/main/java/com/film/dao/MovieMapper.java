package com.film.dao;

import com.film.entity.Movie;
import com.film.entity.Sort;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MovieMapper {

    List<Movie> selectMovies(int offset, int limit);

    int selectMovieRows();

    Movie selectMovieById(int id);

    Movie selectMovieByName(String name);

    int insertMovie(Movie movie);

    int updateMovieById(Movie movie);

    int updateMovieRating(int id, double rating);

    List<Movie> selectMoviesBySort(int sortId, int offset, int limit);

    List<Movie> selectNewMovie();

    int deleteMovieById(int id);

    List<Movie> selectMovieByNameAndPlace(String name, String place, int offset, int limit);
}
