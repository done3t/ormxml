package com.example.mybatis.utils;

import com.example.mybatis.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 用于创建数据源的工具类
 */
public class DataSourceUtil {

    public static Connection getConnection(Configuration config) {
        try {
            Class.forName(config.getDriver());
            return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
