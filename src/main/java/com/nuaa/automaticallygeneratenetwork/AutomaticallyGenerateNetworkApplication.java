package com.nuaa.automaticallygeneratenetwork;

import com.jcraft.jsch.Session;
import com.nuaa.automaticallygeneratenetwork.linuxCommand.ExecLinuxCommands;
import com.nuaa.automaticallygeneratenetwork.linuxCommand.LinuxConnection;
import com.nuaa.automaticallygeneratenetwork.pojo.Hosts;
import com.nuaa.automaticallygeneratenetwork.pojo.Iptables;
import com.nuaa.automaticallygeneratenetwork.pojo.Routers;
import com.nuaa.automaticallygeneratenetwork.protocolAcl.conversion.AclToIptables;
import com.nuaa.automaticallygeneratenetwork.protocolAcl.conversion.CompleteConversion;
import com.nuaa.automaticallygeneratenetwork.protocolAcl.conversion.FileRulePush;
import com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle.CreateLxd;
import com.nuaa.automaticallygeneratenetwork.protocolXml.preHandle.HandleRHList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableTransactionManagement
public class AutomaticallyGenerateNetworkApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AutomaticallyGenerateNetworkApplication.class, args);
        ApplicationContext context = SpringUtil.getApplicationContext();


        /**0.0 定义配置文件所在位置*/
        String routerPath = "src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/routerXml";
        String hostPath = "src/main/java/com/nuaa/automaticallygeneratenetwork/protocolXml/xml/hostXml";
        String aclPath = "src/main/java/com/nuaa/automaticallygeneratenetwork/protocolAcl/acl";

        /**0.1 定义服务器的执行类*/
        LinuxConnection connection = context.getBean(LinuxConnection.class);
        Session session = connection.getJSchSession("192.168.31.104", 22, "root", "root");
        ExecLinuxCommands execLinuxCommands = context.getBean(ExecLinuxCommands.class);


//        System.out.println("=========================1. 数据库写入容器信息===============================");
//        HandleRHList handleRHList = context.getBean(HandleRHList.class);
//        List<Routers> routers = handleRHList.HandleRouterList(routerPath);
//        List<Hosts> hosts = handleRHList.HandleHostList(hostPath);
//
//
//
//        System.out.println("=========================2. 创建容器=====================================");
//        CreateLxd createLxd = context.getBean(CreateLxd.class);
//        List<String> cmds2 = createLxd.createRH(routerPath, hostPath);
//        execLinuxCommands.getCmdResult(session,cmds2);
//        cmds2.forEach(x-> System.out.println(x));
//
//
//
//        System.out.println("=========================3. 创建网桥并且连接网桥===========================");
//        CreateBridge createBridge = context.getBean(CreateBridge.class);
//        List<String> cmds3 = createBridge.CreateAndAttachBridge(routerPath,hostPath);
//        execLinuxCommands.getCmdResult(session,cmds3);
//        cmds3.forEach(x-> System.out.println(x));
//
//
//
//        System.out.println("=========================4. 创建接口信息文件命令===========================");
//        CreateYaml createYaml = context.getBean(CreateYaml.class);
//        List<String> cmds4 = createYaml.createYL(routerPath,hostPath);
//        execLinuxCommands.getCmdResult(session,cmds4);
//        cmds4.forEach(x-> System.out.println(x));
//
//
//        System.out.println("=========================5. 创建frr.config配置文件========================");
//        CreateFrr createFrr = context.getBean(CreateFrr.class);
//        List<String> cmds5 = createFrr.createFR(routerPath);
//        execLinuxCommands.getCmdResult(session,cmds5);
//        cmds5.forEach(x-> System.out.println(x));
//
//
//        System.out.println("=========================6. 启动所有容器==================================");
//        LxdBash lxdBash6 = context.getBean(LxdBash.class);
//        List<String> cmds6 = lxdBash6.StartAll(routerPath, hostPath);
//        execLinuxCommands.getCmdResult(session,cmds6);
//        cmds6.forEach(x-> System.out.println(x));
//
//
//        System.out.println("=========================7. 替换配置配置文件==============================");
//        FilePush filePush = context.getBean(FilePush.class);
//        List<String> cmds7 = filePush.pushYamlAndFrr(routerPath, hostPath);
//        execLinuxCommands.getCmdResult(session,cmds7);
//        cmds7.forEach(x-> System.out.println(x));
//
//
//        System.out.println("=========================8. 重启接口配置以及网络服务========================");
//        LxdBash lxdBash8 = context.getBean(LxdBash.class);
//        List<String> cmds8 = lxdBash8.NetplanApply(routerPath, hostPath);
//        execLinuxCommands.getCmdResult(session,cmds8);
//        cmds8.forEach(x-> System.out.println(x));
//        List<String> cmds9 = lxdBash8.RestartFrr(routerPath);
//        execLinuxCommands.getCmdResult(session,cmds9);
//        cmds9.forEach(x-> System.out.println(x));



//        System.out.println("=========================9. 中兴ACL-华为ACL转化为Iptables写入数据库=======================");
//        AclToIptables aclToIptables = context.getBean(AclToIptables.class);
//        aclToIptables.turnToIptables(aclPath);


//        System.out.println("=========================10. 生成防火墙相关配置文件及脚本=======================");
//        CompleteConversion completeConversion = context.getBean(CompleteConversion.class);
//        List<String> cmds10 = completeConversion.finalConversion(aclPath);
//        execLinuxCommands.getCmdResult(session,cmds10);
//        cmds10.forEach(x-> System.out.println(x));


        System.out.println("=========================11. 替换防火墙配置文件=======================");
        FileRulePush fileRulePush = context.getBean(FileRulePush.class);
        List<String> cmds11 = fileRulePush.pushRule(aclPath);
        execLinuxCommands.getCmdResult(session,cmds11);
        cmds11.forEach(x-> System.out.println(x));


        //关闭服务器连接
        connection.closeJSchSession(session);

    }

}
