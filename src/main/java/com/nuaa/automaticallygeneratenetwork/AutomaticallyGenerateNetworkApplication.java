package com.nuaa.automaticallygeneratenetwork;

import com.jcraft.jsch.Session;
import com.nuaa.automaticallygeneratenetwork.linuxCommand.ExecLinuxCommands;
import com.nuaa.automaticallygeneratenetwork.linuxCommand.LinuxConnection;
import com.nuaa.automaticallygeneratenetwork.pojo.Hosts;
import com.nuaa.automaticallygeneratenetwork.pojo.Routers;
import com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
@EnableTransactionManagement
public class AutomaticallyGenerateNetworkApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AutomaticallyGenerateNetworkApplication.class, args);
        ApplicationContext context = SpringUtil.getApplicationContext();


        /**0.0 定义配置文件所在位置*/
        String routerPath = "src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/routerXml";
        String hostPath = "src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/hostXml";
        /**0.1 定义服务器的执行类*/
//        LinuxConnection connection = context.getBean(LinuxConnection.class);
//        Session session = connection.getJSchSession("192.168.31.104", 22, "root", "root");
//        ExecLinuxCommands execLinuxCommands = context.getBean(ExecLinuxCommands.class);


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


        System.out.println("=======================2. 创建容器成功===================================");
        CreateLxd createLxd = context.getBean(CreateLxd.class);
        List<String> cmds1 = createLxd.createRH(routerPath, hostPath);
        //cmds1.forEach(x-> System.out.println(x));



        System.out.println("======================3. 创建网桥并且连接网桥成功===============================");
        CreateBridge createBridge = context.getBean(CreateBridge.class);
        List<String> cmds2 = createBridge.CreateAndAttachBridge(routerPath,hostPath);
        //cmds2.forEach(x-> System.out.println(x));



        System.out.println("========================4. 创建接口信息文件命令成功===========================");
        CreateYaml createYaml = context.getBean(CreateYaml.class);
        List<String> cmds3 = createYaml.createYL(routerPath,hostPath);
        //execLinuxCommands.getCmdResult(session,cmds3);
        cmds3.forEach(x-> System.out.println(x));


        System.out.println("=========================5. 创建frr.config配置文件成功========================");
        CreateFrr createFrr = context.getBean(CreateFrr.class);
        List<String> cmds4 = createFrr.createFR(routerPath);
        //execLinuxCommands.getCmdResult(session,cmds4);
        cmds4.forEach(x-> System.out.println(x));


        System.out.println("=========================6. 替换配置配置文件成功========================");
        FilePush filePush = context.getBean(FilePush.class);
        List<String> cmds5 = filePush.pushYamlAndFrr(routerPath, hostPath);
        cmds5.forEach(x-> System.out.println(x));

        //关闭服务器连接
        //connection.closeJSchSession(session);
    }

}
