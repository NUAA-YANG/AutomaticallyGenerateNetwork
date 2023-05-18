package com.nuaa.automaticallygeneratenetwork.protocolXml.createXML.ftlReplace;

import com.nuaa.automaticallygeneratenetwork.vo.InterfaceFtl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author YZX
 * @Create 2023-05-17 17:00
 * @Java-version jdk1.8
 */
@Service
@Component
public class FtlXml {


    public static void main(String[] args) throws TemplateException, IOException {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("lxd_name","QR2");

        List<InterfaceFtl> interfaceFtlList = new ArrayList<>();
        interfaceFtlList.add(new InterfaceFtl("eth1","192.168.1.1",24));
        interfaceFtlList.add(new InterfaceFtl("eth2","192.168.2.1",24));
        dataMap.put("interfaceFtlList",interfaceFtlList);

        List<String> ospfIp = new ArrayList<>();
        ospfIp.add("192.168.3.1");
        ospfIp.add("192.168.4.1");
        dataMap.put("ospfIp",ospfIp);

        dataMap.put("ospfRedis","bgp");

        dataMap.put("bgpAns","100");

        List<String> bgpInterIp = new ArrayList<>();
        bgpInterIp.add("192.168.5.1");
        bgpInterIp.add("192.168.6.1");
        dataMap.put("bgpInterIp",bgpInterIp);

        List<String> bgpExterIp = new ArrayList<>();
        bgpExterIp.add("192.168.7.1");
        bgpExterIp.add("192.168.8.1");
        dataMap.put("bgpExterIp",bgpExterIp);

        List<String> bgpIp = new ArrayList<>();
        bgpIp.add("192.168.9.1");
        bgpIp.add("192.168.10.1");
        dataMap.put("bgpIp",bgpIp);

        dataMap.put("bgpRedis","ospf");

        FtlXml ftlXml = new FtlXml();
        ftlXml.ftlToXml(dataMap,"Router","QR2","src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/createXML/ftlExample");

    }



    /**
     * @description  将传入的Map键值对转化为xml文件
     * @date 2023/5/17 20:30
     * @params [dataMap：传入的键值对集合, type：容器类型【 Router或 Host】, lxdName：容器名称, savePath：生成的xml存放路径]
     * @returns void
     */
    public void ftlToXml(Map<String, Object> dataMap, String type,String lxdName,String savePath) throws IOException, TemplateException {
        //设置版本
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        //加载ftl模板所在的父级目录
        configuration.setDirectoryForTemplateLoading(new File("src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/createXML/ftlExample"));
        configuration.setDefaultEncoding("utf-8");
        Template template = null;
        //选择相关ftl配置文件
        if ("Router".equals(type)){
            //父级目录下ftl模板的名称
            template = configuration.getTemplate("RouterExample.ftl", "utf-8");
        }else {
            template = configuration.getTemplate("HostExample.ftl", "utf-8");
        }
        //创建一个位置用于存放生成的文件，指定生成xml文件
        Writer writer = new FileWriter(new File(savePath+"/"+lxdName+".xml"));
        template.process(dataMap,writer);
    }

}
