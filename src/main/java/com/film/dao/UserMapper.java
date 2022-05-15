package com.film.dao;

import com.film.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    List<User> selectUsers(int offset, int limit);

    int insertUser(User user);

    int updatePassword(int id, String password);

    int updateHeaderUrl(int id, String headerUrl);

    int updateEmail(int id, String email);

    int updateStatus(int id);

    int selectUserCount();

    List<User> selectUserByIdAndEmail(String username, String email, int offset, int limit);

    int updateUserById(int id, String username, String email, int type);
}
