package com.film.controller;

import com.film.entity.Movie;
import com.film.entity.Page;
import com.film.entity.Sort;
import com.film.entity.User;
import com.film.service.MovieService;
import com.film.service.SortService;
import com.film.utils.FilmUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private SortService sortService;

    @Value("${film.path.upload.movie.poster}")
    private String moviePoster;

    @RequestMapping("/findFilmBySort/{sortId}")
    @ResponseBody
    public String findFilmBySort(@PathVariable("sortId") int sortId, Page page) {
        List<Movie> movieList = sortId==0
                ? movieService.findMovies(page.getOffset(), page.getLimit())
                : movieService.findMoviesBySort(sortId, page.getOffset(), page.getLimit());



        page.setLimit(16);
        page.setRows(movieList.size());
        page.setPath("/index");

        List<Map<String, Object>> movieVoList = new ArrayList<>();
        for (Movie movie : movieList) {
            Map<String, Object> movieVo = new HashMap<>();
            movieVo.put("movie", movie);
            Sort sort = sortService.findSortById(movie.getSortId());
            movieVo.put("sortName", sort.getName());

            movieVoList.add(movieVo);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("movies", movieVoList);

        return FilmUtil.getJSONString(0, null, map);
    }

    @RequestMapping("/poster/{fileName}")
    public void getPoster(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 电影海报存放路径
        fileName = moviePoster + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 设置响应图片格式
        response.setContentType("image/" + suffix);

        try (OutputStream os = response.getOutputStream();
             FileInputStream fis = new FileInputStream(fileName);
             ) {
            int readCount = 0;
            byte[] bytes = new byte[1024];
            while ((readCount = fis.read(bytes)) != -1) {
                os.write(bytes, 0, readCount);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/admin/addMovie")
    public String addMovie(Movie movie) {
        int rows = movieService.addMovie(movie);
        return "redirect:/admin/movie";
    }

    @RequestMapping("/admin/deleteMovie/{id}")
    @ResponseBody
    public String deleteUserById(@PathVariable("id") int id) {
        int rows = movieService.deleteMovie(id);
        if (rows != 1) {
            return FilmUtil.getJSONString(1);
        }
        return FilmUtil.getJSONString(0);
    }

    @RequestMapping("/admin/searchMovies")
    @ResponseBody
    public List<Movie> searchMovies(String name, String place, Page page) {
        if (name == "") name = null;
        if (place == "") place = null;
        List<Movie> movies = movieService.searchMovies(name, place, page.getOffset(), page.getLimit());
        page.setRows(movies.size());
        page.setPath("/admin/searchMovies?name="+name+"&place="+place);
        return movies;
    }

}
