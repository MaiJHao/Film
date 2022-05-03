package com.film.dao;

import com.film.entity.Sort;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SortMapper {
    List<Sort> selectSorts();

    Sort selectSortById(int id);

    Sort selectSortByName(String name);

    int insertSort(Sort sort);

    int updateNameById(int id, String name);
}
