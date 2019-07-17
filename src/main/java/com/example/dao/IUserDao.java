package com.example.dao;

import com.example.domain.User;
import com.example.mybatis.annoation.Select;

import java.util.List;

public interface IUserDao {
    /**
     * 查询所有操作
     * @return
     */
    @Select("select * from User")
    List<User> findAll();
}
