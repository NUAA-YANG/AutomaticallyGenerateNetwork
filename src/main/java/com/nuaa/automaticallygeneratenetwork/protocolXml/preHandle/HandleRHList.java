package com.nuaa.automaticallygeneratenetwork.protocolXml.preHandle;

import com.nuaa.automaticallygeneratenetwork.pojo.Hosts;
import com.nuaa.automaticallygeneratenetwork.pojo.Routers;
import com.nuaa.automaticallygeneratenetwork.service.HostsService;
import com.nuaa.automaticallygeneratenetwork.service.RoutersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author YZX
 * @Create 2023-05-04 15:56
 * @Java-version jdk1.8
 */
//为了防止耦合，分离文件：处理路由器和主机全部文件
@Service
@Component
public class HandleRHList {
    @Autowired
    HandleRH handleRH;
    @Autowired
    HostsService hostsService;
    @Autowired
    RoutersService routersService;

    //根据目录位置，处理该目录下的所有配置Routers文件
    public List<Routers> HandleRouterList(String parentName){
        //存储结果
        List<Routers> result = new ArrayList<>();
        //获取目录下的所有文件名称
        File file = new File(parentName);
        String[] array = file.list();
        //遍历所有的文件，创造路由器对象
        for (String fileName:array){
            String pathName = parentName+"/"+fileName;
            System.out.println(pathName);
            Routers routers = handleRH.HandleRouter(pathName);
            result.add(routers);
        }
        //不存储到数据库中，直接返回
        //return result;

        //存储到数据库中
        List<Routers> routersList = routersService.saveList(result);
        return routersList;
    }

    //根据目录位置，处理该目录下的所有配置Hosts文件
    public List<Hosts> HandleHostList(String parentName){
        //存储结果
        List<Hosts> result = new ArrayList<>();
        //获取目录下的所有文件名称
        File file = new File(parentName);
        String[] array = file.list();
        //遍历所有的文件，创造路由器对象
        for (String fileName:array){
            String pathName = parentName+"/"+fileName;
            System.out.println(pathName);
            Hosts hosts = handleRH.HandleHost(pathName);
            result.add(hosts);
        }

        //不存储到数据库中，直接返回
        //return result;

        //存储到数据库中
        List<Hosts> hostsList = hostsService.saveList(result);
        return hostsList;
    }
}
