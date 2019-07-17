package com.example.mybatis;

import com.example.mybatis.cfg.Configuration;
import com.example.mybatis.cfg.Mapper;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  用于解析配置文件
 */
public class XMLConfigBuilder {

    /**
     * 解析主配置文件
     * @param config
     * @return
     */
    public static Configuration loadConfiguration(InputStream config) {
        try {
            //定义封装连接信息的配置对象（mybatis的配置对象）
            Configuration cfg = new Configuration();

            SAXReader reader = new SAXReader();
            Document document = reader.read(config);
            Element rootElement = document.getRootElement();
            List<Element> elements = rootElement.selectNodes("//property");
            for (Element element : elements) {
                String name = element.attributeValue("name");
                if ("driver".equals(name)) {
                    cfg.setDriver(element.attributeValue("value"));
                }
                if ("url".equals(name)) {
                    cfg.setUrl(element.attributeValue("value"));
                }
                if ("username".equals(name)) {
                    cfg.setUsername(element.attributeValue("value"));
                }
                if ("password".equals(name)) {
                    cfg.setPassword(element.attributeValue("value"));
                }
            }
            //解析mappers
            List<Element> mapperElements = rootElement.selectNodes("//mappers/mapper");
            for (Element element : elements) {
                Attribute attr = element.attribute("resource");
                if (attr != null) {
                    // xml
                    String mapperPath = attr.getValue(); // xml路径"com/itheima/dao/IUserDao.xml"
                    Map<String, Mapper> mappers = loadMapperConfiguration(mapperpath);
                    cfg.setMappers(mappers);
                } else {
                    String daoClassPath = element.attributeValue("class"); // xml路径"com/itheima/dao/IUserDao.xml"
                    Map<String, Mapper> mappers = loadMapperAnnotation(daoClassPath);
                    cfg.setMappers(mappers);
                }
            }

            return cfg;
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                config.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
