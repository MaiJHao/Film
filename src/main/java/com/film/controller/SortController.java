package com.film.controller;

import com.film.entity.Sort;
import com.film.service.SortService;
import com.film.utils.FilmUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sort")
public class SortController {

    @Autowired
    private SortService sortService;

    @RequestMapping("/admin/addSort")
    public String addSort(Sort sort) {
        int rows = sortService.addSort(sort);
        if (rows != 1) {
            return "redirect:/admin/sort";
        }
        return "redirect:/admin/sort";
    }

    @RequestMapping("/admin/deleteSort/{id}")
    @ResponseBody
    public String deleteUserById(@PathVariable("id") int id) {
        int rows = sortService.deleteSort(id);
        if (rows != 1) {
            return FilmUtil.getJSONString(1);
        }
        return FilmUtil.getJSONString(0);
    }

}
