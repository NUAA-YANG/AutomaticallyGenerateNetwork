package com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author YZX
 * @Create 2023-05-06 11:00
 * @Java-version jdk1.8
 */
//能够将创建好的配置文件直接覆盖原始的文件
@Service
@Component
public class FilePush {

    @Autowired
    GetInfo getInfo;

    //将接口配置文件【10-yaml】和协议配置文件【frr.config】覆盖原来的配置文件
    public List<String> pushYamlAndFrr(String routerPathName,String hostPathName){
        List<String> cmds = new ArrayList<>();
        //查询当前配置文件对应的容器名称
        List<String> allHostName = getInfo.getAllLxdName(hostPathName);
        List<String> allRouterName = getInfo.getAllLxdName(routerPathName);
        //替换路由器文件
        for (String routerName:allRouterName){
            cmds.add("lxc file push /root/AutoNetwork/"+routerName+"/10-lxc.yaml "+routerName+"/etc/netplan/");
            cmds.add("lxc file push /root/AutoNetwork/"+routerName+"/frr.conf "+routerName+"/etc/frr/");
        }
        //替换主机文件
        for (String hostName:allHostName){
            cmds.add("lxc file push /root/AutoNetwork/"+hostName+"/10-lxc.yaml "+hostName+"/etc/netplan/");
        }
        return cmds;
    }

}
