package com.nuaa.automaticallygeneratenetwork;

import com.nuaa.automaticallygeneratenetwork.pojo.Hosts;
import com.nuaa.automaticallygeneratenetwork.pojo.Routers;
import com.nuaa.automaticallygeneratenetwork.protocolXml.handle.HandleRH;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AutomaticallyGenerateNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutomaticallyGenerateNetworkApplication.class, args);
        ApplicationContext context = SpringUtil.getApplicationContext();
        HandleRH handleRH = context.getBean(HandleRH.class);
        String RouterPathName = "src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/QR1.xml";
        Routers routers = handleRH.HandleRouter(RouterPathName);
        System.out.println("路由器信息插入成功:"+routers);

        String HostPathName = "src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/QH1.xml";
        Hosts hosts = handleRH.HandleHost(HostPathName);
        System.out.println("主机信息插入成功:"+hosts);



//        handleRH.testInsert();
//        System.out.println("批量插入成功");
    }

}
