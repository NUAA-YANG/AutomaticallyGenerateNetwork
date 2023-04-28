package com.nuaa.automaticallygeneratenetwork.protocolXml.handle;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author YZX
 * @Create 2023-04-27 15:27
 * @Java-version jdk1.8
 */
@Service
@Component
//处理xml文本文件，将其转化为类对象
public class HandleXml {
    public static void main(String[] args) {
        HandleXml handleXml = new HandleXml();
        // 读取XML文件并打印所有属性
        Map<String, String> properties = handleXml.readXMLFile("src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/QR1.xml");
//        System.out.println("接口为:"+properties.get("interfaces"));
//        System.out.println("ospf协议为:"+properties.get("ospf"));
//        System.out.println("bgp协议为:"+properties.get("bgp"));
//        String ospf = properties.get("ospf");
//        String[] splitOspf = ospf.split("\\s+");
//        System.out.println(Arrays.toString(splitOspf));
//        System.out.println("=================");
//        String bgp = properties.get("bgp");
//        String[] splitBgp = bgp.split("\\s+");
//        System.out.println(Arrays.toString(splitBgp));
//        System.out.println("======================");
//        System.out.println(properties.get("ans")+":"+properties.get("redistribute"));
//        System.out.println(properties.get("external"));
//        System.out.println("========================");
//        System.out.println(properties.get("internal"));
//        System.out.println("========================");
        System.out.println(properties.get("type"));
//        System.out.println("========================");
//        System.out.println(properties.get("bgp_network"));
//                for (String key : properties.keySet()) {
//            System.out.println(key + ": " + properties.get(key));
//        }

    }

    public  Map<String, String> readXMLFile(String fileName) {
        // 创建一个HashMap，用于存储XML文件的属性和值
        Map<String, String> properties = new HashMap<>();
        try {
            // 创建一个DocumentBuilder对象，用于解析XML文件
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 解析XML文件，并将结果存储在Document对象中
            Document doc = builder.parse(new File(fileName));
            // 获取XML文档的根元素
            Element root = doc.getDocumentElement();
            // 从根元素开始遍历整个XML文档
            readNode(root, "", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回XML文件的所有属性和值
        return properties;
    }

    // 递归读取XML文档中的元素节点和属性节点，并将它们存储在HashMap中
    private  void readNode(Node node, String parentPath, Map<String, String> properties) {
        // 获取节点的路径
        String path = parentPath + "/" + node.getNodeName();
        if (node.getNodeName().equals("router")) {
            properties.put("type","router");
        }
        if (node.getNodeName().equals("host")) {
            properties.put("type","host");
        }
        // 如果节点有属性，则将属性添加到HashMap中
        if (node.hasAttributes()) {
            NamedNodeMap attrs = node.getAttributes();
            for (int i = 0; i < attrs.getLength(); i++) {
                Node attr = attrs.item(i);
                // 将属性的名称和值添加到HashMap中
                properties.put(path + "@" + attr.getNodeName(), attr.getNodeValue());
            }
        }
        // 如果节点有文本内容，则将文本内容添加到HashMap中
        String value = node.getTextContent().trim();
        if (!value.isEmpty()) {
            //获取子节点列表
            NodeList childNodesList = node.getChildNodes();
            //遍历获得子节点的所有值
            for (int i = 0 ;i<childNodesList.getLength();i++){
                //将节点的值和名称放入map中
                properties.put(childNodesList.item(i).getNodeName(), childNodesList.item(i).getTextContent());
            }
        }
        // 如果节点有子节点，则遍历子节点并递归调用readNode方法
        if (node.hasChildNodes()) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    // 如果子节点是元素节点，则递归调用readNode方法
                    readNode(child, path, properties);
                }
            }
        }
    }
}
