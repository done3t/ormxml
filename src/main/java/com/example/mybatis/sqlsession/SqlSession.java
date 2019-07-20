package com.example.mybatis.sqlsession;

public interface SqlSession {

    <T> T getMapper(Class<T> daoInterface);

    void close();
}
