package com.example.mybatis.sqlsession;

import com.example.mybatis.cfg.Configuration;
import com.example.mybatis.sqlsession.defaults.DefaultSqlSessionFactory;
import com.example.mybatis.utils.XMLConfigBuilder;

import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(InputStream in) {
        Configuration configuration = XMLConfigBuilder.loadConfiguration(in);

        return new DefaultSqlSessionFactory(configuration);
    }
}
