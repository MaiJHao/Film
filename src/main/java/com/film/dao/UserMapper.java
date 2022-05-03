package com.film.dao;

import com.film.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updatePassword(int id, String password);

    int updateHeaderUrl(int id, String headerUrl);
}
