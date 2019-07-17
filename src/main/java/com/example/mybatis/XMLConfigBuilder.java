package com.example.mybatis;

import com.example.io.Resources;
import com.example.mybatis.annoation.Select;
import com.example.mybatis.cfg.Configuration;
import com.example.mybatis.cfg.Mapper;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
            for (Element element : mapperElements) {
                Attribute attr = element.attribute("resource");
                if (attr != null) {
                    // xml
                    String mapperPath = attr.getValue(); // xml路径"com/itheima/dao/IUserDao.xml"
                    Map<String, Mapper> mappers = loadMapperConfiguration(mapperPath);
                    cfg.setMappers(mappers);
                } else {
                    String daoClassPath = element.attributeValue("class"); // xml路径"com/itheima/dao/IUserDao.xml"
                    Map<String, Mapper> mappers = loadMapperAnnotation(daoClassPath);
                    cfg.setMappers(mappers);
                }
            }

            return cfg;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                config.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据传入的参数，解析XML，并且封装到Map中
     * @param mapperPath    映射配置文件的位置
     * @return  map中包含了获取的唯一标识（key是由dao的全限定类名和方法名组成）
     *          以及执行所需的必要信息（value是一个Mapper对象，里面存放的是执行的SQL语句和要封装的实体类全限定类名）
     */
    public static Map<String, Mapper> loadMapperConfiguration(String mapperPath) {
        InputStream in = null;
        try {
            Map<String, Mapper> mapperMap = new HashMap<>();
            in = Resources.getResourceAsStream(mapperPath);
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);
            Element rootElement = document.getRootElement();
            String namespace = rootElement.attributeValue("namespace");
            List<Element> elements = rootElement.selectNodes("//select");
            for (Element element : elements) {
                String id = element.attributeValue("id");
                String resultType = element.attributeValue("resultType");
                String queryString = element.getText();

                String key = namespace + "." + id;
                // Mapper
                Mapper mapper = new Mapper();
                mapper.setQueryString(queryString);
                mapper.setResultType(resultType);
                mapperMap.put(key, mapper);
            }
            return mapperMap;
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据传入的参数，得到dao中所有被select注解标注的方法。
     * 根据方法名称和类名，以及方法上注解value属性的值，组成Mapper的必要信息
     * @param daoClassPath
     * @return
     */
    public static Map<String, Mapper> loadMapperAnnotation(String daoClassPath) throws ClassNotFoundException {
        Map<String, Mapper> mapperMap = new HashMap<>();
        Class aClass = Class.forName(daoClassPath);
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            boolean isAnnotated = method.isAnnotationPresent(Select.class);
            if (isAnnotated) {
                Mapper mapper = new Mapper();
                Select selectAnno = method.getAnnotation(Select.class);
                String queryString = selectAnno.value();
                mapper.setQueryString(queryString);

                String resultType = "";
                Type type = method.getGenericReturnType();
                if (type instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) type;
                    Type[] typeArguments = pType.getActualTypeArguments();
                    Class domainClass = (Class)typeArguments[0];
                    resultType = domainClass.getName();
                } else {
                    Class domainClazz = (Class) type;
                    resultType = domainClazz.getName();
                }
                mapper.setResultType(resultType);

                String className = method.getDeclaringClass().getName();
                String methodName = method.getName();
                String key = className + "." + methodName;
                mapperMap.put(key, mapper);
            }
        }
        return mapperMap;
    }
}
