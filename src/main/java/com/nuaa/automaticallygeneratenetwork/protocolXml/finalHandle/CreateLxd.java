package com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle;

import com.nuaa.automaticallygeneratenetwork.pojo.Hosts;
import com.nuaa.automaticallygeneratenetwork.pojo.Routers;
import com.nuaa.automaticallygeneratenetwork.service.HostsService;
import com.nuaa.automaticallygeneratenetwork.service.RoutersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    HostsService hostsService;
    @Autowired
    RoutersService routersService;
    @Autowired
    CreateYaml createYaml;

    //从指定的文件目录下面获取到我们所有的配置容器名称
    public List<String> getAllLxdName(String pathName){
        File file = new File(pathName);
        String[] array = file.list();
        List<String> result  = new ArrayList<>();
        for (String name:array){
            result.add(name.split("\\.")[0]);
        }
        return result;
    }

    //根据主机的名称，从数据库中查询出主机的信息
    public List<Hosts> getHostsInfo(List<String> allHostName){
        //存放查询结果
        List<Hosts> list = new ArrayList<>();
        for (String name:allHostName){
            try {
                //因为name的格式为"QH1.xml"的格式，所以我们要获取容器的名称则需要拆分
                list.add(hostsService.getByName(name));
            }catch (Exception e){
                throw new RuntimeException("该容器名称对应的容器早已存在，请重命名配置文件名称以及容器名称");
            }
        }
        return list;
    }

    //根据路由器的名称，从数据库中查询出路由器的信息
    public List<Routers> getRoutersInfo(List<String> allRouterName){
        //存放查询结果
        List<Routers> list = new ArrayList<>();
        for (String name:allRouterName){
            try {
                //因为name的格式为"QR1.xml"的格式，所以我们要获取容器的名称则需要拆分
                list.add(routersService.getByName(name));
            }catch (Exception e){
                throw new RuntimeException("该容器名称对应的容器早已存在，请重命名配置文件名称以及容器名称");
            }
        }
        return list;
    }

    //根据容器信息构造命令行创建主机和路由器
    public List<String> createRH(List<Routers> routersInfo,List<Hosts> hostsInfo) throws IOException {
        List<String> cmds = new ArrayList<>();
        //构造路由器
        for (int i = 0 ; i<routersInfo.size() ; i++){
            Routers routers = routersInfo.get(i);
            //生成网口的配置文件
            //createYaml.touchInterYaml(routers.getInterfacesId(),routers.getName());
            //将创建容器的命令放入到集合中
            cmds.add("lxc copy YzxMouldLxd "+routers.getName());
        }
        //构造主机
        for (int j = 0 ; j < hostsInfo.size() ; j++){
            Hosts hosts = hostsInfo.get(j);
            //将创建容器的命令放入到集合中
            cmds.add("lxc copy YzxMouldLxd "+hosts.getName());
        }
        return cmds;
    }
}
