package com.example.mybatis.sqlsession.defaults;

import com.example.mybatis.cfg.Configuration;
import com.example.mybatis.sqlsession.SqlSession;
import com.example.mybatis.sqlsession.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration config = null;

    public DefaultSqlSessionFactory(Configuration config) {
        this.config = config;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(config);
    }
}
