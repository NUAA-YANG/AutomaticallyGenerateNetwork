package com.nuaa.automaticallygeneratenetwork;

import com.nuaa.automaticallygeneratenetwork.pojo.Hosts;
import com.nuaa.automaticallygeneratenetwork.pojo.Routers;
import com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle.CreateLxd;
import com.nuaa.automaticallygeneratenetwork.protocolXml.preHandle.HandleRH;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@SpringBootApplication
@EnableTransactionManagement
public class AutomaticallyGenerateNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutomaticallyGenerateNetworkApplication.class, args);
        ApplicationContext context = SpringUtil.getApplicationContext();


        /**测试读取文件，插入相关路由器或主机信息**/
//        HandleRH handleRH = context.getBean(HandleRH.class);
//        String RouterPathName = "src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/routerXml/QR1.xml";
//        Routers routers = handleRH.HandleRouter(RouterPathName);
//        System.out.println("路由器信息插入成功:"+routers);
//
//        String HostPathName = "src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/hostXml/QH1.xml";
//        Hosts hosts = handleRH.HandleHost(HostPathName);
//        System.out.println("主机信息插入成功:"+hosts);


        /**测试根据配置文件，从数据库读取路由器或主机信息**/
        CreateLxd createLxd = context.getBean(CreateLxd.class);
        //测试获取主机信息
        List<String> allHostName = createLxd.getAllLxdName("src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/hostXml");
        List<Hosts> hostsInfo = createLxd.getHostsInfo(allHostName);
        hostsInfo.forEach(x-> System.out.println(x));
        //测试获取路由器信息
        List<String> allRouterName = createLxd.getAllLxdName("src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/routerXml");
        List<Routers> routersInfo = createLxd.getRoutersInfo(allRouterName);
        routersInfo.forEach(x-> System.out.println(x));


    }

}
