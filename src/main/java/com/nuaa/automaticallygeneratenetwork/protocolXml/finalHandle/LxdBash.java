package com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author YZX
 * @Create 2023-05-08 10:23
 * @Java-version jdk1.8
 */
//用于一键启动或关闭容器
@Service
@Component
public class LxdBash {
    @Autowired
    GetInfo getInfo;

    //启动所有的容器
    public List<String> StartAll(String routerPathName,String hostPathName){
        List<String> cmds = new ArrayList<>();
        //查询当前配置文件对应的容器名称
        List<String> allHostName = getInfo.getAllLxdName(hostPathName);
        List<String> allRouterName = getInfo.getAllLxdName(routerPathName);
        //构造主机
        for (int i = 0 ; i<allHostName.size() ; i++){
            //将创建容器的命令放入到集合中
            cmds.add("lxc start "+allHostName.get(i));
        }
        //构造路由器
        for (int j = 0 ; j < allRouterName.size() ; j++){
            //将创建容器的命令放入到集合中
            cmds.add("lxc start "+allRouterName.get(j));
        }
        return cmds;
    }

    //停止所有的容器
    public List<String> StopAll(String routerPathName, String hostPathName){
        List<String> cmds = new ArrayList<>();
        //查询当前配置文件对应的容器名称
        List<String> allHostName = getInfo.getAllLxdName(hostPathName);
        List<String> allRouterName = getInfo.getAllLxdName(routerPathName);
        //构造主机
        for (int i = 0 ; i<allHostName.size() ; i++){
            //将创建容器的命令放入到集合中
            cmds.add("lxc stop "+allHostName.get(i));
        }
        //构造路由器
        for (int j = 0 ; j < allRouterName.size() ; j++){
            //将创建容器的命令放入到集合中
            cmds.add("lxc stop "+allRouterName.get(j));
        }
        return cmds;
    }

    //重启所有的容器
    public List<String> ReStartAll(String routerPathName, String hostPathName){
        List<String> cmds = new ArrayList<>();
        //查询当前配置文件对应的容器名称
        List<String> allHostName = getInfo.getAllLxdName(hostPathName);
        List<String> allRouterName = getInfo.getAllLxdName(routerPathName);
        //构造主机
        for (int i = 0 ; i<allHostName.size() ; i++){
            //将创建容器的命令放入到集合中
            cmds.add("lxc restart "+allHostName.get(i));
        }
        //构造路由器
        for (int j = 0 ; j < allRouterName.size() ; j++){
            //将创建容器的命令放入到集合中
            cmds.add("lxc restart "+allRouterName.get(j));
        }
        return cmds;
    }

    //删除所有的容器
    public List<String> DeleteAll(String routerPathName, String hostPathName){
        List<String> cmds = new ArrayList<>();
        //查询当前配置文件对应的容器名称
        List<String> allHostName = getInfo.getAllLxdName(hostPathName);
        List<String> allRouterName = getInfo.getAllLxdName(routerPathName);
        //构造主机
        for (int i = 0 ; i<allHostName.size() ; i++){
            //将创建容器的命令放入到集合中
            cmds.add("lxc delete "+allHostName.get(i));
        }
        //构造路由器
        for (int j = 0 ; j < allRouterName.size() ; j++){
            //将创建容器的命令放入到集合中
            cmds.add("lxc delete "+allRouterName.get(j));
        }
        return cmds;
    }

    //重启所有的网络
    public List<String> NetplanApply(String routerPathName, String hostPathName){
        List<String> cmds = new ArrayList<>();
        //查询当前配置文件对应的容器名称
        List<String> allHostName = getInfo.getAllLxdName(hostPathName);
        List<String> allRouterName = getInfo.getAllLxdName(routerPathName);
        //构造主机
        for (int i = 0 ; i<allHostName.size() ; i++){
            //将创建容器的命令放入到集合中
            cmds.add("lxc exec "+allHostName.get(i)+" netplan apply");
        }
        //构造路由器
        for (int j = 0 ; j < allRouterName.size() ; j++){
            //将创建容器的命令放入到集合中
            cmds.add("lxc exec "+allRouterName.get(j)+" netplan apply");
        }
        return cmds;
    }

    //重启所有的frr
    public List<String> RestartFrr(String routerPathName){
        List<String> cmds = new ArrayList<>();
        //查询当前配置文件对应的容器名称
        List<String> allRouterName = getInfo.getAllLxdName(routerPathName);
        //构造路由器
        for (int j = 0 ; j < allRouterName.size() ; j++){
            //将创建容器的命令放入到集合中
            cmds.add("lxc exec "+allRouterName.get(j)+" systemctl restart frr");
        }
        return cmds;
    }
}
