package com.runner.server.service.utils;

import org.apache.commons.collections.map.HashedMap;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 星空梦语
 * @desc
 * @date 2021/3/23 下午2:18
 */
public class XmlUtil {
    public static Map<String,String> parseDependencyXml(String dependency){
        Map<String, String> dependencyMap = new HashMap<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            byte[] bytes = dependency.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            Document d = dBuilder.parse(bi);
            d.getDocumentElement().normalize();
            String groupId = d.getElementsByTagName("groupId").item(0).getTextContent();
            String artifactId = d.getElementsByTagName("artifactId").item(0).getTextContent();
            String version = d.getElementsByTagName("version").item(0).getTextContent();
            dependencyMap.put("groupId",groupId);
            dependencyMap.put("artifactId",artifactId);
            dependencyMap.put("version",version);
        }catch (Exception exp){
            return null;
        }
        return dependencyMap;
    }

    public static void main(String[] args) {
        String data="<dependency>\n" +
                "            <groupId>org.apache.commons</groupId>\n" +
                "            <artifactId>commons-lang3</artifactId>\n" +
                "            <version>3.5</version>\n" +
                "        </dependency>";
        XmlUtil.parseDependencyXml(data);
    }

}
