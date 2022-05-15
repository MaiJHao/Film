package com.film.controller;

import com.film.entity.Movie;
import com.film.entity.Page;
import com.film.entity.Sort;
import com.film.entity.User;
import com.film.service.MovieService;
import com.film.service.SortService;
import com.film.utils.FilmUtil;
import com.film.utils.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
    private String posterUploadPath;

    @Value("${film.path.upload.video}")
    private String movieUploadPath;

    @Autowired
    private HostHolder hostHolder;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${film.path.domain}")
    private String domain;

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
        fileName = posterUploadPath + "/" + fileName;
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

    @RequestMapping("/admin/updateMovie")
    @ResponseBody
    public String updateMovieById(Movie movie) {
        int rows = movieService.updateMovie(movie.getId(),movie);
        if (rows != 1) {
            return FilmUtil.getJSONString(1);
        }
        return FilmUtil.getJSONString(0);
    }

    @RequestMapping("/admin/uploadMovie")
    @ResponseBody
    public String uploadMovie(@RequestParam("file") MultipartFile movie) {
        if (movie == null) {
            return FilmUtil.getJSONString(1, "您还没有上传电影");
        }

        String fileName = movie.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            return FilmUtil.getJSONString(1, "您还没有上传电影");
        }
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            return FilmUtil.getJSONString(1, "文件格式不正确");
        }

        // 生成随机的文件名
        fileName = FilmUtil.generateUUID() + suffix;
        // 确认文件存放的路径
        File dest = new File(movieUploadPath + "/" + fileName);
        try {
            // 将headerImage文件传入dest对象，写入磁盘中
            movie.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传文件失败，服务器发生异常", e);
        }

        // 更新当前用户的头像路径
        // http://localhost:8080/film/movie/player/xxx.jpg
        String movieUrl = domain + contextPath + "/movie/player/" + fileName;
//        movieService.updateMovie()

        HashMap<String, Object> map = new HashMap<>();
        map.put("movieUrl", movieUrl);
        return FilmUtil.getJSONString(0, null, map);
    }

    @RequestMapping("/admin/uploadPoster")
    @ResponseBody
    public String uploadPoster(@RequestParam("file") MultipartFile poster) {
        if (poster == null) {
            return FilmUtil.getJSONString(1, "您还没有上传海报");
        }

        String fileName = poster.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            return FilmUtil.getJSONString(1, "您还没有上传海报");
        }
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            return FilmUtil.getJSONString(1, "文件格式不正确");
        }

        // 生成随机的文件名
        fileName = FilmUtil.generateUUID() + suffix;
        // 确认文件存放的路径
        File dest = new File(posterUploadPath + "/" + fileName);
        try {
            // 将headerImage文件传入dest对象，写入磁盘中
            poster.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传文件失败，服务器发生异常", e);
        }

        // 更新当前用户的头像路径
        // http://localhost:8080/film/movie/poster/xxx.jpg
        String posterUrl = domain + contextPath + "/movie/poster/" + fileName;
//        movieService.updateMovie()

        HashMap<String, Object> map = new HashMap<>();
        map.put("posterUrl", posterUrl);
        return FilmUtil.getJSONString(0, null, map);
    }

    @RequestMapping("/player/{fileName}")
    public void getMovie(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 电影存放路径
        fileName = movieUploadPath + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 设置响应视频格式
        response.setContentType("video/" + suffix);

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

}
