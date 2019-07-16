package com.example.test;

import com.example.io.Resources;
import com.example.mybatis.XMLConfigBuilder;
import com.example.mybatis.cfg.Configuration;
import org.junit.Test;

import java.io.InputStream;

public class ConfigTest {

    @Test
    public void testConfig() {

        InputStream resource = Resources.getResourceAsStream("SqlMapConfig.xml");
        Configuration configuration = XMLConfigBuilder.loadConfiguration(resource);
        System.out.println(configuration);
    }
}
