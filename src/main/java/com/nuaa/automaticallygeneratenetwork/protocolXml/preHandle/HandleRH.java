package com.nuaa.automaticallygeneratenetwork.protocolXml.preHandle;

import com.nuaa.automaticallygeneratenetwork.pojo.*;
import com.nuaa.automaticallygeneratenetwork.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author YZX
 * @Create 2023-04-28 10:36
 * @Java-version jdk1.8
 */
@Service
@Component
//处理路由器和主机
public class HandleRH {
    @Autowired
    BGPService bgpService;
    @Autowired
    NetInterfacesService netInterfacesService;
    @Autowired
    OSPFService ospfService;
    @Autowired
    HandleXml handleXml;
    @Autowired
    HandleData handleData;

    public void testInsert(){
        NetInterfaces n4 = new NetInterfaces(4,"eth4","4234",43,"测试四号",0);
        NetInterfaces n5 = new NetInterfaces(4,"eth5","4234",43,"测试五号",0);
        NetInterfaces n6 = new NetInterfaces(4,"eth6","4234",43,"测试六号",0);
        List<NetInterfaces> list = new ArrayList<>();
        list.add(n4);
        list.add(n5);
        list.add(n6);
        List<NetInterfaces> list1 = netInterfacesService.saveList(list);
        list1.forEach(x-> System.out.println(x));
    }


    //处理单个router对象
    public Routers HandleRouter(String pathName){
        //获取处理的xml文件
        Map<String, String> xmlFile = handleXml.readXMLFile(pathName);
        //获取路由器名称
        String lxd_name = xmlFile.get("lxd_name");
        //获取处理的接口集合对象->将接口对象存入数据库中，并且获得他们的id，拼接之后赋值给路由器
        String interfaces_id = "";
        List<NetInterfaces> netInterfacesList = handleData.HandleInter(pathName);//待插入对象集合
        List<NetInterfaces> saveNetInterfacesList = netInterfacesService.saveList(netInterfacesList);//插入后返回的对象集合
        for (NetInterfaces n:saveNetInterfacesList){
            interfaces_id += n.getId()+";";//开始拼接网口id
        }
        //获取处理的OSPF对象->将接口对象存入数据库中，并且获得他们的id
        OSPF ospf = handleData.HandleOspf(pathName);
        OSPF saveOspf = ospfService.save(ospf);
        Integer ospf_id = saveOspf.getId();
        //获取处理的BGP对象->将接口对象存入数据库中，并且获得他们的id
        BGP bgp = handleData.HandleBgp(pathName);
        BGP saveBgp = bgpService.save(bgp);
        Integer bgp_id = saveBgp.getId();
        //单纯创建实体类并且返回
        Routers routers = new Routers(0,lxd_name,interfaces_id,ospf_id,bgp_id);
        return routers;

        //创建实体类->保存到数据库中并且返回
//        Routers saveRouters = routersService.save(routers);
//        return saveRouters;

    }



    //处理单个host对象
    public Hosts HandleHost(String pathName){
        //获取处理的xml文件
        Map<String, String> xmlFile = handleXml.readXMLFile(pathName);
        //获取主机名称
        String lxd_name = xmlFile.get("lxd_name");
        //获取处理的接口集合对象->将接口对象存入数据库中，并且获得他们的id
        String interfaces_id = "";
        List<NetInterfaces> netInterfacesList = handleData.HandleInter(pathName);//待插入对象集合
        List<NetInterfaces> saveNetInterfacesList = netInterfacesService.saveList(netInterfacesList);//插入后返回的对象集合
        for (NetInterfaces n:saveNetInterfacesList){
            interfaces_id += n.getId()+";";//开始拼接网口id
        }
        //单纯创建实体类并且返回
        Hosts hosts = new Hosts(0,lxd_name,interfaces_id);
        return hosts;

        //创建实体类->保存到数据库中并且返回
//        Hosts saveHosts = hostsService.save(hosts);
//        return saveHosts;

    }





}
