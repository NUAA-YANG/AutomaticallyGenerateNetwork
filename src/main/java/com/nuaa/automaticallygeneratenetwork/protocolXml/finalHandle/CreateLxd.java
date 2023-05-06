package com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author YZX
 * @Create 2023-05-04 16:06
 * @Java-version jdk1.8
 */
//创建容器
@Service
@Component
public class CreateLxd {


    @Autowired
    GetInfo getInfo;

    //根据容器信息构造命令行创建主机和路由器
    public List<String> createRH(String routerPathName,String hostPathName) throws IOException {
        List<String> cmds = new ArrayList<>();
        //查询当前配置文件对应的容器名称
        List<String> allHostName = getInfo.getAllLxdName(hostPathName);
        List<String> allRouterName = getInfo.getAllLxdName(routerPathName);
        //构造主机
        for (int i = 0 ; i<allHostName.size() ; i++){
            //将创建容器的命令放入到集合中
            cmds.add("lxc copy YzxMouldLxd "+allHostName.get(i));
        }
        //构造路由器
        for (int j = 0 ; j < allRouterName.size() ; j++){
            //将创建容器的命令放入到集合中
            cmds.add("lxc copy YzxMouldLxd "+allRouterName.get(j));
        }
        return cmds;
    }
}
