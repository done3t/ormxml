package com.example.test;

import com.example.io.Resources;
import com.example.mybatis.XMLConfigBuilder;
import com.example.mybatis.cfg.Configuration;
import com.example.mybatis.cfg.Mapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.Map;

public class ConfigTest {

    @Test
    public void testConfig() {

        InputStream resource = Resources.getResourceAsStream("SqlMapConfig.xml");
        Configuration configuration = XMLConfigBuilder.loadConfiguration(resource);
        System.out.println(configuration);
        Map<String, Mapper> mappers = configuration.getMappers();
        Assert.assertNotNull(mappers);
    }

    @Test
    public void testXMLMapperConfig() {
        InputStream in = Resources.getResourceAsStream("com/example/dao/IUserDao.xml");
        //InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        Assert.assertNotNull(in);
    }

}
