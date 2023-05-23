package com.nuaa.automaticallygeneratenetwork.protocolXml.createXML.ftlReplace;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @Author YZX
 * @Create 2023-05-23 16:38
 * @Java-version jdk1.8
 */
//构建路由器或者主机的全部配置文件
@Service
@Component
public class CreateXml {

    @Autowired
    TextMatch textMatch;
    @Autowired
    FtlXml ftlXml;

    /**
     * @description 根据给定的文件路径下的所有网管信息，生成路由器或主机的xml文件
     * @date 2023/5/23 16:40
     * @params [textPath：网管信息路径, routerPath：存储生成的路由器xml路径, hostPath：存储生成的主机xml路径]
     * @returns void
     */
    public void createRouterHostXml(String textPath,String routerPath,String hostPath) throws IOException, TemplateException {
        //获取目录下的所有文件名称
        String[] fileArray = new File(textPath).list();
        for (String name:fileArray){
            //获取对应的全部文件
            File file = new File(textPath +"/"+ name);
            Map<String, Object> map = textMatch.MatchFtl(file);
            if ("Router".equals(map.get("type"))){
                ftlXml.ftlToXml(map, (String) map.get("type"), (String) map.get("lxd_name"),routerPath);
            } else if ("Host".equals(map.get("type"))){
                ftlXml.ftlToXml(map, (String) map.get("type"), (String) map.get("lxd_name"),hostPath);
            }
        }
    }
}
