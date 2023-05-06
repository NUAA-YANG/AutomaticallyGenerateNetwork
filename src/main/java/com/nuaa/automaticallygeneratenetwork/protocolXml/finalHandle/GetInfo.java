package com.nuaa.automaticallygeneratenetwork.protocolXml.finalHandle;

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
 * @Create 2023-05-06 10:10
 * @Java-version jdk1.8
 */
//根据当前的配置文件，从数据库中获得相应的容器信息
@Service
@Component
public class GetInfo {

    @Autowired
    HostsService hostsService;
    @Autowired
    RoutersService routersService;

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
}
