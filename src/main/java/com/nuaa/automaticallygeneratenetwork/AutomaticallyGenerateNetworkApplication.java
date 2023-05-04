package com.nuaa.automaticallygeneratenetwork;

import com.nuaa.automaticallygeneratenetwork.pojo.Hosts;
import com.nuaa.automaticallygeneratenetwork.pojo.Routers;
import com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle.CreateLxd;
import com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle.CreateYaml;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@EnableTransactionManagement
public class AutomaticallyGenerateNetworkApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(AutomaticallyGenerateNetworkApplication.class, args);
        ApplicationContext context = SpringUtil.getApplicationContext();


        /**1. 测试读取文件，插入相关路由器或主机信息**/
//        HandleRHList handleRHList = context.getBean(HandleRHList.class);
//        /** 1.1 测试插入路由器信息 */
//        String RouterPathName = "src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/routerXml";
//        List<Routers> routers = handleRHList.HandleRouterList(RouterPathName);
//        System.out.println("路由器信息插入成功:"+routers);
//        /** 1.2 测试插入主机信息 */
//        String HostPathName = "src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/hostXml";
//        List<Hosts> hosts = handleRHList.HandleHostList(HostPathName);
//        System.out.println("主机信息插入成功:"+hosts);


        /**2. 创建容器**/
        CreateLxd createLxd = context.getBean(CreateLxd.class);
        /** 2.1 测试从数据库获取路由器和主机信息*/
        List<String> allHostName = createLxd.getAllLxdName("src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/hostXml");
        List<Hosts> hostsInfo = createLxd.getHostsInfo(allHostName);
        List<String> allRouterName = createLxd.getAllLxdName("src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/routerXml");
        List<Routers> routersInfo = createLxd.getRoutersInfo(allRouterName);
        /** 2.2 根据上述信息生成接口配置文件以及命令行*/
        List<String> cmds = createLxd.createRH(routersInfo, hostsInfo);
        cmds.forEach(x-> System.out.println(x));
    }

}
