package com.example.test;

import com.example.domain.User;
import com.example.io.Resources;
import com.example.mybatis.utils.DataSourceUtil;
import com.example.mybatis.utils.Executor;
import com.example.mybatis.utils.XMLConfigBuilder;
import com.example.mybatis.cfg.Configuration;
import com.example.mybatis.cfg.Mapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class ConfigTest {

    private Connection conn = null;

    @Before
    public void testConfig() {

        InputStream resource = Resources.getResourceAsStream("SqlMapConfig.xml");
        Configuration configuration = XMLConfigBuilder.loadConfiguration(resource);
        System.out.println(configuration);
        Map<String, Mapper> mappers = configuration.getMappers();
        Assert.assertNotNull(mappers);

        conn = DataSourceUtil.getConnection(configuration);
        Assert.assertNotNull(conn);
        System.out.println(conn.toString());

    }

    @Test
    public void testXMLMapperConfig() {
        InputStream in = Resources.getResourceAsStream("com/example/dao/IUserDao.xml");
        //InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        Assert.assertNotNull(in);
    }

    @Test
    public void testConnection() {
    }

    @Test
    public void testExecutor() {
        Mapper mapper = new Mapper();
        mapper.setResultType("com.example.domain.User");
        mapper.setQueryString("select * from user");

        Executor executor = new Executor();
        List<User> users = executor.selectList(mapper, conn);
        for (User user : users) {
            System.out.println(user.getUsername());
        }
    }
}
