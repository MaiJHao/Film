package com.film.service;

import com.film.dao.SortMapper;
import com.film.entity.Movie;
import com.film.entity.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SortService {

    @Autowired
    private SortMapper sortMapper;

    public List<Sort> findSorts() {
        List<Sort> sortList = sortMapper.selectSorts();
        return sortList;
    }

    public Sort findSortById(int id) {
        Sort sort = sortMapper.selectSortById(id);
        return sort;
    }

    public int addSort(Sort sort) {
        if (sort == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        Sort result = sortMapper.selectSortByName(sort.getName());
        if (result != null) {
            throw new IllegalArgumentException("该分类已存在");
        }

        sort.setCreateTime(new Date());
        int rows = sortMapper.insertSort(sort);

        return rows;
    }

    public List<Sort> findSortList(int offset,int limit) {
        return sortMapper.selectSortList(offset, limit);
    }

    public int deleteSort(int id) {
        return sortMapper.deleteSortById(id);
    }

    public int findSortRows() {
        return sortMapper.selectSortCount();
    }
}
